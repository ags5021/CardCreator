import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class ImagePanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 12L;
	private BufferedImage image;
	private int startX;
	private int startY;
	private int mouseX;
	private int mouseY;

	private int height;
	private int width;

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
		g.drawRect(mouseX, mouseY, width, height);

		if (selectionMade)
		{
			
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

			BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
			Graphics2D g2 = image.createGraphics();
			paint(g2);
			//we add +1 (and -1) here so we do not include the red line in the image
			BufferedImage out = image.getSubimage(mouseX+1, mouseY+1, width-1, height-1); 
			ImageIcon icon = new ImageIcon();

			try {
				
				Image img = ImageIO.read(
						new File("D:/Projects/Arabic/FrequencyDictParser/workspace/CardCreator/images/screenshot.png"));
				icon.setImage(out);
				JOptionPane.showMessageDialog(null, icon);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			selectionMade = true;
			mouseX = 0;
			mouseY = 0;
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

			mouseX = Math.min(startX, e.getX());
			mouseY = Math.min(startY, e.getY());
			e.getComponent().repaint();
		}
	}
}