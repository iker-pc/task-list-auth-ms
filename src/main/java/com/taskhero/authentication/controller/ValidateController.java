package com.taskhero.authentication.controller;

import com.taskhero.authentication.model.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

@RestController
@RequestMapping("/api/validate")
public class ValidateController {

    @GetMapping
    public User validateUser(@SessionAttribute User user) {
        return user;
    }

}
