package kr.ac.cnu.web.controller.api;

import kr.ac.cnu.web.exceptions.NoLoginException;
import kr.ac.cnu.web.exceptions.NoMoreBettingException;
import kr.ac.cnu.web.exceptions.NoSignupException;
import kr.ac.cnu.web.exceptions.NoUserException;
import kr.ac.cnu.web.games.blackjack.GameRoom;
import kr.ac.cnu.web.games.blackjack.Player;
import kr.ac.cnu.web.model.User;
import kr.ac.cnu.web.repository.UserRepository;
import kr.ac.cnu.web.service.BlackjackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.spring.web.json.Json;
import com.google.gson.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by rokim on 2018. 5. 21..
 */
@RestController
@RequestMapping("/api/black-jack")
@CrossOrigin
public class BlackApiController {
    @Autowired
    private BlackjackService blackjackService;
    @Autowired
    private UserRepository userRepository;

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public User login(@RequestBody String name) {
        return userRepository.findById(name).orElseThrow(() -> new NoUserException());
    }

    @PostMapping(value = "/users", consumes = MediaType.APPLICATION_JSON_VALUE)
    public User singup(@RequestBody String name) {
        // TODO check already used name
        Optional<User> userOptional = userRepository.findById(name);
        if (userOptional.isPresent()) {
            throw new NoSignupException();
        }

        // TODO new user
        User user = new User(name, 50000);

        // TODO save in repository
        return userRepository.save(user);
    }

    @PostMapping("/rooms")
    public GameRoom createRoom(@RequestHeader("name") String name) {
        User user = this.getUserFromSession(name);

        return blackjackService.createGameRoom(user);
    }

    @PostMapping(value = "/rooms/{roomId}/bet", consumes = MediaType.APPLICATION_JSON_VALUE)
    public GameRoom bet(@RequestHeader("name") String name, @PathVariable String roomId, @RequestBody long betMoney, HttpServletResponse response) {
        if(betMoney > 10000){
            throw new NoMoreBettingException();
        }
        User user = this.getUserFromSession(name);
        //배팅
        GameRoom betResult = blackjackService.bet(roomId,user,betMoney);
        Player player = betResult.getPlayerList().get(name);
        //블랙잭 처리
        if(blackjackService.isBlackJack(player)){
            //TODO 블랙잭으로 처리하고 게임 종료
            return blackjackService.stand(roomId,user);
        }
        //블랙잭 아닌 경우
        return betResult;
    }

    @PostMapping("/rooms/{roomId}/hit")
    public GameRoom hit(@RequestHeader("name") String name, @PathVariable String roomId) {
        User user = this.getUserFromSession(name);

        return blackjackService.hit(roomId, user);
    }

    @PostMapping(value = "/rooms/{roomId}/doubleDown", consumes = MediaType.APPLICATION_JSON_VALUE)
    public GameRoom doubleDown(@RequestHeader("name") String name, @PathVariable String roomId,  @RequestBody long betMoney) {
        User user = this.getUserFromSession(name);

        return blackjackService.doubleDown(roomId, user,betMoney);
    }

    @PostMapping("/rooms/{roomId}/stand")
    public GameRoom stand(@RequestHeader("name") String name, @PathVariable String roomId) {
        User user = this.getUserFromSession(name);

        return blackjackService.stand(roomId, user);
    }

    @GetMapping("/rooms/{roomId}")
    public GameRoom getGameRoomData(@PathVariable String roomId) {
        return blackjackService.getGameRoom(roomId);
    }

    @GetMapping("/rank")
    public String getRank(){
        return blackjackService.getRank(userRepository.findAll());
    }



    private User getUserFromSession(String name) {
        return userRepository.findById(name).orElseThrow(() -> new NoLoginException());
    }
}
