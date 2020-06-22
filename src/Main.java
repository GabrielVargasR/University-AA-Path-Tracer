import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import model.Box;

public class Main {

    private ScheduledExecutorService painter;
    private Executor renderer;
    private MainFrame frame;
    private PathTracer pathTracer;
    private Runnable paintImage;
    private Runnable renderImage;

    public Main(){
        this.painter = Executors.newSingleThreadScheduledExecutor();
        this.renderer = Executors.newSingleThreadExecutor();
        this.frame = new MainFrame();
        this.pathTracer = PathTracer.getInstance();
        this.createRunnables();
    }

    public void trace(){
        painter.scheduleWithFixedDelay(this.paintImage, 3, 3, TimeUnit.SECONDS);
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
                pathTracer.pathTrace();
            }
        };
    } 

    public static void main(String[] args) {
        Main m = new Main();
        m.trace();
    }
}