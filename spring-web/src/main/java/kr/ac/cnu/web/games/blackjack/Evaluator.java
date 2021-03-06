package kr.ac.cnu.web.games.blackjack;

import kr.ac.cnu.web.model.User;

import java.util.Map;

/**
 * Created by rokim on 2018. 5. 27..
 */
public class Evaluator {
    private Map<String, Player> playerMap;
    private Dealer dealer;

    public Evaluator(Map<String, Player> playerMap, Dealer dealer) {
        this.playerMap = playerMap;
        this.dealer = dealer;
    }

    public boolean evaluate(User user) {
        if (playerMap.values().stream().anyMatch(player -> player.isPlaying())) {
            return false;
        }

        int dealerResult = dealer.getHand().getCardSum();

        Player[] players = new Player[playerMap.size()];
        playerMap.values().toArray(players);
        for(int i=0; i<players.length; i++){
            Player player = players[i];
            int playerResult = player.getHand().getCardSum();
            if (playerResult > 21) {
                player.lost();
                return true;
            } else if (playerResult > dealerResult) {
                player.win();
            } else if (playerResult == dealerResult) {
                player.tie();
            } else { // playerResult < dealerResult
                if (dealerResult > 21) {
                    player.win();
                }else {
                    player.lost();
                }
            }
        }
        return true;
    }

}
