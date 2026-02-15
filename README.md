# DevCalc API

API REST desenvolvida em Java para operações matemáticas simples, criada para demonstrar práticas de CI/CD com GitHub Actions.

## 📋 Objetivo do Projeto

Este projeto tem como objetivo:
- Disponibilizar uma API REST simples com operações matemáticas básicas
- Demonstrar a implementação de pipelines de CI/CD com GitHub Actions
- Aplicar práticas de desenvolvimento com testes automatizados
- Implementar integração e entrega contínua

## 🛠️ Tecnologias Utilizadas

- **Java 11** - Linguagem de programação
- **Javalin 5.6.3** - Framework web minimalista para Java
- **Maven** - Ferramenta de build e gerenciamento de dependências
- **JUnit 5** - Framework de testes unitários
- **GitHub Actions** - Plataforma de CI/CD

## 📁 Estrutura do Projeto

```
devcalc-api/
├── .github/
│   └── workflows/
│       ├── hello.yml          # Pipeline simples de demonstração
│       └── ci.yml             # Pipeline completo de CI/CD
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/
│   │           └── devcalc/
│   │               ├── App.java                    # Aplicação principal
│   │               └── service/
│   │                   └── CalculatorService.java  # Lógica de negócio
│   └── test/
│       └── java/
│           └── com/
│               └── devcalc/
│                   └── service/
│                       └── CalculatorServiceTest.java  # Testes unitários
├── evidencias/            # Capturas de tela do processo
├── pom.xml               # Configuração Maven
├── .gitignore           # Arquivos ignorados pelo Git
└── README.md            # Este arquivo
```

## 🚀 Endpoints da API

A API expõe os seguintes endpoints:

### GET /
Retorna informações sobre a API e endpoints disponíveis.

**Resposta:**
```json
{
  "message": "DevCalc API está funcionando!",
  "info": "Use os endpoints: /add, /subtract, /multiply, /divide com parâmetros a e b"
}
```

### GET /add
Soma dois números.

**Parâmetros:**
- `a` (obrigatório): primeiro operando
- `b` (obrigatório): segundo operando

**Exemplo:**
```
GET /add?a=10&b=5
```

**Resposta:**
```json
{
  "a": 10.0,
  "b": 5.0,
  "result": 15.0
}
```

### GET /subtract
Subtrai dois números.

**Exemplo:**
```
GET /subtract?a=10&b=5
```

**Resposta:**
```json
{
  "a": 10.0,
  "b": 5.0,
  "result": 5.0
}
```

### GET /multiply
Multiplica dois números.

**Exemplo:**
```
GET /multiply?a=10&b=5
```

**Resposta:**
```json
{
  "a": 10.0,
  "b": 5.0,
  "result": 50.0
}
```

### GET /divide
Divide dois números.

**Exemplo:**
```
GET /divide?a=10&b=5
```

**Resposta:**
```json
{
  "a": 10.0,
  "b": 5.0,
  "result": 2.0
}
```

**Erro (divisão por zero):**
```json
{
  "error": "Divisão por zero não é permitida"
}
```

## 💻 Executando Localmente

### Pré-requisitos

- Java 11 ou superior
- Maven 3.6 ou superior

### Clone o repositório

```bash
git clone https://github.com/SEU_USUARIO/devcalc-api.git
cd devcalc-api
```

### Compile o projeto

```bash
mvn clean install
```

### Execute os testes

```bash
mvn test
```

### Execute a aplicação

```bash
mvn exec:java -Dexec.mainClass="com.devcalc.App"
```

Ou execute o JAR gerado:

```bash
java -jar target/devcalc-api.jar
```

A aplicação estará disponível em: `http://localhost:7000`

### Testando os endpoints

```bash
# Teste de adição
curl "http://localhost:7000/add?a=10&b=5"

# Teste de subtração
curl "http://localhost:7000/subtract?a=10&b=5"

# Teste de multiplicação
curl "http://localhost:7000/multiply?a=10&b=5"

# Teste de divisão
curl "http://localhost:7000/divide?a=10&b=5"
```

## 🔄 Workflows CI/CD

### hello.yml - Pipeline Inicial

Pipeline simples que demonstra o funcionamento básico do GitHub Actions.

**Gatilhos:**
- Push na branch `main`
- Pull requests para `main`

**Ações:**
- Exibe mensagem "Pipeline iniciado com sucesso"
- Mostra informações do evento

### ci.yml - Pipeline Completo

Pipeline completo de Integração e Entrega Contínua.

**Gatilhos:**
- Push na branch `main` (apenas em alterações em `src/**`, `pom.xml` ou workflows)
- Pull requests para `main` (apenas em alterações em `src/**` ou `pom.xml`)
- Execução manual via `workflow_dispatch`

**Jobs:**

1. **checkout** - Realiza checkout do código e disponibiliza como artefato
2. **build** - Configura Java 11 e compila o projeto com Maven
3. **test** - Executa os testes automatizados e gera relatórios
4. **package** - Gera o JAR executável da aplicação
5. **deploy** - Simula o deploy da aplicação (exibe mensagem de sucesso)

**Dependências:**
- `build` e `test` dependem de `checkout`
- `package` depende de `build` e `test`
- `deploy` depende de `build`, `test` e `package`

## 📸 Instruções para Evidências

Para documentar o processo, capture as seguintes telas:

### 1. Estruturação inicial
- Comandos `git init`, `git remote add`, `git add`, `git commit`, `git push`
- Repositório criado no GitHub

### 2. Desenvolvimento
- Código do `CalculatorService.java`
- Código do `App.java`
- Código do `CalculatorServiceTest.java`
- Execução dos testes: `mvn test` (com resultados)

### 3. Workflows
- Arquivo `.github/workflows/hello.yml`
- Execução do workflow `hello.yml` na aba Actions
- Arquivo `.github/workflows/ci.yml`
- Execução completa do workflow `ci.yml`
- Grafo de dependências dos jobs
- Logs de cada job individual

### 4. Testes de gatilhos
- Alteração em `README.md` (pipeline NÃO executado)
- Alteração em arquivo dentro de `src/` (pipeline executado)

### 5. Pull Request e execução manual
- Pull request aberto com CI rodando
- Execução manual via workflow_dispatch

Organize todas as capturas na pasta `evidencias/` com nomes descritivos.

## 🎯 Comandos Úteis

```bash
# Compilar o projeto
mvn clean compile

# Executar testes
mvn test

# Compilar e executar testes
mvn clean install

# Gerar JAR
mvn package

# Limpar build anterior
mvn clean

# Ver dependências
mvn dependency:tree

# Executar aplicação
java -jar target/devcalc-api.jar
```

## 📚 Conceitos Aplicados

- **Integração Contínua (CI):** Automação de build e testes a cada commit
- **Entrega Contínua (CD):** Preparação automática para deploy
- **Testes Automatizados:** Validação da qualidade do código
- **Versionamento:** Controle de versões com Git
- **Pipeline as Code:** Workflows definidos em YAML
- **Artefatos:** Preservação de builds e relatórios
- **Jobs e Dependencies:** Orquestração de tarefas paralelas e sequenciais


## 📝 Licença

Este projeto foi desenvolvido para fins educacionais.
