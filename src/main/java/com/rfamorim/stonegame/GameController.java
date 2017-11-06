package com.rfamorim.stonegame;

import com.rfamorim.stonegame.game.Game;
import com.rfamorim.stonegame.game.di.GameFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.security.InvalidParameterException;

@RestController
public class GameController {

    private static Game game;

    @RequestMapping(value = "/game", method = RequestMethod.GET)
    public Game game(
            @RequestParam(value = "p1", defaultValue = "Player 1") String p1,
            @RequestParam(value = "p2", defaultValue = "Player 2") String p2) {
        game = GameFactory.provideGame(p1, p2);
        return game;
    }

    @RequestMapping(value = "/move/{pitIndex}")
    public Game move(@PathVariable int pitIndex, HttpServletResponse response) {
        try {
            game.play(pitIndex);
        } catch (InvalidParameterException e) {
            response.setStatus(422);
            e.printStackTrace();
        }
        return game;
    }
}
