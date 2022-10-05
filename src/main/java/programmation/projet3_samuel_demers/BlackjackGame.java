package programmation.projet3_samuel_demers;

public class BlackjackGame {
    private final Hand playerHand;
    private final Hand dealerHand;
    private final Deck deck;
    private double betAmount;
    private final double minBet;
    private final double maxBet;
    private double totalMoney;
    
	//Initialiser deck, playerHand, dealerHand, minBet et maxBet
	//le minimum et le maximum de la mise sont de 5 et 1000 respectivement.
    public BlackjackGame() {
        this.deck = new Deck();     // Maintenant, on a 52 cartes non ordonnées
        this.playerHand = new Hand("player");
        this.dealerHand = new Hand("dealer");
        this.minBet = 5.0;
        this.maxBet = 1000.0;
    } 
    
    public double loadMoney() {
        return totalMoney = 100;
    } 
    
	//retourne true le total d’argent dont un joueur dispose est inférieur au minimum de mise. False sinon.
    public boolean isOutOfMoney() {
        if(this.totalMoney < this.minBet)
            return true;
        else
            return false;
    }

	// pour initialiser totalMoney a 100
    public void resetMoney() {
        this.totalMoney = 100;
    }
    
	//retourne false si double localBetAmt est inférieur au minBet ou supérieur au maxBet ou supérieur au totalMoney. True sinon.
    public boolean isValidBet(double localBetAmt) {
        if(localBetAmt < minBet || localBetAmt > maxBet || localBetAmt > totalMoney)
            return false;
        else
            return true;
    }
    
	//retourner minBet
    public double getMinBet() {
        return this.minBet;
    }
    
	//retourner le montant total que le joueur peut utiliser pour la mise.
    public double getMaxBet() {
        return this.maxBet;
    }
    
	// pour retrouner le montant total
    public double getTotalMoney() {
        return this.totalMoney;
    }
    
	//pour intialiser le montant de la mise qu'on va le faire
    public void setBet(double amt) {
        this.betAmount = amt;
    }
    
	// distribue deux cartes pour le joueur (playerHand) et deux cartes pour le courtier (dealerHand).
    public void deal() {
        playerHand.addCard(deck.drawCard());
        playerHand.addCard(deck.drawCard());

        dealerHand.addCard(deck.drawCard());
        dealerHand.addCard(deck.drawCard());
    }
    
	//pour distribuer une carte en plus pour le joueur dans le cas où il fait hit.
    public void hit() {
        playerHand.addCard(deck.drawCard());
    }
    
	//qui ajoute des cartes au main du courtier tant que la somme des points dont il dispose est moins que 17.
    public void stand() {
        while (dealerHand.getPoints() < 17 && !playerHand.isBust()) {
            dealerHand.addCard(deck.drawCard());
        }
    }

	//retourne le deuxième carte dans la main du courtier.
    public Card getDealerShowCard() {
        return dealerHand.getCards().get(1);
    }
    
	//retourne dealerHand
    public Hand getDealerHand() {
        return this.dealerHand;
    }

	//retourne playerHand
    public Hand getPlayerHand() {
        return this.playerHand;
    }

    // ice cream
    public boolean isBlackjackOrBust() {
        if(playerHand.isBlackjack() || playerHand.isBust()
                || dealerHand.isBlackjack() || dealerHand.isBust()) {
            return true;
        } else{
            return false;
        }
    }
    
	//retourne true si les points dans la main de joueur est inférieur ou égale 21 et ces points sont égales aux points avec le courtier. False sinon.
    public boolean isPush() {
        if (!getPlayerHand().isBust() && getDealerHand().getPoints() == getPlayerHand().getPoints())
            return true;
        else
            return false;
    }
	
	//retourne true si le player gagne. False sinon.
    public boolean playerWins() {
        if(!getPlayerHand().isBust() && getPlayerHand().getPoints() > getDealerHand().getPoints() && !getDealerHand().isBust())
            return true;
        else if (!getPlayerHand().isBust() && getDealerHand().isBust()) {
            return true;
        } else
            return false;
    }
    
	// ajoute le montant du mise gagner au montant total
    public void addBetToTotal() {
        this.totalMoney += this.betAmount;
    }
    
	// ajoute le montant de mise gagner selon 3:2 au montant total dans le cas de blackjack
    public void addBlackjackToTotal() {
        this.totalMoney += this.betAmount * 1.5;
    }
    
	// soustraire le montant du bet perdu du montant total
    public void subtractBetFromTotal() {
        this.totalMoney -= this.betAmount;
    }

}
