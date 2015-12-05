import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JTextPane;
import java.awt.Dimension;
import java.awt.Color;

public class ImageSnip extends JFrame {
	public ImageSnip(String file) {
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new ImagePanel(file);
		panel.setMinimumSize(new Dimension(100, 100));
		getContentPane().add(panel, BorderLayout.CENTER);
		
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
		
	}

}
