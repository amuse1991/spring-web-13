package kr.ac.cnu.web.service;

import kr.ac.cnu.web.games.blackjack.Deck;
import kr.ac.cnu.web.games.blackjack.GameRoom;
import kr.ac.cnu.web.games.blackjack.Player;
import kr.ac.cnu.web.model.User;
import kr.ac.cnu.web.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rokim on 2018. 5. 26..
 */
@Service
public class BlackjackService {
    private final int DECK_NUMBER = 1;
    private final Map<String, GameRoom> gameRoomMap = new HashMap<>();
    @Autowired
    private UserRepository userRepository;

    public GameRoom createGameRoom(User user) {
        Deck deck = new Deck(DECK_NUMBER);

        GameRoom gameRoom = new GameRoom(deck);
        gameRoom.addPlayer(user.getName(), user.getAccount(),true);

        gameRoomMap.put(gameRoom.getRoomId(), gameRoom);

        return gameRoom;
    }

    public GameRoom joinGameRoom(String roomId, User user) {
        // multi player Game 이 아니므로 필요가 없다.
        return null;
    }

    public void leaveGameRoom(String roomId, User user) {
        gameRoomMap.get(roomId).removePlayer(user.getName());
    }

    public GameRoom getGameRoom(String roomId) {
        return gameRoomMap.get(roomId);
    }

    public GameRoom bet(String roomId, User user, long bet) {
        GameRoom gameRoom = gameRoomMap.get(roomId);

        gameRoom.reset();
        gameRoom.bet(user.getName(), bet,false);
        gameRoom.deal();

        return gameRoom;
    }

    public GameRoom hit(String roomId, User user) {
        GameRoom gameRoom = gameRoomMap.get(roomId);
        String userName = user.getName();
        gameRoom.hit(userName);
        //TODO hit의 결과가 21을 넘으면 게임 종료
        Player player = gameRoom.getPlayerList().get(userName);
        if(player.getHand().getCardSum() > 21){
            player.stand();
            gameRoom.playDealer(user);
        }
        userRepository.save(
                new User(userName,player.getBalance())
        );
        return gameRoom;
    }

    public GameRoom stand(String roomId, User user) {
        GameRoom gameRoom = gameRoomMap.get(roomId);
        String userName = user.getName();
        Player player = gameRoom.getPlayerList().get(userName);
        gameRoom.stand(userName);
        gameRoom.playDealer(user);
        userRepository.save(
                new User(userName,player.getBalance())
        );
        return gameRoom;
    }

    public GameRoom doubleDown(String roomId, User user, long betMoney) {
        GameRoom gameRoom = gameRoomMap.get(roomId);
        String userName = user.getName();
        //TODO 배팅 금액 2배로
        betMoney *= 2;
        gameRoom.bet(userName, betMoney,true);

        //TODO 카드 한장 더 받고 해당 판 종료
        gameRoom.hit(userName);

        Player player = gameRoom.getPlayerList().get(userName);
        player.stand();
        gameRoom.playDealer(user);

        userRepository.save(
                new User(userName,player.getBalance())
        );

        return gameRoom;
    }
}
