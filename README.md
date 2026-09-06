# Endless Runner Ninja — Especificação do Projeto

## 1. Premissa geral do jogo

O projeto consiste em um **endless runner 2D desenvolvido em Java**, inspirado na ideia do jogo do Dinossauro do Google, porém com uma proposta mais complexa, vertical e dinâmica.

O jogador controla um **ninja que corre continuamente para a direita**. Não existe uma fase tradicional com começo, meio e fim: enquanto o jogador conseguir sobreviver, o cenário continuará sendo gerado proceduralmente.

Durante a partida, o jogador deverá reagir aos elementos encontrados no cenário utilizando diferentes ações:

- Pular;
- Atacar com a espada;
- Utilizar jutsu;
- Realizar wall jump;
- Utilizar itens encontrados durante o percurso;
- Escolher, em determinados momentos, entre caminhos superiores e inferiores.

O jogo terá **3 vidas**. Ao receber dano, uma vida é perdida. Durante o percurso, o jogador poderá encontrar itens capazes de recuperar vidas, fornecer escudos ou conceder outros benefícios.

O jutsu possuirá **cooldown**, impedindo sua utilização contínua e obrigando o jogador a administrar o momento correto para utilizá-lo.

A dificuldade aumentará progressivamente. A velocidade do jogo aumentará conforme o jogador avança, e o gerador procedural passará a permitir desafios mais complexos.

O objetivo principal será **sobreviver pelo maior tempo/distância possível e obter a maior pontuação**.

Um dos principais diferenciais do projeto será a integração entre as mecânicas do jogo e os conceitos de **Estruturas de Dados e Algoritmos exigidos pelo professor**. Cada estrutura será utilizada para resolver um problema real do funcionamento do jogo, em vez de ser adicionada apenas para cumprir formalmente a atividade.

Os requisitos solicitados pelo professor serão:

1. **Vetor ou Matriz**;
2. **Generic**;
3. **Arquivos**;
4. **Pilha ou Fila**;
5. **Lista Encadeada**;
6. **Algoritmo de Busca**;
7. **Algoritmo de Ordenação**.

---

# 2. Engenharia de Software: Estruturas de Dados na Prática

O grande diferencial técnico deste projeto é a aplicação prática dos conceitos teóricos estudados na disciplina.

Cada requisito solicitado pelo professor possui uma função específica dentro da arquitetura do jogo.

A tabela abaixo apresenta o mapeamento entre **o requisito acadêmico** e sua aplicação prática no motor do jogo:

| **Requisito do Professor** | **Aplicação no Motor do Jogo**         | **Função / Problema Resolvido**                                                                                              |
| -------------------------- | -------------------------------------- | ---------------------------------------------------------------------------------------------------------------------------- |
| **Vetor (Array)**          | **Banco de Segmentos (Catálogo)**      | Armazena os modelos de cenário disponíveis para a geração procedural. Permite acesso direto aos segmentos por índice.        |
| **Matriz (Array 2D)**      | **Mapeamento Espacial (Grid)**         | Representa espacialmente cada segmento. Cada posição da matriz contém um código que representa um elemento do cenário.       |
| **Lista Encadeada**        | **Gerenciamento da sequência do mapa** | Mantém os segmentos gerados conectados dinamicamente e permite controlar a continuidade das rotas, plataformas e transições. |
| **Generic `<T>`**          | **Sistema de Object Pool**             | Permite criar um sistema genérico capaz de reutilizar diferentes tipos de objetos, como inimigos, obstáculos e itens.        |
| **Pilha (Stack)**          | **Inventário Tático (LIFO)**           | Armazena até 3 itens consumíveis. O último item coletado é o primeiro que poderá ser utilizado.                              |
| **Busca (Search)**         | **Decodificador de Entidades**         | Procura o significado dos códigos presentes na matriz e os transforma em elementos concretos do jogo.                        |
| **Ordenação (Sort)**       | **Balanceamento e Ranking**            | Organiza segmentos por dificuldade para auxiliar a geração procedural e também pode ordenar os resultados do ranking.        |
| **Arquivos (File I/O)**    | **Camada de Persistência**             | Salva e carrega informações entre partidas, como tutorial visto, melhor pontuação, distância, skins e ranking.               |

