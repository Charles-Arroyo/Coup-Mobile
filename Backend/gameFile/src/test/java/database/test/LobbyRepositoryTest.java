package database.test;

import database.Lobby.LobbyRepository;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class LobbyRepositoryTest {

    @Autowired
    private LobbyRepository lobbyRepository;

    @Test
    public void testDeleteExistingLobby() {
        // Assuming lobby with ID 1 exists in the database
        int lobbyIdToDelete = 3252;
        lobbyRepository.deleteById(lobbyIdToDelete);
        assertFalse(lobbyRepository.existsById(lobbyIdToDelete));
    }

    @Test
    public void testDeleteNonExistingLobby() {
        // Assuming lobby with ID 999 does not exist in the database
        int nonExistingLobbyId = 999;
        assertThrows(EmptyResultDataAccessException.class, () -> {
            lobbyRepository.deleteById(nonExistingLobbyId);
        });
    }

    // Add more test methods as needed to cover different scenarios
}
