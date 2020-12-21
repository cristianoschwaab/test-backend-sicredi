## **DESAFIO TÉCNICO SICREDI**

Desafio técnico de implementação de serviço backend do Sicredi. O desafio pode serencontrado [aqui](https://drive.google.com/file/d/1f1Gk-7KjSID3hmWuxtwNw8IDB7VJaFhW/view?usp=sharing).

Sobre as tecnologias: 
Foram utilizados **micro-serviços** com **Spring Boot Webflux** devido suas funcionalidades voltadas para integrações, grande escalabilidade, além de possuírem pluggins que provém formas de integrações via cloud e manutenibilidade dos mesmos. Alguns desses foram utilizados durante desenvolvimento do projeto:

 1. spring-boot-starter-webflux
 2. spring-boot-starter-actuator
 3. spring-boot-starter-data-mongodb-reactive
 4. spring-kafka

Foi utilizado MongoDB para repositório de dados e Kafka para atendimento da tarefa bônus de mensageria e filas.

A implementação do código está disponível [aqui](https://github.com/cristianoschwaab/testDeveloperSicredi):
 - Tarefa Bônus 1: [view](https://drive.google.com/file/d/1gXlKBDnjiZGWcb_0VWytl2aPYhnzSL6a/view?usp=sharing)
 - Tarefa Bônus 2: [view](https://drive.google.com/file/d/1d4ESXiFj5jZTlC6B-J2_3o9g_enkoszM/view?usp=sharing)
 - Tarefa Bônus 4: Seria bom utilizar algum API Gateway juntamente com o versionamento das APIs, mas para a aplicação foi versionado no endpoint dos serviços. [view](https://drive.google.com/file/d/1y-pFlzLYhQBKIr0r-cKciOHGCZda7ioY/view?usp=sharing).

### Testando a aplicação:

Pré requisitos:
 - Possuir Java 11 e Gradle 5 instalados.
 - Possuir docker e docker-compose instalados.
 - As portas *8080*, *9092* e *27017* não devem estar ocupadas.

#### Passo a passo:

 1. Clonar o repositório neste [link](https://github.com/cristianoschwaab/test-backend-sicredi.git)
 2. No diretório clonado executar inciar o docker com mongoDb e Kafka através do docker-compose através do seguinte comando 
 > docker-compose -f docker-compose.yml up --build 
 3. No mesmo diretório construir e executar a aplicação executar com o comando (caso seja necessário fica disponível um Dockerfile de exemplo)
 > ./gradlew build && java -jar build/libs/backend-test-0.0.1-SNAPSHOT.jar
