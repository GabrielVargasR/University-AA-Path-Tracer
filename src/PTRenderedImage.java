import javax.swing.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import model.Box;
import model.IConstants;

import static java.awt.Color.BLACK;

public class PTRenderedImage extends JPanel implements IConstants{

	private static final long serialVersionUID = 8877854516634954684L;
	private BufferedImage bufferedImage;
	private Graphics2D graphics;

	public PTRenderedImage() {
		super();
		setPreferredSize(new Dimension(IMAGE_SIZE,IMAGE_SIZE));
		this.bufferedImage = new BufferedImage(IMAGE_SIZE, IMAGE_SIZE, BufferedImage.TYPE_INT_RGB);
		this.graphics = (Graphics2D) bufferedImage.getGraphics();
		this.graphics.setPaint(BLACK);
		this.graphics.fillRect(0,0,bufferedImage.getWidth(),bufferedImage.getHeight());
		PathTracer.getInstance().setImage(this.bufferedImage);
		this.graphics.drawImage(bufferedImage, 0, 0, IMAGE_SIZE, IMAGE_SIZE, null);
	}

	@Override
	public void paintComponent(Graphics g){
		g.drawImage(bufferedImage, 0, 0, null);
	}

	// private BufferedImage loadScene(Box pBox){
	// 	model.Box b = pBox;
	// 	BufferedImage img = new BufferedImage(IMAGE_SIZE, IMAGE_SIZE, BufferedImage.TYPE_INT_RGB);

	// 	for (int x = 0; x < IMAGE_SIZE; x++){
	// 		for (int y = 0; y < IMAGE_SIZE; y++){
	// 			int[] color = b.getRGB(x, y);
	// 			img.setRGB(x, y, (new Color(color[0], color[1], color[2])).getRGB());
	// 		}
	// 	}
	// 	return img;
	// }
}
