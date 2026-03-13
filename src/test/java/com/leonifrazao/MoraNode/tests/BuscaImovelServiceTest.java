package com.leonifrazao.MoraNode.tests;

import com.leonifrazao.MoraNode.domain.model.ImovelDomain;
import com.leonifrazao.MoraNode.domain.port.out.ImovelRepositoryPort;
import com.leonifrazao.MoraNode.domain.usecase.BuscaImovelService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BuscaImovelServiceTest {

    @Mock
    private ImovelRepositoryPort repositoryPort;

    @InjectMocks
    private BuscaImovelService service;

    @Test
    void deveBuscarImovelPorId() {
        Long usuarioId = 100L;
        ImovelDomain imovel = new ImovelDomain(1000, "Rua A", 50, true);
        when(repositoryPort.buscarPorID(1L, usuarioId)).thenReturn(imovel);

        ImovelDomain resultado = service.buscaID(1L, usuarioId);

        assertEquals(imovel, resultado);
        verify(repositoryPort).buscarPorID(1L, usuarioId);
    }

    @Test
    void deveRetornarListaDeImoveis() {
        Long usuarioId = 100L;
        List<ImovelDomain> imoveis = List.of(
                new ImovelDomain(1000, "Rua A", 50, true),
                new ImovelDomain(2000, "Rua B", 80, false)
        );
        when(repositoryPort.buscar(usuarioId)).thenReturn(imoveis);

        List<ImovelDomain> resultado = service.buscar(usuarioId);

        assertEquals(2, resultado.size());
        verify(repositoryPort).buscar(usuarioId);
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoExistemImoveis() {
        Long usuarioId = 100L;
        when(repositoryPort.buscar(usuarioId)).thenReturn(List.of());

        List<ImovelDomain> resultado = service.buscar(usuarioId);

        assertTrue(resultado.isEmpty());
    }
}
