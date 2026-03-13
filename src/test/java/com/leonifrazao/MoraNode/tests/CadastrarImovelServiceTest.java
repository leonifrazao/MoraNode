package com.leonifrazao.MoraNode.tests;

import com.leonifrazao.MoraNode.domain.model.ContratoDomain;
import com.leonifrazao.MoraNode.domain.model.ImovelDomain;
import com.leonifrazao.MoraNode.domain.model.enums.StatusContrato;
import com.leonifrazao.MoraNode.domain.model.enums.TipoContrato;
import com.leonifrazao.MoraNode.domain.port.out.ContratoRepositoryPort;
import com.leonifrazao.MoraNode.domain.port.out.ImovelRepositoryPort;
import com.leonifrazao.MoraNode.domain.usecase.CadastrarImovelService;
import com.leonifrazao.MoraNode.infrastructure.exceptions.ImovelComContratoAtivo;
import com.leonifrazao.MoraNode.infrastructure.exceptions.SemImovelException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CadastrarImovelServiceTest {

    @Mock
    private ImovelRepositoryPort repositoryPort;

    @Mock
    private ContratoRepositoryPort contratoRepositoryPort;

    @InjectMocks
    private CadastrarImovelService service;

    @Test
    void deveCadastrarImovelComSucesso() {
        Long usuarioId = 100L;
        ImovelDomain imovel = new ImovelDomain(1000, "Rua A, 123", 50, true);

        service.cadastrar(imovel, usuarioId);

        verify(repositoryPort).salvar(imovel);
    }

    @Test
    void deveEditarImovelQuandoNaoHouverContratoAtivo() {
        Long id = 1L;
        Long usuarioId = 100L;
        ImovelDomain imovelExistente = new ImovelDomain(1000, "Rua A, 123", 50, true);
        ImovelDomain imovelAtualizado = new ImovelDomain(2000, "Rua B, 456", 80, true);
        when(repositoryPort.buscarPorID(id, usuarioId)).thenReturn(imovelExistente);
        when(contratoRepositoryPort.buscarPorImovelId(id, usuarioId)).thenReturn(Collections.emptyList());

        service.editarPorID(id, imovelAtualizado, usuarioId);

        verify(repositoryPort).editarPorID(id, imovelAtualizado, usuarioId);
    }

    @Test
    void deveLancarExcecaoAoEditarImovelInexistente() {
        Long id = 99L;
        Long usuarioId = 100L;
        ImovelDomain imovelAtualizado = new ImovelDomain(2000, "Rua B", 80, true);
        when(repositoryPort.buscarPorID(id, usuarioId)).thenThrow(new EntityNotFoundException());

        assertThrows(SemImovelException.class, () -> service.editarPorID(id, imovelAtualizado, usuarioId));
    }

    @Test
    void deveLancarExcecaoAoEditarImovelComContratoAtivo() {
        Long id = 1L;
        Long usuarioId = 100L;
        ImovelDomain imovelExistente = new ImovelDomain(1000, "Rua A", 50, true);
        ImovelDomain imovelAtualizado = new ImovelDomain(2000, "Rua B", 80, true);
        ContratoDomain contratoAtivo = new ContratoDomain(
                1L, id, "Dono", "Inquilino",
                new BigDecimal("1500"), LocalDate.now(), LocalDate.now().plusMonths(6),
                new BigDecimal("1.5"), TipoContrato.ALUGUEL, StatusContrato.ATIVO
        );
        when(repositoryPort.buscarPorID(id, usuarioId)).thenReturn(imovelExistente);
        when(contratoRepositoryPort.buscarPorImovelId(id, usuarioId)).thenReturn(List.of(contratoAtivo));

        assertThrows(ImovelComContratoAtivo.class, () -> service.editarPorID(id, imovelAtualizado, usuarioId));

        verify(repositoryPort, never()).editarPorID(any(), any(), any());
    }

    @Test
    void deveEditarImovelComContratoFinalizado() {
        Long id = 1L;
        Long usuarioId = 100L;
        ImovelDomain imovelExistente = new ImovelDomain(1000, "Rua A", 50, true);
        ImovelDomain imovelAtualizado = new ImovelDomain(2000, "Rua B", 80, true);
        ContratoDomain contratoFinalizado = new ContratoDomain(
                1L, id, "Dono", "Inquilino",
                new BigDecimal("1500"), LocalDate.now(), LocalDate.now().plusMonths(6),
                new BigDecimal("1.5"), TipoContrato.ALUGUEL, StatusContrato.FINALIZADO
        );
        when(repositoryPort.buscarPorID(id, usuarioId)).thenReturn(imovelExistente);
        when(contratoRepositoryPort.buscarPorImovelId(id, usuarioId)).thenReturn(List.of(contratoFinalizado));

        service.editarPorID(id, imovelAtualizado, usuarioId);

        verify(repositoryPort).editarPorID(id, imovelAtualizado, usuarioId);
    }

    @Test
    void deveAlterarDisponibilidadeComSucesso() {
        Long id = 1L;
        Long usuarioId = 100L;
        ImovelDomain imovel = new ImovelDomain(1000, "Rua A", 50, true);
        when(repositoryPort.buscarPorID(id, usuarioId)).thenReturn(imovel);

        service.alterarDisponibilidade(id, false, usuarioId);

        verify(repositoryPort).salvar(imovel);
    }
}
