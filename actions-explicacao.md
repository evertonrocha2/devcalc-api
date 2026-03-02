# Diferença entre Workflows e Actions no GitHub Actions

## Conceitos Fundamentais

### O que é um Workflow?

Um **workflow** é um processo automatizado configurável que você define no seu repositório GitHub. Ele é composto por um ou mais **jobs** que podem ser executados em sequência ou em paralelo, baseado em gatilhos (triggers) específicos.

**Características principais:**
- Definido em arquivos YAML dentro da pasta `.github/workflows/`
- Contém a lógica de orquestração do pipeline (quando executar, em que ordem, etc.)
- Pode ter múltiplos jobs que executam em runners diferentes
- Define os gatilhos (push, pull_request, schedule, workflow_dispatch, etc.)
- É específico do seu repositório e projeto

**Exemplo de estrutura:**
```yaml
name: Meu Workflow
on: [push, pull_request]
jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - run: echo "Hello World"
```

### O que é uma Action?

Uma **action** é uma aplicação customizada e reutilizável que executa tarefas específicas dentro de um workflow. Actions são os "blocos de construção" que você usa dentro dos steps de um job.

**Características principais:**
- Pode ser criada por você, pela comunidade ou pelo GitHub
- Reutilizável em múltiplos workflows e repositórios
- Publicada no GitHub Marketplace ou usada localmente
- Tem seu próprio arquivo `action.yml` que define sua estrutura
- Aceita inputs (parâmetros) e pode retornar outputs (resultados)

**Exemplo de uso:**
```yaml
steps:
  - uses: actions/checkout@v4
  - uses: actions/setup-java@v4
    with:
      distribution: 'temurin'
      java-version: '11'
```

## Diferenças Principais

| Aspecto | Workflow | Action |
|---------|----------|--------|
| **Propósito** | Orquestrar o pipeline completo | Executar uma tarefa específica |
| **Localização** | `.github/workflows/*.yml` | Repositório próprio ou marketplace |
| **Reutilização** | Específico do projeto | Altamente reutilizável entre projetos |
| **Escopo** | Define jobs, runners, triggers | Define uma unidade de trabalho |
| **Arquivo de definição** | Workflow YAML | `action.yml` |
| **Como usar** | Executado pelo GitHub Actions | Chamada via `uses:` em um workflow |

## Estrutura Interna de uma Action

Uma action é definida por um arquivo `action.yml` (ou `action.yaml`) na raiz do seu repositório. Este arquivo especifica:

### 1. Metadados da Action
```yaml
name: 'Nome da Action'
description: 'Descrição do que a action faz'
author: 'Seu nome ou organização'
```

### 2. Inputs (Parâmetros de Entrada)
```yaml
inputs:
  java-version:
    description: 'Versão do Java a ser instalada'
    required: true
    default: '11'
  distribution:
    description: 'Distribuição do Java'
    required: false
    default: 'temurin'
```

- **description:** Explica o propósito do parâmetro
- **required:** Define se o parâmetro é obrigatório
- **default:** Valor padrão se não for fornecido

### 3. Outputs (Resultados de Saída)
```yaml
outputs:
  java-path:
    description: 'Caminho onde o Java foi instalado'
  cache-hit:
    description: 'Se o cache foi encontrado'
```

Outputs podem ser usados por steps subsequentes no workflow através de `steps.<step-id>.outputs.<output-name>`.

### 4. Runs (Comandos Executados)

Existem três tipos principais de actions:

#### JavaScript Actions
```yaml
runs:
  using: 'node20'
  main: 'dist/index.js'
```

#### Docker Container Actions
```yaml
runs:
  using: 'docker'
  image: 'Dockerfile'
```

#### Composite Actions
```yaml
runs:
  using: 'composite'
  steps:
    - run: echo "Step 1"
      shell: bash
    - run: echo "Step 2"
      shell: bash
```

