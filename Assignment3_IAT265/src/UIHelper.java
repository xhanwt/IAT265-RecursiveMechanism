
import controlP5.*;
import processing.core.*;


public class UIHelper extends Canvas {

    Slider createSlider(String key, String label, float x, float y, float min, float max) {
        Slider s = cp5.addSlider(key).setPosition(x, y)
                .setRange(min, max)
                .setLabel(label)
                .setSize(100, 20);
        styleCaptionLabel(s.getCaptionLabel());
        return s;
    }

    RadioButton createRadio(String key, String[] options, float x, float y) {
        RadioButton r = cp5.addRadioButton(key).setPosition(x, y);
        for (int i=0; i<options.length; i++) {
            r.addItem(options[i], i);
        }
        styleRadioButton(r);
        return r;
    }

    void styleRadioButton(RadioButton radio) {
        radio.setItemHeight(20);
        for (Toggle t : radio.getItems()) {
            t.setSize(15, 15);
            styleCaptionLabel(t.getCaptionLabel());
        }
    }

    void styleCaptionLabel(Label l) {
        PFont font = createFont("arial", 16);
        l.setLineHeight(20).setFont(font)
                .setColor(0)
                .align(ControlP5.LEFT_OUTSIDE, CENTER);
        ;
    }

    Button createButton(String key, String label, float x, float y){
        Button b = cp5.addButton(key)
                .setLabel(label)
                .setSize(75, 50)
                .setPosition(x, y);
        styleButton(b);
        return b;
    }

    void styleButton(Button b){

    }

    public void initUI(Canvas canvas) {
        cp5 = canvas.cp5; // Assign the cp5 instance from the Canvas object
        canvas.rSlider = createSlider("redV", "Red", 150, 10, 0, 255);
        canvas.gSlider = createSlider("greenV", "Green", 150, 35, 0, 255);
        canvas.bSlider = createSlider("blueV", "Blue", 150, 60, 0, 255);
        canvas.blengthSlider = createSlider("blength", "Branch Length", 150, 85, 0, 255);
        canvas.rotSlider = createSlider("rot", "Rotation", 150, 110, -PI, PI);
        canvas.radio = createRadio("style", radioOptions, 375, 50);
        canvas.clear = createButton("clear", "Clear Screen", 450, 20);
        canvas.save = createButton("save", "Save Image", 450, 100);
    }



    void drawUISeparators() {
        line(0, 180, width, 180);
        line(275, 0, 275, 180);
        line(425, 0, 425, 180);
        pushStyle();
        fill(0);
        textAlign(CENTER);
        textFont(createFont("arial", 20));
        text("Branch Style", 350, 30);
        popStyle();
    }


}
