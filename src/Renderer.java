import model.*;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.ArrayList;

public class Renderer implements IConstants {
    private static Renderer instance;
    private Box box;
    private BufferedImage canvasImage;
    private Random random;
    private ArrayList<Point[]> segments;
    private ArrayList<Point> sources;
    
    public static Renderer getInstance(){
        if(instance == null) instance = new Renderer();
        return instance;
    }

    public void setImage(BufferedImage pImage){
        canvasImage = pImage;
    }

    public void render(){
        Point point;
        double[] pixelRGB;

        while (true) {
            point = getRandomPoint();
            pixelRGB = castRays(point,0);
            //divideColorBy(pixelRGB, TRACE_DEPTH);
            Color pixelColor = new Color((int)pixelRGB[0], (int)pixelRGB[1], (int)pixelRGB[2]);
            canvasImage.setRGB((int)point.getX(), (int)point.getY(), pixelColor.getRGB()); 
        }
    }
    private double[] castRays(Point pOrigin,int pDepthCount){
        double[] directColor = new double[3];
        double[] indirectColor = new double[3];
        double[] color = new double[3];
        //DirectLigh
        for (Point source : sources) {
            double distance = 0;
            Point dirToSource = source.subtract(pOrigin);
            double length = Intersector.length(dirToSource);
            boolean free = true;
            for (Point[] segment : segments){
                distance = Intersector.intersection(pOrigin, Intersector.normalize(dirToSource), segment[0], segment[1]);
                if (distance != -1 && distance < length) {
                    free = false;
                    break;
                }
            }
            if(free){
                double intensity = getIntensity(length);
                //double intensity = 1;
                int[] originalRGB = box.getRGB((int)pOrigin.getX(), (int)pOrigin.getY());

                directColor[0] += originalRGB[0] * intensity * ((double)LIGHT_R/255);
                directColor[1] += originalRGB[0] * intensity * ((double)LIGHT_G/255);
                directColor[2] += originalRGB[0] * intensity * ((double)LIGHT_B/255);
            } 
        }
        directColor[0] /= SOURCES;
        directColor[1] /= SOURCES;
        directColor[2] /= SOURCES;
        //IndirectLigh
        color[0] += directColor[0];
        color[1] += directColor[0];
        color[2] += directColor[0];
        return color;

    }
    private double getIntensity(double pLenght) {
        double intensity = 1 - (pLenght/(double)IMAGE_SIZE);
        return Math.pow(intensity, 2);
        
    }

    private void divideColorBy(double[] pColor,double pDivisor){
        pColor[0] = pColor[0]/pDivisor;
        pColor[1] = pColor[1]/pDivisor;
        pColor[2] = pColor[2]/pDivisor;
    }

    private Point getRandomPoint() {
        int randomX = random.nextInt(IMAGE_SIZE);
        int randomY = random.nextInt(IMAGE_SIZE);
        return new model.Point(randomX, randomY);
    }

    private Renderer() {
        box = new Box();
        random = new Random();
        segments = box.getSegments();
        sources = box.getSources();
    }

}