# Projeto Integrador Time 19 Puccampinas 2024
##### Membros: Breno, Rafael, Gabriel, Guilherme

# Contas de teste:
## CONTA CLIENTE
EMAIL = linguiça@gmail.com
SENHA: 123456
## CONTA GERENTE
EMAIL = metalifecrypto@gmail.com
SENHA: 123456

# Descrição do projeto (OBS: pule para tópico 5 caso já tenha lido)
1. ## Apresentação do problema
Na atualidade, turistas costumam levar seus pertences e equipamentos eletrônicos à praia,
shows, eventos como festas lotadas e muitas outras situações. Mas, quando decidem
aproveitar o momento e se desligar da preocupação, não conseguem, pois os itens pessoais
estão a todo momento precisando de cuidados. Deixar com alguém nem sempre é uma
opção. Além do incômodo de carregar as bugigangas, há a preocupação com assaltos ou arrastões
como acontecem nas praias e, nessas situações, muita gente sai no prejuízo com o furto de
itens de valor. Baseado neste cenário, o objetivo deste projeto integrador é construir uma solução de
locação de armários para que em locais de entretenimento, pessoas possam guardar seus
itens pessoais com segurança.
2. ## Solução proposta
A proposta para resolver os problemas citados anteriormente é uma solução tecnológica
composta de: armário automatizado e um aplicativo Android que será usado pelo cliente
final (aquele que deseja guardar itens) e pelo gerente de uma unidade locadora de armários.
3. ## Requisitos Funcionais (RF) – Escopo
   Interface do Cliente Final (que usufruirá do serviço)
   #### RF1 – Cliente baixa o app e faz o cadastro (criar conta)
   O cliente fará o download do aplicativo pela Google Play e em seguida, criará uma conta de
   acesso. É importante destacar que o nome do aplicativo, logomarca / ícone e cores, caberá
   à sua equipe de projeto definir em parceria com as atividades da disciplina de experiência
   com o usuário. São informações do cadastro do cliente: Nome Completo, CPF, Data de Nascimento, Telefone
   celular e Senha. Esses dados deverão ser guardados na nuvem (Google Cloud Platform GCP
   – Firebase Firestore e Firebase Authentication). A conta deverá ser confirmada por email ou
   SMS (o próprio serviço do Google tem isso pronto). Ao fazer o login pela primeira vez, o aplicativo permanece com a sessão da conta ativa para
   sempre. Mas, caso ele opte por “sair” deverá entrar novamente.
   #### RF2 – Cliente saiu do aplicativo
   Caso o cliente escolha a opção sair no app, o sistema apresentará a tela de login novamente
   (que é a tela de abertura do app quando o cliente acessa pela primeira vez) com as opções
   de Login, Criar conta e recuperar senha.
   #### RF3 – Cliente esqueceu a senha de acesso (Recuperar senha)
   Pode ser que o cliente “saia” do app sem querer e não lembre da senha para fazer retornar.
   Neste caso, é importante ter um recurso de “esqueci minha senha” que ao informar o email,
   o sistema envie uma mensagem de troca de senha para o email dele (também existe isso no
   Firebase Authentication).
   #### RF4 – Cliente habilita cartão de crédito para locação do armário
   Na página inicial do aplicativo, o cliente deverá cadastrar um cartão de crédito digital ou não,
   para poder habilitar o serviço de locação. Informar ao cliente, que todas as vezes que ele
   realizar uma locação, será cobrada a caução de uma diária e, estornada proporcionalmente
   de acordo com o período utilizado.
   #### RF5 – Cliente consulta locais onde existem armários para locação (com Geolocalização)
   Independentemente de ter cadastro ou não, ao baixar o aplicativo, o cliente poderá sempre
   consultar onde existem armários para locação. A tela deverá apresentar um mapa no
   formato Google Maps com os pins que representam os pontos de locação (filiais). Ao clicar
   sobre um “pin” o sistema deve apresentar referências onde o armário está, por exemplo: Em
   frente ao quiosque da Brahma ou nas dependências do hotel abc e o botão de ir até lá (traçar
   a rota).
   #### RF6 – Locação do Armário
   Chegando no espaço de locação, o cliente abre o app e escolhe a opção alugar armário. Nesse
   momento, o sistema verifica a geolocalização, para saber se o cliente está mesmo próximo
   daquele espaço. Em caso positivo, apresenta as opções de tempo para locação - Por
   exemplo: 30min = R$ 30,00, 1h = R$ 50,00, 2h = R$ 100,00, 4h = R$ 150,00 ou do momento
   até as 18h = R$ 300,00 – só deixar marcar essa opção se o cliente acessar o app entre 7h e
   8h da manhã (Atenção: o preço a ser carregado deve estar no Firebase Firestore, associado
   ao estabelecimento – não é padrão) O usuário escolhe qual lhe convém e prossegue,
   selecionando “Confirmar locação”. Neste momento final, o sistema apresenta um QRCODE
   e uma mensagem: Apresente este código ao gerente (nome do gerente do armário daquela
   localidade). Caso o cliente feche se querer o app ou aconteça uma interrupção como ligação
   etc., ao retornar para o app, mostrar em destaque que existe uma locação para ser efetivada.
   Ao efetivar a locação no espaço, junto ao gerente, será cobrado no cartão de crédito como
   caução o valor da diária. Caso o cliente não extrapole o horário, será estornado o valor com
   a diferença do período da locação.
   Interface do Gerente de Locação de Armários
   #### RF7 – Login do gerente
   O gerente utilizará o mesmo aplicativo que o cliente final, não existirão duas versões de app.
   Apenas uma. Quando o sistema identificar pelo login que aquela conta é de um gerente,
   deverá ser capaz de direcionar o gerente para acessar a interface com as opções dos
   requisitos descritos a seguir.
   #### RF8 – Liberar Locação
   Após a geração do QRCODE, descrito no processo do RF6, o gerente acessará a opção
   “Liberar locação” em seu smartphone e imediatamente, deverá escanear o QRCODE que o
   cliente mostrará; O sistema verificou que o QRCODE é válido e que existe(m) armário(s)
   disponível(eis) e apresenta uma tela perguntando: Quantas pessoas acessarão este armário?
   E duas opções A) uma pessoa B) duas pessoas. Se o gerente escolher apenas uma pessoa, o
   aplicativo do gerente, solicitará que ele fotografe essa pessoa e, em seguida o gerente
   escolherá uma pulseira NFC liberada e fará o vínculo pelo app. Se for duas pessoas,
   acontecerá o mesmo processo: foto e liberação de uma pulseira de acesso para cada pessoa.
   Ao clicar em finalizar, o sistema mostrará na interface qual armário foi alugado para este
   cliente e a porta deste armário, abrirá automaticamente. (Em seguida o cliente guardará as
   coisas dentro do armário e fechará a porta – processo manual).
   #### RF9 – Cliente retorna para acessar o armário
   O cliente final com a sua pulseira NFC, procura o gerente daquele espaço e solicita acesso ao
   armário. O gerente posiciona a pulseira NFC próxima ao smartphone e clica na opção abrir
   armário. Nesse momento, o sistema carrega a foto da pessoa associada à pulseira, para que
   o gerente verifique se a foto do locador coincide com o solicitante; se sim, o gerente escolhe
   a opção PROSSEGUIR:
   O sistema apresenta ao gerente as opções: Abrir momentaneamente ou encerrar
   locação. O gerente pergunta se o cliente deseja encerrar a locação (exemplo o cliente
   vai embora).
   SIM: o gerente solicitará que o cliente retire a pulseira e entregue-a para ele e
   escolherá a opção Encerrar Locação, o sistema exigirá que a pulseira seja aproximada
   para poder limpar os dados de acesso em memória e o armário será aberto e liberado
   e a locação é definitivamente encerrada. Nesse momento o sistema armazena no
   banco de dados um pedido de estorno do caução, subtraindo o valor/tempo da
   locação.
   NÃO: o gerente escolhe a opção abrir momentaneamente e avisa o cliente para
   que acesse e FECHE manualmente o armário em seguida e a locação permanece ativa.
   RNF1 – Dados pré-carregados de gerentes, unidades e armários
   Não haverá interface de cadastro para unidades, gerentes e vinculação de armários nas
   unidades. Você e sua equipe tem a responsabilidade de preparar esses dados antes de
   começar a interface do gerente e descobrir o que é necessário para isso.
   Exemplo: Sua equipe poderá criar um documento no Firebase Firestore manualmente para
   gerentes, unidades e armários. Não faça interfaces no app para cadastrar essas informações.
