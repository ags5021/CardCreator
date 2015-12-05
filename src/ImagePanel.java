import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class ImagePanel extends JPanel {

	private BufferedImage image;
	private int startX;
	private int startY;
	private int x;
	private int y;

	private int height;
	private int width;
	private Rectangle mouseRect = new Rectangle();

	private boolean selectionMade = false;

	public ImagePanel(String file) {
		this.addMouseListener(new MouseHandler());
		this.addMouseMotionListener(new MouseMotionHandler());
		try {
			image = ImageIO.read(new File(file));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.drawImage(image, 0, 0, null); // see javadoc for more info on the
										// parameters
		g.setColor(Color.red);
		g.drawRect(x, y, width, height);

		if (selectionMade) {
			System.out.println("SELECTION MADE");
		}
	}

	private class MouseHandler extends MouseAdapter {

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			startX = e.getX();
			startY = e.getY();
			selectionMade = false;
			e.getComponent().repaint();
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub

			selectionMade = true;
			x = 0;
			y = 0;
			height = 0;
			width = 0;
			e.getComponent().repaint();

		}
	}

	private class MouseMotionHandler extends MouseMotionAdapter {
		@Override
		public void mouseDragged(MouseEvent e) {
			// TODO Auto-generated method stub

			width = Math.abs(startX - e.getX());
			height = Math.abs(startY - e.getY());

			x = Math.min(startX, e.getX());
			y = Math.min(startY, e.getY());
			e.getComponent().repaint();
		}
	}
}