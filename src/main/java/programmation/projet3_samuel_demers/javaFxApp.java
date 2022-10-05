package programmation.projet3_samuel_demers;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class javaFxApp extends Application {

    private static BlackjackGame game;

    // Money
    private final Label moneyLabel = new Label("Money: ");
    private final TextField moneyField = new TextField();

    // Bet
    private final Label betLabel = new Label("Bet: ");
    private final TextField betField = new TextField();

    // Dealer
    private final Label dealerSection = new Label("DEALER");

    private final Label listViewDealerCardsLabel = new Label("Cards: ");
    private final ListView<String> dealerCardsListView = new ListView<>();
    private final Label pointsDealerLabel = new Label("Points: ");
    private final TextField pointsDealerField = new TextField();

    // PLayer
    private final Label playerSection = new Label("YOU");
    private final Label listViewPlayerCardsLabel = new Label("Cards: ");
    private final ListView<String> playerCardsListView = new ListView<>();
    private final Label pointsPlayerLabel = new Label("Points: ");
    private final TextField pointsPlayerField = new TextField();

    // Result
    private final Label resultLabel = new Label("RESULT: ");
    private final TextField resultField = new TextField();

    // Hit - Stand
    private final Button hitButton = new Button("Hit");
    private final Button standButton = new Button("Stand");

    // Play - Exit
    private final Button playButton = new Button("Play");
    private final Button exitButton = new Button("Exit");

    // Erreur
    private final Label betError = new Label("");


    // Démarre le jeu
    private void startGame() {
        game = new BlackjackGame();

        // Les boutons ne sont pas accessible jusqu'à ce que l'utilisateur ait placé
        //une mise
        hitButton.setDisable(true);
        standButton.setDisable(true);

        // Si money field est vide, met 100$ pour commencer et affiche le dans money
        if (moneyField.getText().isEmpty()){
            String money = String.valueOf(game.loadMoney());
            moneyField.setText(money);
        }
    }

    // Clique sur play
     private void playGame() {

             // Reset money si plus assez d'argent
             if (!moneyField.getText().isEmpty() && checkMoneyValue() <= game.getMinBet()){
                 game.resetMoney();
                 String money = String.valueOf(game.loadMoney());
                 moneyField.setText(money);
             }
             // Commence le jeu si resultField est vide
             if (resultField.getText().isEmpty()){

                 // Validation qui met un message d'erreur s'il y a catch
                 betError.setText(validation(betField.getText()));

                 // Exécute la suite s'il n'y a pas de message d'erreur
                 if (betError.getText() == "") {

                    // Désactiver les boutons « Play/Exit » et le champ Bet lorsqu'une partie est en cours
                    playButton.setDisable(true);
                    exitButton.setDisable(true);
                    betField.setEditable(false);

                    // Load cartes
                    game.deal();

                    // Récupère la mise
                    game.setBet(getBetAmount());

                    // Affiche première carte DEALER
                    showDealerShowCard();

                    // Affiche les deux premières cartes PLAYER
                    showPlayerHand();

                    // Rend les boutons hit et stand accessible lorsqu'il y a une mise et appuie sur play
                    hitButton.setDisable(false);
                    standButton.setDisable(false);

                    // Si l'utilisateur à 21 ou dépasse
                    if (game.isBlackjackOrBust()) {
                        playerOrAppStand();
                    }
                 }

                // Si resultField n'est pas vide, on reset le jeu
            } else if (!resultField.getText().isEmpty()) {
                resetGame();
            }
     }

     // Validation du champ bet
    public String validation(String value) {
        String msg = "";
        try {
            Double.parseDouble(value);
            if (!game.isValidBet(getBetAmount())) {
                throw new Exception("Bet must be >= 5 or <= total");
            }
        } catch (NumberFormatException e) {
            msg = "Bet must be a valid number";
        } catch (Exception e) {
            msg = "Bet must be >= 5 or <= total";
        }
        return msg;
    }

    private Double checkMoneyValue() {
        return Double.valueOf(moneyField.getText());
    }

    private double getBetAmount() {
        return Double.parseDouble(betField.getText());
    }

    private void showDealerShowCard() {
        dealerCardsListView.getItems().add(game.getDealerShowCard().display());
    }

    private void showPlayerHand() {
        for(Card card : game.getPlayerHand().getCards()) {
            playerCardsListView.getItems().add(card.display());
        }
    }

    private void showDealerHand() {
        for(Card card : game.getDealerHand().getCards()) {
            dealerCardsListView.getItems().add(card.display());
        }
    }

    // Clique sur Hit
    private void playerHit() {
        if (game.isBlackjackOrBust()) {
            playerOrAppStand();
        } else {
            game.hit();
            playerCardsListView.getItems().clear();
            showPlayerHand();
            if (game.isBlackjackOrBust()) {
                playerOrAppStand();
            }
        }
    }

    // Clique sur stand ou l'app stand
    private void playerOrAppStand() {
        game.stand();
        dealerCardsListView.getItems().clear();
        showDealerHand();
        showWinner();
        //Activer les boutons « Play/Exit » lorsqu'une partie est en
        //cours
        playButton.setDisable(false);
        exitButton.setDisable(false);
        // Désactive hit et stand lorsque la partie se termine
        // Ils seront réactivé dans playGame()
        hitButton.setDisable(true);
        standButton.setDisable(true);
    }

    private void showMoney() {
        String money = String.valueOf((game.getTotalMoney()));
        moneyField.setText(money);
    }

    private void showWinner() {
        //dealer hand avec les points
        String pointsDealer = String.valueOf((game.getDealerHand().getPoints()));
        pointsDealerField.setText(pointsDealer);

        //player hand avec les points
        String pointsPlayer = String.valueOf((game.getPlayerHand().getPoints()));
        pointsPlayerField.setText(pointsPlayer);

        // Résultat et gain ou perte
        if(game.isPush()) {
            resultField.setText("Push!");
        } else if(game.getPlayerHand().isBlackjack()) {
            resultField.setText("BLACKJACK! You win!");
            game.addBlackjackToTotal();
        } else if (game.playerWins()) {
            resultField.setText("You win!");
            game.addBetToTotal();
        } else {
            resultField.setText("Sorry, you lose.");
            game.subtractBetFromTotal();
        }
        showMoney();
    }

    // Clique sur Exit
    private void exitGame() {
        System.exit(0);
    }

    // Reset lorsque clique sur le bouton Play
    // Appelé dams la fonction playGame() s'il y a une valeur dans resultField
    private void resetGame() {
        game.getPlayerHand().resetHand();
        game.getDealerHand().resetHand();
        playerCardsListView.getItems().clear();
        dealerCardsListView.getItems().clear();
        resultField.setText("");
        pointsDealerField.setText("");
        pointsPlayerField.setText("");
        betField.setText("");
        betField.setEditable(true);
    }


    @Override
    public void start(Stage primaryStage) {
        // Titre App
        primaryStage.setTitle("Blackjack");

        // Création du grid qui contient les éléments de mon interface
        GridPane grid = new GridPane();

        // Padding du grid et gap
        grid.setPadding(new Insets(10,25,25, 25));
        grid.setHgap(15);
        grid.setVgap(15);

        HBox moneyBox = new HBox(10);
        moneyBox.getChildren().add(moneyLabel);
        moneyBox.getChildren().add(moneyField);
        grid.add(moneyBox,0,0);

        HBox betBox = new HBox(28);
        betBox.getChildren().add(betLabel);
        betBox.getChildren().add(betField);
        betBox.getChildren().add(betError);
        grid.add(betBox,0,1);

        grid.add(dealerSection,0,2);

        HBox dealerCardsBox = new HBox(10);
        dealerCardsBox.getChildren().add(listViewDealerCardsLabel);
        dealerCardsBox.getChildren().add(dealerCardsListView);
        grid.add(dealerCardsBox,0,3);

        HBox dealerPointsBox = new HBox(10);
        dealerPointsBox.getChildren().add(pointsDealerLabel);
        dealerPointsBox.getChildren().add(pointsDealerField);
        grid.add(dealerPointsBox,0,4);

        grid.add(playerSection,0,5);

        HBox playerCardsBox = new HBox(10);
        playerCardsBox.getChildren().add(listViewPlayerCardsLabel);
        playerCardsBox.getChildren().add(playerCardsListView);
        grid.add(playerCardsBox,0,6);

        HBox playerPointsBox = new HBox(10);
        playerPointsBox.getChildren().add(pointsPlayerLabel);
        playerPointsBox.getChildren().add(pointsPlayerField);
        grid.add(playerPointsBox,0,7);

        HBox hitStandBox = new HBox(10);
        hitStandBox.getChildren().add(hitButton);
        hitStandBox.getChildren().add(standButton);
        grid.add(hitStandBox,0,8);

        HBox resultBox = new HBox(10);
        resultBox.getChildren().add(resultLabel);
        resultBox.getChildren().add(resultField);
        grid.add(resultBox,0,9);

        HBox playExitBox = new HBox(10);
        playExitBox.getChildren().add(playButton);
        playExitBox.getChildren().add(exitButton);
        grid.add(playExitBox,0,10);

        moneyField.setEditable(false);
        pointsDealerField.setEditable(false);
        pointsPlayerField.setEditable(false);
        resultField.setEditable(false);

        // Action et démarrage du jeu
        startGame();
        playButton.setOnAction(event -> playGame());
        exitButton.setOnAction(event -> exitGame());
        hitButton.setOnAction(event -> playerHit());
        standButton.setOnAction(event -> playerOrAppStand());

        Scene scene = new Scene(grid, 440, 600);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
