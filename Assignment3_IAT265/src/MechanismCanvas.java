import controlP5.*;
import processing.core.*;
import java.util.ArrayList;

public class MechanismCanvas extends PApplet {
    Mechanism mechanism;
    ControlP5 cp5;
    Wheel selectedWheel;

    public void settings() {
        size(1200, 800);
    }

    public void setup() {
        cp5 = new ControlP5(this);
        mechanism = new Mechanism(this);
        selectedWheel = null;

        cp5.addButton("clear")
                .setPosition(10, 10)
                .setSize(80, 30)
                .setCaptionLabel("Clear");

        cp5.addButton("saveImage")
                .setPosition(100, 10)
                .setSize(100, 30)
                .setCaptionLabel("Save Image");
    }

    public void draw() {
        background(255);
        mechanism.update();
        mechanism.display();

        // Display instructions for user interaction on screen
        fill(0);
        textAlign(LEFT, TOP);
        textSize(12);
        text("Click on a wheel to select it.", 10, height - 105);
        text("Press '+' to increase the selected wheel's radius.", 10, height - 90);
        text("Press '-' to decrease the selected wheel's radius.", 10, height - 75);
        text("Press '1' to change the selected wheel's color to red.", 10, height - 60);
        text("Press '2' to change the selected wheel's color to green.", 10, height - 45);
        text("Press '3' to change the selected wheel's color to blue.", 10, height - 30);
    }

    public void keyPressed() {
        if (selectedWheel != null) {
            // Adjust properties of the selected wheel based on keyboard input
            if (key == '+') {
                selectedWheel.setRadius(selectedWheel.getRadius() + 5);
            } else if (key == '-') {
                selectedWheel.setRadius(max(5, selectedWheel.getRadius() - 5));
            } else if (key == '1') {
                selectedWheel.setColor(color(255, 0, 0));
            } else if (key == '2') {
                selectedWheel.setColor(color(0, 255, 0));
            } else if (key == '3') {
                selectedWheel.setColor(color(0, 0, 255));
            }
        }
    }

    public void mousePressed() {
        boolean wheelFound = false; // This flag will check if a wheel is found under the mouse click

        // Check if the mouse is clicked on a wheel
        for (Wheel wheel : mechanism.getAllWheels()) {
            if (dist(mouseX, mouseY, wheel.getX(), wheel.getY()) < wheel.getRadius()) {
                selectedWheel = wheel;
                wheelFound = true;
                break; // If a wheel is found, break out of the loop
            }
        }

        if (!wheelFound) {
            selectedWheel = null; // If no wheel is found, deselect
        }
    }



    public void clear() {
        mechanism.clear();
    }

    public void saveImage() {
        save("mechanism.png");
    }

    class Mechanism {
        ArrayList<Wheel> wheels;
        ArrayList<Belt> belts;
        MechanismCanvas parent;

        Mechanism(MechanismCanvas parent) {
            this.parent = parent;
            wheels = new ArrayList<Wheel>();
            belts = new ArrayList<Belt>();
            createMechanism();
        }

        void createMechanism() {
            // Create the root wheel
            float rootRadius = 100;
            float rootSpeed = TWO_PI / 300.0f;
            Wheel root = createWheel(parent, 600, 300, rootRadius, color(255, 0, 0), rootSpeed, null, 2, 0, 70);
            root.setRotationSpeed(rootSpeed * (100 / rootRadius)); // Adjust root speed based on radius
            wheels.add(root);
        }


        // Recursive method to create wheels and belts
        Wheel createWheel(MechanismCanvas parent, float x, float y, float radius, int col, float rotationSpeed, Wheel parentWheel, int depth, int position, float childRadius) {
            float adjustedRotationSpeed = rotationSpeed * (radius / childRadius); // Adjust rotation speed based on the ratio of radii

            Wheel wheel = new Wheel(parent, x, y, radius, col, adjustedRotationSpeed, parentWheel);
            wheels.add(wheel);

            // Base case: if depth is 0, return the wheel
            if (depth == 0) {
                return wheel;
            }

            // Generate colors based on depth
            int colorStep = 30; // Step to change color for each level
            int colorDepth = (col + (depth * colorStep)) % 255; // Vary color based on depth

            // Create child wheels and belts recursively
            float childXOffset = radius * 2.5f; // Offset for child wheels along x-axis
            float childYOffset = radius * 1.5f; // Offset for child wheels along y-axis

            // Create left child
            float leftChildX = x - childXOffset;
            float leftChildY = y + childYOffset;
            Wheel leftChild = createWheel(parent, leftChildX, leftChildY, childRadius, colorDepth, adjustedRotationSpeed, wheel, depth - 1, position * 2, childRadius * 0.75f);

            // Create right child
            float rightChildX = x + childXOffset;
            float rightChildY = y + childYOffset;
            Wheel rightChild = createWheel(parent, rightChildX, rightChildY, childRadius, colorDepth, adjustedRotationSpeed, wheel, depth - 1, position * 2 + 1, childRadius * 0.75f);

            // Create belts
            Belt leftBelt = new Belt(parent, wheel, leftChild);
            belts.add(leftBelt);
            wheel.addBelt(leftBelt);
            leftChild.setParentWheel(wheel);

            Belt rightBelt = new Belt(parent, wheel, rightChild);
            belts.add(rightBelt);
            wheel.addBelt(rightBelt);
            rightChild.setParentWheel(wheel);

            wheel.addChildWheel(rightChild);
            wheel.addChildWheel(leftChild);

            return wheel;
        }

