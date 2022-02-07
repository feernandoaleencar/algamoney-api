CREATE TABLE categoria
(
    id   bigint(20) primary key auto_increment,
    nome varchar(60) not null
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO categoria (nome)
values ('Lazer');
INSERT INTO categoria (nome)
values ('Alimentação');
INSERT INTO categoria (nome)
values ('Supermercado');
INSERT INTO categoria (nome)
values ('Farmácia');
INSERT INTO categoria (nome)
values ('Outros');