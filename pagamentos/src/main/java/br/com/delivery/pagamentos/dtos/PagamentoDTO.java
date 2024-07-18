package br.com.delivery.pagamentos.dtos;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

import br.com.delivery.pagamentos.enums.Status;

@Getter
@Setter
public class PagamentoDTO {
    private Long id;
    private BigDecimal valor;
    private String nome;
    private String numero;
    private String expiracao;
    private String codigo;
    private Status status;
    private Long formaDePagamentoId;
    private Long pedidoId;


}