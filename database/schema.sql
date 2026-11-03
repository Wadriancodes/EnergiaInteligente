-- Tabela 1 distribuidora
DROP TABLE IF EXISTS distribuidora;

CREATE TABLE distribuidora (
    id_distribuidora BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome             TEXT    NOT NULL,
    uf               CHAR(2) NOT NULL,
    UNIQUE (nome, uf)
);

-- Tabela 2: catalogo_aparelho

DROP TABLE IF EXISTS catalogo_aparelho;

CREATE TABLE catalogo_aparelho(
    id_catalogo   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome        TEXT NOT NULL UNIQUE,
    categoria   TEXT,
    potencia_w  NUMERIC(10,2) NOT NULL CHECK(potencia_w > 0),
    horas_uso_sugerido      NUMERIC(4,1) CHECK (horas_uso_sugerido BETWEEN 0 AND 24)

);

DROP TABLE IF EXISTS bandeira_tarifaria;

CREATE TABLE bandeira_tarifaria(
    tipo      TEXT PRIMARY KEY CHECK( tipo IN ('VERDE','AMARELA','VERMELHA_P1', 'VERMELHA_P2')),
    nome        TEXT NOT NULL,
    adicional_por_kwh        NUMERIC(8,5) NOT NULL CHECK(adicional_por_kwh >= 0),
    descricao      TEXT,
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT now()
);


DROP TABLE IF EXISTS usuario;

CREATE TABLE usuario(
    id_usuario      UUID PRIMARY KEY ,
    nome        TEXT NOT NULL,
    meta_kwh        NUMERIC(10,2) CHECK (meta_kwh >= 0),
    id_distribuidora        BIGINT REFERENCES  distribuidora(id_distribuidora) ON DELETE SET NULL,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT now()

);

DROP TABLE IF EXISTS tarifa_historico;

CREATE TABLE tarifa_historico(
    id_tarifa      BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_distribuidora BIGINT NOT NULL REFERENCES distribuidora(id_distribuidora) ON DELETE CASCADE,
    tarifa_base_kwh     NUMERIC(8,5) NOT NULL CHECK (tarifa_base_kwh >= 0),
    vigencia_inicio     DATE NOT NULL,
    vigencia_fim        DATE ,
    created_at       TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at       TIMESTAMPTZ NOT NULL DEFAULT now(),
    CHECK (vigencia_fim >= vigencia_inicio)
);

DROP TABLE IF EXISTS aparelho_usuario;

CREATE TABLE aparelho_usuario (
    id_aparelho        UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    id_usuario         UUID NOT NULL REFERENCES usuario(id_usuario) ON DELETE CASCADE,
    id_catalogo        BIGINT REFERENCES catalogo_aparelho(id_catalogo) ON DELETE SET NULL,
    nome_personalizado TEXT NOT NULL,
    potencia_w         NUMERIC(10,2) NOT NULL CHECK (potencia_w > 0),
    horas_uso_diario   NUMERIC(4,1) NOT NULL CHECK (horas_uso_diario BETWEEN 0 AND 24),
    created_at         TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at         TIMESTAMPTZ NOT NULL DEFAULT now()
);

DROP TABLE IF EXISTS consumo_historico;

CREATE TABLE consumo_historico (
    id_consumo     BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_usuario     UUID NOT NULL REFERENCES usuario(id_usuario) ON DELETE CASCADE,
    mes_referencia DATE NOT NULL CHECK (EXTRACT(DAY FROM mes_referencia) = 1),
    consumo_kwh    NUMERIC(10,2) NOT NULL CHECK (consumo_kwh >= 0),
    custo_total    NUMERIC(10,2) NOT NULL CHECK (custo_total >= 0),
    tipo_bandeira  TEXT NOT NULL REFERENCES bandeira_tarifaria(tipo),
    id_tarifa      BIGINT REFERENCES tarifa_historico(id_tarifa) ON DELETE SET NULL,
    created_at     TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at     TIMESTAMPTZ NOT NULL DEFAULT now(),
    UNIQUE (id_usuario, mes_referencia)
);
