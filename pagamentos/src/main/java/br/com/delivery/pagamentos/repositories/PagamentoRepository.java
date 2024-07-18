package br.com.delivery.pagamentos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.delivery.pagamentos.models.Pagamento;

public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {
}
