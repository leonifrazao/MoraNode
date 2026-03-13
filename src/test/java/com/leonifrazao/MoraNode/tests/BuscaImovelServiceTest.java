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
        ImovelDomain imovel = new ImovelDomain(1000, "Rua A", 50, true);
        when(repositoryPort.buscarPorID(1L)).thenReturn(imovel);

        ImovelDomain resultado = service.buscaID(1L);

        assertEquals(imovel, resultado);
        verify(repositoryPort).buscarPorID(1L);
    }

    @Test
    void deveRetornarListaDeImoveis() {
        List<ImovelDomain> imoveis = List.of(
                new ImovelDomain(1000, "Rua A", 50, true),
                new ImovelDomain(2000, "Rua B", 80, false)
        );
        when(repositoryPort.buscar()).thenReturn(imoveis);

        List<ImovelDomain> resultado = service.buscar();

        assertEquals(2, resultado.size());
        verify(repositoryPort).buscar();
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoExistemImoveis() {
        when(repositoryPort.buscar()).thenReturn(List.of());

        List<ImovelDomain> resultado = service.buscar();

        assertTrue(resultado.isEmpty());
    }
}
