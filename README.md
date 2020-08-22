[![Work in Repl.it](https://classroom.github.com/assets/work-in-replit-14baed9a392b3a25080506f3b7b6d57f295ec2978f6f33ec97e36a161684cbe9.svg)](https://classroom.github.com/online_ide?assignment_repo_id=2973296&assignment_repo_type=AssignmentRepo)

# Chat Multicast

O projeto é composto de dois módulos, cliente e servidor.

O cliente pode solicitar ao servidor por meio de uma conexão UDP para entrar na sala de bate-papo e o servidor intermedia a comunicação entre os clientes.

Ao entrar na sala, o servidor informa um endereço multicast e o cliente cria um socket com o endereço infomado pelo servidor e começa a receber mensagens por meio desse socket.
Mensagens enviadas ao servidor são processadas e redirecionadas ao multicast.

O cliente também pode enviar comandos ao servidor e receber as informações exclusivamente para ele.

## Execução do programa

Compilação:

```sh
mkdir bin
javac ./src/**/*.java -d ./bin
```

Executar em modo servidor:

```sh
java -cp ./bin chatmulticast.cli.Main serve <numero da porta>
```

Executar em modo cliente:

```sh
java -cp ./bin chatmulticast.cli.Main connect <ip do servidor> <porta do servidor>
```

## Comandos do chat:

* `\list`: Mostrar os usuários conectados.

* `\disconnect`: Desconectar e fechar o programa.


## Protocolo de comunicação

**Requisições**:

`CONNECT <nome do usuario>`: Cadastrar um usuário no chat. 
O corpo da resposta será o ip do grupo multicast que o cliente deve se conectar para receber as mensagens gerenciadas pelo servidor (enviadas pelo o método MESSAGE).

`DISCONNECT`: Remove o usuário do chat. Mensagens enviadas por este usuário não serão mais enviadas para o grupo e será repondido com NOK.

`LIST`: Obter a lista de usuários conectados no grupo. O corpo da mensagem de resposta será a lista com o nome dos usuários separados por uma quebra de linha.

`MESSAGE <conteudo da mensagem>` Envia uma mensagem para todos os usuários conectados no grupo multicast.


**Respostas**:

`OK [corpo da mensagem]`: O servidor confirma o processamento da requisição solicitada, e, opcionalmente responde com uma mensagem informativa.

`NOK [corpo da mensagem]`: O servidor informa que a requisição solicitada não foi processada com êxito, e, opcionalmente responde com uma mensagem informativa.


