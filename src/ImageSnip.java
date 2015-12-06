import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.image.BufferedImage;


public class ImageSnip extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1429491L;
	private MainForm _mainForm;
	
	JPanel panel;
	public ImageSnip(String file, MainForm mainForm) {
		setMinimumSize(new Dimension(70, 70));
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		JPanel panel = new ImagePanel(file, this);
		panel.setMinimumSize(new Dimension(100, 100));
		getContentPane().add(panel, BorderLayout.CENTER);
		_mainForm = mainForm;
	}

	public void SendImage(BufferedImage img, int height, int width)
	{
		_mainForm.SendImage(img, height, width);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}

}