Dessa maneira, existe uma relação direta:

```text
REQUISITOS DO PROFESSOR
          ↓
┌─────────────────────────────────┐
│ Vetor                           │
│ Matriz                          │
│ Lista Encadeada                 │
│ Generic                         │
│ Pilha                           │
│ Busca                           │
│ Ordenação                       │
│ Arquivos                        │
└─────────────────────────────────┘
          ↓
FUNCIONALIDADES REAIS DO JOGO
```

A intenção é que, durante uma apresentação, seja possível explicar **não apenas onde determinada estrutura está no código, mas por que ela foi escolhida e qual problema ela resolve**.

---

# 3. Vetor — Banco de Segmentos

**Requisito do professor: VETOR.**

O vetor será utilizado para armazenar o banco de segmentos disponíveis para a geração procedural.

Conceitualmente:

```java
Segmento[] bancoDeSegmentos;
```

Esse vetor contém todos os modelos de cenário que podem ser utilizados pelo gerador.

Por exemplo:

```text
bancoDeSegmentos

[0] → Segmento A
[1] → Segmento B
[2] → Segmento C
[3] → Segmento D
[4] → Segmento E
[5] → Segmento F
```

Cada `Segmento` possuirá sua própria matriz.

Portanto:

```text
VETOR
  ↓
Segmentos disponíveis
  ↓
Cada segmento possui uma MATRIZ
```

O vetor representa o **catálogo de possibilidades**, enquanto a matriz representa a estrutura espacial de cada possibilidade.

O acesso a uma posição específica do vetor é `O(1)`, o que permite acessar diretamente um segmento através de seu índice.

O jogador não percorre necessariamente todos os segmentos existentes. O gerador seleciona um segmento dentre as opções disponíveis e o adiciona ao mapa.

---

# 4. Matriz — Representação espacial

**Requisito do professor: MATRIZ.**

Cada segmento será representado por uma matriz bidimensional.

Exemplo:

```text
0 0 0 0 0 0
0 0 4 0 0 0
0 0 1 1 1 0
1 1 1 1 1 1
```

A matriz representa a estrutura espacial daquele trecho do cenário.

Os números funcionam como identificadores:

```text
0 = vazio
1 = chão
2 = espinho
3 = parede
4 = inimigo
5 = item
6 = plataforma
7 = obstáculo
```

Assim, uma posição:

```text
matriz[1][2] = 4
```

significa que existe um elemento identificado pelo código `4` naquela posição.

A matriz é especialmente importante porque o jogo possui **movimentação vertical**, plataformas superiores, paredes e caminhos diferentes.

Ela permite representar não apenas uma sequência horizontal de obstáculos, mas uma verdadeira grade espacial.

---

# 5. Geração procedural

A geração procedural será responsável por transformar o banco de segmentos em um cenário aparentemente infinito.

O jogo não terá um mapa inteiro previamente construído.

Em vez disso, novos segmentos serão selecionados e adicionados conforme o jogador avança.

O processo será:

```text
Banco de Segmentos
        ↓
Verificar estado atual
        ↓
Verificar compatibilidade
        ↓
Verificar dificuldade
        ↓
Selecionar segmento válido
        ↓
Adicionar ao mapa
        ↓
Interpretar matriz
        ↓
Criar elementos
```

A escolha será aleatória, porém **controlada por regras**.

Isso é importante para evitar situações impossíveis.

Por exemplo:

```text
WALL_JUMP
     ↓
WALL_JUMP
     ↓
WALL_JUMP
     ↓
WALL_JUMP
```

poderia criar uma sequência exageradamente difícil.

Da mesma forma, se uma plataforma superior estiver ativa, o próximo segmento não poderá simplesmente ignorá-la.

