package com.leonifrazao.MoraNode.tests;

import com.leonifrazao.MoraNode.domain.port.out.ContratoRepositoryPort;
import com.leonifrazao.MoraNode.domain.usecase.ValidaEstadoContratoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ValidaEstadoContratoServiceTest {

    @Mock
    private ContratoRepositoryPort repositoryPort;

    @InjectMocks
    private ValidaEstadoContratoService service;

    @Test
    void deveRetornarTrueQuandoExisteContratoAtivo() {
        Long usuarioId = 100L;
        when(repositoryPort.existeContratoAtivoParaImovel(1L, usuarioId)).thenReturn(true);

        assertTrue(service.existeContratoAtivoParaImovel(1L, usuarioId));
    }

    @Test
    void deveRetornarFalseQuandoNaoExisteContratoAtivo() {
        Long usuarioId = 100L;
        when(repositoryPort.existeContratoAtivoParaImovel(1L, usuarioId)).thenReturn(false);

        assertFalse(service.existeContratoAtivoParaImovel(1L, usuarioId));
    }
}
