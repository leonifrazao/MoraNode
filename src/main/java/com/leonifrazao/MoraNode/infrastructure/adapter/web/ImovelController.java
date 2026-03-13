package com.leonifrazao.MoraNode.infrastructure.adapter.web;

import com.leonifrazao.MoraNode.domain.model.ImovelDomain;
import com.leonifrazao.MoraNode.domain.port.in.BuscaImovelUseCase;
import com.leonifrazao.MoraNode.domain.port.in.CadastrarImovelUseCase;
import com.leonifrazao.MoraNode.domain.port.in.DeletaImovelUseCase;
import com.leonifrazao.MoraNode.infrastructure.adapter.in.web.dto.ImovelRequest;
import com.leonifrazao.MoraNode.infrastructure.adapter.out.web.dto.ImovelResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/imoveis")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class ImovelController {

    private final CadastrarImovelUseCase imovelService;
    private final BuscaImovelUseCase imovelBuscaService;
    private final DeletaImovelUseCase imovelDeleteService;

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePorID(@PathVariable Long id) {
        imovelDeleteService.deletar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ImovelResponse> buscarPorID(@PathVariable Long id) {
        ImovelDomain imovel = imovelBuscaService.buscaID(id);
        ImovelResponse resposta = ImovelResponse.fromDomain(imovel);
        return ResponseEntity.ok(resposta);
    }

    @GetMapping
    public ResponseEntity<List<ImovelResponse>> busca() {
        List<ImovelResponse> imoveis = imovelBuscaService.buscar().stream()
                .map(ImovelResponse::fromDomain).toList();
        return ResponseEntity.ok(imoveis);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> editarPorID(@PathVariable Long id,@RequestBody @Valid ImovelRequest request ) {
        imovelService.editarPorID(id, request.toDomain());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping
    public ResponseEntity<Void> criar(@RequestBody @Valid ImovelRequest request) {
        imovelService.cadastrar(request.toDomain());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