---

# 6. Tipos e características dos segmentos

Cada segmento poderá possuir informações como:

```text
tipo
dificuldade
matriz
entrada
saída
caminho superior
caminho inferior
regras de compatibilidade
```

Os tipos podem representar características estruturais:

```text
NORMAL
OBSTÁCULO
WALL_JUMP
PLATAFORMA
TRANSIÇÃO
COMBINAÇÃO
```

É importante destacar que isso **não representa fases separadas**.

Não haverá necessariamente:

```text
FASE DE COMBATE
FASE DE WALL JUMP
FASE DE OBSTÁCULOS
```

O jogo continuará sendo uma corrida contínua.

Um inimigo pode aparecer no meio de um segmento normal.

Um item pode aparecer em uma plataforma.

Um espinho pode aparecer depois de um inimigo.

Uma parede pode aparecer depois de um obstáculo.

Por exemplo:

```text
NORMAL
   ↓
OBSTÁCULO
   ↓
INIMIGO
   ↓
NORMAL
   ↓
WALL_JUMP
   ↓
INIMIGO + ITEM
   ↓
NORMAL
```

O tipo do segmento serve principalmente para auxiliar o gerador a determinar **como aquele segmento pode se conectar aos próximos**.

---

# 7. Lista Encadeada

**Requisito do professor: LISTA ENCADEADA.**

A lista encadeada será utilizada para armazenar a sequência dos segmentos que estão sendo gerados durante a partida.

Conceitualmente:

```text
[Segmento A] → [Segmento B] → [Segmento C] → [Segmento D] → NULL
```

Cada nó possui um segmento e uma referência para o próximo nó:

```java
class NoSegmento {

    Segmento segmento;
    NoSegmento proximo;

}
```

O gerador procedural decide qual segmento deve ser criado.

Depois disso, o segmento é inserido na lista.

Portanto:

```text
GERADOR
   ↓
escolhe segmento
   ↓
LISTA ENCADEADA
   ↓
A → B → C → D
```

A lista não é responsável pela aleatoriedade.

Sua função é **manter a sequência e as relações entre os segmentos que formam o mapa ativo**.

---

# 8. Lista Encadeada e continuidade das rotas

A lista encadeada também será importante para manter a continuidade física do cenário.

Por exemplo, em determinado momento o jogador poderá escolher entre um caminho superior e um inferior:

```text
             CAMINHO SUPERIOR
          ─────────────────────


                   🥷


════════════════════════════════
             CAMINHO INFERIOR
```

A plataforma superior poderá durar apenas um segmento ou continuar por vários.

Por exemplo:

```text
Segmento A
────────────────
    plataforma
────────────────
        ↓

Segmento B
────────────────
    plataforma
────────────────
        ↓

Segmento C
────────────────
    plataforma
────────────────
```

Enquanto o caminho superior estiver ativo, o gerador precisa saber disso.

Não seria correto gerar:

```text
Segmento A → plataforma superior

Segmento B → nenhum caminho superior
```

sem uma transição.

A plataforma simplesmente desapareceria e o cenário pareceria quebrado.

Por isso, os segmentos poderão possuir informações de entrada e saída:

```text
entrada = SUPERIOR
saída   = SUPERIOR
```

ou:

```text
entrada = SUPERIOR
saída   = NORMAL
```

Assim:

```text
Segmento A
saída = SUPERIOR
       ↓
Segmento B
entrada = SUPERIOR
saída = SUPERIOR
       ↓
Segmento C
entrada = SUPERIOR
saída = NORMAL
```

A plataforma pode então terminar de maneira planejada.

Essa lógica também permite que existam objetos na parte superior:

```text
┌─────────────────────────┐
│      ❤️     🧟          │
│─────────────────────────│
│                         │
│            🥷           │
│_________________________│
```

Ou seja, o caminho superior não é apenas uma plataforma visual: ele pode possuir seus próprios obstáculos, inimigos, itens e desafios.

