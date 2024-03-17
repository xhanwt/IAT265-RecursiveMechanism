import processing.core.PApplet;

import controlP5.*;


//public class Main {
//    public static void main(String[] args) {
//        PApplet.main(MechanismCanvas.class.getName());
//    }
//}

public static void main(String[] args) {
    String[] processingArgs = {"MechanismEditor"};
    MechanismEditor editor = new MechanismEditor();
    PApplet.runSketch(processingArgs, editor);
}
}
