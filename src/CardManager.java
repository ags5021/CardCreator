import java.util.ArrayList;

public class CardManager {
	private static CardManager instance = null;
	
	private static ArrayList<Card> _cards = new ArrayList<Card>();
	private static int currentCard = 0;
	
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
	public static Card getCard(int id)
	{
		for(Card card : _cards)
		{
			if (card.get_id() == id)
			{
				return card;
			}
		}
		return null;
	}
	
	public static Card getCurrentCard()
	{
		return getCard(CardManager.currentCard);
	}
	
	public static void AddCard()
	{
		//TODO: CHECK IF THAT ID ALREADY EXISTS
		CardManager.currentCard =+ 1; //increment the ID, yo
		_cards.add(new Card(CardManager.currentCard));
	}

	public static int getCurrentCardID() {
		return currentCard;
	}

	public static void setCurrentCardID(int currentCard) {
		CardManager.currentCard = currentCard;
	}
	
}
