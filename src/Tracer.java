import model.*;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.ArrayList;

public class Tracer implements IConstants{
    private static Tracer instance;
    private Box box;
    private BufferedImage canvasImage;
    private Random random;
    private ArrayList<Point[]> segments;
    private ArrayList<Point> sources;

    private Tracer(){
        box = new Box();
        random = new Random();
        segments = box.getSegments();
        sources = box.getSources();
    }

    public static Tracer getInstance(){
        if (instance==null) instance = new Tracer();
        return instance;
    }

    public void setImage(BufferedImage pImage){
        canvasImage = pImage;
    }

    public void tracePath(){
        model.Point point;
        model.Point dirPoint;
        model.Point direction;
        double[] pixColor;
        double[] calculatedColor;
        boolean iluminated = false;

        while (true){
            point = new model.Point(random.nextInt(IMAGE_SIZE), random.nextInt(IMAGE_SIZE)); // chooses points at random
            pixColor = new double[]{0,0,0}; // RGB value calculated for the point at a given moment inside the while loop
            
            for (int sample = 0; sample < SAMPLE_SIZE; sample++){
                // every sample is taken in random directions
                iluminated = false;
                dirPoint = new model.Point(random.nextInt(IMAGE_SIZE), random.nextInt(IMAGE_SIZE));
                direction = Intersector.normalize(dirPoint.subtract(point)); // normalized for accuracy and simplifying calculations
                calculatedColor = calculatePixel(point, direction, 0);

                if (calculatedColor[0] == -1 & calculatedColor[1] == -1 & calculatedColor[2] == -1) {
                    continue;
                } else {
                    // adds the calculated RGB values to the current color for each sample taken
                    iluminated = true;
                    pixColor[0] += calculatedColor[0]/TRACE_DEPTH;
                    pixColor[1] += calculatedColor[1]/TRACE_DEPTH;
                    pixColor[2] += calculatedColor[2]/TRACE_DEPTH;
                }   
            }

            if (iluminated){
                // averages the color values received using the amount of samples taken
                pixColor[0] =  (pixColor[0] / SAMPLE_SIZE);
                pixColor[1] =  (pixColor[1] / SAMPLE_SIZE);
                pixColor[2] =  (pixColor[2] / SAMPLE_SIZE);

                // updates color por the pixel
                try{
                    // System.out.println(pixColor[0] + ", " + pixColor[1] + ", " + pixColor[2]);
                    canvasImage.setRGB(point.getX(), point.getY(), (new Color((int)pixColor[0], (int)pixColor[1], (int)pixColor[2]).getRGB()));
                } 
                catch(Exception e){
                    System.out.println("Error color mas de 255");
                    System.out.println(pixColor[0] + ", " + pixColor[1] + ", " + pixColor[2]);
                }
            } else{
                canvasImage.setRGB(point.getX(), point.getY(), Color.BLACK.getRGB());
            }
        }
    }

    private double[] calculatePixel(Point pOrigin, Point pDirection, int pDepthCount){
        if (pDepthCount > TRACE_DEPTH){
            return castToSource(pOrigin);
        }

        double intersectionDistance = Double.MAX_VALUE;
        double temp;
        Point[] seg = null;
        model.Point normal = null;

        // gets the closest object in the path of the ray
        for (Point[] segment : segments){
            temp = Intersector.intersection(pOrigin, pDirection, segment[0], segment[1]);
            if (temp >= 0 & temp < intersectionDistance) {
                seg = segment;
                intersectionDistance = temp;
                normal = segment[2];
            }
        }

        // if it does not intersect anything
        if (seg == null){
            if(pOrigin.getY() !=500){
                System.out.println("Origin: "+pOrigin.toString()+" Direction: "+pDirection);
                
            }
            
            return new double[]{0,0,0};
        }

        // gets intersection point as well as the emittance and specularity of the surface where the ray bounces
        Point intersectionPoint = Intersector.intersectionPoint(pOrigin, pDirection, intersectionDistance);

        // check if intersection is inside of the frame
        if (!isInside(intersectionPoint)) return new double[]{0,0,0};

        int[] emittance = box.getRGB(pOrigin.getX(), pOrigin.getY());
        double intensity = 1 - (intersectionDistance/(double)IMAGE_SIZE);
        intensity = Math.pow(intensity, 2);
        int specularity = box.getSpecularity(intersectionPoint.getX(), intersectionPoint.getY());

        double[] color = new double[]{0,0,0};
        // reflection changes depending on surface type
        if (specularity == SPECULAR){
            // calculates direction of reflected ray and recursively calculates values for the pixel
            Point reflectedDir = Intersector.reflect(pDirection, normal);
            color =  calculatePixel(intersectionPoint, Intersector.normalize(reflectedDir), ++pDepthCount);
            if (color[0] == -1 & color[1] == -1 & color[2] == -1) return color;
        }
        else{
            // calculates some of the many rays reflected by the Opaque surface
            Point sample;
            double[] tempColor = new double[]{0,0,0};
            boolean iluminated = false;
            
            for (int sampleCount = 0; sampleCount < SAMPLE_SIZE; sampleCount++){
                sample = opaqueReflectionPoint(pOrigin, seg[0], seg[1]);
                tempColor = calculatePixel(intersectionPoint, Intersector.normalize(sample.subtract(intersectionPoint)), ++pDepthCount);

                if (tempColor[0] == -1 & tempColor[1] == -1 & tempColor[2] == -1) continue;

                iluminated = true;
                color[0] += tempColor[0];
                color[1] += tempColor[1];
                color[2] += tempColor[2];
            }

            if (!iluminated) return new double[]{-1,-1,-1};
            color[0] = (color[0] / SAMPLE_SIZE);
            color[1] = (color[1] / SAMPLE_SIZE);
            color[2] = (color[2] / SAMPLE_SIZE);
        }
        color[0] = ((intensity * color[0]) + (double)emittance[0])/2;
        color[1] = ((intensity * color[1]) + (double)emittance[1])/2;
        color[2] = ((intensity * color[2]) + (double)emittance[2])/2;
       //System.out.println(color[0] + ", " + color[1] + ", " + color[2]);

        return color;
    }

