
public class CardManager {
	private static CardManager instance = null;
	
	private static  Card _card = new Card(1);
	private static int currentCardID = 0;
	
	protected CardManager() {
		// Exists only to defeat instantiation.
	}

	public static CardManager getInstance() {
		if (instance == null) {
			instance = new CardManager();
		}
		return instance;
	}
	
	// simple search for card by id (this is a unique id)
	public static Card getCard()
	{
		return _card;
	}
	
	public static void CreateCard()
	{
		//TODO: CHECK IF THAT ID ALREADY EXISTS
		CardManager.currentCardID =+ 1; //increment the ID, yo
		_card = new Card(currentCardID);
	}

	public static int getCurrentCardID() {
		return currentCardID;
	}

	public static void setCurrentCardID(int currentCard) {
		CardManager.currentCardID = currentCard;
	}
	
}
