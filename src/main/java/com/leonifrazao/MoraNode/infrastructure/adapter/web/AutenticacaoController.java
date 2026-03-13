package com.leonifrazao.MoraNode.infrastructure.adapter.web;

import com.leonifrazao.MoraNode.domain.port.in.RegistroUsuarioUseCase;
import com.leonifrazao.MoraNode.domain.port.out.UsuarioRepositoryPort;
import com.leonifrazao.MoraNode.infrastructure.adapter.in.web.dto.LoginRequest;
import com.leonifrazao.MoraNode.infrastructure.adapter.in.web.dto.RegistroRequest;
import com.leonifrazao.MoraNode.infrastructure.adapter.out.web.dto.TokenResponse;
import com.leonifrazao.MoraNode.infrastructure.security.JwtProvedorToken;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AutenticacaoController {

    private final AuthenticationManager authenticationManager;
    private final JwtProvedorToken jwtProvedorToken;
    private final RegistroUsuarioUseCase registroUsuarioUseCase;
    private final UsuarioRepositoryPort usuarioRepositoryPort;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody @Valid LoginRequest request) {
        Authentication autenticacao = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.senha())
        );

        User usuario = (User) autenticacao.getPrincipal();
        String papel = usuario.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "");

        String accessToken = jwtProvedorToken.gerarAccessToken(usuario.getUsername(), papel);
        String refreshToken = jwtProvedorToken.gerarRefreshToken(usuario.getUsername());

        return ResponseEntity.ok(new TokenResponse(accessToken, refreshToken));
    }

    @PostMapping("/registro")
    public ResponseEntity<Void> registro(@RequestBody @Valid RegistroRequest request) {
        String senhaHash = passwordEncoder.encode(request.senha());
        registroUsuarioUseCase.registrar(request.nome(), request.email(), senhaHash);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");

        if (refreshToken == null || !jwtProvedorToken.tokenValido(refreshToken)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token inválido!");
        }

        String email = jwtProvedorToken.extrairEmail(refreshToken);
        UsuarioRepositoryPort.UsuarioBuscado usuario = usuarioRepositoryPort.buscarPorEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não encontrado!"));

        String novoAccessToken = jwtProvedorToken.gerarAccessToken(email, usuario.papel());
        String novoRefreshToken = jwtProvedorToken.gerarRefreshToken(email);

        return ResponseEntity.ok(new TokenResponse(novoAccessToken, novoRefreshToken));
    }
}
