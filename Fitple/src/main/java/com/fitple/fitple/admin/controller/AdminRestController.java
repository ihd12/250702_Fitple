package com.fitple.fitple.admin.controller;

import com.fitple.fitple.base.user.dto.UserDTO;
import com.fitple.fitple.base.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminRestController {
    private final UserService userService;

    @DeleteMapping("/member")
    public ResponseEntity<String> deleteUser(@RequestBody UserDTO userDTO) {
        userService.delete(userDTO.getEmail());
        return ResponseEntity.ok("success");
    }
}
