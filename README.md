# AgroTech - Sistema de Monitoramento Agrícola

Sistema de monitoramento agrícola inteligente que integra dados de sensores ESP32 instalados em campos de propriedades rurais para coleta de informações climáticas e de solo. A plataforma consolida dados operacionais de colheita e utiliza modelos preditivos para estimar o desempenho da próxima safra com base nas condições climáticas e indicadores da safra atual.

## Descrição do Projeto

O AgroTech é uma solução completa para agricultura de precisão que combina IoT, análise de dados e aprendizado de máquina. A aplicação recebe leituras em tempo real de sensores ESP32 distribuídos pelos campos, armazena séries temporais de dados climáticos e de solo, e cruza essas informações com dados históricos de colheitas para gerar previsões de produtividade e recomendações agrícolas por talhão e propriedade.

### Tecnologias Utilizadas

- Java 21
- Spring Boot 3
- Oracle Database (produção)
- H2 Database (desenvolvimento/testes locais)
- RabbitMQ
- Docker e Docker Compose
- Oracle APEX (integração)
- ESP32 (sensores IoT)
- Swagger/OpenAPI (documentação)
- Azure DevOps Pipelines

## Arquitetura

A aplicação segue uma arquitetura em camadas com separação clara de responsabilidades.

- **Controllers**: Endpoints REST para recepção de dados dos sensores e integração com Oracle APEX
- **Services**: Lógica de negócio, processamento de dados e orquestração de operações
- **Repositories**: Camada de acesso a dados com abstração JPA
- **DTOs**: Objetos de transferência para requests e responses
- **Domains**: Entidades de domínio mapeadas para tabelas do banco de dados
- **Messaging**: Integração assíncrona com RabbitMQ para eventos e processamento interno

### Entidades Principais

- **Sensor**: Representa dispositivos ESP32 instalados nos campos
- **Field**: Campos de cultivo dentro de propriedades
- **Property**: Propriedades rurais monitoradas
- **SensorReading**: Dados de solo e clima coletados em campo
- **Harvest**: Registros de colheitas realizadas
- **User**: Usuários do sistema (agricultores, técnicos)

## Pré-requisitos

- Java Development Kit (JDK) 21 ou superior
- Maven 3.6+
- Docker e Docker Compose plugin
- Git
- Acesso a Oracle Database no ambiente de produção
- Acesso a RabbitMQ no ambiente de produção

## Configuração do Ambiente

### Perfis de Configuração

A aplicação utiliza dois perfis principais.

**Perfil padrão (produção)**
- Banco de dados Oracle
- Configuração via variáveis de ambiente
- Flyway habilitado
- Integração com RabbitMQ habilitada

**Perfil dev (desenvolvimento)**
- Banco de dados H2 em memória ou ambiente local simplificado
- Console H2 habilitado
- Possibilidade de desabilitar integrações externas para desenvolvimento rápido

### Variáveis de Ambiente

Para o perfil de produção, as seguintes variáveis devem ser configuradas.

```bash
DATABASE_URL=jdbc:oracle:thin:@(description=(retry_count=20)(retry_delay=3)(address=(protocol=tcps)(port=1522)(host=SEU_HOST_ORACLE))(connect_data=(service_name=SEU_SERVICE_NAME))(security=(ssl_server_dn_match=yes)))
DATABASE_USERNAME=SEU_USUARIO
DATABASE_PASSWORD=SUA_SENHA

DB_POOL_SIZE=3
DDL_AUTO=validate
SHOW_SQL=false
LOG_SQL=WARN

RABBITMQ_HOST=10.0.0.6
RABBITMQ_PORT=5672
RABBITMQ_USERNAME=sensor_user
RABBITMQ_PASSWORD=sensor_pass
RABBITMQ_VHOST=sensor_vhost

SENSOR_API_URL=http://10.0.0.5:8081

JWT_SECRET=troque_isto_por_um_segredo_forte
SENSOR_WEBHOOK_API_KEY=troque_isto_por_uma_chave_valida
SERVER_PORT=8080
```

## Executando a Aplicação

### Opção 1: Desenvolvimento Local com perfil dev

Para desenvolvimento rápido, utilize um profile local desacoplado de Oracle e RabbitMQ reais.

**Com Maven**

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

**Acessos em modo dev**

- API: http://localhost:8080
- H2 Console: http://localhost:8080/h2-console
- Swagger UI: http://localhost:8080/swagger-ui/index.html

> Observação: para desenvolvimento local, pode ser necessário ajustar o `application-dev.yml` para desabilitar Flyway, RabbitMQ e integrações externas, caso não existam mocks ou containers locais dessas dependências.

### Opção 2: Produção com Docker Compose

Para ambiente de produção, utilize o arquivo `docker-compose.prod.yml`.

