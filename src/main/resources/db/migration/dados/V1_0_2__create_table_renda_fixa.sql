create table lancamento_renda_fixa (
    tipo_de_titulo character varying(10) not null,
    data_de_compra timestamp with time zone not null,
    data_de_vencimento timestamp with time zone not null,
    emissor character varying(100) not null,
    indexador character varying(10) null,
    valor_investido numeric(20,8) not null,
    rentabilidade numeric(20,8) not null,
    forma_de_rendimento character varying(10) not null,
    ativo_fiat character varying(10) not null default 'BRL',
    id_usuario integer not null,
    operacao character varying(10) not null,
    origem character varying(10) not null,
    preco_medio_antes_operacao numeric(20,8) not null
);
ALTER TABLE lancamento_renda_fixa ADD COLUMN id SERIAL;
ALTER TABLE lancamento_renda_fixa ADD CONSTRAINT lancamento_renda_fixa_id_unique UNIQUE (id, data_de_compra);
CREATE INDEX lancamento_renda_fixa_usuario_idx ON lancamento_renda_fixa ( id_usuario, data_de_compra DESC);

SELECT create_hypertable('lancamento_renda_fixa', by_range('data_de_compra', INTERVAL '30 days'));