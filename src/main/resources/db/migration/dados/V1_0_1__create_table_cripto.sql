create table lancamento_cripto (
    ativo character varying(10) not null,
    time date not null,
    preco_ativo numeric(10,8) not null,
    quantidade numeric(10,8) not null,
    outros_custos numeric(10,8) not null,
    ativo_fiat character varying(10) not null default 'BRL',
    id_usuario integer not null,
    operacao character varying(10) not null,
    origem character varying(10) not null,
    preco_medio_antes_operacao numeric(10,8) not null
);

CREATE INDEX lancamento_cripto_usuario_idx ON lancamento_cripto ( id_usuario, time DESC);

SELECT create_hypertable('lancamento_cripto', by_range('time', INTERVAL '30 days'));