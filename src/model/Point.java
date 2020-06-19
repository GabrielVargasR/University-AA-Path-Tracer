package model;

public class Point {
    private int x;
    private int y;

    public Point(int pX, int pY){
        this.x = pX;
        this.y = pY;
    }

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }

    public Point add(Point pOther){
        return (new Point(this.x + pOther.getX(), this.y+pOther.getY()));
    }

    public Point subtract(Point pOther){
        return (new Point(this.x-pOther.getX(), this.y-pOther.getY()));
    }   

    public Point divide(double pScalar){
        int newX = (int) Math.round(this.x/pScalar);
        int newY = (int) Math.round(this.y/pScalar);
        return (new Point(newX, newY));
    }

    public int dot(Point pOther){
        return (this.x*pOther.getX())+(this.y*pOther.getY());
    }

    public int cross(Point pOther){
        return (this.x*pOther.getY())-(this.y*pOther.getX());
    }

    @Override
    public String toString(){
        return "" + this.x + ", " + this.y;
    }
}