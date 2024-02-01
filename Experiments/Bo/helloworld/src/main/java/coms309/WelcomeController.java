package coms309;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
class WelcomeController {

    @GetMapping("/")
    public String welcome() {
        return "Hello and welcome to COMS 309";
    }

//    @GetMapping("/{name}")
//    public String welcome(@PathVariable String name) {
//        return "Hello and welcome to COMS 309: " + name;
//    }

    @GetMapping("/welcome/{name}/{age}")
    public String welcome(@PathVariable String name,@PathVariable int age){
        return "Hello" + name + ", and welcome to COMS 309: and your age is " + age;
    }
}
