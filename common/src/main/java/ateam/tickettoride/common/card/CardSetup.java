package ateam.tickettoride.common.card;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayDeque;
import java.util.Scanner;

import ateam.tickettoride.common.model.Game;

public class CardSetup {
    //Function that sets up all the train cards needed for the game
    public void setUpTrainCards(Game game) {
        ArrayDeque<TrainCard> trainCards = new ArrayDeque<>();


        //Add 12 purple train cars
        for (int i = 0; i < 12; i++) {
            trainCards.add(new TrainCard(0xff800080));
        }

        //Add 12 white train cars
        for (int i = 0; i < 12; i++) {
            trainCards.add(new TrainCard(0xffffffff));
        }

        //Add 12 blue train cars
        for (int i = 0; i < 12; i++) {
            trainCards.add(new TrainCard(0xff0000ff));
        }

        //Add 12 yellow train cars
        for (int i = 0; i < 12; i++) {
            trainCards.add(new TrainCard(0xffffff00));
        }

        //Add 12 orange train cars
        for (int i = 0; i < 12; i++) {
            trainCards.add(new TrainCard(0xffffa500));
        }

        //Add 12 black train cars
        for (int i = 0; i < 12; i++) {
            trainCards.add(new TrainCard(0xff000000));
        }

        //Add 12 red train cars
        for (int i = 0; i < 12; i++) {
            trainCards.add(new TrainCard(0xffff0000));
        }

        //Add 12 green train cars
        for (int i = 0; i < 12; i++) {
            trainCards.add(new TrainCard(0xff008000));
        }

        //Add 14 locomotives/gray train cars
        for (int i = 0; i < 14; i++) {
            trainCards.add(new TrainCard(0xff808080));
        }

        //Make the new deck and shuffle it
        TrainCardDeck trainCardDeck = new TrainCardDeck(trainCards);
        trainCardDeck.shuffle();
        game.setTrainCardDeck(trainCardDeck);

        //Draw the first five cards of the deck and up them in the face up pile
        TrainCard[] cards = new TrainCard[5];

        for (int i = 0; i < 5; i++) {
            cards[i] = trainCardDeck.drawCard();
        }

        FaceUpCards faceUpCards = new FaceUpCards(cards);
        game.setFaceUpCards(faceUpCards);

        //Make a new discard deck that is initially empty
        TrainCardDeck discardTrainCards = new TrainCardDeck(new ArrayDeque<TrainCard>());
        game.setDiscardTrainCards(discardTrainCards);

    }

    //Function that sets up all the destination cards needed for the game
    public void setUpDestinationCards(Game game) {
        ArrayDeque<DestinationCard> destinationCards = new ArrayDeque<>();

        //Read in the file of destination cards
        try {
            Scanner scanner = new Scanner(new File("./DestinationCards.txt"));

            while (scanner.hasNextLine()) {
                String city1 = scanner.nextLine();
                String city2 = scanner.nextLine();
                int pointValue = Integer.parseInt(scanner.nextLine());

                DestinationCard destinationCard = new DestinationCard(city1, city2, pointValue);
                destinationCards.add(destinationCard);
            }

            //Put the cards in a deck and shuffle them
            DestinationCardDeck destinationCardDeck = new DestinationCardDeck(destinationCards);
            destinationCardDeck.shuffle();
            game.setDestinationCardDeck(destinationCardDeck);
        }

        catch (FileNotFoundException e) {
            System.out.println("DESTINATION CARDS FILE NOT FOUND");
        }
    }
}