---

# 9. Algoritmo de Busca

**Requisito do professor: ALGORITMO DE BUSCA.**

A matriz contém apenas códigos numéricos.

O jogo precisa descobrir o significado de cada código.

Para isso será utilizado um algoritmo de busca.

Por exemplo:

```text
Matriz:

0 0 0 4 0
1 1 1 1 1
```

O programa encontra o código:

```text
4
```

e procura esse código no catálogo de entidades:

```text
1 → CHÃO
2 → ESPINHO
3 → PAREDE
4 → INIMIGO
5 → ITEM
6 → PLATAFORMA
```

A busca encontra:

```text
4 → INIMIGO
```

Então o jogo sabe que deve criar um inimigo naquela posição.

Fluxo:

```text
Matriz
  ↓
Código
  ↓
Algoritmo de Busca
  ↓
Entidade correspondente
  ↓
Comportamento
  ↓
Objeto no cenário
```

Por exemplo:

```text
3 → PAREDE → exige WALL JUMP
2 → ESPINHO → exige PULO
4 → INIMIGO → permite ESPADA/JUTSU
5 → ITEM → coletável
```

A busca transforma a representação abstrata da matriz em elementos concretos do jogo.

---

# 10. Generic `<T>` e Object Pool

**Requisito do professor: GENERIC.**

O Generic será utilizado na implementação de um **Object Pool**.

O Object Pool resolve um problema bastante comum em jogos: objetos são constantemente criados e removidos.

Sem pool:

```text
new Inimigo()
      ↓
inimigo aparece
      ↓
sai da tela
      ↓
objeto destruído
      ↓
new Inimigo()
      ↓
...
```

Com Object Pool:

```text
             OBJECT POOL
          ┌───────────────┐
          │ Inimigo livre │
          │ Inimigo livre │
          │ Obstáculo     │
          │ Item livre    │
          └───────┬───────┘
                  ↓
             reutilizar
                  ↓
              CENÁRIO
                  ↓
           saiu da tela
                  ↓
             desativar
                  ↓
             voltar ao pool
```

O mesmo objeto pode ser reutilizado posteriormente.

A implementação será genérica:

```java
ObjectPool<Inimigo>
ObjectPool<Obstaculo>
ObjectPool<Item>
```

Assim, uma única estrutura pode trabalhar com diferentes tipos.

O benefício é especialmente relevante em um endless runner, pois existe uma quantidade constante de objetos entrando e saindo da área visível.

O objeto não precisa ser necessariamente destruído quando sai da tela. Ele pode ser desativado e devolvido ao pool para ser utilizado novamente.

---

# 11. Sistema de vida

O jogador começará com **3 vidas**:

```text
❤️ ❤️ ❤️
```

Ao sofrer dano:

```text
❤️ ❤️ 💔
```

Se perder outra:

```text
❤️ 💔 💔
```

E ao perder a última:

```text
💔 💔 💔
```

a partida termina.

A vida máxima será de 3.

Durante o percurso, o jogador poderá encontrar itens de recuperação:

```text
❤️
```

Ao coletar:

```text
❤️ ❤️ 💔
       ↓
     coletar
       ↓
❤️ ❤️ ❤️
```

Caso o jogador já possua 3 vidas, o item não poderá ultrapassar o limite máximo.

---

# 12. Sistema de itens

Os itens serão encontrados espalhados pelo cenário.

Eles não precisam necessariamente representar poderes completos.

Podem ser recursos simples ou temporários.

Exemplos:

```text
VIDA
ESCUDO
RECUPERAÇÃO
JUTSU
BÔNUS
```

### Vida

Recupera uma vida.

### Escudo

Protege o jogador contra um dano ou durante determinado período.

### Jutsu

Pode fornecer uma utilização ou recurso relacionado ao jutsu.

### Outros itens

Podem fornecer efeitos temporários, vantagens ou habilidades específicas.

Os itens também podem funcionar como recompensa por escolher caminhos mais difíceis.

