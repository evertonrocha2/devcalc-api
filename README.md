# DevCalc API

![CI/CD Pipeline](https://github.com/evertonrocha2/devcalc-api/actions/workflows/ci.yml/badge.svg)

API REST em Java para operações matemáticas. Projeto da disciplina de CI/CD.

## Stack

- Java 11
- Javalin 5.6.3
- Maven
- JUnit 5
- GitHub Actions
- Checkstyle

## Endpoints

- `GET /add?a=10&b=5` - Soma
- `GET /subtract?a=10&b=5` - Subtração
- `GET /multiply?a=10&b=5` - Multiplicação
- `GET /divide?a=10&b=5` - Divisão
- `GET /sqrt?x=16` - Raiz quadrada (novo no TP3)

---

## TP1 - Pipeline Básico

**O que foi implementado:**

1. **Aplicação base**
   - API REST com Javalin
   - CalculatorService com operações básicas
   - Testes com JUnit 5

2. **Workflows criados**
   - `hello.yml` - workflow simples de hello world
   - `ci.yml` - pipeline completo com:
     - Build com Maven
     - Testes automatizados
     - Package do JAR
     - Upload de artefatos
     - Deploy simulado

**Arquivos:** `.github/workflows/ci.yml`, `.github/workflows/hello.yml`

---

## TP2 - Actions e Modularização

**1. Action do Marketplace**
- Action integrada: `jwgmeligmeyling/checkstyle-github-action@v1.2`
- Plugin Maven Checkstyle configurado em `pom.xml`
- Arquivo `checkstyle.xml` com regras de análise
- Job `code-quality` adicionado ao `ci.yml`

**2. Documentação - actions-explicacao.md**
- Diferença entre workflows e actions
- Estrutura do action.yml
- Como passar parâmetros (uses + with)
- Exemplos práticos com actions usadas no projeto

**3. Workflow Reutilizável**
- Criado `lint-and-test.yml` com `workflow_call`
- Aceita parâmetros: java-version, run-lint, run-tests
- Jobs condicionais baseados nos inputs
- Chamado pelo ci.yml principal

**4. Execução Manual com Parâmetros**
- `workflow_dispatch` configurado no ci.yml
- Inputs: environment (choice), run_tests (boolean), run_lint (boolean)
- Jobs usam `if:` para executar condicionalmente
- Testado via interface do GitHub

**5. Provocando e Corrigindo Erro**
- Erro proposital: comando inválido inserido no workflow
- Pipeline falhou com exit code 127
- Erro identificado nos logs da aba Actions
- Corrigido e processo documentado

**6. Badge de Status**
- Badge do workflow principal no README
- Execuções comparadas: push automático vs manual

**Arquivos criados:**
- `.github/workflows/lint-and-test.yml`
- `actions-explicacao.md`
- `checkstyle.xml`
- Evidências: `evidencias/pipeline-sucesso-tp2.png`, `pipeline-erro-tp2.png`, etc

---

## TP3 - Segurança e Ambientes

**1. Runner Auto-Hospedado**
- Workflow `runner-auto-hospedado.yml` criado
- Configurado com `runs-on: self-hosted`
- Exibe informações do sistema (uname, java version)
- Instala software adicional durante execução
- Instruções de setup no README

**2. Variáveis e Secrets**
- Workflow `vars-e-secrets.yml` criado
- Variáveis de repositório: APP_MODE, SUPPORT_EMAIL, API_URL
- Secret sensível: PROD_TOKEN
- Acessados via `${{ vars.* }}` e `${{ secrets.* }}`
- Configurados em Settings → Secrets and variables

**3. Contextos e Escopos**
- Workflow `env-context-demo.yml` criado
- Mostra contextos: `github.actor`, `github.repository`, `runner.os`, etc
- Variável STAGE definida em 3 níveis (workflow/job/step)
- Demonstra precedência e isolamento entre jobs

**4. Permissões e GITHUB_TOKEN**
- Workflow `github-token-demo.yml` criado
- Permissões configuradas: `contents: read`, `issues: write`
- Cria issue automaticamente via `actions/github-script@v7`
- Input `criar_issue` controla execução
- Demonstra uso seguro do token padrão

**5. Ambientes Dev e Prod**
- Workflows criados:
  - `deploy-dev.yml` - deploy automático no push para branch dev
  - `deploy-prod.yml` - deploy com aprovação manual no push para main
- Ambientes configurados no GitHub:
  - dev: sem proteções
  - prod: required reviewers, wait timer
- Variáveis e secrets específicos por ambiente
- URLs diferentes para cada ambiente

**6. Nova Funcionalidade - Raiz Quadrada**
- Endpoint `GET /sqrt?x=16` adicionado
- Método `sqrt(double x)` em CalculatorService
  - Valida número negativo
  - Retorna Math.sqrt(x)
- Classe `SingleOperationResponse` para resposta
- 6 novos testes em CalculatorServiceTest:
  - testSqrtPositiveNumbers
  - testSqrtZero
  - testSqrtOne
  - testSqrtDecimalNumbers
  - testSqrt100
  - testSqrtNegativeNumber
- Pipeline roda testes automaticamente

**Arquivos criados:**
- `.github/workflows/runner-auto-hospedado.yml`
- `.github/workflows/vars-e-secrets.yml`
- `.github/workflows/env-context-demo.yml`
- `.github/workflows/github-token-demo.yml`
- `.github/workflows/deploy-dev.yml`
- `.github/workflows/deploy-prod.yml`
- Código modificado: App.java, CalculatorService.java, CalculatorServiceTest.java
- Evidências: `evidencias/tp3-*.png`

---

## Como rodar

```bash
git clone https://github.com/evertonrocha2/devcalc-api.git
cd devcalc-api
mvn clean install
mvn exec:java -D"exec.mainClass=com.devcalc.App"
```

Roda em `http://localhost:7000`

Testar:
```bash
curl "http://localhost:7000/add?a=10&b=5"
curl "http://localhost:7000/sqrt?x=16"
```

---

## Workflows

`.github/workflows/ci.yml` - Pipeline principal  
`.github/workflows/lint-and-test.yml` - Reutilizável (TP2)  
`.github/workflows/runner-auto-hospedado.yml` - Runner local (TP3)  
`.github/workflows/vars-e-secrets.yml` - Variáveis (TP3)  
`.github/workflows/env-context-demo.yml` - Contextos (TP3)  
`.github/workflows/github-token-demo.yml` - Token (TP3)  
`.github/workflows/deploy-dev.yml` - Deploy dev (TP3)  
`.github/workflows/deploy-prod.yml` - Deploy prod (TP3)

---

## Setup GitHub

**Variáveis:**
- APP_MODE
- SUPPORT_EMAIL

**Secrets:**
- PROD_TOKEN

**Ambientes:**
- dev (sem proteção)
- prod (com aprovação manual)

---

Projeto da disciplina CI/CD - 2026

