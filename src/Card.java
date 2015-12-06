import java.awt.Image;

public class Card {

	private CardSide _front = new CardSide();
	private CardSide _back = new CardSide();
	
	private int _id = 0;
	
	public Card(int id)
	{
		_id = id;
	}
	
	public void AddFront(Image img, int height, int width)
	{
		_front.AddImage(img, height, width);
	}
	
	public void AddBack(Image img, int height, int width)
	{
		_back.AddImage(img, height, width);
	}
	
	public CardSide getFront()
	{
		return _front;
	}
	
	public CardSide getBack()
	{
		return _back;
	}

	int get_id() {
		return _id;
	}

	void set_id(int _id) {
		this._id = _id;
	}
}
