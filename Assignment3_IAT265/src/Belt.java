public class Belt {
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


