package model;

import java.awt.Color;
import java.util.ArrayList;

public class Box {
    private ArrayList<Point[]> segments;
    private ArrayList<Point> sources;

    public Box(){
        this.segments = new ArrayList<Point[]>();
        this.sources = new ArrayList<Point>();
        this.createSegments();
        this.createSources();
    }

    private void createSegments(){
        Point[] redBorder = new Point[]{new Point(12,0), new Point(12, 500)};
        Point[] greenBorder = new Point[]{new Point(488,0), new Point(488, 500)};

        Point[] rectL = new Point[]{new Point(70, 350), new Point(70, 500)};
        Point[] rectR = new Point[]{new Point(160, 350), new Point(160, 500)};
        Point[] rectT = new Point[]{new Point(70, 350), new Point(160, 350)};

        Point[] squareL = new Point[]{new Point(230, 230), new Point(230, 340)};
        Point[] squareR = new Point[]{new Point(380, 230), new Point(380, 340)};
        Point[] squareT = new Point[]{new Point(235, 200), new Point(375, 200)};

        this.segments.add(redBorder);
        this.segments.add(greenBorder);
        this.segments.add(rectL);
        this.segments.add(rectR);
        this.segments.add(rectT);
        this.segments.add(squareL);
        this.segments.add(squareR);
        this.segments.add(squareT);
    }

    private void createSources(){
        sources.add(new Point(250, 0));
        sources.add(new Point(305, 201));
    }


    public static void main(String[] args) {
        // Para probar los colores más rápido
        Color luz = new Color(255, 255, 130);
        Color caja = new Color(210, 210, 210);
        Color fondo = new Color(255, 255, 230);
        Color customGreen = new Color(0, 120, 0);
        System.out.println(luz.getRGB());
        System.out.println(caja.getRGB());
        System.out.println(fondo.getRGB());
        System.out.println(Color.RED.getRGB());
        System.out.println(customGreen.getRGB());
    }
}