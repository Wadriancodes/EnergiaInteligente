-- =====================================================
-- supabase.sql: só para rodar no SQL Editor do Supabase
-- Ordem: schema.sql, seed.sql, supabase.sql
-- =====================================================

-- ---------- 1. Liga usuario ao login ----------
ALTER TABLE usuario DROP CONSTRAINT IF EXISTS usuario_id_usuario_fkey;
ALTER TABLE usuario
    ADD CONSTRAINT usuario_id_usuario_fkey
    FOREIGN KEY (id_usuario) REFERENCES auth.users(id) ON DELETE CASCADE;

-- ---------- 2. RLS ligado em todas as tabelas ----------
ALTER TABLE distribuidora      ENABLE ROW LEVEL SECURITY;
ALTER TABLE catalogo_aparelho  ENABLE ROW LEVEL SECURITY;
ALTER TABLE bandeira_tarifaria ENABLE ROW LEVEL SECURITY;
ALTER TABLE tarifa_historico   ENABLE ROW LEVEL SECURITY;
ALTER TABLE usuario            ENABLE ROW LEVEL SECURITY;
ALTER TABLE aparelho_usuario   ENABLE ROW LEVEL SECURITY;
ALTER TABLE consumo_historico  ENABLE ROW LEVEL SECURITY;

-- ---------- 3. Permissões (GRANT) para a API ----------
-- Tabelas de referência: usuário logado só lê
GRANT SELECT ON distribuidora, catalogo_aparelho, bandeira_tarifaria, tarifa_historico
    TO authenticated;
-- Tabelas do usuário: usuário logado lê e grava
GRANT SELECT, INSERT, UPDATE ON usuario TO authenticated;
GRANT SELECT, INSERT, UPDATE, DELETE ON aparelho_usuario, consumo_historico TO authenticated;

-- ---------- 4. Políticas: tabelas de referência (todos os logados leem) ----------
DROP POLICY IF EXISTS "leitura referencia" ON distribuidora;
CREATE POLICY "leitura referencia" ON distribuidora
    FOR SELECT TO authenticated USING (true);

DROP POLICY IF EXISTS "leitura referencia" ON catalogo_aparelho;
CREATE POLICY "leitura referencia" ON catalogo_aparelho
    FOR SELECT TO authenticated USING (true);

DROP POLICY IF EXISTS "leitura referencia" ON bandeira_tarifaria;
CREATE POLICY "leitura referencia" ON bandeira_tarifaria
    FOR SELECT TO authenticated USING (true);

DROP POLICY IF EXISTS "leitura referencia" ON tarifa_historico;
CREATE POLICY "leitura referencia" ON tarifa_historico
    FOR SELECT TO authenticated USING (true);

-- ---------- 5. Políticas: cada usuário só mexe nos próprios dados ----------
DROP POLICY IF EXISTS "usuario le o proprio" ON usuario;
CREATE POLICY "usuario le o proprio" ON usuario
    FOR SELECT TO authenticated USING (id_usuario = (SELECT auth.uid()));

DROP POLICY IF EXISTS "usuario cria o proprio" ON usuario;
CREATE POLICY "usuario cria o proprio" ON usuario
    FOR INSERT TO authenticated WITH CHECK (id_usuario = (SELECT auth.uid()));

DROP POLICY IF EXISTS "usuario edita o proprio" ON usuario;
CREATE POLICY "usuario edita o proprio" ON usuario
    FOR UPDATE TO authenticated
    USING (id_usuario = (SELECT auth.uid()))
    WITH CHECK (id_usuario = (SELECT auth.uid()));

DROP POLICY IF EXISTS "aparelhos do proprio usuario" ON aparelho_usuario;
CREATE POLICY "aparelhos do proprio usuario" ON aparelho_usuario
    FOR ALL TO authenticated
    USING (id_usuario = (SELECT auth.uid()))
    WITH CHECK (id_usuario = (SELECT auth.uid()));

DROP POLICY IF EXISTS "consumo do proprio usuario" ON consumo_historico;
CREATE POLICY "consumo do proprio usuario" ON consumo_historico
    FOR ALL TO authenticated
    USING (id_usuario = (SELECT auth.uid()))
    WITH CHECK (id_usuario = (SELECT auth.uid()));

-- ---------- 6. Cria o usuario automaticamente no cadastro ----------
CREATE OR REPLACE FUNCTION public.cria_usuario_no_cadastro()
RETURNS TRIGGER
LANGUAGE plpgsql
SECURITY DEFINER
SET search_path = ''
AS $$
BEGIN
    INSERT INTO public.usuario (id_usuario, nome)
    VALUES (NEW.id, COALESCE(NEW.raw_user_meta_data ->> 'nome', 'Usuário'));
    RETURN NEW;
END;
$$;

REVOKE EXECUTE ON FUNCTION public.cria_usuario_no_cadastro() FROM PUBLIC, anon, authenticated;

DROP TRIGGER IF EXISTS trg_cria_usuario_no_cadastro ON auth.users;
CREATE TRIGGER trg_cria_usuario_no_cadastro
AFTER INSERT ON auth.users
FOR EACH ROW EXECUTE FUNCTION public.cria_usuario_no_cadastro();    