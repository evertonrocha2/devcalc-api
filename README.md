# DevCalc API

## Objetivo do Projeto

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

## Workflows CI/CD

### ci.yml - Pipeline Completo (TP1 + TP2)

Pipeline completo de Integração e Entrega Contínua com melhorias do TP2.

**Gatilhos:**
- Push na branch `main` (apenas em alterações em `src/**`, `pom.xml` ou workflows)
- Pull requests para `main`
- Execução manual via `workflow_dispatch` com parâmetros customizáveis

**Parâmetros de Execução Manual:**
- `environment`: Escolha do ambiente (development, staging, production)
- `run_tests`: Booleano para executar ou não os testes automatizados
- `run_lint`: Booleano para executar ou não a análise de código

**Jobs:**

1. **checkout** - Realiza checkout do código e disponibiliza como artefato
2. **lint-and-test** - **[NOVO TP2]** Chama workflow reutilizável para lint e testes
3. **code-quality** - **[NOVO TP2]** Análise de código com Action do Marketplace (Checkstyle)
4. **build** - Configura Java 11 e compila o projeto com Maven
5. **package** - Gera o JAR executável da aplicação
6. **deploy** - Simula o deploy da aplicação

### lint-and-test.yml - Workflow Reutilizável (TP2)

**[NOVIDADE TP2]** Workflow modular e reutilizável para análise de código e testes.

**Tipo:** Workflow Reutilizável (`workflow_call`)

**Parâmetros de Entrada:**
- `java-version`: Versão do Java (padrão: '11')
- `run-lint`: Executar análise de código (padrão: true)
- `run-tests`: Executar testes automatizados (padrão: true)

**Jobs:**
1. **lint** - Executa Checkstyle e gera relatórios de qualidade de código
2. **test** - Executa testes automatizados e gera relatórios de cobertura

### test-error.yml - Pipeline de Teste de Erro (TP2)

**[NOVIDADE TP2]** Workflow criado para demonstrar depuração de erros.

**Propósito:** Educacional - mostra como identificar e corrigir falhas no pipeline

## Depuração de Erros no Pipeline

Durante o desenvolvimento do TP2, provocamos intencionalmente um erro no pipeline para exercitar o processo de depuração.

### Como Identificar Problemas

1. **Aba Actions do GitHub:**
   - Acesse a aba "Actions" no repositório
   - Identifique execuções com falha vs sucesso
   - Clique na execução com falha para ver detalhes

2. **Navegação pelos Logs:**
   - Cada job aparece no painel lateral esquerdo
   - Jobs com erro aparecem em vermelho
   - Clique no job com problema
   - Expanda os steps para ver onde ocorreu a falha
   - O log mostra a mensagem de erro específica

3. **Ferramentas Utilizadas:**
   - **Interface web do GitHub Actions:** Visualização hierárquica dos jobs e steps
   - **Logs detalhados:** Cada step mostra stdout/stderr completo
   - **Timestamps:** Identificação de quanto tempo cada step levou
   - **Anotações:** Erros são destacados automaticamente

### Erro Provocado e Resolução

**Erro Intencional:**
Criamos um workflow `test-error.yml` com um comando inválido:
```yaml
- name: Comando inválido que vai falhar
  run: comando_invalido_inexistente
```

**Sintomas:**
- Pipeline falhou com status code 127 (command not found)
- Mensagem de erro: "comando_invalido_inexistente: command not found"
- Steps subsequentes não foram executados

**Como Identificamos:**
1. Aba Actions mostrou erro na execução
2. Clicamos no workflow "Test Error Pipeline"
3. Expandimos o step "Comando inválido que vai falhar"
4. Log mostrou claramente: `/bin/sh: 1: comando_invalido_inexistente: not found`
5. Exit code 127 indicou comando não encontrado

**Correção:**
Após identificar o erro através dos logs do GitHub Actions, corrigimos o problema substituindo o comando inválido `comando_inexistente_maven_build` pelo comando correto `mvn clean install -DskipTests`. O pipeline voltou a funcionar normalmente após o commit da correção.

**Evidências:**
- `evidencias/pipeline-sucesso-tp2.png` - Pipeline executando com sucesso
- `evidencias/pipeline-erro-tp2.png` - Pipeline com erro proposital identificado

## Comparação: Execução Automática vs Manual

### Execução por Push (Automática)

Quando fazemos `git push` para a branch `main`, o pipeline é acionado automaticamente se os arquivos modificados estiverem nos paths configurados.

**Características:**
- **Gatilho:** `on: push`
- **Contexto:** `github.event_name = 'push'`
- **Parâmetros:** Não aceita parâmetros personalizados
- **Execução:** Roda todos os jobs configurados
- **Uso típico:** CI contínua durante desenvolvimento

### Execução Manual (workflow_dispatch)

Quando clicamos em "Run workflow" na aba Actions, podemos escolher parâmetros antes da execução.

**Características:**
- **Gatilho:** `on: workflow_dispatch`
- **Contexto:** `github.event_name = 'workflow_dispatch'`
- **Parâmetros:** Aceita inputs customizados (`run_tests`, `run_lint`, `environment`)
- **Execução:** Pode executar seletivamente jobs com base nos parâmetros
- **Uso típico:** Testes específicos, deploys controlados, debugging

### Observações Práticas

Após realizar execuções de ambos os tipos, observamos:

1. **Flexibilidade:** A execução manual permite testar apenas componentes específicos (ex: só lint, ou só testes)
2. **Velocidade:** Execuções manuais seletivas são mais rápidas quando não precisamos de todo o pipeline
3. **Controle:** workflow_dispatch é ideal para ambientes controlados (staging, production)
4. **Filtros na Aba Actions:** É possível filtrar execuções por tipo de gatilho (Event)
5. **Histórico:** Execuções manuais aparecem marcadas com o ícone de "play" manual
6. **Parâmetros Visíveis:** Na interface, é possível ver quais parâmetros foram passados em execuções manuais

**Exemplo de Uso:**
- **Push automático:** Desenvolvedor faz commit → Pipeline valida tudo automaticamente
- **Execução manual:** Antes de merge, rodar só os testes sem rebuild completo para economizar tempo

## Novidades do TP2

### 1. Action do GitHub Marketplace
Integrada a action `jwgmeligmeyling/checkstyle-github-action@v1.2` para análise automática de código Java com Checkstyle.

### 2. Documentação Conceitual
Criado o arquivo `actions-explicacao.md` que explica em detalhes a diferença entre workflows e actions, estrutura interna de uma action, e exemplos práticos.

### 3. Workflow Reutilizável
Criado `lint-and-test.yml` como workflow reutilizável que pode ser chamado por outros workflows com parâmetros customizáveis.

### 4. Execução Manual Parametrizável
O workflow `ci.yml` agora aceita execução manual com parâmetros booleanos (`run_tests` e `run_lint`) para controle fino da execução.

### 5. Prática de Debugging
Criado workflow `test-error.yml` com erro intencional para demonstrar o processo de identificação e correção de falhas em pipelines.

### 6. Monitoramento
Adicionado status badge que mostra o estado atual do pipeline principal, com documentação detalhada comparando diferentes modos de execução.

## Autor

Desenvolvido como parte da disciplina de CI/CD.

---

**Nota:** Este é um projeto educacional para demonstração de práticas de CI/CD com GitHub Actions.
