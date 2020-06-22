import model.Box;
import model.Intersector;
import model.Point;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class PathTracer {
    private static PathTracer instance;
    private BufferedImage canvasImage;
    private Box box;
    private Random random;
    private int imageSize;
    private boolean IsRunning;

    public static PathTracer getInstance() {
        return instance;
    }

    public static void createInstance(BufferedImage pCanvasImage, Box pBox) {
        instance = new PathTracer(pCanvasImage, pBox);
    }

    public void pathTrace() {
        model.Point point;
        int colorValue;
        IsRunning = true;
        while(IsRunning) {
            point = getRandomPoint();
            colorValue = 0;
            for (Point source : box.getSources()) {
                Point direction = source.subtract(point);
                double distance = Intersector.lenght(direction);

                boolean rayIsFree = true;
                for (Point[] segment : box.getSegments()) {
                    int intersectionDistance = Intersector.intersection(point, direction, segment[0], segment[1]);
                    if (intersectionDistance != -1 && intersectionDistance < distance) {
                        rayIsFree = false;
                        break;
                    }
                }
                if (rayIsFree) {
                    double intensity = Math.pow((1 - (distance / 500)), 2);
                    int originalColor = box.getRGB(point.getX(), point.getY());
                    double newColor = originalColor * intensity * Color.YELLOW.getRGB();
                    colorValue += Math.ceil(newColor);
                    // System.out.println(colorValue);
                }
                int averageColorValue = colorValue / box.getSources().size();
                canvasImage.setRGB(point.getX(), point.getY(), averageColorValue);
                // System.out.println(averageColorValue);
            }
            // canvasImage.setRGB(point.getX(),point.getY(),Color.WHITE.getRGB());
            // System.out.println(point.toString());
        }
    }

    public void Stop() {
        IsRunning = false;
    }

    private PathTracer(BufferedImage pCanvasImage, Box pBox) {
        canvasImage = pCanvasImage;
        box = pBox;
        imageSize = canvasImage.getHeight();
        random = new Random();
    }

    private model.Point getRandomPoint() {
        return new Point(random.nextInt(imageSize), random.nextInt(imageSize));
    }
}
