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

    public static double length(Point pPoint){
        return Math.sqrt(Math.pow(pPoint.getX(),2)+Math.pow(pPoint.getY(), 2));
    }

    public static Point normalize(Point pPoint){
        return (pPoint.divide(length(pPoint)));
    }

    public static double angle(Point pPoint, Point pNormalDir, Point pIntersection){
        Point v1 = pPoint.subtract(pIntersection);
        Point v2 = pNormalDir;

        double cosT = v1.dot(v2) / (Intersector.length(v1) * Intersector.length(v2));
        cosT = Math.abs(cosT);
        return Math.acos(cosT);
    }

    public static Point reflect(Point pIncoming, Point pNormal){
        // https://math.stackexchange.com/questions/13261/how-to-get-a-reflection-vector
        // reflected = incoming - 2(incoming . normal) * normal
        // pNormal must be normalized
        Point p = pNormal.multiply((2 * pIncoming.dot(pNormal)));
        return pIncoming.subtract(p);
    }
}