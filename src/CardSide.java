import java.awt.Image;
import java.util.LinkedList;

public class CardSide {

	LinkedList<ImageItem> _list = new LinkedList<ImageItem>();
	
	 public void AddImage(Image img, int height, int width)
	 {
		 _list.addLast(new ImageItem(img, height, width));
		 
	 }	 

	 public void UndoLastImage()
	 {
		 _list.removeLast();
	 }
	 
	 public LinkedList<ImageItem> getImageItems()
	 {
		 return _list;
	 }
	 
}
