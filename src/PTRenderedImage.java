import javax.swing.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.*;
import javax.imageio.*;
import java.awt.event.*;
import model.*;

public class PTRenderedImage extends JPanel{

	private static final long serialVersionUID = 8877854516634954684L;
	private BufferedImage bufferedImage;
	private Graphics2D graphics;

	public PTRenderedImage() {
		super();
		super.setBounds(0,0,500,500);
		super.setLayout(null);
		this.bufferedImage = loadScene();
		this.graphics = (Graphics2D) bufferedImage.getGraphics();
		this.graphics.drawImage(this.bufferedImage, 0, 0, 500, 500, null);
		this.setVisible(true);
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

	private BufferedImage loadScene(){
		model.Box b = new model.Box();
		BufferedImage img = new BufferedImage(500, 500, BufferedImage.TYPE_INT_RGB);

		for (int x = 0; x < 500; x++){
			for (int y = 0; y < 500; y++){
				int color = b.getRGB(x, y);
				img.setRGB(x, y, color);
			}
		}
		return img;
	}

	@Override
	public void paintComponent(Graphics g){
		g.drawImage(bufferedImage, 0, 0, null);
	}

	public void changeColor(int x, int y){
		System.out.println(this.bufferedImage.getRGB(x, y));
		this.bufferedImage.setRGB(x, y, Color.RED.getRGB());
		repaint();
	}
}
