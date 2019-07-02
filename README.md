# Projeto integrador III

![carro final](https://lh3.googleusercontent.com/LqoB6I_hYRhTCgJoQqegOzl3_oj7gsrVqQ5c9yX_i_Xo6xYGvLDxJsnojAWBJNjhxmzHD0BspGnEPaLkNhUkuf-XcLjGkCu5xpJwrxSQucMxPNFxM4Srg2RS2LRAV4o29il401KGduSMs51XkYtCGMXnTaaGRxnnhSsCVW6XOuGzkzcBZVruLBWwZCUnOP-29RjyosbSnP0yMVzpwi8JFfsi62fE4JLSlhi1bwFjSNZ7dnl0m-0cR0k-6nTqQJKW6LSHcenm64GIhWNn4-Uq78WMTc37R-z-R0q8UlM_zEGh4o-j_Pa4BfDYLLarCCNy3gHoF3rQOimbw_TxJydx5ukwH-DntfrE5e_nSltR6xrQOLRoesnQBqGU1WCrcoll3fU9buYY6XLcqI5ibuQTj-Cka71ccNh2hl3pNTGxrIoRQ6t-Tjs9Ooyqrp38w6M4EgNXUiwDjkttpdeRQhtKDDQCpPVcqwNN0yJ0Wkn-yLohpY0tWpTtRf5q8HEjIs81TEguVsGy0PcVpBn57RoicvlzY8XW4of2VdSLZj4FjdTlS02wUifk-bv8o-ZiQF0zlODx2_Q8170UNESlaFom_zm0k7eNTCuKspzPLWaJYYWfxwbx9QEjPWl6_a1wzMSXkil9XVIlGmABnskZ7MwDkwMSi-UUXM3O=w834-h625-no)

## Informações gerais
Dessenvolvido no escopo da disciplina de Projeto Integrador III, do curso de Eng. Eletrônica do IFSC campus Florianopolis no primeiro semestre de 2019, esse projeto tem como objetivo utilizar os conhecimentos obtidos ao decorrer do curso, implementando um carro controlado via aplicativo android.

## Tabela inicial de requisitos
* Controle via aplicativo android
* Comunicação via bluetooth
* Sensoreamento de velocidade e transmição para o app 
* Controle de velocidade via aplicativo 
* Controle da bateria

## Tecnologias
* Parte mecânica do carro, disponível em https://s.click.aliexpress.com/e/bD9t0zL3
* Celular android
* Microcontrolador STM32
* Bateria de íon de lítio
	
# Hardware

* ## Acionamento dos motores

Para acionar os motores foram levadas em conta os requisitos de projeto, que nesse quesito são:
* Controle de direção do movimento
* Controle de velocidade

Com relação ao controle de direção foi escolhido o sistema de ponte H, na qual é possivel alterar o sentido da corrente invertendo as tensões nos terminais L e R, a forma escolhida de acinamento dos motores foi conectar em paralelo os motores do mesmo lado do carro, necessitando de duas pontes para o controle dos quatro motores 

![Ponte H](https://i.ibb.co/JCv0MNN/ponte-h.png)

O primeiro filtro para escolha do circuito foi a disponibilidade de compontentes no almoxarifado, que para ponte H se limitava a:
* L293B
Corrente maxima 1A por canal

* L6203		
Corrente maxima 5A

* SN754410NE
Com uso restrito para manutenção

* Circuito discreto com BD140 e BD139
Corrente maxima 1,5A
 
### Escolha
Como nesse projeto vamos trabalhar com quatro motores, dois por canal, e sendo eles de tensão nominal de 3 - 6V e corrente nominal de 300mA todos os circuitos cumprem o requisito de potência mínima, O L6203 foi descartado por ser uma ponte H de potência, ficando superdimensionado e encarecendo o projeto, ficando a escolha entre o circuito discreto e o L2931B no quesito eficiência energética, afinal, se trata de um sistema embarcado, e por isso necessita de um cuidado maior com a eficiência
Montado os dois circuitos de acinamento alimentados em 5V foi obtido uma tensão de 3,08V com o integrado e 4,6V no circuito discreto, uma diferença de 35% no consumo do circuito, sendo escolhido o circuito discreto.  

Circuito final de alimentação dos motores, com queda de 0,4V de tensão na carga em relação a VCC e corrente de polarização externa de 640pA, não exigindo muita corrente do microcontrolador.
![Ponte H](https://i.ibb.co/ZXjB03W/ponte.png)

* ## Microcontrolador
Para controle do carro foi utilizado o stm32f103, na sua versão de desenvolvimento conhecido como "blue pill", que possui um cortex M3 com um clock de 72MHz e que conta com 20kb de RAM, quatro Timers, dois ADCs entre outros, que serão usados na aplicação.

![STM](https://wiki.stm32duino.com/images/thumb/d/db/STM32_Blue_Pill_perspective.jpg/300px-STM32_Blue_Pill_perspective.jpg)


* ## Comunicação
Como a comunicação entre celular e carro é feita via bluetooth foi escolhido o modulo HC-05, que trata todo o protocolo de comuncação e se comunica com microcontrolador via UART, sendo de facil uso e configuração.
![HC-05](https://i.ebayimg.com/images/g/U7MAAOSw6jJb868K/s-l300.jpg)

* ## Aferição de velocidade 
Para medir a velocidade foi escolhido um encoder com sensor óptico, que consiste em um diodo emissor de luz e um receptor, separados por uma roda perfurada, que quando a roda esta em movimento gera um sinal com frequência proporcional a velocidade da roda.

![encoder2](https://1.bp.blogspot.com/-OuZFfcUnO-M/VsUIDJKn2WI/AAAAAAAAEsA/ZPVqUi8w9GM/s1600/Disco-Encoder.png)
![encoder](https://cdn.awsli.com.br/600x450/154/154718/produto/24268110/4b2e443ab4.jpg)

* ## Baterias 
Para alimentar o carro foram escolhidas duas baterias de LIPO, de 3,7V e 1200mAh, conectadas em série, o que resulta em 7,4V de tensão. 

![Bateria](https://encrypted-tbn2.gstatic.com/shopping?q=tbn:ANd9GcSpjiAIQ2f9JGUJtZus21vVg8vtvALbAbm2rqdRHKAY2R2VNm6NoqiVKkhJ6pQQutPrGLsdANjfVmnbD21Rb0aYAq7dlIuCOGFzmBbqDxXh0q080XQtv5Aq&usqp=CAc)
* ## Regulador de tensão
Como o conjunto de baterias tem como tensão 7,4V na saida, é necessario baixar a tensão para niveis aceitaveis para o microcontrolador e modulo bluetooth, no caso foi usado um regulador ajustável Step-up-down, por questão de disponibilidade, mesmo esse sendo superdimensionado para a aplicação em questão, já que suporta correntes de até 5A e nesse caso não passa de 200mA.

![Regulador](https://encrypted-tbn2.gstatic.com/shopping?q=tbn:ANd9GcRjVENEgTrkiV1Vze6-s10tEys9r3vkaPzhg1qAIwBDORowJBwdG1PJLQN5fljmezylaK7oFhByX1NvVY5adCad2kIMTrO27W_udAmgpGz_5cjcbanmmh95&usqp=CAc)

* ## Medição da tensão das baterias
Como regra geral, não se pode descarregar uma bateria abaixo de uma certa tensão, com as usadas nesse projeto não é diferente, não sendo recomendado baixar a tensão da marca dos 3V, como o sistema possui duas baterias é necessario medir a tensão delas individualmente. O conversor analogico digital do microcontrolador tem como tensão base 3V, então é preciso baixar as tensões para menos de 3V, o circuito usado para isso é o diferencial com ampop, que pode dar um ganho menor que 1, sem usar tensão negativa. O Ampop escolhido foi o LM324, com ganho de 1/3 para a tensão superior (bateria 1 em serie com a bateria 2), e de 2/3 para a tensão media (bateria 1). 
![diferencial](http://www.c2o.pro.br/hackaguas/figuras/amplificador_operacional_diferencial.png)


* ## Placa de circuito
Com todos os requisitos de Hardware definidos foi desenvolvido o esquematico e placa do projeto
![esquematico](https://i.ibb.co/CJ0CqbJ/esquematico-completo.png)
![Placa 3d](https://i.ibb.co/C7m8LZ0/Placa-3d.png)

![carro visto de cima](https://lh3.googleusercontent.com/XHgXW_0hG--oXE6uksJy7TSglDY0m_3GjwJ7JpxUj3nDGJl_j5kw8t41qFqI3KVQebgnOixmsnlhJWuOOmGEBwOP9TRfeRpaNYIoX5CaxXfUMoYwmZtibPrEwG1qelVRuH2Ht5bJxe4XIjcndk-vlNCzfa9I7wqE4hAHR1dnG9Mfc3DZOrcGQJZJ0kBpwk9WV5Z9XiZtq6caUjpY_Q7VtCFR3JaJ_YBxzC-LkU3g2XBaja3BT3_fmBItKwr26wGlN6lZX_U7AkY6bs6LvmjEq5iOWTZNz4afp40fs5y9rJixaFzpfDbfBVEDrs0u2O9NK0jaZ4a9uIYgn0f3tjlfhU8jnNGRl1VR19TjuiiqR0hocCxPJ9riAJDEJIjA1NgjUYk4Xx2mlM_v-yGQa8YbQL12lg2yes7WE5hrMUiF4CzDNctjAm7OMXmLvNUfbgQy8PAeBzc5-pytBM97ovhWTkDPTlwB7liflztpgzAt_ofb0G7sJkzYhrtdks1Wjh2jRRSgqJkcDEL9xNpR5TW52FUpuHmS73J9A8qpl3OOT7A1xmlFHrEYquWsuQV0Y9VU__CJYoH0DgZiMnmrZ7rgj7_4ZiPkOW8a8d2uS4X-kfckcw_2nx95IqOZOPg0rupB53jOHk49uN-mZHuJSVyK8SPyz_P_3JVU=w469-h625-no)

A placa de circuito impresso foi projetada para que o circuito de comunicação bluetooth ficasse longe do principal foco do ruido do circuito, que são os motores, contudo ainda foi deixado o uC ao lado dos motores e das pontes H, uma implementação mais correta seria distanciar esses dois componentes geradores de ruido, juntamente com o regulador de tensão. e acoplar o sensor de velocidade de outra maneira, que não por cabos longos como foi projetado, essas e outras correções foram feitas em outra placa a ser utilizada em disciplinas ligadas a programação de microcontroladores.

O carro também foi objeto de estudo por uma dupla de colegas dentro da disciplina de compatibilidade eletromagnetica, estando todos dados disponiveis no link https://github.com/lsmanoel/CEM_

# Software do microcontrolador

* ## Controle de velocidade
O controle da velocidade vai ser feito por meio da tecnica de modulação PWM, via microcontrolador atuando sobre a ponte H.
Como o circuito de ponte H escolhido não pode em hipotese alguma ter o os dois transistores de atuação polarizados ao mesmo tempo, optou-se por ultilizar o PWM no modo normal e no modo inverso, de forma a construir um sistama redundante nesse sendido, se houver uma falha no software mesmo assim não havera um problema critico na ponte H.

![PWM](https://i.ibb.co/jfv3VWc/TEK0000.png)

A implementação foi feita via dois Timers, com frequência fixa em 1kHz, em ambos usando o canal 1 e o canal 1N, trabalhando sempre com pelo menos um deles desligado. Sabendo que a velocidade vai ser transmitida pelo aplicativo em 1Byte, com 256 possibilidades, de -128 a 127. Sendo assim o valor 127 corresponde a 100% da velocidade para frente, enquanto -128 corresponde a 100% da velocidade na ré.
Como os motores possuem uma intercia, não sera qualquer valor de tensão que dará partida no carro, sabendo disso pode-se dar um offset no valor do pwm, nesse caso escolhi cerca de 45% de razão ciclica, o que é suficiente para dar partida nos motores.

* ## Medição das baterias
A medição das baterias foi feita por um ADC, utilizando dois canais, um para a tensão total (bateria 1 em serie com a bateria 2) e um canal para a tensã da bateria 1, lembrando que estas tensões já receberam um ganho de 1/3 (tensão total) e 2/3 (tensão da bateria 1). então levando em cosideração esse ganho e os dois canais pode-se medir individualmente a tensão de cada bateria para que o sistema possa desligar os motores e avisar o usuário que a carga das baterias esta crítica.

* ## Medição da velocidade
A velocidade é calculada com base na variação de um sensor optico e como a roda perfurada possui 20 furos, para cada uma volta da roda são medidas 20 alternancias no sensor, com isso e o diametro da roda é possivel saber a distancia percorrida por pulso. Além da componente de distancia é necessario saber o tempo entre medições, nesse caso foi utilizada um tempo de 100ms.

![equacao](https://i.ibb.co/YcLZTSq/pi.png)
onde n é o numero de pulsos por periodo

Também foi implementado um [filtro media movel](http://borgescorporation.blogspot.com/2013/05/filtro-de-media-movel.html
) de 16 fatores afim de filtrar ruidos indesejados

* ## Comunicação
A comunicação entre o microcontrolador e o modulo bluetooth HC-05 é feita via UART que possui hardware dedicado na maioria dos microcontroladores, então a implementação consiste basicamente em criar um pacote de dados para envio de todos os parametros juntos no formato "00;00;00;00". O tratamento de dados recebidos pelo uC é feito por interrupção, reconhecendo o final do dado, que é "\r\n" e separando os demais bits, que são as cordenadas Y e Z.



# Controle de Direção e sentido
Tanto o controle de direção quanto o de sentido serão feitas via aplicação em Android e transmitidas por bluetooth para o microcontrolador embarcado no carro, o controle de sentido sera feito via dois botões, um para ré e outro para frente, não precinado nenhum o carro fica parado, com a possibilidade de fixar uma velocidade constante. A direção sera feita via medição dos acelerometros do celular, que fara o celular nesse caso atuar como um volante.

# Aplicativo Android

O aplicativo conta com as seguintes funcionalidades:

* Botão de coneção bluetooth no canto superior direito, que é responsavel por ligar o bluetooth caso o mesmo esteja desligado, buscar a lista de dispositivos pareados e conectar no mesmo, iniciando a comunicação.
* Botão Start/Stop responsavel por interromper ou não o fluxo de dados de aceleração do celular para o dispositivo pareado.
* Texto central com os valores de aceleração individual dos eixos.
* Texto na lateral esquerda com os valores de bateria e velocidade enviados pelo dispositivo conectado. 

![Aplicativo](https://lh3.googleusercontent.com/T5aIZLYmc6zRDyDfLEqJC3oWj4Pd2Ac50nLRMcWeIVMvBAhJH4QnIqoNmZWGkuMOup17q_hcQBqf0fbI74OJFKL_JOTTJfrZmn1BqJRP3FT6PgIWjQsw8Crh39ej9xpUBQKmtby4ZbV0DjX2OEaTw3Kkup1uSGyfLJ0P7GQGtviut7T99Qi5z-OW3CYiEyorRHv7D4Z2RzXOyND6nf3d2-NozdS-ieg80q4pbMRHK8FsjqmBbwxWehvBI-EzKGPdD7ZxbudscNODAKRCJ72baIyvOktY5X1D8UwgkZtHmHkdRL8K8v3wFWlYl1jMJe109PldMfF9gIV1ILQUQxn1_tPLpX9A8WmtTJbkLfSOSvC0c6fW97XjhWFCW-Ppu9_RxbEHqrmtns13VYhcKkLoQGkFKgZklZqULzBxE5a0SuFaRjndmpOd_YRIyYpAA77Vrg0hb2U_iNaU9qX1FMpAYbVTsVSFnptJqyVm3inCmZuMEcCllJuct43Gb7sPlPFuDs2hFjaevUmjxI0CWDE59DHP62juBxGtQn3gqUU8uDF5ee-G46IDLvMsG32qyCrcRYfMW_F-Vfj25OeDrMhnPvsJObaxC_Z7uey1kcZlTlIxAe_JXIsGeNbbfgSUjdeABIyW29dERHd22aoSfuyPQK3680GnEAYf=w1112-h625-no)


<p>
A estrutura dos dados de aceleração é a seguinte: 
<pre><code>byte dado[]=new byte[4];
        dado[0]=y;
        dado[1]=z;
        dado[2]='\r';
        dado[3]='\n';
        connectedThread.write(dado);
  </code></pre>
</p>

Já a estrutura dos dados recebidos tem que estar no seguinte formato :"00;00;00;00", sendo que o aplicativo insere uma virgula entre cada um dos valores.
	