4. ## Não escopo
   Não é escopo projetar como o armário deve funcionar. O hardware do armário, as
   comunicações dele com o resto do sistema não é de responsabilidade do seu time. Você
   deve partir da premissa que isso já está pronto;
   Não pense em como controlar se o cliente, após retirar os pertences, fechará corretamente
   ou não o armário vazio ou se esqueceu itens dentro – sua equipe não está desenvolvendo o
   hardware do armário;
   Não se preocupe com a questão de ter um gerente pessoa física na unidade. Sabemos que
   poderia ser feito tudo por reconhecimento facial automatizado. Porém, para este projeto,
   para o nível de complexidade das disciplinas desse semestre, não será solicitada essa
   funcionalidade. Portanto, não implemente. Se você ou sua equipe fizer isso não ganhará
   pontos extras. Lembrando do que está escrito no manual do projeto integrador: trabalhar a
   mais não fará seu cliente pagar pelo que não contratou e o que não foi combinado. Inclusive
   será descontado pontos caso o escopo funcional não seja integralmente cumprido.
   Não é escopo integrar o aplicativo com APIs de pagamento reais ou operadoras de cartão de
   crédito. Simule o cadastro e a caução cobrada no cartão de crédito.
   Não é escopo deste projeto controlar se na unidade que o cliente tem a intenção de fazer a
   locação, existe ou não disponibilidade de armários livres. É evidente que num cenário real, é
   necessário, porém para o projeto integrador não teremos a integração com armários reais.
   Atenção: A integração COM NFC pressupõe que seu smartphone de testes, tenha capacidade
   de LER E ESCREVER tags NFC. Portanto, use uma TAG NFC comum ou uma pulseira NFC para
   testes. Não use smartbands de marcas como Xiaomi pois o protocolo NFC não é ativado no
   Brasil. Então não é escopo usar NFC dos smartwatches ou smartbands. Seu time pode usar
   tags simples do tipo chaveiro ou adesivos como se fossem pulseiras. Cabe a sua equipe
   descobrir como funcionam as versões e tipos de NFC disponíveis no mercado.
   Apesar de sabermos que é possível implementar o aplicativo do cliente para que funcione
   de maneira multiplataforma (Android/iOS) com Flutter, definimos que não é escopo
   funcionar no iPhone. Somente em aparelhos Android. Não implemente nada em Flutter para
   o projeto integrador. Não dará tempo para fazer tudo numa situação ideal de produto.
   Entendemos que se uma pessoa perder a pulseira, de acordo com os requisitos especificados
   e limitados ao projeto, não existe forma de abrir o armário digitalmente e nem opção para
   o gerente abrir por meio do aplicativo sem a pulseira. Isso é um requisito de segurança para
   que o gerente, não tenha acesso aos pertences caso seja um ladrão.
5. ## Como Baixar?
##### Arquivo APK está no repositório '/app'
6. ##Git Workflow
##### Git Workflow é separado em branches individuais para cada integrante:
* master
* breno-updates
* rafael-updates
* gabriel-updates
* guilherme-updates
### Todas as branches são eventualmentes mergidas na master
7. ## Mapeamento de requisitos e suas classes
#### RF1 -> LoginActivity e CreateAccountActivity
#### RF2 -> AccountFragment
#### RF3 -> RecoveryPasswordActivity
#### RF4 -> PaymentFragment
#### RF5 -> MapsFragment
#### RF6 -> RentLockerActivity
#### RF7 -> TODO
#### RF8 -> TODO
#### RF9 -> TODO
8. ## Firebase Setup
### Collections
* user_data: É a substituição de "pessoas" que guarda os dados extras além de email e senha de um usuário como CPF, Número de telefone, etc.
* armarios: Coleção que armazena os dados de um armário e seu status.
* locacao: Coleção que liga um usuário à um armário.
* precos_hora: Armazena os preços fixos de cada hora disponível.
### Authentication
We are using a simple email and password authentication and adding the extra data to a firebase collection called user_data.
