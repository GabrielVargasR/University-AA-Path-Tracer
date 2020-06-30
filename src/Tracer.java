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

    private Tracer(){
        box = new Box();
        random = new Random();
        segments = box.getSegments();
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
        int[] pixColor;
        int[] calculatedColor;

        while (true){
            point = new model.Point(random.nextInt(IMAGE_SIZE), random.nextInt(IMAGE_SIZE)); // chooses points at random
            pixColor = new int[]{0,0,0}; // RGB value calculated for the point at a given moment inside the while loop
            
            for (int sample = 0; sample < SAMPLE_SIZE; sample++){
                // every sample is taken in random directions
                dirPoint = new model.Point(random.nextInt(IMAGE_SIZE), random.nextInt(IMAGE_SIZE));
                direction = Intersector.normalize(dirPoint.subtract(point)); // normalized for accuracy and simplifying calculations
                calculatedColor = calculatePixel(point, direction, 0);

                // adds the calculated RGB values to the current color for each sample taken
                pixColor[0] += calculatedColor[0];
                pixColor[1] += calculatedColor[1];
                pixColor[2] += calculatedColor[2];
            }

            // averages the color values received using the amount of samples taken
            pixColor[0] = (int) (pixColor[0] / SAMPLE_SIZE);
            pixColor[1] = (int) (pixColor[1] / SAMPLE_SIZE);
            pixColor[2] = (int) (pixColor[2] / SAMPLE_SIZE);

            // updates color por the pixel
            canvasImage.setRGB(point.getX(), point.getY(), (new Color(pixColor[0], pixColor[1], pixColor[2]).getRGB())); 
        }
    }

    private int[] calculatePixel(Point pOrigin, Point pDirection, int pDepthCount){

        if (pDepthCount == TRACE_DEPTH){
            // por mientras así, pero mejor poner que trace directo a algún source
            return new int[]{0,0,0}; // return black
        }

        int intersectionDistance = Integer.MAX_VALUE;
        int temp;
        model.Point normal;

        // gets the closest object in the path of the ray
        for (Point[] segment : segments){
            temp = Intersector.intersection(pOrigin, pDirection, segment[0], segment[1]);
            if (temp > 0 & temp < intersectionDistance) {
                intersectionDistance = temp;
                normal = segment[2];
            }
        }

        // if it does not intersect anything
        if (intersectionDistance == Integer.MAX_VALUE) return new int[]{0,0,0};

        // gets intersection point as well as the emittance and specularity of the surface where the ray bounces
        Point intersectionPoint = Intersector.intersectionPoint(pOrigin, pDirection, intersectionDistance);
        int[] emittance = box.getRGB(intersectionPoint.getX(), intersectionPoint.getY());
        int specularity = box.getSpecularity(intersectionPoint.getX(), intersectionPoint.getY());

        // reflection changes depending on surface type
        if (specularity == SPECULAR){
            // se calcula uno solo 
            // calcula el ángulo y lo refleja
        } else{
            // se saca uno o varios random 
        }


        
        return null;
    }

    public static void main(String[] args) {
        // Main m = new Main();
        // m.trace();

        Random rand = new Random();

        model.Point o = new Point(rand.nextInt(6), rand.nextInt(6));
        model.Point d = new Point(rand.nextInt(6), rand.nextInt(6));
        model.Point dir = d.subtract(o);
        model.Point s1 = new Point(3,5);
        model.Point s2 = new Point(3,1);
        model.Point l1 = new Point(5,3);
        model.Point l2 = new Point(5,5);

        int i1 = Intersector.intersection(o, Intersector.normalize(dir), s1, s2);
        int i2 = Intersector.intersection(o, Intersector.normalize(dir), l1, l2);

        // Al ser un grid tan pequeño, pueden hacer errores de precisión por rounding en el Point division
        System.out.println("Point: " + o + " d: "+ d);
        System.out.println("Direction: " + dir + ", Normalized: " + Intersector.normalize(dir) );
        if (i1 != -1) System.out.println("inter1: "+Intersector.intersectionPoint(o, Intersector.normalize(dir), i1));
        if (i2 != -1) System.out.println("inter2: "+Intersector.intersectionPoint(o, Intersector.normalize(dir), i2));
    }
}