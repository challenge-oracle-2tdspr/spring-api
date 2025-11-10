# AgroTech - Sistema de Monitoramento Agrícola

Sistema de monitoramento agrícola inteligente que integra dados de sensores ESP32 instalados em campos de propriedades rurais para coleta de informações climáticas e de solo. A plataforma consolida dados operacionais de colheita e utiliza modelos preditivos para estimar o desempenho da próxima safra com base nas condições climáticas e indicadores da safra atual.

## Descrição do Projeto

O AgroTech é uma solução completa para agricultura de precisão que combina IoT, análise de dados e aprendizado de máquina. A aplicação recebe leituras em tempo real de sensores ESP32 distribuídos pelos campos, armazena séries temporais de dados climáticos e de solo, e cruza essas informações com dados históricos de colheitas para gerar previsões de produtividade e recomendações agrícolas por talhão e propriedade.

### Tecnologias Utilizadas

- Java 21
- Spring Boot 3
- PostgreSQL (produção)
- H2 Database (desenvolvimento)
- Redis (cache)
- Docker e Docker Compose
- Oracle APEX (integração)
- ESP32 (sensores IoT)
- Swagger/OpenAPI (documentação)

## Arquitetura

A aplicação segue uma arquitetura em camadas com separação clara de responsabilidades.

- **Controllers**: Endpoints REST para recepção de dados dos sensores e integração com Oracle APEX
- **Services**: Lógica de negócio, processamento de dados e orquestração de operações
- **Repositories**: Camada de acesso a dados com abstração JPA
- **DTOs**: Objetos de transferência para requests e responses
- **Domains**: Entidades de domínio mapeadas para tabelas do banco de dados

### Entidades Principais

- **Sensor**: Representa dispositivos ESP32 instalados nos campos
- **Field**: Campos de cultivo dentro de propriedades
- **Property**: Propriedades rurais monitoradas
- **SensorReading**: Dados de solo (umidade, pH, condutividade)
- **Harvest**: Registros de colheitas realizadas
- **User**: Usuários do sistema (agricultores, técnicos)

## Pré-requisitos

- Java Development Kit (JDK) 21 ou superior
- Maven 3.6+
- Docker Desktop (para execução do PostgreSQL)
- Git

## Configuração do Ambiente

### Perfis de Configuração

A aplicação utiliza dois perfis principais.

**Perfil padrão (produção)**
- Banco de dados PostgreSQL
- Configuração via variáveis de ambiente
- DDL auto-update

**Perfil dev (desenvolvimento)**
- Banco de dados H2 em memória
- Console H2 habilitado
- DDL auto-create-drop para testes

### Variáveis de Ambiente

Para o perfil de produção, as seguintes variáveis devem ser configuradas.

```bash
DATABASE_URL=jdbc:postgresql://localhost:5432/agrotech
DATABASE_USERNAME=agrotech_user
DATABASE_PASSWORD=agrotech_password
DB_POOL_SIZE=10
DDL_AUTO=update
SHOW_SQL=false
LOG_SQL=INFO
SERVER_PORT=8080
```

## Executando a Aplicação

### Opção 1: Desenvolvimento Local com H2

Para desenvolvimento rápido sem necessidade de Docker, utilize o perfil dev com banco H2 em memória.

**Com Maven**

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

**Acessos em modo dev**

- API: http://localhost:8080
- H2 Console: http://localhost:8080/h2-console
    - JDBC URL: `jdbc:h2:mem:testdb`
    - Username: `sa`
    - Password: (deixar em branco)
- Swagger UI: http://localhost:8080/swagger-ui/index.html

### Opção 2: PostgreSQL com Docker Compose

Para ambiente mais próximo de produção, utilize PostgreSQL em container.

**1. Subir o banco de dados PostgreSQL**

```bash
docker compose up -d
```

Este comando iniciará um container PostgreSQL com as configurações definidas no arquivo `docker-compose.yml`.

**2. Configurar variáveis de ambiente**

Crie um arquivo `.env` na raiz do projeto com as variáveis necessárias.

```bash
DATABASE_URL=jdbc:postgresql://localhost:5432/agrotech
DATABASE_USERNAME=agrotech_user
DATABASE_PASSWORD=agrotech_password
SERVER_PORT=8080
```

**3. Executar a aplicação**

**Com Maven**

```bash
./mvnw spring-boot:run
```


**Acessos em modo produção**

- API: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui/index.html
- Endpoints de health: http://localhost:8080/actuator/health

### Opção 3: Executar JAR compilado

Para executar a aplicação como arquivo JAR.

**Compilar o projeto**