## Exemplo Prático: Analisando uma Action Utilizada

Vamos analisar a action **`actions/setup-java@v4`** que usamos no nosso projeto DevCalc.

### Como ela é chamada no workflow

```yaml
- name: Configurar Java 11
  uses: actions/setup-java@v4
  with:
    distribution: 'temurin'
    java-version: '11'
    cache: 'maven'
```

### Análise dos Componentes

1. **`uses: actions/setup-java@v4`**
   - `actions/setup-java` é o repositório onde a action está hospedada (github.com/actions/setup-java)
   - `@v4` especifica a versão da action (tag v4 no repositório)
   - Isso garante que usamos uma versão estável e evita breaking changes

2. **`with:` (passagem de parâmetros)**
   - `distribution: 'temurin'` - Define qual distribuição do JDK instalar
   - `java-version: '11'` - Especifica a versão do Java
   - `cache: 'maven'` - Habilita cache das dependências Maven
   
   Estes parâmetros correspondem aos **inputs** definidos no `action.yml` da action setup-java.

3. **Como a action funciona internamente**
   
   O arquivo `action.yml` da setup-java define aproximadamente:
   ```yaml
   name: 'Setup Java JDK'
   description: 'Set up a specific version of Java JDK'
   
   inputs:
     java-version:
       description: 'The Java version to set up'
       required: true
     distribution:
       description: 'Java distribution'
       required: true
     cache:
       description: 'Name of dependency manager for caching'
       required: false
   
   outputs:
     java-version:
       description: 'Actual version of Java that was installed'
     path:
       description: 'Path to where Java was installed'
   
   runs:
     using: 'node20'
     main: 'dist/index.js'
   ```

4. **Execução**
   - A action baixa e instala a versão especificada do Java
   - Configura as variáveis de ambiente (JAVA_HOME, PATH)
   - Se solicitado, configura o cache para o Maven
   - Retorna outputs com o caminho da instalação e versão instalada

### Outro Exemplo: Action do Marketplace

No nosso projeto, também utilizamos a action **`jwgmeligmeyling/checkstyle-github-action@v1.2`**:

```yaml
- name: Checkstyle Report Action (Marketplace)
  uses: jwgmeligmeyling/checkstyle-github-action@v1.2
  with:
    path: '**/checkstyle-result.xml'
```

**Análise:**
- Esta é uma action externa do GitHub Marketplace
- Ela lê os relatórios XML gerados pelo Checkstyle
- Cria anotações automáticas no código com os problemas encontrados
- O parâmetro `path` é um input que especifica onde encontrar os arquivos XML
- A action processa esses arquivos e cria comentários visuais no GitHub

## Workflows Reutilizáveis

Desde 2021, o GitHub Actions também permite criar **workflows reutilizáveis** usando `workflow_call`:

```yaml
on:
  workflow_call:
    inputs:
      config-path:
        required: true
        type: string
```

Isso permite que workflows sejam chamados por outros workflows, criando uma camada adicional de reutilização.

## Resumo

- **Workflows** são os processos completos que orquestram todo o pipeline
- **Actions** são componentes reutilizáveis que executam tarefas específicas
- Actions são chamadas dentro de workflows usando `uses:`
- Parâmetros são passados para actions através do bloco `with:`
- O arquivo `action.yml` define a estrutura, inputs, outputs e comandos de uma action
- Actions podem ser públicas (Marketplace) ou privadas (no seu repositório)
- Workflows definem QUANDO e COMO executar, actions definem O QUE executar

## Referências

- [GitHub Actions Documentation](https://docs.github.com/en/actions)
- [Creating Actions](https://docs.github.com/en/actions/creating-actions)
- [GitHub Actions Marketplace](https://github.com/marketplace?type=actions)
- [Metadata syntax for GitHub Actions](https://docs.github.com/en/actions/creating-actions/metadata-syntax-for-github-actions)
