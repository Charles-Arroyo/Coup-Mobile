package database.Theme;

import database.Users.User;
import database.Users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/themes")
public class ThemeController {
    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/{userEmail}")
    public ResponseEntity<Theme> createTheme(@PathVariable String userEmail, @RequestBody Theme theme) {
        User user = userRepository.findByUserEmail(userEmail);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        theme.setUser(user);
        Theme savedTheme = themeRepository.save(theme);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTheme);
    }

    @GetMapping("/{userEmail}")
    public ResponseEntity<Theme> getThemeByUserId(@PathVariable String userEmail) {
        User user = userRepository.findByUserEmail(userEmail);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        Theme theme = themeRepository.findByUser(user);
        if (theme == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(theme);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Theme> updateTheme(@PathVariable String userEmail, @RequestBody Theme updatedTheme) {
        User user = userRepository.findByUserEmail(userEmail);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        Theme existingTheme = themeRepository.findByUser(user);
        if (existingTheme == null) {
            return ResponseEntity.notFound().build();
        }

        existingTheme.setData(updatedTheme.getData());
        Theme savedTheme = themeRepository.save(existingTheme);
        return ResponseEntity.ok(savedTheme);
    }
}