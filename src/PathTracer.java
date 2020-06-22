import model.Box;
import model.Intersector;
import model.Point;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.Vector;

public class PathTracer {
    BufferedImage originalImage;
    BufferedImage canvasImage;
    Box box;
    Random random;

    boolean IsRunning;
    int imageSize;

    public PathTracer(BufferedImage pOriginalImage, BufferedImage pCanvasImage, Box pBox){
        originalImage = pOriginalImage;
        canvasImage = pCanvasImage;
        box = pBox;
        imageSize = canvasImage.getHeight();
        random = new Random();
    }

    public void PathTrace(){
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
                    int originalColor = originalImage.getRGB(point.getX(),point.getY());
                    double newColor = originalColor*intensity* Color.YELLOW.getRGB();
                    colorValue+=Math.ceil(newColor);
                    System.out.println(colorValue);
                }
                int averageColorValue = colorValue / box.getSources().size();
                canvasImage.setRGB(point.getX(),point.getY(),averageColorValue);
                //System.out.println(averageColorValue);
            }
            //canvasImage.setRGB(point.getX(),point.getY(),Color.WHITE.getRGB());
            //System.out.println(point.toString());
        }
    }
    
    public void Stop(){
        IsRunning = false;
    }

    private model.Point getRandomPoint(){
        return new Point(random.nextInt(imageSize),random.nextInt(imageSize));
    }
}
