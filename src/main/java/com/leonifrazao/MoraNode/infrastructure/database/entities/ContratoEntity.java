package com.leonifrazao.MoraNode.infrastructure.database.entities;

import com.leonifrazao.MoraNode.domain.model.enums.StatusContrato;
import com.leonifrazao.MoraNode.domain.model.enums.TipoContrato;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "tb_contratos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContratoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long imovelId;

    @Column(nullable = false)
    private Long usuarioId;

    @Column(length = 255)
    private String nomeDono;

    @Column(length = 255)
    private String nomeInquilino;

    @Column(precision = 19, scale = 2) // Padrão financeiro
    private BigDecimal valorAcordado;

    private LocalDate dataInicio;
    private LocalDate dataFim;
    private boolean podeRenovar;

    @Column(precision = 5, scale = 2) // Ex: 100.00%
    private BigDecimal taxaJurosMensal;

    @Enumerated(EnumType.STRING) // Salva "ATIVO" no banco, não 0 ou 1
    private StatusContrato statusContrato;

    @Enumerated(EnumType.STRING)
    private TipoContrato tipo;
}