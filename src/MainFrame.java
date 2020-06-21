import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainFrame extends JFrame{

    private static final long serialVersionUID = 1L;
    public MainFrame(){
        super("Path Tracing Example");
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.setResizable(false);
		setContentPane(new PTRenderedImage());
        pack();
        super.setVisible(true);
    }



    public static void main(String[] args) {
        MainFrame f = new MainFrame();
    }
}