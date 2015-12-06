import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;


public class ImageSnip extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1429491L;

	public ImageSnip(String file) {
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		JPanel panel = new ImagePanel(file);
		panel.setMinimumSize(new Dimension(100, 100));
		getContentPane().add(panel, BorderLayout.CENTER);
		
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
	
	}

}
