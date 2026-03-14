# Guia de Testes - TP2 e TP3

## Testes Locais (Antes de Commitar)

### 1. Testar a Aplicacao Java

```bash
# Compilar o projeto
mvn clean install

# Executar testes
mvn test

# Executar Checkstyle
mvn checkstyle:checkstyle

# Iniciar a aplicacao
mvn exec:java -Dexec.mainClass="com.devcalc.App"
```

**Testar os endpoints:**

```bash
# Adicao
curl "http://localhost:7000/add?a=5&b=3"

# Subtracao
curl "http://localhost:7000/subtract?a=10&b=4"

# Multiplicacao
curl "http://localhost:7000/multiply?a=6&b=7"

# Divisao
curl "http://localhost:7000/divide?a=20&b=4"

# Raiz quadrada (TP3)
curl "http://localhost:7000/sqrt?x=16"
curl "http://localhost:7000/sqrt?x=25"
curl "http://localhost:7000/sqrt?x=0"
```

### 2. Validar Workflows (Sintaxe)

Os workflows estao corretos se:
- Arquivos `.yml` estao em `.github/workflows/`
- Sintaxe YAML correta (indentacao com espacos)
- Referencias a actions existem (@v4, @v7, etc)

---

## Testes no GitHub

### Preparacao: Fazer Commit e Push

```bash
# Adicionar todos os arquivos
git add .

# Commitar
git commit -m "Implementacao completa TP2 e TP3"

# Push para dev (testa deploy-dev)
git push origin dev

# Criar e push para main (testa deploy-prod e ci.yml)
git checkout -b main
git push origin main
```

---

## TP2 - Testes dos Workflows

### 1. Testar CI/CD Principal (ci.yml)

**Teste Automatico:**
```bash
# Faz push e observa pipeline
git push origin main
```

**Teste Manual:**
1. Va em: `Actions` > `CI/CD Pipeline`
2. Clique em `Run workflow`
3. Escolha parametros:
   - `run_tests`: true/false
   - `run_lint`: true/false
   - `environment`: development/staging/production
4. Clique `Run workflow`
5. Observe execucao

**O que verificar:**
- Jobs executam conforme parametros
- Checkstyle gera relatorio
- Build funciona
- Testes passam
- Artifacts sao criados

### 2. Testar Workflow Reutilizavel (lint-and-test.yml)

Este workflow e chamado automaticamente pelo `ci.yml`.

**Verificar:**
- Job "Lint and Test (Reusable)" aparece no ci.yml
- Gera artifacts de checkstyle e testes

### 3. Testar Action do Marketplace

Executado automaticamente no job `code-quality` do ci.yml.

**Verificar:**
- Job "Code Quality Check with Marketplace Action"
- Action `jwgmeligmeyling/checkstyle-github-action@v1.2` executa
- Gera anotacoes no PR (se houver)

### 4. Provocar Erro (TP2 Requisito 5)

**Opcao A - Usar workflow de teste:**
```bash
# Editar .github/workflows/test-error.yml
# Descomentar linha com erro
git add .github/workflows/test-error.yml
git commit -m "Teste: provocar erro"
git push
```

**Opcao B - Criar erro temporario:**
1. Editar `ci.yml` ou `pom.xml` com erro
2. Commitar e observar falha
3. Corrigir e commitar novamente

**Verificar:**
- Pipeline falha
- Logs mostram erro claramente
- Correcao faz pipeline passar

---

## TP3 - Testes dos Workflows

### IMPORTANTE: Configuracao Necessaria

Antes de testar TP3, configure no GitHub:

1. **Variables** (Settings > Secrets and variables > Actions > Variables):
   - `APP_MODE`: `production`
   - `SUPPORT_EMAIL`: `contato@evertonrocha.dev`
   - `API_URL`: `https://api.devcalc.evertonrocha.dev`

2. **Secrets** (Settings > Secrets and variables > Actions):
   - `PROD_TOKEN`: qualquer valor (ex: `token_secreto_123`)
   - `PROD_API_KEY`: qualquer valor (ex: `api_key_456`)

3. **Environments** (Settings > Environments):
   - Criar `dev` (sem protecao)
   - Criar `prod` (com required reviewers)

4. **Permissions** (Settings > Actions > General):
   - Workflow permissions: `Read and write permissions`

### 1. Testar Runner Auto-Hospedado (runner-auto-hospedado.yml)

**Sem runner proprio (teste basico):**
```bash
# Editar runner-auto-hospedado.yml temporariamente
# Trocar: runs-on: self-hosted
# Por: runs-on: ubuntu-latest
git add .github/workflows/runner-auto-hospedado.yml
git commit -m "Teste: runner em ubuntu"
git push origin main
```