Por exemplo:

```text
CAMINHO SUPERIOR
→ maior risco
→ inimigos
→ item raro

CAMINHO INFERIOR
→ menor risco
→ menor recompensa
```

---

# 13. Pilha — Inventário Tático

**Requisito do professor: PILHA OU FILA.**

Será utilizada uma **Pilha** para armazenar os itens consumíveis encontrados durante a partida.

O jogador poderá carregar no máximo 3 itens.

Exemplo:

```text
┌─────────────┐
│   ESCUDO    │ ← topo
├─────────────┤
│    VIDA     │
├─────────────┤
│    JUTSU    │
└─────────────┘
```

A estrutura seguirá o princípio **LIFO — Last In, First Out**.

Ou seja:

> O último item coletado será o primeiro item utilizado.

Se o jogador coletar:

```text
VIDA
```

depois:

```text
ESCUDO
```

e depois:

```text
JUTSU
```

a pilha será:

```text
JUTSU ← topo
ESCUDO
VIDA
```

Ao utilizar o JUTSU:

```java
pop()
```

o topo será removido:

```text
ESCUDO ← topo
VIDA
```

Essa é uma aplicação real da Pilha porque o jogador **não pode simplesmente acessar qualquer item interno da estrutura**. Ele precisa retirar os itens a partir do topo.

---

# 14. Jutsu e cooldown

O jutsu possuirá um sistema próprio de cooldown.

Exemplo:

```text
JUTSU DISPONÍVEL
       ↓
     usar
       ↓
  cooldown = 5s
       ↓
JUTSU INDISPONÍVEL
       ↓
4s
       ↓
3s
       ↓
2s
       ↓
1s
       ↓
JUTSU DISPONÍVEL
```

A Pilha e o cooldown possuem responsabilidades diferentes.

A Pilha determina:

> **Quais recursos o jogador possui.**

O cooldown determina:

> **Quando o jutsu pode ser utilizado novamente.**

Isso permite que o jogador precise administrar tanto os itens coletados quanto o tempo de reutilização de sua habilidade.

---

# 15. Sistema de combate

O combate não será dividido em uma "fase de combate".

O jogador estará sempre correndo.

Os inimigos simplesmente aparecerão no decorrer do mapa.

Por exemplo:

```text
CORRIDA
   ↓
obstáculo
   ↓
inimigo
   ↓
plataforma
   ↓
outro inimigo
   ↓
espinho
   ↓
wall jump
```

O jogador reage conforme o elemento:

```text
INIMIGO
→ espada ou jutsu

ESPINHO
→ pular

PAREDE
→ wall jump

OBSTÁCULO
→ pular ou atacar
```

Isso mantém a identidade de endless runner.

---

# 16. Wall Jump e movimentação vertical

O wall jump será uma das principais mecânicas responsáveis por diferenciar o projeto de um endless runner puramente horizontal.

Determinados segmentos terão paredes verticais:

```text
      █
      █
🥷 →  █
      █
```

O jogador deverá utilizar wall jump para atravessar determinadas áreas.

Também poderão existir sequências:

```text
PAREDE
   ↓
WALL JUMP
   ↓
PAREDE
   ↓
WALL JUMP
   ↓
SAÍDA
```

O gerador procedural deverá controlar essas sequências para evitar situações impossíveis ou excessivamente repetitivas.

---

# 17. Parte visual do ninja

O ninja terá uma separação entre a parte inferior e superior do sprite.

A parte inferior permanecerá associada à animação de corrida.

A parte superior mudará de acordo com a ação executada.

Por exemplo:

```text
      PARTE SUPERIOR
       ataque/jutsu
            │
            ↓
       ┌─────────┐
       │   🥷    │
       └─────────┘
            │
            ↓
       PARTE INFERIOR
       animação corrida
```

Assim, mesmo quando o jogador estiver atacando, pulando ou utilizando jutsu, a parte inferior continuará representando a corrida.

