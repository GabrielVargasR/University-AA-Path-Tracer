import javax.swing.*;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class RenderedImage extends JFrame{

	private static final long serialVersionUID = 8877854516634954684L;

	public RenderedImage() {
		super();
		super.setSize(500, 500);  
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		super.setLayout(null);
		super.setResizable(false);
		super.setVisible(true);
	}

	public static void main(String args[]){
		Image img = new Image();
	}
}
