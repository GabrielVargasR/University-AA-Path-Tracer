import model.Box;
import model.Intersector;
import model.Point;
import model.IConstants;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class PathTracer implements IConstants{
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
        while(IsRunning){
            point = getRandomPoint();
            colorValue = 0;
            for (Point source : box.getSources()) {
                Point direction = source.subtract(point);
                double distance = Intersector.lenght(direction);

                boolean rayIsFree = true;
                for (Point[] segment : box.getSegments()) {
                    int intersectionDistance = Intersector.intersection(point, Intersector.normalize(direction), segment[0], segment[1]);
                    if (intersectionDistance != -1 && intersectionDistance < distance) {
                        rayIsFree = false;
                        break;
                    }
                }
                if (rayIsFree) {
                    float intensity = (float)Math.pow((1 - (distance / 500)), 2);
                    int originalColor = box.getRGB(point.getX(), point.getY());
                    //colorValue += calculateColor(originalColor, LIGHT, intensity);
                    colorValue += originalColor; //temp para pruebas
                }
                int averageColorValue = averageColor(colorValue, box.getSources().size());
                canvasImage.setRGB(point.getX(), point.getY(), averageColorValue);
            }
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
    //Esto se puede factorizar en un getColor pero no se si lo vamos a cambiar para on tener que convertir
    private int calculateColor(int pOriginalColor, int pLightSourceColor,float pIntensity ){
        Color originalColor = Color.getColor("", pOriginalColor);
        Color lightColor = Color.getColor("", pLightSourceColor);
        float[] originalRGB = originalColor.getColorComponents(null);
        float[] lightRGB = lightColor.getColorComponents(null);
        float[] retRGB = new float[3];
        retRGB[0] = (originalRGB[0] * pIntensity * lightRGB[0]);
        retRGB[1] = (originalRGB[1] * pIntensity * lightRGB[1]);
        retRGB[2] = (originalRGB[2] * pIntensity * lightRGB[2]);
        return new Color(retRGB[0],retRGB[1],retRGB[2]).getRGB();
    }

    private int averageColor(int pColor, int sourcesNum){
        Color originalColor = Color.getColor("", pColor);
        float[] originalRGB = originalColor.getColorComponents(null);
        float[] retRGB = new float[3];
        retRGB[0] = (originalRGB[0] *1/sourcesNum);
        retRGB[1] = (originalRGB[1] *1/sourcesNum);
        retRGB[2] = (originalRGB[2] *1/sourcesNum);
        return new Color(retRGB[0],retRGB[1],retRGB[2]).getRGB();
    }
}
