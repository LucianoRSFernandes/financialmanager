# Financial Manager API 💰

Este projeto é uma API de Gestão Financeira robusta desenvolvida com Java 17 e Spring Boot 3. A aplicação segue os princípios da Clean Architecture e DDD (Domain-Driven Design), garantindo desacoplamento entre a lógica de negócio e os detalhes de infraestrutura. Ela suporta processamento assíncrono de transações via Kafka, integração com APIs externas e autenticação segura via JWT.

## 🚀 Tecnologias Utilizadas
* Linguagem: Java 17.

* Framework: Spring Boot 3.5.10.

* Banco de Dados: PostgreSQL.

* Mensageria: Apache Kafka.

* Segurança: Spring Security com autenticação JWT.

* Documentação: SpringDoc OpenAPI (Swagger).

* Contêiner: Docker e Docker Compose.

* Ferramentas de Relatório: Apache POI (para exportação de Excel).

## 🏗️ Arquitetura e Estrutura do Projeto
O projeto está organizado em camadas para separar as regras de domínio da infraestrutura:

- Domain: Contém as entidades de negócio (Usuario, Transacao), enums (StatusTransacao, TipoTransacao) e regras puras de validação.


- Application (Use Cases): Implementa a lógica de aplicação através de casos de uso como CriarTransacao, ProcessarTransacao e ImportarUsuarios. Define também as interfaces (gateways) para comunicação externa.


- Infrastructure: Detalhes de implementação técnica:


- Controllers: Endpoints REST.


- Persistence: Repositórios JPA e entidades de banco de dados.


- Security: Configurações de filtro e geração de tokens JWT.


- Gateways/Clients: Implementações de clientes HTTP (BrasilAPI, MockAPI) e produtores/consumidores Kafka.

## 🛠️ Funcionalidades Principais
- Gestão de Usuários

- Cadastro, listagem, atualização e exclusão de usuários.

- Autenticação via login com CPF e senha, gerando Token JWT.
- Importação em massa de usuários através de planilhas Excel.
- Gestão de Transações
- Solicitação Assíncrona: Ao criar uma transação, ela é publicada em um tópico Kafka (transaction.requested) para processamento posterior.
- Conversão de Moeda: Integração automática com a BrasilAPI para obter cotações atualizadas caso a moeda não seja BRL.
- Validação de Saldo: Antes de aprovar uma transação de saída, a API consulta um serviço externo (MockAPI) para validar se o usuário possui saldo e limite suficientes.
- Análise Mensal: Gera um resumo diário de entradas, saídas e saldo para um mês específico.
- Exportação: Download de relatório completo de transações em formato .xlsx.

## 📦 Como Executar

### Pré-requisitos:

Docker e Docker Compose instalados.

## Passo a Passo:
Subir a Infraestrutura: 
Utilize o Docker Compose para subir o banco de dados PostgreSQL, Zookeeper e Kafka:

### Bash:
docker-compose up -d

Isso inicializará os serviços necessários conforme definido no docker-compose.yml.

### Compilar e Rodar a Aplicação: 
A aplicação pode ser executada em dois perfis diferentes:

### API (Padrão): 
Atende as requisições REST.

### Worker: 
Consome e processa as mensagens do Kafka.


- OBS: Você pode rodar ambos simultaneamente via Docker Compose:

## Bash
## O docker-compose já está configurado para subir o ms-api e o ms-worker
docker-compose up --build

### Acessar a Documentação: 
Com a aplicação rodando, acesse o Swagger UI para testar os endpoints: http://localhost:8081/swagger-ui.html.

## 🔒 Segurança e Resiliência
- Autenticação Stateless: Proteção de rotas através de filtros JWT.
- Resiliência no Kafka: Implementação de Dead Letter Queues (DLT). Se uma transação falhar 3 vezes no processamento, ela é enviada para o tópico transaction.requested.DLT para análise manual.
- Validação de Dados: Uso de Bean Validation para garantir a integridade de CPFs, e-mails e valores monetários.

## 🧪 Testes
### O projeto possui alta cobertura de testes, incluindo:

Testes Unitários: Validação de Use Cases e Entidades.

Testes de Integração: Testes de Controllers e fluxo de mensageria com @EmbeddedKafka.

Para executar os testes:

### Bash
./mvnw test
