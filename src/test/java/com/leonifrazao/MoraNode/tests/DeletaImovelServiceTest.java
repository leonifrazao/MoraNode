package com.leonifrazao.MoraNode.tests;

import com.leonifrazao.MoraNode.domain.port.out.ContratoRepositoryPort;
import com.leonifrazao.MoraNode.domain.port.out.ImovelRepositoryPort;
import com.leonifrazao.MoraNode.domain.usecase.DeletaImovelService;
import com.leonifrazao.MoraNode.infrastructure.exceptions.ImovelComContratoAtivo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeletaImovelServiceTest {

    @Mock
    private ImovelRepositoryPort repositoryPort;

    @Mock
    private ContratoRepositoryPort contratoRepositoryPort;

    @InjectMocks
    private DeletaImovelService service;

    @Test
    void deveLancarExcecaoQuandoImovelTiverContratoAtivo() {
        Long id = 1L;
        Long usuarioId = 100L;
        when(contratoRepositoryPort.existeContratoAtivoParaImovel(id, usuarioId)).thenReturn(true);

        assertThrows(ImovelComContratoAtivo.class, () -> service.deletar(id, usuarioId));

        verify(repositoryPort, never()).deletar(id, usuarioId);
    }

    @Test
    void deveDeletarComSucessoQuandoNaoHouverContratoAtivo() {
        Long id = 1L;
        Long usuarioId = 100L;
        when(contratoRepositoryPort.existeContratoAtivoParaImovel(id, usuarioId)).thenReturn(false);

        service.deletar(id, usuarioId);

        verify(repositoryPort, times(1)).deletar(id, usuarioId);
    }
}