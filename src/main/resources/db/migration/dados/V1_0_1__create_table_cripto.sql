create table lancamento_cripto (
    ativo character varying(10) not null,
    time date not null,
    preco_ativo numeric(10,8) not null,
    quantidade numeric(10,8) not null,
    outros_custos numeric(10,8) not null,
    ativo_fiat character varying(10) not null default 'BRL',
    id_usuario integer not null,
    operacao character varying(10) not null
);

SELECT create_hypertable('lancamento_cripto', by_range('time', INTERVAL '30 days'));