import model.*;
import java.awt.Color;
import java.awt.Graphics;
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

    public static Renderer getInstance() {
        if (instance == null)
            instance = new Renderer();
        return instance;
    }

    public void setImage(BufferedImage pImage) {
        canvasImage = pImage;
    }

    public void render() {
        Point point;
        double[] pixelRGB;

        //for (int i = 0; i < 1; i++) {
        while (true) {
            point = getRandomPoint();
            //point = new Point(390,250);
            pixelRGB = castRays(point, null, null, 0);
            // divideColorBy(pixelRGB, TRACE_DEPTH);
            Color pixelColor = new Color((int) pixelRGB[0], (int) pixelRGB[1], (int) pixelRGB[2]);
            canvasImage.setRGB((int) point.getX(), (int) point.getY(), pixelColor.getRGB());
        }
    }

    private double[] castRays(Point pOrigin, Point pIncomingSource,Point[] pSegment, int pDepthCount) {
        double[] color = new double[3];
        if (pDepthCount <= TRACE_DEPTH) {
            double[] directColor = calculateDirectLight(pOrigin);
            double[] indirectColor = new double[3];
            double[] sampleColor = new double[3];
            double intersectionDistance;
            double intensity;
            double length;
            Point[] segmentContainer = new Point[3];
            Point intersectionPoint;
            Point direction;
            Point dirToIntersection;
            int specularity = box.getSpecularity((int) pOrigin.getX(), (int) pOrigin.getY());
            if ((specularity == SPECULAR) && (pSegment != null)) {
                Point incomingLightDir = Intersector.normalize(pOrigin.subtract(pIncomingSource));
                direction = Intersector.reflect(incomingLightDir, pSegment[2]);
                intersectionDistance = getClosestIntersection(pOrigin, direction, segmentContainer);
                if (intersectionDistance == Double.MAX_VALUE) {
                   // System.out.println("Nulo en especular");
                    indirectColor = color;
                } 
                else {
                    intersectionPoint = Intersector.intersectionPoint(pOrigin, direction, intersectionDistance);
                    dirToIntersection = intersectionPoint.subtract(pOrigin);
                    length = Intersector.length(dirToIntersection);
                    intensity = getIntensity(length);
                    sampleColor = castRays(intersectionPoint, pOrigin,segmentContainer,++pDepthCount);
                    multiplyColorBy(sampleColor, intensity);
                    indirectColor = sampleColor;
                }

            } 
            else {
                Point dirPoint;
                // Graphics g = canvasImage.getGraphics();
                // g.setColor(Color.BLUE);
                for (int i = 0; i < SAMPLE_SIZE; i++) {
                    if(pSegment == null){
                        dirPoint = getRandomPoint();
                    }
                    else{
                        dirPoint = opaqueReflectionPoint(pIncomingSource,pSegment[0],pSegment[1]);
                    }
                    direction = Intersector.normalize(dirPoint.subtract(pOrigin));
                    intersectionDistance = getClosestIntersection(pOrigin, direction, segmentContainer);
                    if (intersectionDistance == Double.MAX_VALUE) {
                        System.out.println("Siguen exitiendo nulos");
                        continue;
                    }
                    intersectionPoint = Intersector.intersectionPoint(pOrigin, direction, intersectionDistance);
                    dirToIntersection = intersectionPoint.subtract(pOrigin);
                    length = Intersector.length(dirToIntersection);
                    intensity = getIntensity(length);


                   // g.drawLine(((int)pOrigin.getX()), ((int)pOrigin.getY()), ((int)intersectionPoint.getX()), ((int)intersectionPoint.getY()));

                    sampleColor = castRays(intersectionPoint,pOrigin,segmentContainer, ++pDepthCount);
                    multiplyColorBy(sampleColor, intensity);
                    indirectColor[0] += sampleColor[0];
                    indirectColor[1] += sampleColor[1];
                    indirectColor[2] += sampleColor[2];
                }
                divideColorBy(indirectColor, SAMPLE_SIZE);
            }
            color[0] = (directColor[0] + indirectColor[0]) / 2;
            color[1] = (directColor[1] + indirectColor[1]) / 2;
            color[2] = (directColor[2] + indirectColor[2]) / 2;
        }
        return color;
    }

    private double[] calculateDirectLight(Point pOrigin) {
        double[] directColor = new double[3];
        int sourcesHit = 0;
        for (Point source : sources) {
            double distance = 0;
            Point dirToSource = source.subtract(pOrigin);
            double length = Intersector.length(dirToSource);
            boolean free = true;
            for (Point[] segment : segments) {
                distance = Intersector.intersection(pOrigin, Intersector.normalize(dirToSource), segment[0],
                        segment[1]);
                if (distance != -1 && distance < length) {
                    free = false;
                    break;
                }
            }
            if (free) {
                double intensity = getIntensity(length);
                int[] originalRGB = box.getRGB((int) pOrigin.getX(), (int) pOrigin.getY());
                directColor[0] += originalRGB[0] * intensity * ((double) LIGHT_R / 255);
                directColor[1] += originalRGB[1] * intensity * ((double) LIGHT_G / 255);
                directColor[2] += originalRGB[2] * intensity * ((double) LIGHT_B / 255);
                sourcesHit++;
            }
        }
        if (sourcesHit == 0) {
            sourcesHit++;
        }
        directColor[0] /= sourcesHit;
        directColor[1] /= sourcesHit;
        directColor[2] /= sourcesHit;

        return directColor;
    }

    private double getClosestIntersection(Point pOrigin, Point pDirection,Point[] pSegment) {
        double tempDistance;
        double intersectionDistance = Double.MAX_VALUE;
        for (Point[] segment : segments) {
            tempDistance = Intersector.intersection(pOrigin, pDirection, segment[0], segment[1]);
            if (tempDistance > 0 & tempDistance < intersectionDistance) {
                intersectionDistance = tempDistance;
                pSegment[0] = segment[0];
                pSegment[1] = segment[1];
                pSegment[2] = segment[2];
            }
        }
        return intersectionDistance;
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
            int y = ((int)pSeg1.getY() != 0) ? (int)pSeg1.getY() : 1;
            if (before){
                int adjust = IMAGE_SIZE - (int)pSeg1.getY();
                return (new Point(random.nextInt(IMAGE_SIZE), random.nextInt(adjust)+y+1));
            } else{
                return (new Point(random.nextInt(IMAGE_SIZE), random.nextInt(y)));
            }
        } else{
            if (before){
                return (new Point(random.nextInt((int)pSeg1.getX()+1), random.nextInt(IMAGE_SIZE)));
            } else{
                int adjust = IMAGE_SIZE - (int)pSeg1.getX();
                return (new Point(random.nextInt(adjust)+(int)pSeg1.getX()+1, random.nextInt(IMAGE_SIZE)));
            }
        }
    }

    private double getIntensity(double pLenght) {
        double intensity = 1 - (pLenght / (double) IMAGE_SIZE);
        return Math.pow(intensity, 2);

    }

    private void divideColorBy(double[] pColor, double pDivisor) {
        pColor[0] /= pDivisor;
        pColor[1] /= pDivisor;
        pColor[2] /= pDivisor;
    }

    private void multiplyColorBy(double[] pColor, double pScalar) {
        pColor[0] *= pScalar;
        pColor[1] *= pScalar;
        pColor[2] *= pScalar;
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

    public static void main(String[] args) {
        Main m = new Main();
        m.trace();
    }
}