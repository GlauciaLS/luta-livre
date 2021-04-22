# Luta Livre
<p> Desafio de API Rest de luta livre utilizando Spring Boot e com cobertura de testes unitários. Há 6 endpoints: </p>

- GET /lutadores: Retorna todos os lutadores cadastrados.
- POST /lutadores: Cadastra um lutador no banco de dados, o corpo da requisição deve conter o valor dos campos "nome" e "forcaGolpe".
- GET /lutadores/contagem-vivos: Retorna um inteiro que representa a quantidade de lutadores que ainda estão vivos.
- POST /lutadores/{id}/concentrar: Concentra um lutador específico com base no id informado.
- POST /lutadores/golpe: Realiza um golpe, o corpo da requisição deve conter o valor dos campos "idLutadorBate" e "idLutadorApanha".
- GET /lutadores/mortos: Retorna um inteiro que representa a quantidade de lutadores que estão mortos.

<h2>🛠 Tecnologias</h2>

  - Java 8
  - Spring Boot
  - JPA / Hibernate
  - JUnit
  - Mockito
  - Hamcrest
  
<h2>▶️ Como executar o projeto</h2>

Para executar o projeto no terminal, digite o seguinte comando:

```shell script
mvn spring-boot:run 
```

Para executar a suíte de testes desenvolvida, basta executar o seguinte comando:

```shell script
mvn clean test
```