As ações possíveis poderão incluir:

- Corrida;
- Ataque de espada;
- Jutsu;
- Pulo;
- Wall jump;
- Outras ações futuras.

---

# 18. Ordenação e balanceamento

**Requisito do professor: ALGORITMO DE ORDENAÇÃO.**

A ordenação será utilizada principalmente para auxiliar o **balanceamento da geração procedural**.

Cada segmento possuirá um peso de dificuldade.

Por exemplo:

```text
NORMAL          → 1
OBSTÁCULO       → 2
INIMIGO         → 2
SALTO           → 3
WALL_JUMP       → 4
COMBINAÇÃO      → 5
```

O banco poderá ser organizado:

```text
1
1
2
2
2
3
3
4
4
5
```

Isso permite ao gerador trabalhar com faixas de dificuldade.

No início:

```text
dificuldade disponível: 1–2
```

Depois:

```text
dificuldade disponível: 2–3
```

Mais tarde:

```text
dificuldade disponível: 3–4
```

E em níveis avançados:

```text
dificuldade disponível: 4–5
```

A ordenação, porém, **não é a responsável sozinha pelo balanceamento**.

Ela organiza o banco por dificuldade.

O gerador utiliza essa informação juntamente com suas regras de compatibilidade.

Por exemplo:

```text
SEGMENTO MUITO DIFÍCIL
        ↓
próximo segmento
        ↓
preferencialmente menos difícil
```

Isso evita sequências como:

```text
5 → 5 → 5 → 5 → 5
```

e permite criar uma curva de dificuldade mais justa:

```text
1 → 2 → 2 → 3 → 2 → 4 → 3 → 5 → 2 → 4
```

O algoritmo de ordenação também poderá ser utilizado para organizar o ranking final.

---

# 19. Aumento progressivo de velocidade

A velocidade do jogo aumentará conforme o jogador sobreviver.

Exemplo:

```text
0s
velocidade = 1.0

30s
velocidade = 1.2

60s
velocidade = 1.5

120s
velocidade = 2.0
```

Os valores são apenas exemplos.

O importante é estabelecer uma progressão:

```text
Tempo
  ↓
Velocidade
  ↓
Dificuldade
  ↓
Segmentos disponíveis
```

Quanto mais tempo o jogador sobreviver, maior será o desafio.

---

# 20. Arquivos e persistência

**Requisito do professor: ARQUIVOS.**

Os arquivos serão utilizados para preservar informações entre diferentes execuções do jogo.

Um arquivo de salvamento poderá conter:

```text
tutorialVisto=true
melhorDistancia=5340
melhorPontuacao=17320
skin=2
```

Na primeira execução:

```text
arquivo não possui tutorialVisto
        ↓
mostrar tutorial
        ↓
jogador conclui tutorial
        ↓
salvar tutorialVisto=true
```

Na próxima execução:

```text
ler arquivo
     ↓
tutorialVisto=true
     ↓
não mostrar tutorial novamente
```

Também poderão ser armazenados:

- Melhor distância;
- Melhor pontuação;
- Skin selecionada/desbloqueada;
- Configurações;
- Outros dados de progresso.

---

# 21. Ranking e Arquivos

Também poderá existir um arquivo específico para o ranking.

Por exemplo:

```text
João;15000;5200
Maria;13500;4800
Carlos;11200;3900
```

Cada registro representa:

```text
Nome
Pontuação
Distância
```

Ao finalizar uma partida:

```text
Resultado atual
      ↓
Ler ranking
      ↓
Adicionar novo resultado
      ↓
Ordenar ranking
      ↓
Salvar arquivo
```

Assim, **Arquivos e Ordenação também trabalham juntos**.

---

# 22. Fluxo completo do jogo

O funcionamento geral será:

