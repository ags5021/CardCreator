import java.util.ArrayList;

public class CardManager {
	private static CardManager instance = null;
	
	private static ArrayList<Card> _cards = new ArrayList<Card>();
	
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
	
	public static void AddCard(Card card)
	{
		//TODO: CHECK IF THAT ID ALREADY EXISTS
		_cards.add(card);
	}
	
}
