package coms309;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
class WelcomeController {

    @GetMapping("/")
    public String welcome() {
        return "test";
    }

    @GetMapping("/{name}/{num}/{number2}")
    public String welcome(@PathVariable String name, @PathVariable Long num,@PathVariable long number2) {
        return "Your name is " + name + num + number2;
    }
}