```text
                       INÍCIO
                          ↓
                   Ler arquivos
                          ↓
                 Carregar progresso
                          ↓
                    Tutorial?
                   /         \
                 SIM          NÃO
                  ↓            ↓
             Mostrar        continuar
             tutorial
                  ↓
             salvar estado
                          ↓
                   Iniciar partida
                          ↓
                       3 VIDAS
                          ↓
              Gerar primeiros segmentos
                          ↓
                 GERADOR PROCEDURAL
                          ↓
              verificar compatibilidade
                          ↓
               verificar dificuldade
                          ↓
              escolher segmento válido
                          ↓
                adicionar à LISTA
                          ↓
                   obter MATRIZ
                          ↓
                algoritmo de BUSCA
                          ↓
              interpretar os códigos
                          ↓
              obter objetos do POOL
                          ↓
                      CENÁRIO
                          ↓
                   jogador corre
                          ↓
          ┌───────────────┼───────────────┐
          ↓               ↓               ↓
       INIMIGO         OBSTÁCULO         ITEM
          ↓               ↓               ↓
     espada/jutsu       pulo             PILHA
                                           ↓
                                        máximo 3
          │
          ↓
     jogador avança
          ↓
     gerar segmento
          ↓
  verificar continuidade
          ↓
  verificar dificuldade
          ↓
         ...
          ↓
   perdeu todas as vidas?
       /           \
     NÃO            SIM
      ↓              ↓
 continuar        GAME OVER
                     ↓
              salvar resultado
                     ↓
              atualizar ranking
```

---

# 23. Mapeamento final dos requisitos do professor

Para deixar explícito o atendimento à atividade:

### REQUISITO 1 — VETOR

**Onde:** Banco de segmentos.

```java
Segmento[] bancoDeSegmentos;
```

**Objetivo:** armazenar os modelos disponíveis para geração procedural.

---

### REQUISITO 2 — MATRIZ

**Onde:** Dentro de cada segmento.

```java
int[][] matriz;
```

**Objetivo:** representar espacialmente os elementos do cenário.

---

### REQUISITO 3 — GENERIC

**Onde:** Object Pool.

```java
ObjectPool<T>
```

**Objetivo:** permitir que o mesmo sistema reutilize diferentes tipos de objetos.

---

### REQUISITO 4 — PILHA

**Onde:** Inventário de itens.

```text
JUTSU
ESCUDO
VIDA
```

**Objetivo:** implementar o comportamento LIFO dos itens consumíveis.

---

### REQUISITO 5 — LISTA ENCADEADA

**Onde:** Sequência de segmentos do mapa.

```text
A → B → C → D
```

**Objetivo:** manter dinamicamente a sequência do cenário e suas conexões.

---

### REQUISITO 6 — BUSCA

**Onde:** Interpretação dos códigos da matriz.

```text
4 → busca → INIMIGO
```

**Objetivo:** transformar códigos numéricos em entidades do jogo.

---

### REQUISITO 7 — ORDENAÇÃO

**Onde:** Banco de segmentos e ranking.

```text
dificuldade:
1 → 2 → 3 → 4 → 5
```

**Objetivo:** auxiliar o balanceamento da geração procedural e organizar os resultados.

---

### REQUISITO 8 — ARQUIVOS

**Onde:** Save e ranking.

```text
save.dat
ranking.dat
```

**Objetivo:** preservar dados entre execuções.

---

# 24. Arquitetura geral

A arquitetura completa pode ser resumida desta forma:

```text
                 BANCO DE SEGMENTOS
                         │
                       VETOR
                         │
                    ORDENAÇÃO
                 por dificuldade
                         │
                         ↓
               GERADOR PROCEDURAL
                         │
              ┌──────────┴──────────┐
              │                     │
      regras de dificuldade    regras de encaixe
              │                     │
              └──────────┬──────────┘
                         ↓
                 LISTA ENCADEADA
                         │
           ┌─────────────┼─────────────┐
           ↓             ↓             ↓
       Segmento A    Segmento B    Segmento C
           │             │             │
        Matriz A      Matriz B      Matriz C
           │
           ↓
         BUSCA
           │
           ↓
    código → entidade
           │
           ↓
     objeto do jogo
           │
           ↓
       OBJECT POOL
           │
           ↓
    reutilização de objetos
```

