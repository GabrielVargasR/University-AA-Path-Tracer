import java.awt.image.BufferedImage;

public class PathTracer {
    BufferedImage originalImage;
    BufferedImage canvasImage;

    PathTracer(BufferedImage pOriginalImage,BufferedImage pCanvasImage){
        originalImage = pOriginalImage;
        canvasImage = pCanvasImage;
    }

    void PathTrace(){

    }

    public BufferedImage getCanvasImage() {
        return canvasImage;
    }
}
