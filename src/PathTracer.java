import model.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.ArrayList;

public class PathTracer implements IConstants{
    private static PathTracer instance;
    private Box box;
    private int size;
    private BufferedImage canvasImage;
    private Random random;
    private ArrayList<model.Point[]> segments;
    private ArrayList<model.Point> sources;

    private PathTracer(){
        box = new Box();
        segments = box.getSegments();
        sources = box.getSources();
        random = new Random();
    }

    public static PathTracer getInstance(){
        if (instance == null) instance = new PathTracer();
        return instance;
     }

    public void setImage(BufferedImage pImage){
        canvasImage = pImage;
        size = canvasImage.getWidth();
    }

    public void tracePath(){

        model.Point point;
        int[] pixColor;

        model.Point direction;
        double length;
        double intensity;
        int[] pixValue;

        int distance;
        boolean free;

        while (true){
            point = new model.Point(random.nextInt(size), random.nextInt(size));
            pixColor = new int[]{0,0,0};

            for (model.Point source : sources){
                direction = source.subtract(point);
                length = Intersector.length(direction);
                free = true;

                for(model.Point[] segment : segments){
                    distance = Intersector.intersection(point, Intersector.normalize(direction), segment[0], segment[1]);

                    if (distance != -1 & distance < length){
                        free = false;
                        break;
                    }
                }

                if (free){
                    // intensity = Math.pow((1-(length / IMAGE_SIZE)), 2);
                    intensity = 1-(length / IMAGE_SIZE);
                    pixValue = box.getRGB(point.getX(), point.getY());
                    pixValue = updateColor(pixValue, intensity);

                    pixColor[0] += pixValue[0];
                    pixColor[1] += pixValue[1];
                    pixColor[2] += pixValue[2];
                }

                // box.updateSceneAt(point.getX(), point.getY(), pixColor);
                updateImage(point.getX(), point.getY(), pixColor);
                // canvasImage.setRGB(point.getX(), point.getY(), (new Color(pixColor[0]/SOURCES, pixColor[2]/SOURCES, pixColor[2]/SOURCES).getRGB()));
            }
        }
    }

    private int[] updateColor(int[] pCurrent, double pIntensity){
        int red = (int) (pCurrent[0] + (pIntensity));
        int green = (int) (pCurrent[1] + (pIntensity));
        int blue = (int) (pCurrent[2] + (pIntensity * 0.75));
        return new int[]{red, green, blue};
    }

    private void updateImage(int pX, int pY, int[] pPixColor){
        // System.out.println(rgbValues[0] +", "+rgbValues[1]+", "+ rgbValues[2]);
        Color color = new Color(pPixColor[0]/(SOURCES+1), pPixColor[2]/(SOURCES+1), pPixColor[2]/(SOURCES+1));
        canvasImage.setRGB(pX, pY, color.getRGB());
    }

    public static void main(String[] args) {
        Main m = new Main();
        m.trace();
    }
}
