import javax.swing.*;
import java.io.File; 
import java.io.IOException; 
import java.awt.image.BufferedImage; 
import javax.imageio.ImageIO; 

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
		RenderedImage img = new RenderedImage();
	}
}
