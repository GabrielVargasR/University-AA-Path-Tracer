package model;

public class Point {
    private double x;
    private double y;

    public Point(double pX, double pY){
        this.x = pX;
        this.y = pY;
    }

    public double getX(){
        return this.x;
    }

    public double getY(){
        return this.y;
    }
    public void setX(double x) {
        this.x = x;
    }
    public void setY(double y) {
        this.y = y;
    }

    public Point add(Point pOther){
        return (new Point(this.x + pOther.getX(), this.y+pOther.getY()));
    }

    public Point subtract(Point pOther){
        return (new Point(this.x-pOther.getX(), this.y-pOther.getY()));
    }

    public Point multiply(double pScalar){
        return (new Point(this.x*pScalar, this.y*pScalar));
    }

    public Point divide(double pScalar){
        double newX = this.x/pScalar;
        double newY = this.y/pScalar;
        return (new Point(newX, newY));
    }

    public double dot(Point pOther){
        return (this.x*pOther.getX())+(this.y*pOther.getY());
    }

    public double cross(Point pOther){
        return (this.x*pOther.getY())-(this.y*pOther.getX());
    }

    @Override
    public String toString(){
        return "" + this.x + ", " + this.y;
    }
}