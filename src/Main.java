import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {

    private ScheduledExecutorService painter;
    private Executor renderer;
    private MainFrame frame;
   // private Tracer tracer;
    //private PathTracer pt;
    private Renderer tracer;
    private Runnable paintImage;
    private Runnable renderImage;

    public Main(){
        this.painter = Executors.newSingleThreadScheduledExecutor();
        this.renderer = Executors.newSingleThreadExecutor();
        this.frame = new MainFrame();
        // this.pathTracer = PathTracer.getInstance();
        //this.tracer = Tracer.getInstance();
        //this.pt = PathTracer.getInstance();
        tracer = Renderer.getInstance();
        this.createRunnables();
    }

    public void trace(){
        painter.scheduleWithFixedDelay(this.paintImage, 500, 500, TimeUnit.MILLISECONDS);
        renderer.execute(this.renderImage);
    }

    private void createRunnables(){
        this.paintImage = new Runnable(){
            @Override
            public void run(){
                frame.repaintImage();
            }
        };

        this.renderImage = new Runnable(){
            @Override
            public void run() {
                try{
                     tracer.render();
                    //pt.tracePath();
                } catch (StackOverflowError e){
                    e.printStackTrace();
                }
            }
        };
    } 

    public static void main(String[] args) {
        Main m = new Main();
        m.trace();
    }
}