package model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;

public class Box implements IConstants{
    private ArrayList<Point[]> segments;
    private ArrayList<Point> sources;
    private HashMap<Integer, HashMap<Integer, Integer>> scene;

    public Box(){
        this.segments = new ArrayList<Point[]>();
        this.sources = new ArrayList<Point>();
        this.createSegments();
        this.createSources();
        this.initScene();
        this.createScene();
    }

    public int getRGB(int pXCoord, int pYCoord){
        return this.scene.get(pXCoord).get(pYCoord);
    }

    public ArrayList<Point> getSources(){
        return this.sources;
    }

    public ArrayList<Point[]> getSegments(){
        return this.segments;
    }

    private void createSegments(){
        Point[] redBorder = new Point[]{new Point(12,0), new Point(12, 499)};
        Point[] greenBorder = new Point[]{new Point(488,0), new Point(488, 499)};

        Point[] boxL = new Point[]{new Point(70, 350), new Point(70, 499)};
        Point[] boxR = new Point[]{new Point(160, 350), new Point(160, 499)};
        Point[] boxT = new Point[]{new Point(70, 350), new Point(160, 350)};

        Point[] squareL = new Point[]{new Point(230, 230), new Point(230, 340)};
        Point[] squareR = new Point[]{new Point(380, 230), new Point(380, 340)};
        Point[] squareT = new Point[]{new Point(235, 200), new Point(375, 200)};

        this.segments.add(redBorder);
        this.segments.add(greenBorder);
        this.segments.add(boxL);
        this.segments.add(boxR);
        this.segments.add(boxT);
        this.segments.add(squareL);
        this.segments.add(squareR);
        this.segments.add(squareT);
    }

    private void createSources(){
        sources.add(new Point(250, 0));
        sources.add(new Point(305, 201));
    }

    private void initScene(){
        this.scene = new HashMap<Integer, HashMap<Integer, Integer>>();
        HashMap<Integer, Integer> temp;
        for (int x = 0; x < 500; x++){
            scene.put(x, new HashMap<Integer, Integer>());
            temp = scene.get(x);
            for (int y = 0; y < 500; y++){
                temp.put(y, BACKGROUND);
            }
        }
    }

    private void createScene(){
        // red wall
        for(int x = 0; x < 13; x++){
            for(int y = 0; y < 500; y++){
                this.scene.get(x).replace(y, RED);
            }
        }

        // green wall
        for(int x = 488; x < 500; x++){
            for(int y = 0; y < 500; y++){
                this.scene.get(x).replace(y, GREEN);
            }
        }

        // box
        for(int x = 70; x < 161; x++){
            for(int y = 350; y < 500; y++){
                this.scene.get(x).replace(y, BOX);
            }
        }

        // box
        for(int x = 235; x < 376; x++) this.scene.get(x).replace(200, BOX);
        for (int y = 230; y < 341; y++){
            this.scene.get(230).replace(y, BOX);
            this.scene.get(380).replace(y, BOX);
        }

        // sources
        this.scene.get(250).replace(0, LIGHT);
        this.scene.get(305).replace(201, LIGHT);

    }

    public static void main(String[] args) {
        // Para probar los colores más rápido
        // Color luz = new Color(255, 255, 130);
        // Color caja = new Color(210, 210, 210);
        // Color fondo = new Color(255, 255, 230);
        // Color customGreen = new Color(0, 120, 0);
        // System.out.println(luz.getRGB());
        // System.out.println(caja.getRGB());
        // System.out.println(fondo.getRGB());
        // System.out.println(Color.RED.getRGB());
        // System.out.println(customGreen.getRGB());
    }
}