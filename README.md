# API Consultório
Essa é uma API REST para gerenciamento de um consultório médico, que permite o cadastro, atualização, exclusão e agendamento de consultas de médicos e pacientes.

Este projeto foi desenvolvido no IFBA, para a matéria de Programação Web.

# Tecnologias Utilizadas
A API Consultório é desenvolvida utilizando as seguintes tecnologias:

- Java
- SpringBoot
- Maven
- Spring Web
- Spring Data JPA
- H2 Database
- Hibernate Validator
- Lombok

# Funcionalidades
A API Consultório possui as seguintes funcionalidades:

## Cadastro de Médicos
Permite o cadastro de médicos com as seguintes informações obrigatórias:

- Nome
- E-mail
- Telefone
- CRM
- Especialidade (Ortopedia, Cardiologia, Ginecologia ou Dermatologia, etc)

## Listagem de Médicos
Permite a listagem dos médicos cadastrados, exibindo as seguintes informações de cada médico:

- Nome
- Email
- CRM
- Especialidade
A listagem é ordenada pelo nome do médico.

## Atualizar Médicos
Permite a atualização dos dados cadastrais dos médicos, com as seguintes informações que podem ser atualizadas:

- Nome
- Telefone

## As seguintes regras de negócio são validadas pela API:

- Não é permitido alterar o e-mail do médico;
- Não é permitido alterar o CRM do médico;
- Não é permitido alterar a especialidade do médico.

## Deletar Médicos
Permite a exclusão de médicos cadastrados. A exclusão não apaga os dados do médico, mas marca o médico como "inativo" no sistema.

## Cadastro de Pacientes
Permite o cadastro de pacientes com as seguintes informações obrigatórias:

- Nome
- E-mail
- Telefone
- CPF

## Listar Pacientes
Permite a listagem dos pacientes cadastrados, exibindo as seguintes informações de cada paciente:

- Nome
- Email
- CPF

A listagem é ordenada pelo nome do paciente em ordem crescente.

## Atualizar Pacientes
Permite a atualização dos dados cadastrais dos pacientes, com as seguintes informações que podem ser atualizadas:

- Nome
- Telefone

## As seguintes regras de negócio são validadas pela API:

- Não é permitido alterar o e-mail do paciente;
- Não é permitido alterar o CPF do paciente.
- Apagar Pacientes

Permite a exclusão de pacientes cadastrados. A exclusão não apaga os dados do paciente, mas marca o paciente como "inativo" no sistema.

## Agendar Consulta
Permite o agendamento de consultas, preenchendo as seguintes informações:

- Paciente
- Médico
- Data/Hora da consulta

## As seguintes regras de negócio são validadas pela API:

- O horário de funcionamento da clínica é de segunda a sábado, das 07:00 às 19:00;
- As consultas têm duração fixa de 1 hora;
- Não é permitido agendar consultas com pacientes inativos no sistema;
- Não é permitido agendar consultas com médicos inativos no sistema;
- Não é permitido agendar mais de uma consulta no mesmo dia para o mesmo paciente;
- Não é permitido agendar uma consulta com um médico que já possui outra consulta agendada na mesma data/hora.

## Cancelar Consulta
Permite o cancelamento de consultas, fornecendo as seguintes informações:

- Consulta
- Motivo do cancelamento

É obrigatório informar o motivo do cancelamento da consulta, escolhendo entre as opções: paciente desistiu, médico cancelou ou outros.
