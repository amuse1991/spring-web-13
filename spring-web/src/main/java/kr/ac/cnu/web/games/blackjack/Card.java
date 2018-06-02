package kr.ac.cnu.web.games.blackjack;

import kr.ac.cnu.web.exceptions.NoSuchRankException;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Created by rokim on 2018. 5. 26..
 */
@Data
public class Card {
    private final int rank;
    private final Suit suit;
    private int aceRank;

    public Card(int rank, Suit suit) {
        if (rank > 13) {
            throw new NoSuchRankException();
        }

        if (rank == 1) {    // ACE일 경우, 1 or 11로 값을 설정
            List<Integer> solution = new ArrayList<>();
            solution.add(1);
            solution.add(11);
            Collections.shuffle(solution);

            //int arr[] = {1, 11};
            //arr = shuffle(arr);
            aceRank = solution.get(0).intValue();   // 1과 11 중 shuffle한 값을 적용
        }

        this.rank = rank;
        this.suit = suit;
    }
}
