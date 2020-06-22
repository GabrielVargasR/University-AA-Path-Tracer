import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {

    private ScheduledExecutorService painter;
    private Executor renderer;
    private MainFrame frame;
    private Runnable paintImage;
    private Runnable renderImage;

    public Main(){
        this.painter = Executors.newSingleThreadScheduledExecutor();
        this.renderer = Executors.newSingleThreadExecutor();
        this.frame = new MainFrame();
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
                System.out.println("Painting");
            }
        };

        this.renderImage = new Runnable(){
            @Override
            public void run() {
                while(true){
                    try{
                        System.out.println("Rendering");
                        Thread.sleep(1000);
                    } catch (Exception e){}
                }
            }
        };
    } 

    public static void main(String[] args) {
        Main m = new Main();
        m.trace();
    }
}