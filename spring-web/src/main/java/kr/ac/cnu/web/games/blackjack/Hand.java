package kr.ac.cnu.web.games.blackjack;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rokim on 2018. 5. 26..
 */
public class Hand {
    private Deck deck;
    @Getter
    private List<Card> cardList = new ArrayList<>();

    public Hand(Deck deck) {
        this.deck = deck;
    }

    public Card drawCard() {
        Card card = deck.drawCard();
        cardList.add(card);
        return card;
    }

    public int getCardSum() {
        int sum = 0;
        for (Card card :cardList) {
            if(card.getRank() <= 10 && card.getRank() >= 1) // J, Q, K가 아니라면 현 카드의 Rank로 계산
                sum += card.getRank();
            else if(card.getRank() >= 11 && card.getRank() < 14) // J, Q, K라면 10으로 계산
                sum += 10;
        }
        return sum;
    }
//    public int getCardSum() {
//        return cardList.stream().mapToInt(card -> card.getRank()).sum();
//    }

    public void reset() {
        cardList.clear();
    }
}
