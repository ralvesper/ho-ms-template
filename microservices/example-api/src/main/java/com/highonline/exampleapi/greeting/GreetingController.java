package com.highonline.exampleapi.greeting;

import com.highonline.common.DomainEntityNotFoundException;
import com.highonline.common.PageModel;
import io.hypersistence.tsid.TSID;
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

    public record GreetingInput(@NotBlank String message, @NotBlank String recipientEmail) {}
    public record TranslationModel(String id, String lang, String text) {}

    private final GreetingRepository repository;
    private final TranslatorClient translator;

    @GetMapping
    public PageModel<GreetingModel> list(Pageable pageable) {
        return PageModel.of(repository.findAll(pageable).map(GreetingModel::of));
    }

    @GetMapping("/{id}")
    public GreetingModel get(@PathVariable String id) {
        return GreetingModel.of(find(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GreetingModel create(@RequestBody @Valid GreetingInput input) {
        return GreetingModel.of(repository.save(new Greeting(input.message(), input.recipientEmail())));
    }

    @GetMapping("/{id}/translation")
    public TranslationModel translate(@PathVariable String id, @RequestParam String lang) {
        Greeting greeting = find(id);
        return new TranslationModel(id, lang, translator.translate(greeting.getMessage(), lang));
    }

    private Greeting find(String id) {
        long key;
        try {
            key = TSID.from(id).toLong();
        } catch (IllegalArgumentException e) {
            throw new DomainEntityNotFoundException("Greeting " + id + " not found");
        }
        return repository.findById(key)
                .orElseThrow(() -> new DomainEntityNotFoundException("Greeting " + id + " not found"));
    }
}
