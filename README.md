# Delivery Microsservices #

Delivery Microsservices uma solução para aplicativos de entrega, separado em microsserviços para explorar esse tipo de arquitetura e demonstrar um exemplo prático da comunicação entre os serviços

## Setup Inicial ##
- Abrir separadamente cada um dos serviços presentes neste repositório.
- Configurar o application-properties de acordo com seu banco de dados.

## Sobre a Solução ##
Nesta solução há 4 serviços principais:

- Server: É o Service Discovery dessa aplicação e será responsável por subir uma instância do Eureka Server, onde todos os outros microsserviços poderão se registrar.
- Gateway: É um Service Registry que fica responsável por centralizar o acesso aos serviços, abstraíndo a necessidade de saber a porta padrão de cada um dos serviços e também atuando como um Load Balancer, distribuíndo as requisições para todas as instâncias dele.
- Pedidos: Microsserviço que gerencia os pedidos.
- Pagamentos: Microsserviço que gerencia os pagamentos.


## Executando Multiplas Instâncias ##

Para executar mais de uma instância de cada serviço, execute o comando abaixo no terminal com o caminho correto de onde está salvo o seu serviço.

`& "C:\Users\seu.usuario\caminho-do-projeto\mvnw.cmd" spring-boot:run -f "C:\Users\seu.usuario\caminho-do-projeto\pom.xml"`

## Comunicação Entre Microsserviços ##

A comunicação entre os microsserviços é realizada utilizando `Feign Client`. Por exemplo, ao confirmar um pagamento no serviço `pagamentos` (alterando o status de `CRIADO` para `CONFIRMADO`), o Feign Client dispara uma requisição para o microsserviço de `pedidos`, atualizando o status do pedido de `REALIZADO` para `PAGO`.

## Exemplo da Comunicação ##

Para confirmar o pagamento de um pedido e atualizar seu status, siga os seguintes passos:

1. Obtenha um pedido que esteja com o status REALIZADO.
2. Dispare a seguinte requisição para confirmar o pagamento, utilizando o ID do pedido:

   `PATCH http://localhost:8082/pagamentos-ms/pagamentos/{pedidoId}/confirmar`

3. Verifique se o status do pedido foi atualizado para PAGO:

   `GET http://localhost:8082/pedidos-ms/pedidos/{id}`

## Padrões de Resiliência ##
Foi aplicado o padrão de resiliência `Circuit Breaker` com fallback para tratamento de erros, garantindo que o sistema continue a funcionar corretamente mesmo quando um dos microsserviços estiver indisponível.
Um exemplo de uso desse padrão é caso o serviço de pedido não esteja disponível ao confirmar um pagamento, aquele pagamento ficará com o status `CONFIRMADO_SEM_INTEGRACAO`, onde o pagamento vai estar confirmado e quando o serviço de pedidos retornar, basta fazer novamente a requisição que o pedido já irá constar com o status `PAGO`

# Conclusão #

Esta solução de microsserviços demonstra a arquitetura e a comunicação eficiente entre serviços independentes. Com o Eureka Server atuando como Service Discovery, os serviços podem se registrar e descobrir uns aos outros dinamicamente. O Gateway centraliza o acesso aos serviços, simplificando a interação com múltiplas instâncias e aplicando balanceamento de carga para distribuir as requisições de forma eficiente. Além disso, padrões de resiliência como Circuit Breaker e fallback garantem a robustez e a continuidade do serviço, mesmo em cenários de falha.
 
     
