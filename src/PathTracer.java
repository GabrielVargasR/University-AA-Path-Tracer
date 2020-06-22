import model.Box;
import model.Intersector;
import model.Point;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.Vector;

public final class PathTracer {
    private static BufferedImage canvasImage;
    private static Box box;
    private static Random random;

    private static boolean IsRunning;
    private static int imageSize;
    private static PathTracer tracer;

    private static void PathTracer(BufferedImage pCanvasImage, Box pBox){
        canvasImage = pCanvasImage;
        box = pBox;
        imageSize = canvasImage.getHeight();
        random = new Random();
    }

    public static PathTracer getInstance(){
        return tracer;
    }

    public static void createInstance(BufferedImage pCanvasImage, Box pBox){
        PathTracer(pCanvasImage, pBox);
    }

    public static void pathTrace(){
        model.Point point;
        int colorValue;
        for (int i = 0; i < 100000;i++){
            point = getRandomPoint();
            colorValue = 0;
            for (Point source:box.getSources()) {
                Point direction = source.subtract(point);
                double distance = Intersector.lenght(direction);

                boolean intersectionExist = false;
                for (Point[] segment:box.getSegments()) {
                    int intersectionDistance = Intersector.intersection(point,direction,segment[0],segment[1]);
                    if(intersectionDistance != -1 && intersectionDistance < distance){
                        intersectionExist = true;
                        break;
                    }
                }
                if (intersectionExist){
                    double intensity = Math.pow((1-(distance/500)),2);
                    int originalColor = box.getRGB(point.getX(),point.getY());
                    double newColor = originalColor*intensity* Color.YELLOW.getRGB();
                    colorValue+=Math.ceil(newColor);
                    // System.out.println(colorValue);
                }
                int averageColorValue = colorValue / box.getSources().size();
                canvasImage.setRGB(point.getX(),point.getY(),averageColorValue);
                //System.out.println(averageColorValue);
            }
            //canvasImage.setRGB(point.getX(),point.getY(),Color.WHITE.getRGB());
            //System.out.println(point.toString());
        }
    }
    
    public static void Stop(){
        IsRunning = false;
    }

    private static model.Point getRandomPoint(){
        return new Point(random.nextInt(imageSize),random.nextInt(imageSize));
    }
}