**1. Criar arquivo `.env`**

Na raiz do projeto, crie um arquivo `.env` com as variáveis necessárias.

```bash
DATABASE_URL=jdbc:oracle:thin:@(description=(retry_count=20)(retry_delay=3)(address=(protocol=tcps)(port=1522)(host=SEU_HOST_ORACLE))(connect_data=(service_name=SEU_SERVICE_NAME))(security=(ssl_server_dn_match=yes)))
DATABASE_USERNAME=SEU_USUARIO
DATABASE_PASSWORD=SUA_SENHA
DB_POOL_SIZE=3
DDL_AUTO=validate
SHOW_SQL=false
LOG_SQL=WARN
RABBITMQ_HOST=10.0.0.6
RABBITMQ_PORT=5672
RABBITMQ_USERNAME=sensor_user
RABBITMQ_PASSWORD=sensor_pass
RABBITMQ_VHOST=sensor_vhost
SENSOR_API_URL=http://10.0.0.5:8081
JWT_SECRET=troque_isto_por_um_segredo_forte
SENSOR_WEBHOOK_API_KEY=troque_isto_por_uma_chave_valida
SERVER_PORT=8080
```

**2. Subir a aplicação**

```bash
docker compose -f docker-compose.prod.yml up -d --build
```

**3. Verificar status**

```bash
docker compose -f docker-compose.prod.yml ps
```

**4. Ver logs**

```bash
docker compose -f docker-compose.prod.yml logs -f agrotech-api
```

**Acessos em modo produção**

- API: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui/index.html
- Healthcheck: http://localhost:8080/actuator/health
- Liveness: http://localhost:8080/actuator/health/liveness

### Opção 3: Executar JAR compilado

**Compilar o projeto**

```bash
./mvnw clean package
```

**Executar o JAR**

Com perfil dev:
```bash
java -jar target/agrotech-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev
```

Com perfil produção:
```bash
export DATABASE_URL='jdbc:oracle:thin:@(...)'
export DATABASE_USERNAME='SEU_USUARIO'
export DATABASE_PASSWORD='SUA_SENHA'
export RABBITMQ_HOST='10.0.0.6'
export RABBITMQ_PORT='5672'
export RABBITMQ_USERNAME='sensor_user'
export RABBITMQ_PASSWORD='sensor_pass'
export RABBITMQ_VHOST='sensor_vhost'
export SENSOR_API_URL='http://10.0.0.5:8081'
export JWT_SECRET='troque_isto_por_um_segredo_forte'
export SENSOR_WEBHOOK_API_KEY='troque_isto_por_uma_chave_valida'
java -jar target/agrotech-0.0.1-SNAPSHOT.jar
```

## Documentação da API

A aplicação possui documentação interativa via Swagger/OpenAPI disponível após iniciar o servidor.

**Acessar Swagger UI**

```text
http://localhost:8080/swagger-ui/index.html
```

**Especificação OpenAPI**

```text
http://localhost:8080/v3/api-docs
```

## Integração com Oracle APEX

A aplicação foi projetada para integração com Oracle APEX através de RESTful Web Services.

### Fluxo de Integração

1. APEX consome endpoints REST do AgroTech para exibir dashboards.
2. Formulários APEX enviam dados via POST para registrar colheitas.
3. Relatórios APEX consultam previsões e históricos via GET.
4. Webhooks notificam APEX sobre alertas e anomalias.

## Mensageria com RabbitMQ

A aplicação utiliza RabbitMQ para comunicação assíncrona entre componentes e processamento de eventos ligados à telemetria e integração.

### Variáveis relacionadas ao RabbitMQ

```bash
RABBITMQ_HOST=10.0.0.6
RABBITMQ_PORT=5672
RABBITMQ_USERNAME=sensor_user
RABBITMQ_PASSWORD=sensor_pass
RABBITMQ_VHOST=sensor_vhost
```

### Cuidados

- Certifique-se de que a VM ou container da aplicação possui conectividade com o host do RabbitMQ.
- Verifique se o `vhost`, usuário e senha existem no broker.
- Caso o ambiente local não tenha RabbitMQ, utilize profile dev com integração desabilitada ou suba um broker local.

## Modelo Preditivo

O sistema utiliza dados históricos para treinar modelos de previsão de safra.

### Dados Utilizados

- Temperatura média, máxima e mínima
- Precipitação acumulada
- Umidade do ar e do solo
- pH e umidade do solo
- Histórico de produtividade

### Algoritmo

O modelo correlaciona condições climáticas da safra atual com padrões históricos para estimar produtividade da próxima safra considerando sazonalidade e tendências.

## Estrutura do Projeto

