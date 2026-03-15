# DevCalc API

![CI/CD Pipeline](https://github.com/evertonrocha2/devcalc-api/actions/workflows/ci.yml/badge.svg)

API REST desenvolvida em Java para operações matemáticas simples, criada para demonstrar práticas de CI/CD com GitHub Actions.

## Sumário

- [Sobre o Projeto](#sobre-o-projeto)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Endpoints da API](#endpoints-da-api)
- [TP1 - Fundamentos](#tp1---fundamentos)
- [TP2 - Actions e Workflows Avançados](#tp2---actions-e-workflows-avançados)
- [TP3 - Segurança e Ambientes](#tp3---segurança-e-ambientes)
- [Como Executar](#como-executar)

---

## Sobre o Projeto

Este projeto tem como objetivo:
- Disponibilizar uma API REST simples com operações matemáticas básicas
- Demonstrar a implementação de pipelines de CI/CD com GitHub Actions
- Aplicar práticas de desenvolvimento com testes automatizados
- Implementar integração e entrega contínua

## Tecnologias Utilizadas

- **Java 11** - Linguagem de programação
- **Javalin 5.6.3** - Framework web minimalista para Java
- **Maven** - Ferramenta de build e gerenciamento de dependências
- **JUnit 5** - Framework de testes unitários
- **GitHub Actions** - Plataforma de CI/CD
- **Checkstyle** - Análise estática de código

## Endpoints da API

### GET /
Retorna informações sobre a API e endpoints disponíveis.

### GET /add?a=10&b=5
Soma dois números.
```json
{"a": 10.0, "b": 5.0, "result": 15.0}
```

### GET /subtract?a=10&b=5
Subtrai dois números.
```json
{"a": 10.0, "b": 5.0, "result": 5.0}
```

### GET /multiply?a=10&b=5
Multiplica dois números.
```json
{"a": 10.0, "b": 5.0, "result": 50.0}
```

### GET /divide?a=10&b=5
Divide dois números.
```json
{"a": 10.0, "b": 5.0, "result": 2.0}
```

### GET /sqrt?x=16 [NOVO TP3]
Calcula a raiz quadrada de um número.
```json
{"x": 16.0, "result": 4.0}
```

---

## TP1 - Fundamentos

### Objetivos
Estruturar a base da aplicação e criar o pipeline inicial de CI/CD.

### O que foi implementado

#### 1. Estrutura do Projeto
- Aplicação Java com Maven
- Classes de serviço (CalculatorService)
- Classe principal (App) com Javalin
- Testes unitários com JUnit 5

#### 2. Pipeline Básico (hello.yml)
- Workflow simples de demonstração
- Gatilhos: push e pull_request
- Jobs básicos de validação

#### 3. Pipeline Completo (ci.yml)
- **Jobs implementados:**
  1. checkout - Checkout do código
  2. build - Compilação com Maven
  3. test - Testes automatizados
  4. package - Geração do JAR
  5. deploy - Simulação de deploy

- **Funcionalidades:**
  - Gatilhos configurados (push, PR, manual)
  - Upload/download de artefatos
  - Dependências entre jobs
  - Cache de dependências Maven

### Evidências TP1
Pasta: `evidencias/` (prints do TP1)

---

## TP2 - Actions e Workflows Avançados

### Objetivos
Aprofundar o uso do GitHub Actions com modularização, actions externas e debugging.

### O que foi implementado

#### 1. Action do Marketplace
**Action:** `jwgmeligmeyling/checkstyle-github-action@v1.2`

- Integrada no workflow ci.yml (job code-quality)
- Plugin Maven Checkstyle configurado
- Arquivo checkstyle.xml criado com regras
- Análise automática de código Java

**Evidência:** `evidencias/pipeline-sucesso-tp2.png`

#### 2. Documento Explicativo
**Arquivo:** `actions-explicacao.md`

Contém:
- Diferença conceitual entre workflows e actions
- Estrutura do arquivo action.yml
- Explicação de inputs, outputs e runs
- Exemplos práticos: actions/setup-java@v4 e checkstyle-github-action
- Como usar actions com uses: e with:

#### 3. Workflow Reutilizável
**Arquivo:** `.github/workflows/lint-and-test.yml`

- Usa workflow_call para ser reutilizável
- Parâmetros: java-version, run-lint, run-tests
- Jobs: lint e test (executam condicionalmente)
- Chamado pelo ci.yml principal

#### 4. Execução Manual Parametrizada
**Melhorias no ci.yml:**

Parâmetros do workflow_dispatch:
- `environment`: choice (development, staging, production)
- `run_tests`: boolean - controla execução de testes
- `run_lint`: boolean - controla análise de código

Jobs usam expressões `if:` para execução condicional.

**Evidências:**
- `evidencias/execucao-manual-formulario.png`
- `evidencias/execucao-manual-lista.png`
- `evidencias/execucao-manual-pipeline.png`

#### 5. Depuração de Erros
**Processo realizado:**

1. Criado erro proposital: `comando_invalido_inexistente`
2. Pipeline falhou com exit code 127
3. Identificado via aba Actions → logs detalhados
4. Erro corrigido e documentado

**Ferramentas utilizadas:**
- Interface web GitHub Actions
- Logs detalhados por step
- Timestamps e anotações automáticas

**Evidência:** `evidencias/pipeline-erro-tp2.png`

#### 6. Monitoramento
- Badge de status adicionado ao README
- Comparação entre execuções push vs manual
- Análise de filtros e histórico na aba Actions

### Arquivos do TP2
- `.github/workflows/lint-and-test.yml` - Workflow reutilizável
- `.github/workflows/test-error.yml` - Teste de erro
- `actions-explicacao.md` - Documentação conceitual
- `checkstyle.xml` - Configuração Checkstyle
- `pom.xml` - Plugin Checkstyle adicionado

---

## TP3 - Segurança e Ambientes

### Objetivos
Aprofundar controle, segurança e separação de ambientes de implantação.

### O que foi implementado

#### Etapa 1: Runner Auto-Hospedado
**Arquivo:** `.github/workflows/runner-auto-hospedado.yml`

**Configuração:**
- Workflow usa `runs-on: self-hosted`
- Exibe informações do sistema (uname, hardware)
- Verifica software instalado (Java)
- Instala software adicional via comandos do SO
- Demonstra execução em máquina local/nuvem própria

**Instruções para configurar runner:**
1. No GitHub: Settings → Actions → Runners → New self-hosted runner
2. Seguir instruções para seu SO (Linux/Mac/Windows)
3. Executar o workflow para validar

#### Etapa 2: Variáveis e Secrets
**Arquivo:** `.github/workflows/vars-e-secrets.yml`

**Variáveis criadas (não sensíveis):**
- `APP_MODE` - Modo de execução (development/production)
- `SUPPORT_EMAIL` - Email de suporte

**Secret criado (sensível):**
- `PROD_TOKEN` - Token de autenticação para produção

**Uso no workflow:**
```yaml
env:
  MODE: ${{ vars.APP_MODE }}
  EMAIL: ${{ vars.SUPPORT_EMAIL }}
  TOKEN: ${{ secrets.PROD_TOKEN }}
```

**Configurar no GitHub:**
- Settings → Secrets and variables → Actions
- New repository variable (para APP_MODE e SUPPORT_EMAIL)
- New repository secret (para PROD_TOKEN)

#### Etapa 3: Contextos e Escopos
**Arquivo:** `.github/workflows/env-context-demo.yml`

**Demonstra:**
- Contexto `github.*` (actor, repository, sha, event_name)
- Contexto `runner.*` (os, arch, name)
- Variáveis em nível workflow, job e step
- Precedência: Step > Job > Workflow
- Isolamento entre jobs

**Variável STAGE definida em 3 níveis:**
- Workflow: `STAGE: "test"`
- Job: `STAGE: "job-override"`
- Step: `STAGE: "step-override"`

#### Etapa 4: Permissões e GITHUB_TOKEN
**Arquivo:** `.github/workflows/github-token-demo.yml`

**Permissões configuradas:**
```yaml
permissions:
  contents: read
  issues: write
```

**Funcionalidades:**
- Cria issue automaticamente usando GITHUB_TOKEN
- Usa actions/github-script@v7 para interagir com API
- Issue criada contém informações do workflow
- Demonstra uso seguro do token

**Condições para criar issue:**
- Quando parâmetro `criar_issue` = true
- Quando deploy falha (condicional)

#### Etapa 5: Ambientes Dev e Prod
**Arquivos:**
- `.github/workflows/deploy-dev.yml` - Deploy automático para dev
- `.github/workflows/deploy-prod.yml` - Deploy protegido para prod

**Ambiente Dev:**
- Acionado em push na branch `dev`
- Liberação automática
- Variáveis específicas do ambiente dev

**Ambiente Prod:**
- Acionado em push na branch `main`
- Requer aprovação manual (configurar no GitHub)
- Secrets específicos: PROD_TOKEN, PROD_API_KEY
- URL de produção definida

**Configurar ambientes no GitHub:**
1. Settings → Environments → New environment
2. Criar "dev" (sem proteções)
3. Criar "prod" com:
   - Required reviewers (adicionar você ou membro da equipe)
   - Wait timer (opcional)
4. Adicionar variáveis e secrets específicos de cada ambiente

#### Etapa 6: Nova Funcionalidade - Raiz Quadrada
**Implementação:**

**CalculatorService.java:**
- Método `sqrt(double x)` adicionado
- Validação: número não pode ser negativo
- Retorna `Math.sqrt(x)`

**App.java:**
- Novo endpoint `GET /sqrt?x=16`
- Método `handleSingleOperation` criado
- Classe `SingleOperationResponse` para resposta
- Interface `SingleOperation` para lambda

**CalculatorServiceTest.java:**
- 6 novos testes para sqrt:
  - testSqrtPositiveNumbers
  - testSqrtZero
  - testSqrtOne
  - testSqrtDecimalNumbers
  - testSqrt100
  - testSqrtNegativeNumber (valida exceção)

**Integração com Pipeline:**
- Testes automaticamente executados no ci.yml
- Workflow valida nova funcionalidade a cada commit
- Cobertura de testes mantida

**Testando o endpoint:**
```bash
curl "http://localhost:7000/sqrt?x=16"
# Resposta: {"x": 16.0, "result": 4.0}

curl "http://localhost:7000/sqrt?x=2.25"
# Resposta: {"x": 2.25, "result": 1.5}

curl "http://localhost:7000/sqrt?x=-4"
# Resposta: {"error": "Não é possível calcular raiz quadrada de número negativo"}
```

### Arquivos do TP3
**Workflows:**
- `runner-auto-hospedado.yml` - Runner local
- `vars-e-secrets.yml` - Variáveis e secrets
- `env-context-demo.yml` - Contextos e escopos
- `github-token-demo.yml` - Permissões e token
- `deploy-dev.yml` - Deploy development
- `deploy-prod.yml` - Deploy production

**Código:**
- `CalculatorService.java` - Método sqrt() adicionado
- `App.java` - Endpoint /sqrt adicionado
- `CalculatorServiceTest.java` - 6 testes para sqrt

---

## Como Executar

### Pré-requisitos
- Java 11 ou superior
- Maven 3.6 ou superior

### Executar localmente
```bash
# Clone o repositório
git clone https://github.com/evertonrocha2/devcalc-api.git
cd devcalc-api

# Compile e execute testes
mvn clean install

# Execute a aplicação
mvn exec:java -Dexec.mainClass="com.devcalc.App"
```

A aplicação estará disponível em `http://localhost:7000`

### Testar os endpoints
```bash
# Operações básicas
curl "http://localhost:7000/add?a=10&b=5"
curl "http://localhost:7000/subtract?a=10&b=5"
curl "http://localhost:7000/multiply?a=10&b=5"
curl "http://localhost:7000/divide?a=10&b=5"

# Nova funcionalidade (TP3)
curl "http://localhost:7000/sqrt?x=16"
curl "http://localhost:7000/sqrt?x=2.25"
```

---

## Workflows Disponíveis

### Workflows Principais
- **ci.yml** - Pipeline completo de CI/CD
- **hello.yml** - Pipeline inicial de demonstração

### Workflows TP2
- **lint-and-test.yml** - Workflow reutilizável
- **test-error.yml** - Teste de debugging

### Workflows TP3
- **runner-auto-hospedado.yml** - Demonstração de runner local
- **vars-e-secrets.yml** - Uso de variáveis e secrets
- **env-context-demo.yml** - Contextos e escopos
- **github-token-demo.yml** - Permissões e criação de issues
- **deploy-dev.yml** - Deploy para desenvolvimento
- **deploy-prod.yml** - Deploy para produção

---

## Configurações Necessárias

### Variáveis de Repositório (Settings → Actions → Variables)
- `APP_MODE` = "development" ou "production"
- `SUPPORT_EMAIL` = seu email
- `API_URL` = URL da API (opcional)

### Secrets do Repositório (Settings → Actions → Secrets)
- `PROD_TOKEN` = token de autenticação para produção

### Ambientes (Settings → Environments)

**Ambiente: dev**
- Sem proteções
- Deploy automático

**Ambiente: prod**
- Required reviewers: adicionar revisor
- Wait timer: 5 minutos (opcional)
- Secrets específicos:
  - `PROD_TOKEN`
  - `PROD_API_KEY`

### Runner Auto-Hospedado (Settings → Actions → Runners)
1. New self-hosted runner
2. Seguir instruções para seu SO
3. Executar runner em máquina local/nuvem

---

## Executando os Workflows

### Execução Automática
```bash
# Push para main - executa ci.yml e deploy-prod.yml
git push origin main

# Push para dev - executa deploy-dev.yml
git push origin dev
```

### Execução Manual
1. Vá em Actions → escolha o workflow
2. Clique em "Run workflow"
3. Configure os parâmetros
4. Clique em "Run workflow"

---

## Estrutura do Projeto

```
ci-cd/
├── .github/workflows/        # Workflows do GitHub Actions
│   ├── ci.yml               # Pipeline principal (TP1/TP2)
│   ├── hello.yml            # Pipeline inicial (TP1)
│   ├── lint-and-test.yml   # Workflow reutilizável (TP2)
│   ├── test-error.yml       # Teste de erro (TP2)
│   ├── runner-auto-hospedado.yml  # Runner local (TP3)
│   ├── vars-e-secrets.yml   # Variáveis e secrets (TP3)
│   ├── env-context-demo.yml # Contextos (TP3)
│   ├── github-token-demo.yml # Permissões (TP3)
│   ├── deploy-dev.yml       # Deploy dev (TP3)
│   └── deploy-prod.yml      # Deploy prod (TP3)
├── src/
│   ├── main/java/com/devcalc/
│   │   ├── App.java         # Aplicação principal
│   │   └── service/
│   │       └── CalculatorService.java  # Lógica de negócio
│   └── test/java/com/devcalc/service/
│       └── CalculatorServiceTest.java  # Testes unitários
├── evidencias/              # Screenshots e evidências
├── actions-explicacao.md    # Documentação TP2
├── checkstyle.xml          # Configuração Checkstyle
├── pom.xml                 # Configuração Maven
├── .gitignore             # Arquivos ignorados
└── README.md              # Este arquivo
```

---

## Conceitos Aplicados

### TP1
- Integração Contínua (CI)
- Entrega Contínua (CD)
- Testes Automatizados
- Versionamento com Git
- Pipeline as Code
- Artefatos
- Jobs e Dependencies

### TP2
- Actions do Marketplace
- Workflows Reutilizáveis (workflow_call)
- Parâmetros booleanos em workflows
- Expressões condicionais (if:)
- Debugging de pipelines
- Análise estática de código
- Monitoramento com badges

### TP3
- Runner Auto-Hospedado (self-hosted)
- Variáveis de repositório (vars.*)
- Secrets sensíveis (secrets.*)
- Contextos (github.*, runner.*)
- Escopos de variáveis (workflow/job/step)
- Permissões do GITHUB_TOKEN
- Ambientes de deploy (dev/prod)
- Proteções e aprovações manuais

---

## Autor

Desenvolvido como parte da disciplina de CI/CD.

---

**Nota:** Este é um projeto educacional para demonstração de práticas de CI/CD com GitHub Actions.

