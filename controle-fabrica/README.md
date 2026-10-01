# Controle de Parâmetros da Fábrica

Sistema em Java (console) para registrar parâmetros de produção de uma fábrica
do ramo alimentício, substituindo as anotações manuais em papel.

## Funcionalidades

- Registro de operador, data, hora, temperatura e umidade
- Validação automática (temperatura entre 18 e 24 °C; umidade até 65%)
- Campo para sugestões de melhoria
- Armazenamento em banco de dados SQLite
- Consulta de todos os registros e apenas dos reprovados

## Requisitos

- JDK 17 ou superior

## Estrutura

```
src/fabrica/
├── Main.java         # menu e interação com o usuário
├── Registro.java     # modelo de dados e regras de validação
├── RegistroDAO.java  # acesso ao banco (INSERT / SELECT)
└── Conexao.java      # conexão e criação da tabela
```