        void update() {
            for (Wheel wheel : wheels) {
                wheel.update();
            }
        }

        void display() {
            for (Belt belt : belts) {
                belt.display();
            }
            for (Wheel wheel : wheels) {
                wheel.display();
            }
        }

        void clear() {
            wheels.clear();
            belts.clear();
            selectedWheel = null;
        }

        Wheel[] getAllWheels() {
            return wheels.toArray(new Wheel[0]);
        }
    }

    class Wheel  extends MechanismCanvas{
        float x, y, radius, angle;
        int color;
        float rotationSpeed;
        MechanismCanvas parent;
        ArrayList<Belt> connectedBelts;

        Wheel parentWheel; // Reference to the parent wheel
        ArrayList<Wheel> childWheels;

        Wheel(MechanismCanvas parent, float x, float y, float radius, int color, float rotationSpeed, Wheel parentWheel) {
            // ... initialization ...
            this.parent = parent;
            this.x = x;
            this.y = y;
            this.radius = radius;
            this.color = color;
            this.rotationSpeed = rotationSpeed; // Initially set rotation speed
            this.parentWheel = parentWheel; // Initialize the parent wheel
            connectedBelts = new ArrayList<Belt>();
            childWheels = new ArrayList<>();
        }

        void addChildWheel(Wheel child) {
            childWheels.add(child);
        }

        // Recursive method to update child wheel radii and tangents
        void updateChildWheels(float newRadius) {
            for (Wheel child : childWheels) {
                child.setRadius(newRadius); // Update radius for child wheel
                child.updateChildWheels(newRadius); // Recursively update child wheels
            }
        }

        void update() {
            angle += rotationSpeed;
        }

        void display() {
            parent.pushMatrix();
            parent.translate(x, y);
            parent.rotate(angle); // Rotate the wheel

            // Set highlight for selected wheel
            if (parent.selectedWheel == this) {
                parent.stroke(255, 204, 0); // Highlight color
                parent.strokeWeight(4); // Highlight with a thicker stroke
            } else {
                parent.stroke(0); // Default stroke color
                parent.strokeWeight(2); // Default stroke weight
            }

            // Outer wheel
            parent.fill(color);
            parent.ellipse(0, 0, radius * 2, radius * 2);

            // Inner circles
            parent.strokeWeight(1);
            parent.ellipse(0, 0, radius * 1.5f, radius * 1.5f); // Inner circle at 75% of radius
            parent.ellipse(0, 0, radius, radius * 0.5f); // Inner circle at 50% of radius
            parent.ellipse(0, 0, radius * 0.5f, radius); // Inner circle at 50% of radius

            // Draw the spokes
            for (int i = 0; i < 4; i++) {
                parent.line(0, 0, 0, -radius);
                parent.rotate(PI / 2);
            }

            // Center dot
            parent.fill(0); // Fill center dot with black color
            parent.ellipse(0, 0, radius * 0.1f, radius * 0.1f); // Center dot with 10% of radius

            parent.popMatrix();
        }
        void setParentWheel(Wheel parentWheel) {
            this.parentWheel = parentWheel;
        }

        float getX() {
            return x;
        }

        float getY() {
            return y;
        }

        float getRadius() {
            return radius;
        }

