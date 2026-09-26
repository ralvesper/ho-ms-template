package com.highonline.exampleapi.greeting;

import com.highonline.common.DomainEntityNotFoundException;
import com.highonline.common.PageModel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/greetings")
@RequiredArgsConstructor
public class GreetingController {

    public record GreetingInput(@NotBlank String message) {}

    private final GreetingRepository repository;

    @GetMapping
    public PageModel<Greeting> list(Pageable pageable) {
        return PageModel.of(repository.findAll(pageable));
    }

    @GetMapping("/{id}")
    public Greeting get(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new DomainEntityNotFoundException("Greeting " + id + " not found"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Greeting create(@RequestBody @Valid GreetingInput input) {
        return repository.save(new Greeting(input.message()));
    }
}
