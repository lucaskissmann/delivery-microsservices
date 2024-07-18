package br.com.delivery.pagamentos.services;

import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.delivery.pagamentos.dtos.PagamentoDTO;
import br.com.delivery.pagamentos.enums.Status;
import br.com.delivery.pagamentos.http.PedidoClient;
import br.com.delivery.pagamentos.models.Pagamento;
import br.com.delivery.pagamentos.repositories.PagamentoRepository;

@Service
public class PagamentoService {

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PedidoClient feignPedido;

    public Page<PagamentoDTO> getPagamentos(Pageable paginacao) {
        return pagamentoRepository
                                .findAll(paginacao)
                                .map(p -> modelMapper.map(p, PagamentoDTO.class));
    }

    public PagamentoDTO getPagamento(Long id) {
        Pagamento pagamento = pagamentoRepository.findById( id )
                .orElseThrow(() -> new EntityNotFoundException("Pagamento não localizado para o id: #" + id ));

        return modelMapper.map(pagamento, PagamentoDTO.class);
    }

    public PagamentoDTO createPagamento(PagamentoDTO dto) {
        Pagamento pagamento = modelMapper.map(dto, Pagamento.class);
        pagamento.setStatus(Status.CRIADO);
        pagamentoRepository.save(pagamento);

        return modelMapper.map(pagamento, PagamentoDTO.class);
    }

    public PagamentoDTO updatePagamento(Long id, PagamentoDTO dto) {
        Pagamento pagamento = modelMapper.map(dto, Pagamento.class);
        pagamento.setId( id );
        pagamento = pagamentoRepository.save(pagamento);

        return modelMapper.map(pagamento, PagamentoDTO.class);
    }

    public void deletePagamento(Long id) {
        pagamentoRepository.deleteById( id );
    }

    public void confirmarPagamento(Long id){
        Pagamento pagamento = pagamentoRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        pagamento.setStatus(Status.CONFIRMADO);
        pagamentoRepository.save(pagamento);
        feignPedido.atualizaPagamento(pagamento.getPedidoId());
    }

    public void alteraStatus(Long id) {
        Pagamento pagamento = pagamentoRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        pagamento.setStatus(Status.CONFIRMADO_SEM_INTEGRACAO);
        pagamentoRepository.save(pagamento);
    }
}
