# Trabalho 1 - API Harry Potter

Aplicativo Android feito em Kotlin para consumir a HP-API em tempo real. O projeto foi desenvolvido para a disciplina de Desenvolvimento Mobile, usando corrotinas, Retrofit, RecyclerView e tratamento básico de erros nas consultas.

## Tecnologias usadas

- Kotlin
- Android nativo com XML
- Retrofit para consumir os endpoints da API
- Gson Converter para transformar JSON em objetos Kotlin
- Corrotinas com `lifecycleScope`, `Dispatchers.Main` e `Dispatchers.IO`
- RecyclerView para listas
- Picasso para carregar imagens da internet
- ProgressBar para indicar carregamento
- Toast para mensagens de validação e erro

## API

A API usada no projeto é:

`https://hp-api.onrender.com/api/`

Endpoints usados:

- `character/{id}`: busca um personagem pelo ID
- `characters/staff`: lista os funcionários/professores de Hogwarts
- `characters/house/{casa}`: lista personagens de uma casa
- `spells`: lista os feitiços

## Estrutura principal

O código está dividido em pacotes simples:

- `api`: interface do Retrofit com os endpoints
- `model`: classes de dados que representam o retorno da API
- `controller`: telas do aplicativo
- `adapter`: adapters usados nas listas com RecyclerView

As classes de modelo usam `@SerializedName` porque a API retorna campos em inglês, mas o código do projeto mantém os nomes em português.

## Tela principal

Arquivo principal: `MainActivity.kt`

Essa é a tela inicial do app. Ela mostra os botões pedidos no trabalho:

- Listar personagem por ID
- Listar professor da escola
- Listar estudantes de uma casa
- Ver feitiços
- Sair

Cada botão abre uma tela específica usando `Intent`. O botão `Sair` chama `finish()` para fechar o aplicativo.

Pontos importantes para explicar:

- É uma tela de navegação.
- Não faz chamada para a API.
- Só direciona o usuário para a funcionalidade escolhida.

## Listar personagem por ID

Arquivo principal: `PersonagemActivity.kt`

Nessa tela o usuário digita o ID de um personagem e toca em `Buscar`. A busca usa o endpoint `character/{id}`.

O app exibe:

- Nome
- Espécie
- Casa
- Foto do personagem

O que foi usado:

- `EditText` para o usuário digitar o ID
- Validação para impedir busca com campo vazio
- `ProgressBar` durante a consulta
- Retrofit para chamar a API
- Corrotina para não travar a tela
- `withContext(Dispatchers.IO)` para fazer a chamada de rede fora da thread principal
- Picasso para carregar a imagem
- Imagem alternativa quando o personagem não tem foto
- `Toast` quando o personagem não é encontrado ou quando ocorre erro de conexão

Fluxo da tela:

1. O usuário digita o ID.
2. O app valida se o campo foi preenchido.
3. A ProgressBar aparece.
4. A chamada é feita na API.
5. Os dados retornados são mostrados na tela.
6. A ProgressBar é escondida.

## Listar professor da escola

Arquivo principal: `ProfessorActivity.kt`

Nessa tela o usuário informa o nome de um professor. O app consulta o endpoint `characters/staff` e procura o professor pelo nome ou pelos nomes alternativos.

O app exibe:

- Nome
- Nomes alternativos
- Espécie
- Casa

O que foi usado:

- `EditText` para digitar o nome
- Validação de campo obrigatório
- Retrofit para buscar a lista de professores
- Corrotinas para fazer a consulta sem travar a interface
- `ProgressBar` enquanto a requisição está acontecendo
- `Toast` para professor não encontrado ou falha na consulta

Ponto importante:

A tela não mostra imagem porque o requisito do trabalho para professor pede apenas `name`, `alternate_names`, `species` e `house`.

## Listar estudantes de uma casa

Arquivo principal: `EstudantesCasaActivity.kt`

Nessa tela o usuário escolhe uma casa usando RadioButtons e toca em `Listar`. A busca usa o endpoint `characters/house/{casa}`.

Casas disponíveis:

- Gryffindor
- Slytherin
- Ravenclaw
- Hufflepuff

O app exibe a lista de estudantes em um RecyclerView com:

- Foto pequena
- Nome do estudante
- Casa do estudante

O que foi usado:

- `RadioGroup` e `RadioButton` para escolher a casa
- Validação para garantir que uma casa foi escolhida
- Retrofit para buscar personagens da casa
- Filtro para manter apenas personagens marcados como estudantes
- RecyclerView para mostrar a lista
- Adapter próprio em `EstudantesAdapter.kt`
- Picasso para carregar as fotos
- Imagem alternativa quando não há foto disponível
- ProgressBar durante a busca
- Toast em caso de erro ou lista vazia

Ponto importante:

Não foi necessário implementar paginação. A lista completa é carregada pela API e o RecyclerView cuida da reciclagem visual dos itens na tela.

## Ver feitiços

Arquivo principal: `FeiticosActivity.kt`

Essa tela lista os feitiços retornados pelo endpoint `spells`.

O app mostra os feitiços em RecyclerView com:

- Nome
- Descrição curta

O que foi usado:

- Retrofit para buscar todos os feitiços
- Corrotina para fazer a chamada em segundo plano
- ProgressBar enquanto carrega
- RecyclerView para exibir a lista
- Adapter próprio em `FeiticosAdapter.kt`
- Clique no item para abrir a tela de detalhes
- Toast para erro de consulta ou lista vazia

Ponto importante:

A chamada acontece quando o usuário entra na tela de feitiços pelo botão da tela principal.

## Detalhe do feitiço

Arquivo principal: `DetalheFeiticoActivity.kt`

Essa tela abre quando o usuário toca em um feitiço da lista.

Ela exibe:

- Nome do feitiço
- Descrição completa

O que foi usado:

- `Intent` para enviar o nome e a descrição da tela de lista para a tela de detalhe
- `TextView` para exibir os dados recebidos
- Botão `Voltar` para retornar à lista

## Tratamento de carregamento e erros

As telas que consultam a API seguem o mesmo padrão:

1. Validam os dados informados pelo usuário.
2. Mostram a `ProgressBar`.
3. Executam a chamada com Retrofit dentro de uma corrotina.
4. Usam `Dispatchers.IO` para a requisição de rede.
5. Atualizam a tela quando a resposta chega.
6. Escondem a `ProgressBar`.
7. Mostram `Toast` quando acontece algum erro.

Esse padrão aparece nas telas de personagem, professor, estudantes e feitiços.

## Como rodar

1. Abrir o projeto no Android Studio.
2. Esperar o Gradle sincronizar.
3. Escolher um emulador ou celular Android.
4. Rodar o app pela configuração `app`.

O projeto usa `minSdk 29`, conforme pedido no trabalho.

## Observações para apresentação

- Os dados vêm diretamente da HP-API, então algumas descrições aparecem em inglês porque a própria API retorna assim.
- As chamadas são feitas no momento da ação do usuário.
- O app não usa banco de dados local.
- O app não guarda dados em cache.
- O foco do projeto é demonstrar consumo de API, corrotinas, tratamento de erro e exibição de dados em tela.