```text
src/
├── main/
│   ├── java/
│   │   └── com/challengeoracle/agrotech/
│   │       ├── gateways/
│   │       │   ├── controllers/
│   │       │   ├── repositories/
│   │       │   ├── requests/
│   │       │   └── responses/
│   │       ├── services/
│   │       ├── repositories/
│   │       ├── domains/
│   │       ├── enums/
│   │       └── configurations/
│   └── resources/
│       ├── application.yml
│       └── application-dev.yml
└── test/
docker-compose.yml
docker-compose.prod.yml
pom.xml
.gitignore
README.md
azure-pipelines.yml
```

## Pipeline CI/CD

O projeto possui pipeline automatizada no Azure DevOps para build e deploy contínuo em uma VM Ubuntu com self-hosted agent.

### Fluxo da pipeline

1. Desenvolvedor faz commit no IntelliJ.
2. Push é enviado para a branch `main` no GitHub.
3. Azure DevOps detecta alteração na `main`.
4. A pipeline é disparada automaticamente.
5. O job é executado no pool self-hosted `agrotech-pool`.
6. O agent online na VM executa o `docker compose -f docker-compose.prod.yml`.
7. O container antigo é removido e a nova versão sobe na mesma VM.

### Exemplo de `azure-pipelines.yml`

```yaml
trigger:
  - main

pool:
  name: agrotech-pool

variables:
  - group: agrotech-variables

steps:
  - checkout: self

  - script: |
      echo "Running on self-hosted VM"
      whoami
      hostname
      docker --version
      docker compose version
      docker compose -f docker-compose.prod.yml config
      docker compose -f docker-compose.prod.yml down --remove-orphans || true
      docker rm -f agrotech_api || true
      docker compose -f docker-compose.prod.yml up --build -d
      docker compose -f docker-compose.prod.yml ps
      docker logs agrotech_api --tail 100 || true
    displayName: Deploy API
    env:
      DATABASE_URL: $(DATABASE_URL)
      DATABASE_USERNAME: $(DATABASE_USERNAME)
      DATABASE_PASSWORD: $(DATABASE_PASSWORD)
      DB_POOL_SIZE: $(DB_POOL_SIZE)
      DDL_AUTO: $(DDL_AUTO)
      SHOW_SQL: $(SHOW_SQL)
      LOG_SQL: $(LOG_SQL)
      RABBITMQ_HOST: $(RABBITMQ_HOST)
      RABBITMQ_PORT: $(RABBITMQ_PORT)
      RABBITMQ_USERNAME: $(RABBITMQ_USERNAME)
      RABBITMQ_PASSWORD: $(RABBITMQ_PASSWORD)
      RABBITMQ_VHOST: $(RABBITMQ_VHOST)
      SENSOR_API_URL: $(SENSOR_API_URL)
      JWT_SECRET: $(JWT_SECRET)
      SENSOR_WEBHOOK_API_KEY: $(SENSOR_WEBHOOK_API_KEY)
```

### Variáveis da pipeline

As variáveis estão centralizadas em **Library > Variable Groups** no Azure DevOps, no grupo `agrotech-variables`.

```bash
DATABASE_URL=jdbc:oracle:thin:@(description=(retry_count=20)(retry_delay=3)(address=(protocol=tcps)(port=1522)(host=SEU_HOST_ORACLE))(connect_data=(service_name=SEU_SERVICE_NAME))(security=(ssl_server_dn_match=yes)))
DATABASE_USERNAME=SEU_USUARIO
DATABASE_PASSWORD=SUA_SENHA
DB_POOL_SIZE=3
DDL_AUTO=validate
SHOW_SQL=false
LOG_SQL=WARN
RABBITMQ_HOST=10.0.0.6
RABBITMQ_PORT=5672
RABBITMQ_USERNAME=sensor_user
RABBITMQ_PASSWORD=sensor_pass
RABBITMQ_VHOST=sensor_vhost
SENSOR_API_URL=http://10.0.0.5:8081
JWT_SECRET=troque_isto_por_um_segredo_forte
SENSOR_WEBHOOK_API_KEY=troque_isto_por_uma_chave_valida
```

### Requisitos da VM de deploy

- Ubuntu 22.04 ou 24.04
- Docker instalado
- Docker Compose plugin instalado
- Self-hosted agent do Azure DevOps instalado e online
- Conectividade com Oracle Database
- Conectividade com RabbitMQ
- Porta 8080 liberada para acesso externo, quando necessário

## Troubleshooting

### Erro de conexão com Oracle

Verifique se as variáveis `DATABASE_URL`, `DATABASE_USERNAME` e `DATABASE_PASSWORD` estão corretas.

Teste conectividade de rede com o host Oracle e confirme se a porta 1522 está acessível.

### Erro de conexão com RabbitMQ

Verifique se o host configurado em `RABBITMQ_HOST` está acessível.

Teste a porta do broker:

