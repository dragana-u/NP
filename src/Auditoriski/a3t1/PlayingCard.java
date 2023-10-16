package Auditoriski.a3t1;

import java.sql.Array;
import java.util.*;

class Deck{
    private PlayingCard[] cards;
    private boolean[] cardsPicked;
    private int total;
    private int counter=0;
    public Deck(){
        total=0;
        cards = new PlayingCard[52];
        cardsPicked = new boolean[52];
        for(int i=2;i<=14;i++){
            for(int j=0;j<4;j++){
                cards[counter]=new PlayingCard(i,TYPE.values()[j]);
                counter++;
            }
        }
    }

    public PlayingCard[] shuffle(){
        List<PlayingCard> cardsList = Arrays.asList(cards);
        Collections.shuffle(cardsList);
        return cards;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for(PlayingCard card : cards){
            stringBuilder.append(card);
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }
    public static void main(String[] args) {
        Deck deck = new Deck();
        System.out.print(deck);
    }

}
class MultipleDeck{
    private List<Deck> decks;

}
 enum TYPE{
    HEARTS,
    DIAMONDS,
    SPADES,
    CLUBS
}
public class PlayingCard {
    private int cardNumber;
    private TYPE type;

    public PlayingCard(int cardNumber, TYPE type) {
        this.cardNumber = cardNumber;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayingCard that = (PlayingCard) o;
        return cardNumber == that.cardNumber && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cardNumber, type);
    }

    @Override
    public String toString() {
        return String.format("%d %s",cardNumber,type.toString());
    }
}