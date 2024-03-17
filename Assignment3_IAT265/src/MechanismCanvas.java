import controlP5.*;
import processing.core.*;

public class MechanismEditor extends PApplet {
    Mechanism mechanism;
    ControlP5 cp5;
    Wheel selectedWheel;

    public void settings() {
        size(800, 600);
    }

    public void setup() {
        cp5 = new ControlP5(this);
        mechanism = new Mechanism();
        selectedWheel = null;

        // Add controls for selecting parts, clearing screen, etc.
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
        mechanism.display();
    }

    public void keyPressed() {
        if (selectedWheel != null) {
            // Adjust properties of the selected wheel based on keyboard input
            if (key == 'r') {
                selectedWheel.setColor(color(255, 0, 0)); // Change color to red
            }
            if (key == '+') {
                selectedWheel.setRadius(selectedWheel.getRadius() + 5); // Increase radius
            }
            if (key == '-') {
                selectedWheel.setRadius(max(5, selectedWheel.getRadius() - 5)); // Decrease radius, with a minimum of 5
            }
        }
    }

    public void mousePressed() {
        // Check if the mouse is clicked on a wheel
        for (Wheel wheel : mechanism.getAllWheels()) {
            if (dist(mouseX, mouseY, wheel.getX(), wheel.getY()) < wheel.getRadius()) {
                selectedWheel = wheel;
                break;
            }
        }
    }

    public void clear() {
        mechanism.clear();
    }

    public void saveImage() {
        save("mechanism.png");
    }

    class Mechanism {
        Wheel root;

        Mechanism() {
            root = new Wheel(100, 100, 50, color(255, 0, 0));
            Wheel child1 = new Wheel(250, 150, 30, color(0, 0, 255));
            Wheel child2 = new Wheel(150, 250, 20, color(0, 255, 0));
            Belt belt1 = new Belt(root, child1);
            Belt belt2 = new Belt(root, child2);
        }

        void display() {
            root.display();
        }

        void clear() {
            root = null;
        }

        // Get all wheels in the mechanism
        Wheel[] getAllWheels() {
            Wheel[] wheels = {root};
            return wheels;
        }
    }

    class Wheel {
        float x, y;
        float radius;
        int color;

        Wheel(float x, float y, float radius, int color) {
            this.x = x;
            this.y = y;
            this.radius = radius;
            this.color = color;
        }

        void display() {
            fill(color);
            ellipse(x, y, radius * 2, radius * 2);
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

        void setRadius(float radius) {
            this.radius = radius;
        }

        void setColor(int color) {
            this.color = color;
        }
    }

    class Belt {
        Wheel start, end;

        Belt(Wheel start, Wheel end) {
            this.start = start;
            this.end = end;
        }

        void display() {
            stroke(0);
            line(start.x, start.y, end.x, end.y);
        }
    }

    public static void main(String[] args) {
        String[] processingArgs = {"MechanismEditor"};
        MechanismEditor editor = new MechanismEditor();
        PApplet.runSketch(processingArgs, editor);
    }
}
