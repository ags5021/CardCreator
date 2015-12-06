import java.awt.Graphics;
import java.awt.Image;
import java.util.LinkedList;

import javax.swing.JPanel;

public class PreviewPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4656288048619441084L;
	private LinkedList<ImageItem> _images = new LinkedList<ImageItem>();

	public PreviewPanel() {

	}

	// quick and dirty...we don't need super duper performance so we just
	// repaint everything since it is fast enough
	public void RefreshView(CardSide cardSide) {
		_images.clear();  // keeping this because sometimes we have to manually generate tests (yes, to lazy to automate tests right now)
		_images = cardSide.getImageItems();
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub		
		super.paintComponent(g);
		int previousY = 0;
		if (_images.size() > 0) {
			for (ImageItem imageItem : _images) {
				g.drawImage(imageItem.get_image(), 0, previousY, null);
				previousY += imageItem.getHeight();
			}
		}

	}

}
