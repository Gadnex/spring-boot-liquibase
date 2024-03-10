package net.binarypaper.springbootliquibase.greeting;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = "greetings")
@CrossOrigin("${application.cors.origins}")
@RequiredArgsConstructor
public class GreetingController {

    private final GreetingRepository greetingRepository;

    @GetMapping
    public List<Greeting> getAllGreetings() {
        return greetingRepository.findAll();
    }

    @GetMapping(path = "{name}")
    public Greeting generateGreeting(@PathVariable String name) {
        Greeting greeting = new Greeting();
        greeting.setMessage("Hello " + name);
        greetingRepository.save(greeting);
        return greeting;
    }

}