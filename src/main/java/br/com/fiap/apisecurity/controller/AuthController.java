package br.com.fiap.apisecurity.controller;

import br.com.fiap.apisecurity.dto.AuthDTO;
import br.com.fiap.apisecurity.dto.LoginResponseDTO;
import br.com.fiap.apisecurity.dto.RegisterDTO;
import br.com.fiap.apisecurity.entity.Usuario;
import br.com.fiap.apisecurity.repository.UsuarioRepository;
import br.com.fiap.apisecurity.service.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthDTO authDTO) {
        // Gera um token do tipo UserPasswordAuthentication para esse usuário e senha
        var userPwd = new UsernamePasswordAuthenticationToken(
                authDTO.login(),
                authDTO.senha()
        );
        // Autentica o usuário
        var auth = this.authenticationManager.authenticate(userPwd);
        // Gera um token JWT
        var token = tokenService.generateToken((Usuario) auth.getPrincipal());
        return ResponseEntity.ok(token);
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterDTO registerDTO) {
        if (usuarioRepository.findByLogin(registerDTO.login()) != null) {
            return ResponseEntity.badRequest().build();
        }
        String senhaCriptografada = new BCryptPasswordEncoder()
                .encode(registerDTO.senha());
        Usuario novoUsuario = new Usuario(
                registerDTO.login(),
                senhaCriptografada,
                registerDTO.role());
        usuarioRepository.save(novoUsuario);
        return ResponseEntity.ok().build();
    }
}