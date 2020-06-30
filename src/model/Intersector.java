package model;

public class Intersector {
    public Intersector(){}

    public static int intersection(Point origin, Point direction, Point point1, Point point2){
        Point v1 = origin.subtract(point1);
        Point v2 = point2.subtract(point1);
        Point v3 = new Point(-(direction.getY()), direction.getX());

        int dotProd = v2.dot(v3);
        if (Math.abs(dotProd) < 0.000001) return -1;

        int t1 = v2.cross(v1) / dotProd;
        int t2 = v1.dot(v3) / dotProd;

        if (t1 >= 0 & (t2 >= 0 & t2 <= 1)) return t1;

        return -1;
    }

    public static Point intersectionPoint(Point pOrigin, Point pDirection, int pDistance){
        int x = pOrigin.getX() + (pDirection.getX()*pDistance);
        int y = pOrigin.getY() + (pDirection.getY()*pDistance);
        return (new Point(x,y));
    }

    public static double lenght(Point pPoint){
        return Math.sqrt(Math.pow(pPoint.getX(),2)+Math.pow(pPoint.getY(), 2));
    }

    public static Point normalize(Point pPoint){
        return (pPoint.divide(lenght(pPoint)));
    }

    public static void main(String[] args) {
        Point o = new Point(1,2);
        Point d = new Point(1,0);
        Point s1 = new Point(3,5);
        Point s2 = new Point(3,1);
        Point l1 = new Point(5,3);
        Point l2 = new Point(5,1);

        int i1 = Intersector.intersection(o, normalize(d), s1, s2);
        int i2 = Intersector.intersection(o, normalize(d), l1, l2);
        System.out.println(intersectionPoint(o, d, i1));
        System.out.println(intersectionPoint(o, d, i2));
    }
}