    private Point opaqueReflectionPoint(Point pPoint, Point pSeg1, Point pSeg2){
        boolean horizontal; // line is horizontal
        boolean before = false; // point is before or below the segment

        if (pSeg1.getX() == pSeg2.getX()){
            horizontal = false;
            if (pPoint.getX() < pSeg1.getX()) before = true;
        } else { // segment has same y value
            horizontal = true;
            if (pPoint.getY() > pSeg1.getY()) before = true;
        }

        if (horizontal){
            int y = (pSeg1.getY() != 0) ? pSeg1.getY() : 1;
            if (before){
                int adjust = IMAGE_SIZE - pSeg1.getY();
                return (new Point(random.nextInt(IMAGE_SIZE), random.nextInt(++y)+adjust));
            } else{
                return (new Point(random.nextInt(IMAGE_SIZE), random.nextInt(y)));
            }
        } else{
            if (before){
                return (new Point(random.nextInt(pSeg1.getX()+1), random.nextInt(IMAGE_SIZE)));
            } else{
                int adjust = IMAGE_SIZE - pSeg1.getX();
                return (new Point(random.nextInt(pSeg1.getX()+1)+adjust, random.nextInt(IMAGE_SIZE)));
            }
        }
    }

    private boolean isInside(Point pIntersection){
        return (pIntersection.getX() >= 0 & pIntersection.getY() >= 0 & pIntersection.getX() <= IMAGE_SIZE & pIntersection.getY() <= IMAGE_SIZE);
    }
   
    private double[] castToSource(Point pPoint){
        // returns color reflected by the point due to the light shining on it

        double[] color = new double[]{0,0,0};
        int[] emittance = box.getRGB(pPoint.getX(), pPoint.getY());
        Point direction;
        double distance;
        double intensity;
        boolean intersects;
        int sourceAmount = 0;

        for (Point source : sources){
            direction = source.subtract(pPoint);
            intersects = false;

            for (Point[] segment : segments){
                if (Intersector.intersection(pPoint, Intersector.normalize(direction), segment[0], segment[1]) != -1) {
                    intersects = true;
                    break;
                }
            }

            if (!intersects){
                // calculates the distance between the point and the source to later calculate the intensity of the light
                sourceAmount++;

                distance = Intersector.length(source.subtract(pPoint));
                intensity = 1 - (distance/IMAGE_SIZE);
                intensity = Math.pow(intensity, 2);

                //Calculate color
                color[0] += ((intensity * (double)LIGHT_R) + (double)emittance[0])/2;
                color[1] += ((intensity * (double)LIGHT_G) + (double)emittance[1])/2;
                color[2] += ((intensity * (double)LIGHT_B) + (double)emittance[2])/2;
            }
        }

        if (sourceAmount>0){
            color[0] = color[0] / sourceAmount;
            color[1] = color[1] / sourceAmount;
            color[2] = color[2] / sourceAmount;
        } else {
            return (new double[] {-1,-1,-1});
        }

         //System.out.println(color[0] + ", " + color[1] + ", " + color[2]);
        return color;
    }

    public static void main(String[] args) {
        Main m = new Main();
        m.trace();
    }
}