import processing.core.*;


public class Tree extends PApplet {


        Tree left, right;
        PVector pos, end;
        float rot, blength, bwidth;
        int c;
        int branchStyle = 0;
        PFont serif = createFont("Serif", 10);
        PFont sans = createFont("Sans Serif", 10);

        Tree(float x, float y, float bw, float bl, float rot, int depth) {
            c = color(128);
            pos = new PVector(x, y);
            PVector l = new PVector(0, -bl);
            l.rotate(rot);
            end = new PVector(pos.x + l.x, pos.y + l.y);
            blength = bl;
            bwidth = bw;
            this.rot = rot;

            //max depth 5
            if (depth < 5) {

                if (depth == 4) {
                    left = new Leaf(end.x, end.y, 10, depth+1);
                    right = new Leaf(end.x, end.y, 10, depth+1);
                } else {
                    left = new Tree(end.x, end.y, bw * 0.7f, bl * 0.7f, (float) (rot - Math.PI / 6), depth + 1);
                    right = new Tree(end.x, end.y, bw * 0.7f, bl * 0.7f, (float) (rot + Math.PI / 6), depth + 1);
                }
            }
        }

        void drawMe() {
            pushStyle();
            pushMatrix();

            if (branchStyle == 0) {
                strokeWeight(bwidth);
                stroke(c);
                line(pos.x, pos.y, end.x, end.y);
            }else if(branchStyle == 1){
                drawLetter(serif);
            }else {
                drawLetter(sans);
            }



            popMatrix();
            popStyle();
            if (left != null) {
                left.drawMe();
            }

            if (right!=null) {
                right.drawMe();
            }
        }

        Tree pickMe(float mX, float mY) {
            float minX = min(pos.x, end.x)-bwidth/2;
            float maxX = max(pos.x, end.x)+bwidth/2;
            float minY = min(pos.y, end.y)-bwidth/2;
            float maxY = max(pos.y, end.y)+bwidth/2;

            if (mX > minX && mX < maxX && mY > minY && mY < maxY) {
                return this;
            } else {
                Tree s = null;

                if (left != null) {
                    s = left.pickMe(mX, mY);
                }

                if (s == null && right != null) {
                    s = right.pickMe(mX, mY);
                }

                //
                if (s!= null) {
                    return s;
                } else {
                    return null;
                }
            }
        }

        void setBlength(float newL) {
            this.blength = newL;
            updatePoints(pos.x, pos.y);

            if (left != null) {
                left.updatePoints(end.x, end.y);
                left.setBlength(left.blength);
            }

            if (right != null) {
                right.updatePoints(end.x, end.y);
                right.setBlength(right.blength);
            }
        }

        void updatePoints(float startx, float starty) {
            pos.x = startx;
            pos.y = starty;
            PVector l = new PVector(0, -blength);
            l.rotate(rot);
            end = new PVector(pos.x + l.x, pos.y + l.y);
        }

        void setRotation(float newR) {
            float diffRot = newR - this.rot;

            this.rot = newR;

            PVector l = new PVector(0, -blength);
            l.rotate(rot);
            end = new PVector(pos.x + l.x, pos.y + l.y);

            if (left != null) {
                //left.rot = left.rot + diffRot;
                left.pos.x = end.x;
                left.pos.y = end.y;
                left.setRotation(left.rot + diffRot);
            }

            if (right!=null) {
                right.pos.x = end.x;
                right.pos.y = end.y;
                right.setRotation(right.rot + diffRot);
            }
        }

        void drawLetter(PFont f){
            textAlign(CENTER);
            textFont(f);
            textSize((float) (blength * 1.5));
            translate(pos.x, pos.y);
            fill(c);
            rotate(this.rot);
            text("T", 0, 0);
        }
    }




