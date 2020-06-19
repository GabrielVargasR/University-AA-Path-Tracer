import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainFrame extends JFrame{

    private static final long serialVersionUID = 1L;
    PTRenderedImage img;

    public MainFrame(){
        super();
        super.setSize(500, 500);  
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.setLayout(null);
        super.setResizable(false);
		this.img = new PTRenderedImage();
		this.img.setVisible(true);
		this.add(this.img);
        this.addMouseListener(this.frameClick());
        super.setVisible(true);
    }

    public MouseListener frameClick(){
		MouseListener ml = new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				int x=e.getX();
                int y=e.getY();
				img.changeColor(x, y);
				repaint();
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}
		};
		return ml;
	}

    public static void main(String[] args) {
        MainFrame f = new MainFrame();
    }
}