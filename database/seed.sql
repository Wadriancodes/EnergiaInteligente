INSERT INTO distribuidora (nome, uf) VALUES ('Equatorial Pará', 'PA');

INSERT INTO bandeira_tarifaria (tipo, nome, adicional_por_kwh, descricao) VALUES
('VERDE',       'Bandeira Verde',       0.00000, 'Condições favoráveis de geração, sem acréscimo'),
('AMARELA',     'Bandeira Amarela',     0.01885, 'Condições menos favoráveis de geração'),
('VERMELHA_P1', 'Bandeira Vermelha P1', 0.04463, 'Condições mais custosas de geração, patamar 1'),
('VERMELHA_P2', 'Bandeira Vermelha P2', 0.07877, 'Condições ainda mais custosas, patamar 2');

INSERT INTO tarifa_historico (id_distribuidora, tarifa_base_kwh, vigencia_inicio)
SELECT id_distribuidora, 0.97830, '2026-01-01' FROM distribuidora WHERE nome = 'Equatorial Pará';

INSERT INTO catalogo_aparelho (nome, categoria, potencia_w, horas_uso_sugerido) VALUES
('Geladeira',        'Cozinha',     150, 24),
('Ar-condicionado',  'Climatização', 1400, 8),
('Chuveiro elétrico','Banho',       5500, 0.5),
('Ventilador',       'Climatização', 80, 8),
('Televisão',        'Entretenimento', 100, 5),
('Micro-ondas',      'Cozinha',     1200, 0.5),
('Máquina de lavar', 'Lavanderia',  500, 1),
('Ferro de passar',  'Lavanderia',  1000, 1),
('Lâmpada LED',      'Iluminação',  9, 6),
('Computador',       'Escritório',  250, 6);