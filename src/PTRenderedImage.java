import javax.swing.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.*;
import javax.imageio.*;
import java.awt.event.*;
import model.*;
import model.Box;

import static java.awt.Color.BLACK;

public class PTRenderedImage extends JPanel{

	private static final long serialVersionUID = 8877854516634954684L;
	private BufferedImage bufferedImage;
	private Graphics2D graphics;

	public PTRenderedImage() {
		super();
		setPreferredSize(new Dimension(500,500));
		addMouseListener(panelClick());
		this.bufferedImage = new BufferedImage(500, 500, BufferedImage.TYPE_INT_RGB);
		this.graphics = (Graphics2D) bufferedImage.getGraphics();
		this.graphics.setPaint(BLACK);
		this.graphics.fillRect(0,0,bufferedImage.getWidth(),bufferedImage.getHeight());
		// Box box = new Box();
		// PathTracer pathTracer = new PathTracer(loadScene(box),bufferedImage,box);
		// pathTracer.PathTrace(); //Aqui va a estar todo el algoritmo que va pintando
		this.graphics.drawImage(bufferedImage, 0, 0, 500, 500, null);
	}

	private BufferedImage loadImg(){
		try{
			File img = new File("aaa.jpg");
			BufferedImage bf = ImageIO.read(img);
			return bf;
		} catch (IOException e){
			e.printStackTrace();
			return null;
		}
	}

	private BufferedImage loadScene(Box pBox){
		model.Box b = pBox;
		BufferedImage img = new BufferedImage(500, 500, BufferedImage.TYPE_INT_RGB);

		for (int x = 0; x < 500; x++){
			for (int y = 0; y < 500; y++){
				int color = b.getRGB(x, y);
				img.setRGB(x, y, color);
			}
		}
		return img;
	}
	public MouseListener panelClick(){
		MouseListener ml = new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				int x=e.getX();
				int y=e.getY();
				System.out.println(x + ","+y);
				changeColor(x, y,bufferedImage);
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
	@Override
	public void paintComponent(Graphics g){
		g.drawImage(bufferedImage, 0, 0, null);
	}

	public void changeColor(int x, int y,BufferedImage pImage){
		System.out.println(pImage.getRGB(x, y));
		pImage.setRGB(x, y, Color.RED.getRGB());
		repaint();
	}
}
