class Leaf extends Tree {
    float r;
    Leaf(float x, float y, float r, int depth) {
        //Tree(float x, float y, float bw, float bl, float rot, int depth)
        super(x, y, 0f, 0f, 0f, depth);
        this.r = r;
    }

    void drawMe() {
        pushStyle();
        fill(c);
        ellipse(pos.x, pos.y, r, r);
        popStyle();
    }
}