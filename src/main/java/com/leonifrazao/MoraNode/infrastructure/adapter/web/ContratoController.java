package com.leonifrazao.MoraNode.infrastructure.adapter.web;

import com.leonifrazao.MoraNode.domain.model.ContratoDomain;
import com.leonifrazao.MoraNode.domain.port.in.BuscaContratoUseCase;
import com.leonifrazao.MoraNode.domain.port.in.CadastrarContratoUseCase;
import com.leonifrazao.MoraNode.infrastructure.adapter.in.web.dto.ContratoRequest;
import com.leonifrazao.MoraNode.infrastructure.adapter.in.web.dto.StatusRequest;
import com.leonifrazao.MoraNode.infrastructure.adapter.out.web.dto.ContratoResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.leonifrazao.MoraNode.infrastructure.security.UsuarioDetalhe;

import java.util.List;

@RestController
@RequestMapping("/contratos")
@RequiredArgsConstructor
public class ContratoController {
    private final CadastrarContratoUseCase contratoCadastroService;
    private final BuscaContratoUseCase buscaContratoUseCase;

    @PostMapping
    public ResponseEntity<Void> criar(@RequestBody @Valid ContratoRequest request, @AuthenticationPrincipal UsuarioDetalhe usuario) {
        contratoCadastroService.cadastrar(request.toDomain(), usuario.getId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<ContratoResponse>> buscar(@AuthenticationPrincipal UsuarioDetalhe usuario) {
        List<ContratoResponse> contratos = buscaContratoUseCase.buscar(usuario.getId()).stream()
                .map(ContratoResponse::fromDomain)
                .toList();
        return ResponseEntity.ok(contratos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContratoResponse> buscarPorId(@PathVariable Long id, @AuthenticationPrincipal UsuarioDetalhe usuario) {
        ContratoDomain imovel = buscaContratoUseCase.buscarPorId(id, usuario.getId());
        ContratoResponse resposta = ContratoResponse.fromDomain(imovel);
        return ResponseEntity.ok(resposta);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> atualizarStatus(@PathVariable Long id, @RequestBody StatusRequest request, @AuthenticationPrincipal UsuarioDetalhe usuario) {
        contratoCadastroService.atualizarStatus(id, request.statusContrato(), usuario.getId());
        return ResponseEntity.noContent().build();
    }
}
