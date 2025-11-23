package org.uk_energy.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.uk_energy.Service.UserService;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/avgEnergyMix")
    public ResponseEntity<Object> getAvgEnergyMix() {
        Object res = userService.getAvgEnergyMix();
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @GetMapping("/bestInterval")
    public Object getBestInterval(@RequestParam int duration) {
        return userService.getBestInterval(duration);
    }
}
