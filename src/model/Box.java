package model;

import java.util.ArrayList;
import java.util.HashMap;

public class Box implements IConstants{
    private ArrayList<Point[]> segments;
    private ArrayList<Point> sources;
    private HashMap<Integer, HashMap<Integer, int[]>> scene;

    public Box(){
        this.segments = new ArrayList<Point[]>();
        this.sources = new ArrayList<Point>();
        this.createSegments();
        this.createSources();
        this.initScene();
        this.createScene();
    }

    public int[] getRGB(int pXCoord, int pYCoord){
        int[] color = new int[3];
        int[] temp = this.scene.get(pXCoord).get(pYCoord);
        color[0] = temp[0];
        color[1] = temp[1];
        color[2] = temp[2];
        return color;
    }

    public int getSpecularity(int pXCoord, int pYCoord){
        return this.scene.get(pXCoord).get(pYCoord)[3];
    }

    public ArrayList<Point> getSources(){
        return this.sources;
    }

    public ArrayList<Point[]> getSegments(){
        return this.segments;
    }

    private void createSegments(){
        // First two points are the points for the segment
        // Third point is the direction of the normal line
        Point[] redBorder = new Point[]{new Point(12,0), new Point(12, 499), new Point(1,0)};
        Point[] greenBorder = new Point[]{new Point(488,0), new Point(488, 499), new Point(-1,0)};

        Point[] boxL = new Point[]{new Point(70, 350), new Point(70, 499), new Point(-1,0)}; // though it has two normals, the angle method takes care of that
        Point[] boxR = new Point[]{new Point(160, 350), new Point(160, 499), new Point(1,0)};
        Point[] boxT = new Point[]{new Point(70, 350), new Point(160, 350), new Point(0,1)};

        Point[] squareL = new Point[]{new Point(230, 230), new Point(230, 340), new Point(-1,0)};
        Point[] squareR = new Point[]{new Point(380, 230), new Point(380, 340), new Point(1,0)};
        Point[] squareT = new Point[]{new Point(235, 200), new Point(375, 200), new Point(0,1)};

        
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
        this.scene = new HashMap<Integer, HashMap<Integer, int[]>>();
        HashMap<Integer, int[]> temp;
        for (int x = 0; x <= IMAGE_SIZE; x++){
            scene.put(x, new HashMap<Integer, int[]>());
            temp = scene.get(x);
            for (int y = 0; y <= IMAGE_SIZE; y++){
                temp.put(y, new int[]{BACKGROUND_R, BACKGROUND_G, BACKGROUND_B, OPAQUE});
            }
        }
    }

    private void createScene(){
        // red wall
        for(int x = 0; x < 13; x++){
            for(int y = 0; y <= IMAGE_SIZE; y++){
                this.scene.get(x).replace(y, new int[]{RED_R, RED_G, RED_B, SPECULAR});
            }
        }

        // green wall
        for(int x = 488; x <= IMAGE_SIZE; x++){
            for(int y = 0; y <= IMAGE_SIZE; y++){
                this.scene.get(x).replace(y, new int[]{GREEN_R, GREEN_G, GREEN_B, SPECULAR});
            }
        }

        // box
        for(int x = 70; x < 161; x++){
            for(int y = 350; y <= IMAGE_SIZE; y++){
                this.scene.get(x).replace(y, new int[]{BOX_R, BOX_G, BOX_B, OPAQUE});
            }
        }

        // segmented box
        for(int x = 235; x < 376; x++) this.scene.get(x).replace(200, new int[]{BOX_R, BOX_G, BOX_B, OPAQUE});
        for (int y = 230; y < 341; y++){
            this.scene.get(230).replace(y, new int[]{BOX_R, BOX_G, BOX_B, OPAQUE});
            this.scene.get(380).replace(y, new int[]{BOX_R, BOX_G, BOX_B, OPAQUE});
        }

        // sources
        this.scene.get(250).replace(0, new int[]{LIGHT_R, LIGHT_G, LIGHT_B, SPECULAR});
        this.scene.get(305).replace(201, new int[]{LIGHT_R, LIGHT_G, LIGHT_B, SPECULAR});
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
        // System.out.println(Color.RED.getRed() + ", " + Color.RED.getGreen()+ ", " + Color.RED.getBlue());
        // System.out.println(customGreen.getRGB());
    }
}