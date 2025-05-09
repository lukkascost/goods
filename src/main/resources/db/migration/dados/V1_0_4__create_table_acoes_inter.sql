create table lancamento_etf_inter (
    ativo character varying(10) not null,
    time timestamp with time zone not null,
    preco_ativo numeric(20,8) not null,
    quantidade numeric(20,8) not null,
    outros_custos numeric(20,8) not null,
    ativo_fiat character varying(10) not null default 'USD',
    id_usuario integer not null,
    operacao character varying(10) not null,
    origem character varying(10) not null,
    preco_medio_antes_operacao numeric(20,8) not null
);
ALTER TABLE lancamento_etf_inter ADD COLUMN id SERIAL;
ALTER TABLE lancamento_etf_inter ADD CONSTRAINT lancamento_etf_inter_id_unique UNIQUE (id, time);
CREATE INDEX lancamento_etf_inter_usuario_idx ON lancamento_etf_inter ( id_usuario, time DESC);

SELECT create_hypertable('lancamento_etf_inter', by_range('time', INTERVAL '30 days'));