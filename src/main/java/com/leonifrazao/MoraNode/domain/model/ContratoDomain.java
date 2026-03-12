package com.leonifrazao.MoraNode.domain.model;
import com.leonifrazao.MoraNode.domain.model.enums.StatusContrato;
import com.leonifrazao.MoraNode.domain.model.enums.TipoContrato;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
public class ContratoDomain {
    @Setter(AccessLevel.PACKAGE)
    private Long id;
    @Setter(AccessLevel.PACKAGE)
    private Long imovelId;

    private String nomeDono;
    private String nomeInquilino;
    private BigDecimal valorAcordado; // O valor que foi fechado no papel
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private boolean podeRenovar;
    private BigDecimal taxaJurosMensal;
    @Setter(AccessLevel.PACKAGE)
    private StatusContrato statusContrato; // Ex: ATIVO, ENCERRADO
    @Setter(AccessLevel.PACKAGE)
    private TipoContrato tipo;

    public ContratoDomain(Long id, Long imovelId, String nomeDono, String nomeInquilino, BigDecimal valorAcordado,
                          LocalDate dataInicio, LocalDate dataFim, BigDecimal taxaJurosMensal, TipoContrato tipo, StatusContrato status) {
        this.setId(id);
        this.setImovelId(imovelId);
        this.setTipo(tipo);
        this.setStatusContrato(status);

        this.setNomeDono(nomeDono);
        this.setNomeInquilino(nomeInquilino);
        this.setValorAcordado(valorAcordado);
        this.setDataInicio(dataInicio);
        this.setDataFim(dataFim);
        this.setTaxaJurosMensal(taxaJurosMensal);
        this.setPodeRenovar();
    }

    private void validateNotBlank(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " não pode estar vazio!");
        }
    }

    public void setNomeDono(String nome) {
        validateNotBlank(nome, "nomeDono");
        this.nomeDono = nome;
    }

    public void setNomeInquilino(String nome) {
        validateNotBlank(nome, "nomeInquilino");
        this.nomeInquilino = nome;
    }

    public void setValorAcordado(BigDecimal valor) {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O valor acordado não pode ser negativo ou nulo!");
        }
        this.valorAcordado = valor;
    }

    public void setTaxaJurosMensal(BigDecimal valor) {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O valor acordado não pode ser negativo ou nulo!");
        }
        this.taxaJurosMensal = valor;
    }

    public void setDataInicio(LocalDate dataInicio) {
        if (dataInicio == null) {
            throw new IllegalArgumentException("Data de início é obrigatória!");
        }
        if (dataInicio.isBefore(LocalDate.now().minusDays(30))) {
            throw new IllegalArgumentException("Data de início não pode ser anterior a 30 dias do presente!");
        }
        this.dataInicio = dataInicio;

        validarDatas();
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
        validarDatas();
    }

    private void validarDatas() {
        if (dataInicio != null && dataFim != null && dataFim.isBefore(dataInicio)) {
            throw new IllegalArgumentException("A data de fim não pode ser anterior à data de início!");
        }
    }

    public void setPodeRenovar() {
        this.podeRenovar = validarSePodeTerRenovacao();
    }

    private boolean validarSePodeTerRenovacao() {
        if (this.statusContrato != StatusContrato.ATIVO) {
            return false;
        }
        if (this.tipo != TipoContrato.ALUGUEL) {
            return false;
        }
        return true;
    }



}
