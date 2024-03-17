import controlP5.*;
import processing.core.*;

public class Canvas extends PApplet {

    ControlP5 cp5;

    Tree selected; //the clicked component
    Slider rSlider, gSlider, bSlider, blengthSlider, rotSlider;
    RadioButton radio;
    Button clear, save;
    String[] radioOptions = {"Line", "Serif", "Sans Serif"};
    Tree myTree;
    UIHelper uiHelper;

    public void settings() {
        size(600, 600);
    }

    public void setup() {
        cp5 = new ControlP5(this);
        uiHelper = new UIHelper();
        uiHelper.initUI(this); // Pass the Canvas object to initUI method
        myTree = new Tree(width / 2, height, 20, 150, 0, 0);
    }

    public void draw() {
        background(255);
        uiHelper.drawUISeparators();
        myTree.drawMe();
    }

    void drawMyTree() {
    }

    void redV(float r) {
        if (selected != null) {
            selected.c = color(r, green(selected.c), blue(selected.c));
        }
    }

    void greenV(float g) {
        if (selected != null) {
            selected.c = color(red(selected.c), g, blue(selected.c));
        }
    }

    void blueV(float b) {
        if (selected != null) {
            selected.c = color(red(selected.c), green(selected.c), b);
        }
    }

    void blength(float bl) {
        if (selected != null) {
            selected.setBlength(bl);
        }
    }

    void rot(float r) {
        if (selected != null) {
            selected.setRotation(r);
        }
    }

    void style(int option) {
        if (selected != null && option > 0) {
            selected.branchStyle = option;
        }
    }

    public void clear() {
        println("clear screen");
    }

    void save() {
        println("save image");
    }

    public void mousePressed() {
        if (mouseY > 180) {
            selected = myTree.pickMe(mouseX, mouseY);

            if (selected != null) {
                bSlider.setValue(blue(selected.c));
                rSlider.setValue(red(selected.c));
                gSlider.setValue(green(selected.c));
                blengthSlider.setValue(selected.blength);
                rotSlider.setValue(selected.rot);
                radio.activate(selected.branchStyle);
            }
        }
    }

}



