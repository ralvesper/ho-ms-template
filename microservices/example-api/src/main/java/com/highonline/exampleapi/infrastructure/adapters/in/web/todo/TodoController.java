package com.highonline.exampleapi.infrastructure.adapters.in.web.todo;

import com.highonline.common.infrastructure.adapters.in.web.PageModel;
import com.highonline.exampleapi.core.ports.in.todo.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/todos")
@RequiredArgsConstructor
public class TodoController {

    private final ForManagingTodos forManagingTodos;
    private final ForQueryingTodos forQueryingTodos;

    @GetMapping
    public PageModel<TodoOutput> findAll(Pageable pageable) {
        return PageModel.of(forQueryingTodos.findAll(pageable));
    }

    @GetMapping("/{id}")
    public TodoOutput findById(@PathVariable String id) {
        return forQueryingTodos.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TodoOutput create(@RequestBody @Valid TodoInput input) {
        return forQueryingTodos.findById(forManagingTodos.create(input));
    }

    @PutMapping("/{id}")
    public TodoOutput changeTitle(@PathVariable String id, @RequestBody @Valid TodoInput input) {
        forManagingTodos.changeTitle(id, input);
        return forQueryingTodos.findById(id);
    }

    @PostMapping("/{id}/complete")
    public TodoOutput complete(@PathVariable String id) {
        forManagingTodos.complete(id);
        return forQueryingTodos.findById(id);
    }

    @PostMapping("/{id}/reopen")
    public TodoOutput reopen(@PathVariable String id) {
        forManagingTodos.reopen(id);
        return forQueryingTodos.findById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@PathVariable String id) {
        forManagingTodos.remove(id);
    }
}