No lado do jogador:

```text
                    JOGADOR
                       │
          ┌────────────┼────────────┐
          ↓            ↓            ↓
        VIDAS        PILHA        JUTSU
          │         DE ITENS      COOLDOWN
          │            │
          │        máximo 3
          │            │
          ↓            ↓
       3 vidas        LIFO
```

E no sistema de persistência:

```text
                    ARQUIVOS
                   /        \
                 SAVE      RANKING
                  │           │
             progresso    pontuação
             tutorial     distância
             skins
```

---

# 25. Objetivo acadêmico do projeto

O projeto não tem como objetivo simplesmente inserir estruturas de dados no código para cumprir uma lista de requisitos.

A proposta é fazer com que cada estrutura resolva um problema real do jogo.

O **Vetor** organiza o banco de segmentos.

A **Matriz** representa espacialmente esses segmentos.

A **Lista Encadeada** mantém a sequência dinâmica do mapa e suas conexões.

O **Generic** permite criar um Object Pool reutilizável para diferentes tipos de objetos.

A **Pilha** controla os itens consumíveis do jogador através do princípio LIFO.

O **Algoritmo de Busca** transforma códigos da matriz em elementos concretos do jogo.

O **Algoritmo de Ordenação** organiza os segmentos por dificuldade e auxilia o balanceamento da geração procedural, além de poder ordenar o ranking.

Os **Arquivos** permitem que informações importantes sejam preservadas entre diferentes execuções.

Dessa maneira, os requisitos da disciplina não são componentes isolados. Eles formam uma arquitetura integrada:

```text
VETOR + MATRIZ
       ↓
GERAÇÃO PROCEDURAL
       ↓
LISTA ENCADEADA
       ↓
BUSCA
       ↓
OBJECT POOL / GENERICS
       ↓
CENÁRIO FUNCIONAL

PILHA
  ↓
ITENS DO JOGADOR

ORDENAÇÃO
  ↓
BALANCEAMENTO + RANKING

ARQUIVOS
  ↓
PERSISTÊNCIA
```

O resultado esperado é um jogo em que o jogador percebe apenas uma experiência contínua de corrida, enquanto internamente diversas estruturas de dados e algoritmos trabalham em conjunto para construir, interpretar, balancear e manter o mundo do jogo.

---

# 26. Resumo da experiência do jogador

Do ponto de vista do jogador:

```text
🥷 → → → → →
```

O jogador corre continuamente e reage aos elementos:

```text
       🧟
        ↓
     ⚔️ atacar


      🌵
       ↓
      pular


      █
      █
       ↓
   wall jump


      ❤️
       ↓
    coletar
```

Em determinados momentos:

```text
          CAMINHO SUPERIOR
       ─────────────────────
              ↑
            🥷
              │
══════════════════════════════
          CAMINHO INFERIOR
```

O jogador decide qual caminho seguir, podendo encontrar diferentes riscos e recompensas.

Enquanto isso, internamente:

```text
Gerar segmento
      ↓
verificar compatibilidade
      ↓
verificar dificuldade
      ↓
selecionar matriz
      ↓
buscar códigos
      ↓
obter objetos do pool
      ↓
colocar objetos no cenário
      ↓
controlar vidas
      ↓
controlar itens
      ↓
controlar cooldown
      ↓
controlar rotas
      ↓
gerar próximo segmento
```

Essa é a essência do projeto:

> **Um endless runner 2D de ninja desenvolvido em Java, com geração procedural controlada, movimentação vertical, múltiplas ações, combate integrado à corrida, sistema de 3 vidas, itens, jutsu com cooldown, caminhos alternativos e uma arquitetura que utiliza de forma prática e integrada os requisitos de Vetor, Matriz, Generic, Pilha, Lista Encadeada, Busca, Ordenação e Arquivos solicitados pelo professor.**
