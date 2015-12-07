import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class CardSide {

	LinkedList<ImageItem> _list = new LinkedList<ImageItem>();
	
	 public void AddImage(BufferedImage img, int height, int width)
	 {
		 _list.addLast(new ImageItem(img, height, width));
		 
	 }	 

	 public void UndoLastImage()
	 {
		 if (_list.size() != 0)  // "nice people" clicking undo when there isnt anything to undo!
		 {
			 _list.removeLast();
		 }
	 }
	 
	 public LinkedList<ImageItem> getImageItems()
	 {
		 return _list;
	 }
	 
}
