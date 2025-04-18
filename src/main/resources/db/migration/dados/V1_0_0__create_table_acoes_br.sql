create table lancamento_acao_br (
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

CREATE INDEX lancamento_acao_br_usuario_idx ON lancamento_acao_br ( id_usuario, time DESC);


SELECT create_hypertable('lancamento_acao_br', by_range('time', INTERVAL '30 days'));