*** Tabela 1 distribuidora
DROP TABLE IF EXISTS distribuidora;

CREATE TABLE distribuidora (
    id_distribuidora BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome             TEXT    NOT NULL,
    uf               CHAR(2) NOT NULL,
    UNIQUE (nome, uf)
);

*** tabela 2: catalogo_aparelho

DROP TABLE IF EXISTS catalogo_aparelho

CREATE TABLE catalogo_aparelho(
    id_catalogo   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome        TEXT NOT NULL UNIQUE,
    categoria   TEXT,
    potencia_w  NUMERIC(10,2) NOT NULL CHECK(potencia_w > 0),
    horas_uso_sugerido      NUMERIC(4,1) CHECK (horas_uso_sugerido BETWEEN 0 AND 24)

);
