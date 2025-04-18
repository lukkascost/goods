INSERT INTO lancamento_renda_fixa
(tipo_de_titulo, data_de_compra, data_de_vencimento, emissor, indexador, valor_investido, rentabilidade, 
 forma_de_rendimento, ativo_fiat, id_usuario, operacao, origem, preco_medio_antes_operacao)
VALUES ('CDB', '2024-01-15', '2025-01-15', 'Banco XYZ', 'CDI', 5000.00000000, 
        110.00000000, 'PREFIXADO', 'BRL', 1, 'COMPRA', 'CLEAR', 0.00000000);

INSERT INTO lancamento_renda_fixa
(tipo_de_titulo, data_de_compra, data_de_vencimento, emissor, indexador, valor_investido, rentabilidade, 
 forma_de_rendimento, ativo_fiat, id_usuario, operacao, origem, preco_medio_antes_operacao)
VALUES ('LCI', '2024-01-16', '2026-01-16', 'Banco ABC', 'IPCA', 10000.00000000, 
        5.50000000, 'POSFIXADO', 'BRL', 1, 'COMPRA', 'XP', 0.00000000);

INSERT INTO lancamento_renda_fixa
(tipo_de_titulo, data_de_compra, data_de_vencimento, emissor, indexador, valor_investido, rentabilidade, 
 forma_de_rendimento, ativo_fiat, id_usuario, operacao, origem, preco_medio_antes_operacao)
VALUES ('TESOURO', '2024-01-17', '2030-01-17', 'Tesouro Nacional', 'SELIC', 7500.00000000, 
        100.00000000, 'POSFIXADO', 'BRL', 1, 'VENDA', 'ITAU', 7000.00000000);
