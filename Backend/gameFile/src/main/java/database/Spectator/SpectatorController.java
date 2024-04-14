package database.Spectator;


import database.Friends.Friend;
import database.Friends.FriendRepository;

import database.Chat.MessageRepository;
import database.Lobby.LobbyRepository;
import database.Ranking.Ranking;
import database.Ranking.RankingRepository;
import database.Signin.Signin;
import database.Signin.SigninRepository;
import database.Stats.Stat;
import database.Stats.StatRepository;
import database.Ranking.RankingRepository;
import database.Users.UserRepository;
import database.Users.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import java.security.PublicKey;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class SpectatorController {

    @Autowired
    private SpectatorRepository spectatorRepository;


    //get list of spectators
    @GetMapping(path = "/getSpectators")
    public ResponseEntity<List<Spectator>> getSpectators() {
        List<Spectator> spectators = spectatorRepository.findAll();
        if (spectators.isEmpty()) {
            return ResponseEntity.noContent().build(); // Return 204 No Content if the list is empty
        }
        return ResponseEntity.ok(spectators); // Return 200 OK with the list of spectators
    }


}
