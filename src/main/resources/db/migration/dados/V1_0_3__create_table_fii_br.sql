create table lancamento_fii_br (
    ativo character varying(10) not null,
    time timestamp with time zone not null,
    preco_ativo numeric(20,8) not null,
    quantidade numeric(20,8) not null,
    outros_custos numeric(20,8) not null,
    ativo_fiat character varying(10) not null default 'BRL',
    id_usuario integer not null,
    operacao character varying(10) not null,
    origem character varying(10) not null,
    preco_medio_antes_operacao numeric(20,8) not null
);

CREATE INDEX lancamento_fii_br_usuario_idx ON lancamento_fii_br ( id_usuario, time DESC);
ALTER TABLE lancamento_fii_br ADD COLUMN id SERIAL;
ALTER TABLE lancamento_fii_br ADD CONSTRAINT lancamento_fii_br_id_unique UNIQUE (id, time);
SELECT create_hypertable('lancamento_fii_br', by_range('time', INTERVAL '30 days'));