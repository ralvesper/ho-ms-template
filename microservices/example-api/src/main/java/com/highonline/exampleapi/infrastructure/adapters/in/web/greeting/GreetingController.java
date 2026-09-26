package com.highonline.exampleapi.infrastructure.adapters.in.web.greeting;

import com.highonline.common.infrastructure.adapters.in.web.PageModel;
import com.highonline.exampleapi.core.ports.in.greeting.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/greetings")
@RequiredArgsConstructor
public class GreetingController {

    private final ForManagingGreetings forManagingGreetings;
    private final ForQueryingGreetings forQueryingGreetings;

    @GetMapping
    public PageModel<GreetingOutput> findAll(Pageable pageable) {
        return PageModel.of(forQueryingGreetings.findAll(pageable));
    }

    @GetMapping("/{id}")
    public GreetingOutput findById(@PathVariable String id) {
        return forQueryingGreetings.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GreetingOutput create(@RequestBody @Valid GreetingInput input) {
        return forQueryingGreetings.findById(forManagingGreetings.create(input));
    }
}
