package uz.pdp.bankcard.controller;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.pdp.bankcard.payload.ApiResponse;
import uz.pdp.bankcard.payload.LoginDto;
import uz.pdp.bankcard.service.AuthService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    AuthService authService;

    @PostMapping("login")
    public ApiResponse login(LoginDto loginDto) {
        return authService.login(loginDto);
    }



}