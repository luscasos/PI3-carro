# Projeto integrador III

![Hardware base](https://www.dhresource.com/0x0s/f2-albu-g7-M01-06-F9-rBVaSls0mXOACrsVAAHXBK3wNPo734.jpg/rob-um-conjunto-diy-inteligente-eletr-nica.jpg)

## Informações gerais
Dessenvolvido no escopo da disciplina de Projeto Integrador III, do curso de Eng. Eletrônica do IFSC campus Florianopolis no primeiro semestre de 2019, esse projeto tem como objetivo utilizar os conhecimentos obtidos ao decorrer do curso, implementando um carro controlado via aplicativo android.

## Tabela inicial de requisitos
* Controle via aplicativo android
* Comunicação via bluetooth
* Sensoreamento de velocidade e transmição para o app 
* Controle de velocidade via aplicativo 
* Aferição de distancia percorrida 
* Controle da bateria

## Tecnologias
* Parte mecânica do carro, disponível em https://s.click.aliexpress.com/e/bD9t0zL3
* Celular android
* Microcontrolador STM32
* Bateria de íon de lítio
	
## Cronograma
* Acionamento dos motores
* Controle de tensão nos motores via PWM
* Medição dos dados do acelerometro no APP
* Envio dos dados de direção e sentido via bluetooth
* Recebimento dos dados de direção e sentido no ucontrolador
* Definição da relação entre dados do acelerometro e diferença na relação de PWM
* Aferição de velocidade com encoder 
* colocação das baterias
* 



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


### Controle de velocidade
O controle da velocidade vai ser feito por meio da tecnica de modulação PWM, via microcontrolador atuando sobre a ponte H.
Como o circuito de ponte H escolhido não pode em hipotese alguma ter o os dois transistores de atuação polarizados ao mesmo tempo, optou-se por ultilizar o PWM no modo normal e no modo inverso, de forma a construir um sistama redundante nesse sendido, se houver uma falha no software mesmo assim não havera um problema critico na ponte H.

![PWM](https://i.ibb.co/jfv3VWc/TEK0000.png)

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
Como regra geral, não se pode descarregar uma bateria abaixo de uma certa tensão, com as usadas nesse projeto não é diferente, não sendo recomendado baixar a tensão da marca dos 3V, como o sistema possui duas baterias é necessario medir a tensão delas individualmente. O conversor analogico digital do microcontrolador tem como tensão base 3V, então é preciso baixar as tensões para menos de 3V, o circuito usado para isso é o diferencial com ampop, que pode dar um ganho menor que 1, sem usar tensão negativa. O Ampop escolhido foi o LM324.
![diferencial](http://www.c2o.pro.br/hackaguas/figuras/amplificador_operacional_diferencial.png)


* ## Placa de circuito
Com todos os requisitos de Hardware definidos foi desenvolvido o esquematico e placa do projeto

![esquematico](https://i.ibb.co/CJ0CqbJ/esquematico-completo.png)



# Controle de Direção e sentido
Tanto o controle de direção quanto o de sentido serão feitas via aplicação em Android e transmitidas por bluetooth para o microcontrolador embarcado no carro, o controle de sentido sera feito via dois botões, um para ré e outro para frente, não precinado nenhum o carro fica parado, com a possibilidade de fixar uma velocidade constante. A direção sera feita via medição dos acelerometros do celular, que fara o celular nesse caso atuar como um volante.

# Aplicativo Android
O app é o mais simples possivel visualmente, suas funcionalidades são conectar-se a um dispositivo bluetooth já pareado e enviar os dados obtidos quando há uma variação nos sensores de aceleração. o sistema operacional fornece a aceleração no SI por meio de uma variavel long porém, como tamanha precisão não se faz necessaria a aceleração foi representada como um byte, onde para 'A' igual a -10 o byte é 0x00, já para 'A' igual a 0xFF


<p>
  Esta é a a configuração do dado enviado
  <pre><code>byte dado[]=new byte[4];
        dado[0]=y;
        dado[1]=z;
        dado[2]='\r';
        dado[3]='\n';
        connectedThread.write(dado);
  </code></pre>
</p>
	
![Aplicativo](https://i.ibb.co/M5RTsR4/Screenshot-2019-04-24-17-24-30-473-com-example-lucas-pi3.png)

