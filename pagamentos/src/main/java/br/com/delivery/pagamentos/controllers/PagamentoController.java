package br.com.delivery.pagamentos.controllers;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.delivery.pagamentos.dtos.PagamentoDTO;
import br.com.delivery.pagamentos.services.PagamentoService;

import java.net.URI;

@RestController
@RequestMapping("/pagamentos")
public class PagamentoController
    extends
        Controller {

    @Autowired
    private PagamentoService pagamentoService;

    @GetMapping
    public ResponseEntity<Page<PagamentoDTO>> getPagamentos(@PageableDefault(size=10) Pageable pageable) {
        return ok(pagamentoService.getPagamentos(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagamentoDTO> getPagamento(@PathVariable @NotNull Long id) {
        return ok(pagamentoService.getPagamento(id));
    }

    @PostMapping
    public ResponseEntity<PagamentoDTO> createPagamento(@RequestBody @Valid PagamentoDTO pagamentoDTO, UriComponentsBuilder uriBuilder) {
        PagamentoDTO pagamento = pagamentoService.createPagamento( pagamentoDTO );
        URI endereco = uriBuilder.path("/pagamentos/{id}").buildAndExpand(pagamento.getId()).toUri();

        return ResponseEntity.created(endereco).body(pagamento);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PagamentoDTO> updatePagamento(@PathVariable @NotNull Long id, @RequestBody @Valid PagamentoDTO pagamentoDTO) {
        return ok(pagamentoService.updatePagamento( id, pagamentoDTO ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePagamento(@PathVariable @NotNull Long id) {
        pagamentoService.deletePagamento(id);
        return noContent();
    }

    @PatchMapping("/{id}/confirmar")
    @CircuitBreaker(name = "atualizaPedido", fallbackMethod = "integracaoPendente")
    public void confirmarPagamento(@PathVariable @NotNull Long id){
        pagamentoService.confirmarPagamento(id);
    }

    public void integracaoPendente(Long id, Exception e) {
        pagamentoService.alteraStatus(id);
    }
}
