package com.leonifrazao.MoraNode.infrastructure.adapter.web;

import com.leonifrazao.MoraNode.domain.port.in.BuscaContratoUseCase;
import com.leonifrazao.MoraNode.domain.port.in.CadastrarContratoUseCase;
import com.leonifrazao.MoraNode.infrastructure.adapter.in.web.dto.ContratoRequest;
import com.leonifrazao.MoraNode.infrastructure.adapter.out.web.dto.ContratoResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contratos")
@RequiredArgsConstructor
public class ContratoController {
    private final CadastrarContratoUseCase contratoCadastroService;
    private final BuscaContratoUseCase buscaContratoUseCase;

    @PostMapping
    public ResponseEntity<Void> criar(@RequestBody @Valid ContratoRequest request) {
        contratoCadastroService.cadastrar(request.toDomain());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<ContratoResponse>> buscar() {
        List<ContratoResponse> contratos = buscaContratoUseCase.buscar().stream()
                .map(ContratoResponse::fromDomain)
                .toList();
        return ResponseEntity.ok(contratos);
    }
}
