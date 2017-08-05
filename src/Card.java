import java.awt.image.BufferedImage;

public class Card {

	private CardSide _front = new CardSide();
	private CardSide _back = new CardSide();
	private CardSide _all = new CardSide();
	private boolean _exportedToAnki = false;
	
	private int _id = 0;
	
	public Card(int id)
	{
		_id = id;
	}
	
	public void UndoFront()
	{
		_front.UndoLastImage();
	}
	
	public void UndoBack()
	{
		_back.UndoLastImage();
	}
	
	public void UndoAll()
	{
		_all.UndoLastImage();
	}
	
	public void AddFront(BufferedImage img, int height, int width)
	{
		_front.AddImage(img, height, width);
	}
	
	public void AddBack(BufferedImage img, int height, int width)
	{
		_back.AddImage(img, height, width);
	}
	
	public void AddAll(BufferedImage img, int height, int width)
	{
		_all.AddImage(img, height, width);
	}
	
	public CardSide getFront()
	{
		return _front;
	}
	
	public CardSide getBack()
	{
		return _back;
	}
	
	public CardSide getAll()
	{
		return _all;
	}

	int get_id() {
		return _id;
	}

	void set_id(int _id) {
		this._id = _id;
	}

	public boolean getIsExportedToAnki() {
		return _exportedToAnki;
	}

	public void setIsExportedToAnki(boolean _exportedToAnki) {
		this._exportedToAnki = _exportedToAnki;
	}
}