Com Maven:
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
java -jar target/agrotech-0.0.1-SNAPSHOT.jar
```

## Documentação da API

A aplicação possui documentação interativa via Swagger/OpenAPI disponível após iniciar o servidor.

**Acessar Swagger UI**

```
http://localhost:8080/swagger-ui/index.html
```

O Swagger UI permite:

- Visualizar todos os endpoints disponíveis
- Testar requisições diretamente pela interface
- Consultar schemas de DTOs e entidades
- Verificar códigos de resposta e estruturas de dados

**Especificação OpenAPI**

O documento JSON da especificação OpenAPI está disponível em:

```
http://localhost:8080/v3/api-docs
```

## Integração com Oracle APEX

A aplicação foi projetada para integração com Oracle APEX através de RESTful Web Services.

### Fluxo de Integração

1. APEX consome endpoints REST do AgroTech para exibir dashboards
2. Formulários APEX enviam dados via POST para registrar colheitas
3. Relatórios APEX consultam previsões e históricos via GET
4. Webhooks notificam APEX sobre alertas e anomalias

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

```
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
pom.xml
.gitignore
README.md
```

## Troubleshooting

### Erro de conexão com PostgreSQL

Verifique se o container está rodando:

```bash
docker compose ps
```

Verifique logs do container:

```bash
docker compose logs db
```

### H2 Console não acessível

Certifique-se de estar usando o perfil dev:

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

### Porta 8080 em uso

Altere a porta via variável de ambiente:

```bash
export SERVER_PORT=8081
./mvnw spring-boot:run
```

## Logs

Os logs da aplicação seguem os níveis configurados:

- **Produção**: INFO para aplicação, INFO para SQL
- **Desenvolvimento**: DEBUG para aplicação, DEBUG para SQL e TRACE para bindings Hibernate

## Contribuindo

Para contribuir com o projeto:

1. Faça fork do repositório
2. Crie uma branch para sua feature (`git checkout -b feat/username/nova-funcionalidade`)
3. Commit suas mudanças (`git commit -m 'feat: add new functionality'`)
4. Push para a branch (`git push origin feat/username/nova-funcionalidade`)
5. Abra um Pull Request


## Cronograma

O cronograma do projeto está no dentro do GitHub Projects, no seguinte link:

https://github.com/orgs/challenge-oracle-2tdspr/projects/1/views/1

O acesso é publico para facilitar visualização.

## Vídeo de apresentação

https://www.youtube.com/watch?v=TwlQuYMBLX0

## Guia para deploy em cloud

Guia simplificado para deploy em provedores cloud usando Docker.

### Pré-requisitos

- VM Linux Ubuntu 22.04/24.04
- Acesso SSH configurado
- IP público
- Mínimo: 1 vCPU + 1 GB RAM

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

### Passo 2: instalar Docker
```bash
sudo apt install -y apt-transport-https ca-certificates curl software-properties-common

sudo install -m 0755 -d /etc/apt/keyrings
sudo curl -fsSL https://download.docker.com/linux/ubuntu/gpg -o /etc/apt/keyrings/docker.asc
sudo chmod a+r /etc/apt/keyrings/docker.asc

echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.asc] https://download.docker.com/linux/ubuntu \
  $(. /etc/os-release && echo "$VERSION_CODENAME") stable" | \
  sudo tee /etc/apt/sources.list.d/docker.list > /dev/null

sudo apt install -y docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin
```
### Passo 3: configurar seu provedor
No console do seu provedor cloud, libere a porta 8080 para acesso TCP no seu Security Group ou Firewall

### Passo 4: deploy da aplicação
Clone o repositório
```bash
git clone https://github.com/challenge-oracle-2tdspr/spring-api.git

cd spring-api
```

Crie seu arquivo .env
```bash
nano .env
```

Configure as variaveis com suas credenciais

Dê permissão para o Maven
```bash
chmod +x mvnw
```

Faça o build com o Docker Compose
```bash
docker compose -f docker-compose.prod.yml up -d --build
```

Se necessário, acompanhe os logs dos containers
```bash
docker compose -f docker-compose.prod.yml logs -f
```

### Passo 5: verificar
Verifique o status dos containers
```bash
docker compose -f docker-compose.prod.yml ps
```

Teste localmente
```bash
curl http://localhost:8080/actuator/health
```

Teste externamente
```bash
curl http://<ip-publico-vm>:8080/actuator/health
```

### Troubleshooting
**Container não sobe:**
```bash
docker compose -f docker-compose.prod.yml logs agrotech-api --tail=100
```

**API não acessível:**
- Verifique firewall do provedor cloud
- Verifique: `sudo iptables -L -n | grep 8080`
- Verifique: `docker compose -f docker-compose.prod.yml ps`

### Conectar ao Banco (DBeaver/DataGrip)

**Via SSH Tunnel:**
- Database Host: `localhost`
- Database Port: `5432`
- SSH Host: `<ip-publico-vm>`
- SSH Port: `22`
- SSH User: `ubuntu`
- SSH Key: <sua-chave-privada>