**Com runner proprio:**
1. Settings > Actions > Runners > New self-hosted runner
2. Seguir instrucoes para instalar na sua maquina
3. Executar workflow manualmente
4. Verificar que rodou localmente

### 2. Testar Variaveis e Secrets (vars-e-secrets.yml)

**Execucao Manual:**
1. `Actions` > `Demonstracao de Variaveis e Secrets`
2. `Run workflow` > `Run workflow`

**Verificar:**
- Variaveis sao exibidas corretamente
- Token e mascarado nos logs
- Autenticacao simulada funciona

### 3. Testar Contextos (env-context-demo.yml)

**Execucao Manual:**
1. `Actions` > `Demonstracao de Contextos e Escopos`
2. `Run workflow` > `Run workflow`

**Verificar:**
- Contexto `github.*` exibe dados corretos
- Contexto `runner.*` mostra info do runner
- Variaveis nos diferentes escopos (workflow/job/step)
- Precedencia de variaveis funciona

### 4. Testar GITHUB_TOKEN (github-token-demo.yml)

**Execucao Manual:**
1. `Actions` > `Demonstracao de Permissoes e GITHUB_TOKEN`
2. `Run workflow`
3. Marcar `criar_issue: true`
4. `Run workflow`

**Verificar:**
- Permissoes sao exibidas
- Issue e criada automaticamente
- Issue tem label `tp3` e `automated`
- Comentarios em PR (se houver PR aberto)

### 5. Testar Ambientes Dev e Prod

**Deploy Dev:**
```bash
# Push na branch dev
git checkout dev
git push origin dev
```

**Verificar:**
- Workflow `deploy-dev.yml` executa automaticamente
- Usa variaveis do ambiente `dev`
- Sem aprovacao manual

**Deploy Prod:**
```bash
# Push na branch main
git checkout main
git push origin main
```

**Verificar:**
- Workflow `deploy-prod.yml` executa
- Pausa para aprovacao manual
- Usa secrets do ambiente `prod`
- Requer aprovacao antes de completar

### 6. Testar Nova Funcionalidade (sqrt)

**Localmente:**
```bash
mvn test
# Verificar que todos os 6 novos testes de sqrt passam
```

**Via API:**
```bash
# Iniciar app
mvn exec:java -Dexec.mainClass="com.devcalc.App"

# Em outro terminal
curl "http://localhost:7000/sqrt?x=16"
# Esperado: {"x":16.0,"result":4.0}

curl "http://localhost:7000/sqrt?x=25"
# Esperado: {"x":25.0,"result":5.0}

curl "http://localhost:7000/sqrt?x=-4"
# Esperado: erro 500 com mensagem de numero negativo
```

---

## Checklist de Evidencias

### TP2
- [ ] Screenshot de pipeline executando automaticamente
- [ ] Screenshot de execucao manual com parametros
- [ ] Screenshot de erro provocado e logs
- [ ] Screenshot de erro corrigido
- [ ] Screenshot do badge no README
- [ ] Screenshot comparando execucao automatica vs manual

### TP3
- [ ] Screenshot de runner auto-hospedado funcionando
- [ ] Screenshot de variaveis e secrets sendo usados
- [ ] Screenshot de contextos exibidos
- [ ] Screenshot de issue criada automaticamente
- [ ] Screenshot de deploy em dev (automatico)
- [ ] Screenshot de deploy em prod (com aprovacao)
- [ ] Screenshot de testes da funcionalidade sqrt
- [ ] Screenshot da API retornando sqrt

---

## Comandos Rapidos

### Ver todos os workflows
```bash
ls .github/workflows/
```

### Validar sintaxe YAML online
Copiar conteudo e colar em: https://www.yamllint.com/

### Ver logs de um workflow especifico
1. GitHub > Actions
2. Clicar no workflow
3. Clicar no run especifico
4. Expandir jobs e steps

### Reexecutar workflow que falhou
1. GitHub > Actions
2. Workflow que falhou
3. Botao `Re-run jobs` > `Re-run all jobs`

---

## Dicas

1. **Teste local primeiro**: Sempre compile e teste antes de push
2. **Commits pequenos**: Facilita identificar problemas
3. **Observe os logs**: GitHub Actions mostra tudo em tempo real
4. **Use workflow_dispatch**: Permite testar sem fazer push
5. **Artifacts**: Baixe para ver relatorios de checkstyle e testes
6. **Environments**: Teste dev antes de prod

---

## Ordem Recomendada de Testes

1. Teste local: `mvn clean install && mvn test`
2. Commit e push para `dev`
3. Verifique `deploy-dev.yml`
4. Execute workflows manuais (vars, contexts, token)
5. Merge para `main`
6. Verifique `ci.yml` completo
7. Verifique `deploy-prod.yml` (com aprovacao)
8. Colete evidencias de tudo

Boa sorte!
