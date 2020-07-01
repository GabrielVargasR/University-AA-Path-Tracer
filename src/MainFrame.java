import javax.swing.*;

public class MainFrame extends JFrame{

    private static final long serialVersionUID = 1L;
    private PTRenderedImage image;
    
    public MainFrame(){
        super("Path Tracing");
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.setResizable(false);
        this.image = new PTRenderedImage();
		setContentPane(this.image);
        pack();
        super.setVisible(true);
    }

    public void repaintImage(){
        this.image.repaint();
        this.repaint();
    }
}