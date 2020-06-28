import model.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.ArrayList;

public class Tracer implements IConstants{
    private static Tracer instance;
    private Box box;
    private BufferedImage canvasImage;
    private Random random;

    private Tracer(){
        box = new Box();
        random = new Random();
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
        int[] pixColor;
        int[] calculatedColor;

        while (true){
            point = new model.Point(random.nextInt(IMAGE_SIZE), random.nextInt(IMAGE_SIZE)); // chooses points at random
            pixColor = new int[]{0,0,0}; // RGB value calculated for the point at a given moment inside the while loop
            
            for (int sample = 0; sample < SAMPLE_SIZE; sample++){
                calculatedColor = calculatePixel();

                // adds the calculated RGB values to the current color for each sample taken
                pixColor[0] += calculatedColor[0];
                pixColor[1] += calculatedColor[1];
                pixColor[2] += calculatedColor[2];
            }

            // Averages the color values received using the amount of samples taken
            pixColor[0] = (int) (pixColor[0] / SAMPLE_SIZE);
            pixColor[1] = (int) (pixColor[1] / SAMPLE_SIZE);
            pixColor[2] = (int) (pixColor[2] / SAMPLE_SIZE);

            // updates color por the pixel
            canvasImage.setRGB(point.getX(), point.getY(), (new Color(pixColor[0], pixColor[1], pixColor[2]).getRGB())); 
        }
    }

    private int[] calculatePixel(){
        return null;
    }

    public static void main(String[] args) {
        Main m = new Main();
        m.trace();
    }
}