```bash
nc -vz 10.0.0.6 5672
```

### Conflito com nome do container

Se ocorrer erro informando que `agrotech_api` já está em uso, remova o container antigo antes de subir a nova versão:

```bash
docker compose -f docker-compose.prod.yml down --remove-orphans || true
docker rm -f agrotech_api || true
```

### API não acessível

- Verifique firewall do provedor cloud.
- Verifique o bind da porta `8080:8080` no container.
- Verifique: `docker compose -f docker-compose.prod.yml ps`
- Verifique: `docker logs agrotech_api --tail 100`

### Healthcheck

```bash
curl http://localhost:8080/actuator/health
curl http://localhost:8080/actuator/health/liveness
```

## Logs

Os logs da aplicação seguem os níveis configurados:

- **Produção**: INFO/WARN para aplicação, SQL controlado por `LOG_SQL`
- **Desenvolvimento**: DEBUG para aplicação quando habilitado no profile local

## Contribuindo

Para contribuir com o projeto:

1. Faça fork do repositório.
2. Crie uma branch para sua feature (`git checkout -b feat/username/nova-funcionalidade`).
3. Commit suas mudanças (`git commit -m 'feat: add new functionality'`).
4. Push para a branch (`git push origin feat/username/nova-funcionalidade`).
5. Abra um Pull Request.

## Cronograma

O cronograma do projeto está no GitHub Projects, no seguinte link:

[https://github.com/orgs/challenge-oracle-2tdspr/projects/1/views/1](https://github.com/orgs/challenge-oracle-2tdspr/projects/1/views/1)

## Vídeo de apresentação

[https://www.youtube.com/watch?v=TwlQuYMBLX0](https://www.youtube.com/watch?v=TwlQuYMBLX0)

## Guia para deploy em cloud

Guia simplificado para deploy em provedores cloud usando Docker e Azure DevOps.

### Pré-requisitos

- VM Linux Ubuntu 22.04/24.04
- Acesso SSH configurado
- IP público
- Mínimo: 1 vCPU + 1 GB RAM
- Docker e Docker Compose plugin
- Self-hosted agent configurado, se o deploy for automatizado pela pipeline

### Passo 1: Preparar VM

#### Conectar via SSH
```bash
ssh seu-usuario@ip-publico-vm
```

#### Atualizar sistema e instalar ferramentas
```bash
sudo apt update && sudo apt upgrade -y
sudo apt install -y wget curl git
```

### Passo 2: Instalar Docker
```bash
sudo apt install -y apt-transport-https ca-certificates curl software-properties-common

sudo install -m 0755 -d /etc/apt/keyrings
sudo curl -fsSL https://download.docker.com/linux/ubuntu/gpg -o /etc/apt/keyrings/docker.asc
sudo chmod a+r /etc/apt/keyrings/docker.asc

echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.asc] https://download.docker.com/linux/ubuntu \
  $(. /etc/os-release && echo "$VERSION_CODENAME") stable" | \
  sudo tee /etc/apt/sources.list.d/docker.list > /dev/null

sudo apt update
sudo apt install -y docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin
```

### Passo 3: Configurar seu provedor

No console do seu provedor cloud, libere a porta 8080 para acesso TCP no Security Group ou Firewall.

### Passo 4: Deploy manual da aplicação

Clone o repositório:

```bash
git clone https://github.com/challenge-oracle-2tdspr/spring-api.git
cd spring-api
```

Crie seu arquivo `.env`:

```bash
nano .env
```

Configure as variáveis com suas credenciais de Oracle, RabbitMQ e integrações externas.

Suba a aplicação:

```bash
docker compose -f docker-compose.prod.yml up -d --build
```

Se necessário, acompanhe os logs dos containers:

```bash
docker compose -f docker-compose.prod.yml logs -f
```

### Passo 5: Verificar
Verifique o status dos containers:

```bash
docker compose -f docker-compose.prod.yml ps
```

Teste localmente:
```bash
curl http://localhost:8080/actuator/health
```

Teste externamente:
```bash
curl http://<ip-publico-vm>:8080/actuator/health
```

### Troubleshooting
**Container não sobe:**
```bash
docker compose -f docker-compose.prod.yml logs agrotech-api --tail=100
```

**API não acessível:**
- Verifique firewall do provedor cloud.
- Verifique `sudo iptables -L -n | grep 8080`.
- Verifique `docker compose -f docker-compose.prod.yml ps`.

### Conectar ao Banco (DBeaver/DataGrip)

**Via SSH Tunnel:**
- Database Host: `localhost`
- Database Port: `1522` ou a porta aplicável ao seu acesso Oracle
- SSH Host: `<ip-publico-vm>`
- SSH Port: `22`
- SSH User: `ubuntu`
- SSH Key: `<sua-chave-privada>`
