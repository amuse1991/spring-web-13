package kr.ac.cnu.web.games.blackjack;

import kr.ac.cnu.web.exceptions.NotEnoughBalanceException;
import lombok.Getter;

/**
 * Created by rokim on 2018. 5. 26..
 */

public class Player {
    @Getter
    private long balance;
    @Getter
    private long currentBet;
    @Getter
    private boolean isPlaying;
    @Getter
    private Hand hand;


    public Player(long seedMoney, Hand hand) {
        this.balance = seedMoney;
        this.hand = hand;
        isPlaying = false;
    }

    public Player(long seedMoney, Hand hand, long defaultBet) {
        this.balance = seedMoney-defaultBet;
        this.hand = hand;
        this.currentBet = defaultBet;
        isPlaying = false;
    }

    public void reset() {
        hand.reset();
        isPlaying = false;
    }

    public void placeBet(long bet,boolean isDoubleDown) {
        long totalBalance = balance + currentBet;

        if(balance+1000 < bet) {
            throw new NotEnoughBalanceException();
        }

        balance = totalBalance - bet;
        currentBet = bet;

        if(isDoubleDown){
            isPlaying=false;
        }else{
            isPlaying = true;
        }
    }

    public void deal() {
        hand.drawCard();
        hand.drawCard();
    }

    public void win() {
        if(this.hand.getCardSum() == 21){
            balance += currentBet * 2.5;
        }
        else{
            balance += currentBet * 2;
        }
        reCalculation();
    }

    public void tie() {
        /*
            balance += currentBet;
            currentBet = 1000;
        */

        long totalBalance = balance + currentBet;
        reCalculation();
    }

    public void lost() {
        reCalculation();
    }

    public void reCalculation() {
        currentBet = 1000;
        balance -= currentBet;
    }

    public Card hitCard() {
        return hand.drawCard();
    }

    public void stand() {
        this.isPlaying = false;
    }

}
