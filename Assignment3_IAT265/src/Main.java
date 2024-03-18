import processing.core.PApplet;

import controlP5.*;


//public class Main {
//    public static void main(String[] args) {
//        PApplet.main(MechanismCanvas.class.getName());
//    }
//}
public class Main {
public static void main(String[] args) {
    String[] processingArgs = {"MechanismCanvas"};
    MechanismCanvas editor = new MechanismCanvas();
    PApplet.runSketch(processingArgs, editor);
}
}

