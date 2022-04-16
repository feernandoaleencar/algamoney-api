ALTER TABLE pessoa DROP COLUMN cidade;
ALTER TABLE pessoa DROP COLUMN estado;
ALTER TABLE pessoa ADD COLUMN codigo_ibge BIGINT(20);
ALTER TABLE pessoa ADD CONSTRAINT fk_pessoa_municipio FOREIGN KEY (codigo_ibge) REFERENCES municipio(codigo_ibge);

UPDATE pessoa SET codigo_ibge = 2204402;