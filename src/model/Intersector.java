package model;

public class Intersector {
    public Intersector(){}

    public static double intersection(Point origin, Point direction, Point point1, Point point2){
        Point v1 = origin.subtract(point1);
        Point v2 = point2.subtract(point1);
        Point v3 = new Point(-(direction.getY()), direction.getX());

        double dotProd = v2.dot(v3);
        if (Math.abs(dotProd) < 0.000001) return -1;

        double t1 = (double)v2.cross(v1) / (double)dotProd;
        double t2 = (double)v1.dot(v3) / (double)dotProd;
        if (t1 >= 0.0 && (t2 >= 0.0 && t2 <= 1.0)) return t1;

        return -1;
    }
    public static double intersection2(Point origin, Point direction, Point point1, Point point2){
        int x1 = (int)point1.getX();
        int y1 = (int)point1.getY();
        int x2 = (int)point2.getX();
        int y2 = (int)point2.getY();
    
        int x3 = (int)origin.getX();
        int y3 = (int)origin.getY();
        int x4 = x3 + (int)direction.getX();
        int y4 = y3 + (int)direction.getY();
    
        double den = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);
        if (den < 0.000001) {
          return -1;
        }
    
        double t = (double)((x1 - x3) * (y3 - y4) - (y1 - y3) * (x3 - x4)) / (double)den;
        double u = -(double)((x1 - x2) * (y1 - y3) - (y1 - y2) * (x1 - x3)) / (double)den;
        if (t > 0.0 && t < 1.0 && u > 0.0) {
          return u;
        } 
        else {
          return -1;
        }
    }


    public static Point intersectionPoint(Point pOrigin, Point pDirection, double pDistance){
        int x = (int)pOrigin.getX() + (int)((int)pDirection.getX()*pDistance);
        int y = (int)pOrigin.getY() + (int)((int)pDirection.getY()*pDistance);
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


    // method was added from Tracer for testing purposes
    public static Point opaqueReflectionPoint(Point pPoint, Point pSeg1, Point pSeg2){
        java.util.Random random = new java.util.Random();
        boolean horizontal; // line is horizontal
        boolean before = false; // point is before or below the segment

        if (pSeg1.getX() == pSeg2.getX()){
            horizontal = false;
            if (pPoint.getX() < pSeg1.getX()) before = true;
        } else { // segment has same y value
            horizontal = true;
            if (pPoint.getY() < pSeg1.getY()) before = true;
        }

        if (horizontal){
            if (before){
                int adjust = 10 - (int)pSeg1.getY();
                return (new Point(random.nextInt(10), random.nextInt((int)pSeg1.getY())+adjust));
            } else{
                return (new Point(random.nextInt(10), random.nextInt((int)pSeg1.getY())));
            }
        } else{
            if (before){
                return (new Point(random.nextInt((int)pSeg1.getX()), random.nextInt(10)));
            } else{
                int adjust = 10 - (int)pSeg1.getX();
                return (new Point(random.nextInt((int)pSeg1.getX())+adjust, random.nextInt(10)));
            }
        }
     }
    public static void main(String[] args) {
        Point origin = new Point(230,0);
        Point dir = new Point(-1,0);
        Box b = new Box();
        Double distance = 999.0;
        Point a;
        for (Point[] segment : b.getSegments()) {
            
             distance = intersection(origin, dir, segment[0], segment[1]);
             a = intersectionPoint(origin, dir, distance);
             System.out.println("Origin: "+origin.toString()+" Direction: "+dir+"  Seg[0]"+segment[0]+" Seg[1]"+segment[1]+" Distance"+distance);
             //System.out.println(a+","+distance);
        }
    }
    // public static void main(String[] args) {
    //     Point[] opSegment = new Point[] {new Point(5,1), new Point(5,6), new Point(-1,0)}; // opaque segment
    //     Point[] specSegment = new Point[] {new Point(4,1), new Point(4,6), new Point(1,0)}; // specular segment
    //     Point[][] segments = new Point[][] {opSegment, specSegment};
    //     // the normals to the segments were chosen to go in opposit directions for testing purposes

    //     // Points that will be used as origins to the rays
    //     Point left = new Point(3,1); // point to the left of the segments
    //     Point right = new Point(7,4); // point to the right of the segments

    //     // General direction vectors
    //     Point east = new Point(1,0);
    //     Point west = new Point(-1,0);
    //     Point north = new Point(0,1);
    //     Point south = new Point(0,-1);
    //     Point ne = new Point(1,1);
    //     Point nw = new Point(1,-1);
    //     Point se = new Point(-1,1);
    //     Point sw = new Point(-1,-1);


    //     double left2spec = intersection(left, ne, specSegment[0], specSegment[1]);
    //     Point l2sPoint = intersectionPoint(left, ne, left2spec);
    //     System.out.println("Intersection distance from point left to specSegment: " + left2spec);
    //     System.out.println("Intersection point between point left to specSegment: " + l2sPoint.getX() + ", " + l2sPoint.getY());

    //     double left2op = intersection(left, ne, opSegment[0], opSegment[1]);
    //     Point l2oPoint = intersectionPoint(left, ne, left2op);
    //     System.out.println("Intersection distance from point left to opSegment: " + left2op);
    //     System.out.println("Intersection point between point left to opSegment: " + l2oPoint.getX() + ", " + l2oPoint.getY());

    //     System.out.println("Direction of reflected left with ne: " + reflect(ne, specSegment[2]));

    //     double right2op = intersection(right, sw, opSegment[0], opSegment[1]);
    //     Point r2oPoint = intersectionPoint(right, sw, right2op);
    //     System.out.println("Intersection distance from point right to opSegment: " + right2op);
    //     System.out.println("Intersection point between point right to opSegment: " + r2oPoint.getX() + ", " + r2oPoint.getY());

    //     double intersectionDistance = Integer.MAX_VALUE;
    //     double temp;
    //     Point[] seg = null;

    //     // gets the closest object in the path of the ray
    //     for (Point[] segment : segments){
    //         temp = Intersector.intersection(right, west, segment[0], segment[1]); // can be tested with left, ne for instance
    //         if (temp > 0 & temp < intersectionDistance) {
    //             seg = segment;
    //             intersectionDistance = temp;
    //         }
    //     }

    //     boolean inter = (seg[0].getX() == 4) ? false:true;
    //     if (inter){
    //         System.out.println("Closest segment if opSegment");
    //     } else System.out.println("Closest segment if specSegment");

    //     Point randomDirPoint = opaqueReflectionPoint(right, opSegment[0], opSegment[1]);
    //     System.out.println("Reflected direction = " + normalize(randomDirPoint.subtract(r2oPoint)));
    // }
}