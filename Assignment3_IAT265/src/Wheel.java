
public class Wheel {
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
}