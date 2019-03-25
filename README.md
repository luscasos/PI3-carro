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
* Acionamento dos motores (completa em 22/03)
* Controle de tensão nos motores via PWM
* Aferição de velocidade com um sensor optico
* colocação das baterias
* 



# Acionamento dos motores

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

