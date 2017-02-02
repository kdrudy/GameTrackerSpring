package com.theironyard.controller;

import com.theironyard.entities.Game;
import com.theironyard.services.GameRepository;
import com.theironyard.entities.User;
import com.theironyard.services.UserRepository;
import com.theironyard.utilities.PasswordStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by kdrudy on 12/21/16.
 */
@Controller
public class GameTrackerController {
    @Autowired
    GameRepository games;

    @Autowired
    UserRepository users;

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String login(HttpSession session, String userName, String password) throws Exception {
        User user = users.findFirstByName(userName);
        if(user == null) {
            user = new User(userName, PasswordStorage.createHash(password));
            users.save(user);
        }
        else if(!PasswordStorage.verifyPassword(password, user.password)) {
            throw new Exception("Incorrect Password");
        }
        session.setAttribute("userName", userName);
        return "redirect:/";
    }

    @RequestMapping(path = "/logout", method = RequestMethod.POST)
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String home(HttpSession session, Model model, String genre, Integer releaseYear, String search, Integer page) {
        if(page == null) {
            page = 0;
        }
        PageRequest pr = new PageRequest(page, 5);

        String userName = (String) session.getAttribute("userName");
        User user = users.findFirstByName(userName);
        if(user != null) {
            model.addAttribute("user", user);
        }

        Page<Game> gamesList;
        if(genre != null) {
            gamesList = games.findByGenre(pr, genre);
        } else if(releaseYear != null) {
            gamesList = games.findByReleaseYear(pr, releaseYear);
        } else if(search != null) {
            gamesList = games.findByNameStartsWith(pr, search);
        } else {
            gamesList = games.findAll(pr);
        }
        model.addAttribute("games", gamesList);
        model.addAttribute("nextPage", page+1);
        model.addAttribute("showNext", gamesList.hasNext());
        model.addAttribute("genre", genre);
        return "home";
    }

    @RequestMapping(path = "/add-game", method = RequestMethod.POST)
    public String addGame(HttpSession session, String gameName, String gamePlatform, String gameGenre, int gameYear) {
        String userName = (String) session.getAttribute("userName");
        User user = users.findFirstByName(userName);

        Game game = new Game(gameName, gamePlatform, gameGenre, gameYear, user);
        games.save(game);
        return "redirect:/";
    }

    @PostConstruct
    public void init() throws PasswordStorage.CannotPerformOperationException {
        if(users.count() == 0) {
            User user = new User();
            user.name = "Kyle";
            user.password = PasswordStorage.createHash("password");
            users.save(user);
        }
    }

}
