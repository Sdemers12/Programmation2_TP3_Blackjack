package programmation.projet3_samuel_demers;

public class Deck {
    private Card[] deck;
    private int currentCardIndex;
    
	//stocke les cartes dans Card[ ] deck et ensuite il appelle la fonction shuflleDeck().
    public Deck() {
        this.deck = new Card[52];
        this.currentCardIndex = 0;

        String[] allRanks = {"Ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King"};
        String[] allSuites = { "Clubs", "Diamonds", "Hearts", "Spades" };

        int counter = 0;

        for(String suite : allSuites){
            for(String rank : allRanks){
                int points = switch(rank) {
                    case "Jack", "Queen", "King" -> 10;
                    case "Ace" -> 11;
                    default -> Integer.parseInt(rank);
                };

                deck[counter] = new Card(suite, rank, points);
                counter++;
            }
        }
        shuffleDeck();

    }
    
	//shuffleDeck, pour mélanger les cartes à l'aide de l'algorithme de mélange de Fisher-Yates:
	//https://www.geeksforgeeks.org/shuffle-a-given-array-using-fisher-yates-shuffle-algorithm/
    private void shuffleDeck() {
       for (int i = 51; i > 0; i--) {
            int j = (int)(Math.random() * i);
            Card temp = deck[i];
            deck[i] = deck[j];
            deck[j] = temp;
       }
    }

    public Card drawCard() {
        if(this.currentCardIndex == 51) {
            Card currCard = this.deck[this.currentCardIndex];
            shuffleDeck();
            return currCard;
        }
        else {
            return this.deck[this.currentCardIndex++];
        }
    }
}
