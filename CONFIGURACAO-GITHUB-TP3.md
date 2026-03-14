# Configuração do GitHub para TP3

Este documento contém o passo a passo detalhado para configurar todas as funcionalidades do TP3 no GitHub.

---

## 1. Configurar Variáveis de Repositório

1. Acesse seu repositório no GitHub
2. Clique em **Settings** (Configurações)
3. No menu lateral, clique em **Secrets and variables** → **Actions**
4. Na aba **Variables**, clique em **New repository variable**

**Criar as seguintes variáveis:**

**Variável 1:**
- Name: `APP_MODE`
- Value: `development`
- Click em "Add variable"

**Variável 2:**
- Name: `SUPPORT_EMAIL`
- Value: `contato@evertonrocha.dev`
- Click em "Add variable"

**Variável 3 (opcional):**
- Name: `API_URL`
- Value: `https://api.devcalc.evertonrocha.dev`
- Click em "Add variable"

---

## 2. Configurar Secrets

Na mesma tela (Settings → Secrets and variables → Actions):

1. Clique na aba **Secrets**
2. Clique em **New repository secret**

**Criar o seguinte secret:**

**Secret 1:**
- Name: `PROD_TOKEN`
- Secret: `meu-token-super-secreto-123456` (substitua por um token real ou fictício)
- Click em "Add secret"

**Importante:** Após salvar, você não conseguirá ver o valor novamente (apenas editar).

---

## 3. Configurar Ambientes

### Criar Ambiente DEV

1. Settings → **Environments**
2. Click em **New environment**
3. Name: `dev`
4. Click em "Configure environment"
5. **Não adicione proteções** (deixe tudo desmarcado)
6. Click em "Save protection rules"

**Adicionar variáveis ao ambiente dev (opcional):**
- Na página do ambiente, seção "Environment variables"
- Add variable: `APP_MODE` = `development`

### Criar Ambiente PROD

1. Settings → **Environments**
2. Click em **New environment**
3. Name: `prod`
4. Click em "Configure environment"

**Configurar proteções:**

5. Marque **"Required reviewers"**
6. Click em "Add reviewers"
7. Selecione você mesmo ou membros da equipe
8. (Opcional) Marque "Wait timer" e coloque 5 minutos
9. Click em "Save protection rules"

**Adicionar secrets ao ambiente prod:**
- Na página do ambiente, seção "Environment secrets"
- Add secret: `PROD_API_KEY` = `chave-api-producao-123`
- Add secret: `PROD_TOKEN` = `token-producao-456` (ou deixe usar o do repositório)

**Adicionar variáveis ao ambiente prod:**
- Add variable: `APP_MODE` = `production`

---

## 4. Configurar Runner Auto-Hospedado

### Opção A: Máquina Local (Recomendado para testes)

1. Settings → Actions → **Runners**
2. Click em **New self-hosted runner**
3. Escolha seu sistema operacional

**Para Linux/macOS:**
```bash
# Criar diretório
mkdir -p ~/actions-runner && cd ~/actions-runner

# Baixar (o GitHub mostrará o comando exato)
curl -o actions-runner-linux-x64-X.XXX.X.tar.gz -L https://github.com/actions/runner/releases/download/vX.XXX.X/actions-runner-linux-x64-X.XXX.X.tar.gz

# Extrair
tar xzf ./actions-runner-linux-x64-X.XXX.X.tar.gz

# Configurar (use o token que o GitHub mostrar)
./config.sh --url https://github.com/evertonrocha2/ci-cd --token SEU_TOKEN_GERADO

# Executar
./run.sh
```

**Para Windows (PowerShell como Admin):**
```powershell
# Criar diretório
mkdir C:\actions-runner ; cd C:\actions-runner

# Baixar (o GitHub mostrará o link exato)
Invoke-WebRequest -Uri https://github.com/actions/runner/releases/download/vX.XXX.X/actions-runner-win-x64-X.XXX.X.zip -OutFile actions-runner-win-x64-X.XXX.X.zip

# Extrair
Add-Type -AssemblyName System.IO.Compression.FileSystem
[System.IO.Compression.ZipFile]::ExtractToDirectory("$PWD\actions-runner-win-x64-X.XXX.X.zip", "$PWD")

# Configurar
.\config.cmd --url https://github.com/evertonrocha2/ci-cd --token SEU_TOKEN_GERADO

# Executar
.\run.cmd
```

**Deixar rodando:**
- O runner precisa estar em execução para aceitar jobs
- Mantenha o terminal aberto ou configure como serviço

**Como serviço (Linux):**
```bash
sudo ./svc.sh install
sudo ./svc.sh start
```

### Opção B: Máquina na Nuvem

Mesmas instruções acima, mas executar em:
- AWS EC2
- Google Cloud Compute Engine
- Azure VM
- Digital Ocean Droplet

---

## 5. Criar Branch Dev

Se ainda não existe:

```bash
# Localmente
git checkout -b dev
git push -u origin dev
```

Ou no GitHub:
1. Página principal do repositório
2. Click no dropdown da branch
3. Digite "dev" e click em "Create branch: dev"

---

## 6. Configurar Permissões do Repositório

Para alguns workflows funcionarem (criação de issues, etc.):

1. Settings → Actions → General
2. Seção "Workflow permissions"
3. Selecione **"Read and write permissions"**
4. Marque "Allow GitHub Actions to create and approve pull requests"
5. Save

---

## Resumo das Configurações

### Variáveis (3)
- ✅ APP_MODE
- ✅ SUPPORT_EMAIL
- ✅ API_URL (opcional)

### Secrets (1)
- ✅ PROD_TOKEN

### Ambientes (2)
- ✅ dev (sem proteções)
- ✅ prod (com required reviewers)

### Runner (1)
- ✅ self-hosted (Linux/Mac/Windows)

### Branches (2)
- ✅ main (já existe)
- ✅ dev (criar)

### Permissões
- ✅ Read and write permissions habilitadas

---

## Validação

Após configurar tudo, execute:

1. **Testar variáveis:** Actions → Vars e Secrets → Run workflow
2. **Testar contextos:** Actions → Env Context Demo → Run workflow
3. **Testar token:** Actions → GitHub Token Demo → Run workflow (marcar criar_issue)
4. **Testar runner:** Actions → Runner Auto-Hospedado → Run workflow
5. **Testar dev:** Push para branch dev
6. **Testar prod:** Push para branch main (aguardar aprovação)

---

## Capturas Necessárias

Tire screenshots de:

1. Settings → Actions → Variables (mostrando as 3 variáveis)
2. Settings → Actions → Secrets (mostrando PROD_TOKEN - valor oculto)
3. Settings → Environments (mostrando dev e prod)
4. Ambiente prod mostrando required reviewers
5. Settings → Actions → Runners (mostrando runner online)
6. Cada workflow executando com sucesso
7. Issue criada automaticamente
8. Deploy prod aguardando aprovação
9. Aprovação sendo concedida

---

**Siga este guia passo a passo e tire prints de cada etapa!**