        void setRadius(float newRadius) {
            if (newRadius != radius) {
                float oldRadius = radius;
                radius = newRadius;

                // Adjust the rotation speed to maintain a consistent speed at the edge of the wheel
                rotationSpeed *= oldRadius / newRadius;



//            if (parentWheel != null) {
//                // Recursively update tangent lines for belts connected to child wheels
//                for (Wheel child : childWheels) {
//                    child.setRadius(child.getRadius()); // This will trigger update for child wheels and their belts
//
//                }
//                // Adjust rotation speed based on the ratio of radii
//                float ratio = parentWheel.getRadius() * 0.01f * rotationSpeed;
//                setRotationSpeed(ratio );
//
//
//            } else {
//
//                float ratio = oldRadius / newRadius;
//                rotationSpeed *= ratio;



            }



            // Update child wheels' rotation speeds
            // updateChildWheelSpeeds(newRadius);

            // Notify all connected belts to recalculate their tangents
            for (Belt belt : connectedBelts) {
                belt.calculateTangents();
            }

            // Update belt between selected wheel and its parent
            if (parentWheel != null && parent.selectedWheel == this) {
                for (Belt belt : parentWheel.connectedBelts) {
                    if (belt.getEnd() == this) {
                        belt.calculateTangents();
                        break;
                    }
                }
            }


        }

        // Recursive method to update child wheel rotation speeds
        void updateChildWheelSpeeds(float newRadius) {
            for (Wheel child : childWheels) {
                float childRatio = child.getRadius() / radius; // Calculate ratio of child's radius to parent's radius
                child.setRotationSpeed(rotationSpeed * childRatio); // Update child's rotation speed
                child.updateChildWheelSpeeds(newRadius); // Recursively update child wheels
            }
        }




        void setRotationSpeed(float newSpeed) {
            rotationSpeed = newSpeed;
        }


        void setColor(int newColor) {
            color = newColor;
        }

        PVector getPos() {
            return new PVector(x, y);
        }

        void addBelt(Belt belt) {
            connectedBelts.add(belt);
            if (parentWheel != null) {
                parentWheel.addBelt(belt); // Add belt to parent wheel
            }
            for (Wheel child : childWheels) {
                child.addBelt(belt); // Add belt to all child wheels recursively
            }
        }

    }

    class Belt extends MechanismCanvas{
        Wheel start, end;
        MechanismCanvas parent; // Reference to the MechanismCanvas instance
        PVector[] tangents; // Store the tangent points

        Belt(MechanismCanvas parent, Wheel start, Wheel end) {
            this.parent = parent;
            this.start = start;
            this.end = end;
            tangents = new PVector[4];
            calculateTangents();
        }

        // Used CHATGPT's Help for this
        void calculateTangents() {
            // Vector from the center of the start wheel to the center of the end wheel
            PVector d = PVector.sub(end.getPos(), start.getPos());

            // Distance between centers
            float dist = d.mag();

            // Angles to calculate
            float angleBetweenCenters = PVector.angleBetween(new PVector(1, 0), d);
            float angleOffset = PApplet.asin((end.getRadius() - start.getRadius()) / dist);

            // Outer tangents
            float angleA = angleBetweenCenters + PApplet.PI / 2;
            float angleB = angleBetweenCenters - PApplet.PI / 2;

            // Inner tangents
            float angleC = angleBetweenCenters + PApplet.PI / 2;
            float angleD = angleBetweenCenters - PApplet.PI / 2;

            // Outer tangent points on start wheel
            tangents[0] = new PVector(start.getRadius() * PApplet.cos(angleA), start.getRadius() * PApplet.sin(angleA)).add(start.getPos());
            tangents[1] = new PVector(start.getRadius() * PApplet.cos(angleB), start.getRadius() * PApplet.sin(angleB)).add(start.getPos());

            // Inner tangent points on end wheel
            tangents[2] = new PVector(end.getRadius() * PApplet.cos(angleC), end.getRadius() * PApplet.sin(angleC)).add(end.getPos());
            tangents[3] = new PVector(end.getRadius() * PApplet.cos(angleD), end.getRadius() * PApplet.sin(angleD)).add(end.getPos());
        }
        Wheel getEnd() {
            return end;
        }



        void display() {
            parent.strokeWeight(2);
            parent.stroke(0);
            // Make sure to check if tangents are not null before trying to draw them
            if (tangents[0] != null && tangents[1] != null && tangents[2] != null && tangents[3] != null) {
                parent.line(tangents[0].x, tangents[0].y, tangents[2].x, tangents[2].y);
                parent.line(tangents[1].x, tangents[1].y, tangents[3].x, tangents[3].y);
            }
        }

    }


    public static void main(String[] args) {
        PApplet.main("MechanismCanvas");
    }
}
