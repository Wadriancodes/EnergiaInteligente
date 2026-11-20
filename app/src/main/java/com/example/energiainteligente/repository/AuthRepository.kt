package com.example.energiainteligente.repository

import com.example.energiainteligente.network.SupabaseNetwork
import com.example.energiainteligente.security.CryptoUtils
import com.example.energiainteligente.security.SessionManager
import io.github.jan.supabase.auth.providers.builtin.Email

class AuthRepository(private val sessionManager: SessionManager, private val supabaseNetwork: SupabaseNetwork) {
    fun isUsuarioLogado(): Boolean {
        return sessionManager.isUsuarioLogado()
    }

    suspend fun realizarLogout() {
        try {
            SupabaseNetwork.auth.signOut()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            sessionManager.limparSessao()
        }
    }

    suspend fun cadastrarUsuario(nome: String, email: String, senhaPura: String): Result<Boolean> {
        return try {
            // 1. Gera o Hash SHA-256 da senha pura para nunca expor a senha real na rede
            val senhaSegura = CryptoUtils.hashPassword(senhaPura)

            // 2. Envia para o Supabase Auth
            SupabaseNetwork.auth.signUpWith(Email) {
                this.email = email
                this.password = senhaSegura
                // Podemos passar o nome como metadado adicional
                // data = buildJsonObject { put("full_name", nome) }
            }
            Result.success(true)
        } catch (e: Exception) {
            // Ex: E-mail já cadastrado, formato inválido, etc.
            e.printStackTrace()
            Result.failure(Exception("Falha ao criar conta: ${e.message}"))
        }
    }

    // Função de LOGIN (Sign In) - Valida e salva a sessão segura no dispositivo
    suspend fun realizarLogin(email: String, senhaPura: String): Result<Boolean> {
        return try {
            // 1. Gera o Hash SHA-256 da tentativa de senha
            val senhaSegura = CryptoUtils.hashPassword(senhaPura)

            // 2. Envia credenciais para o Supabase
            SupabaseNetwork.auth.signInWith(Email) {
                this.email = email
                this.password = senhaSegura
            }

            val sessionToken = SupabaseNetwork.auth.currentAccessTokenOrNull()

            // 4. Salva o Token localmente usando o EncryptedSharedPreferences (Blindagem Local)
            if (sessionToken != null) {
                sessionManager.salvarTokenSessao(sessionToken)
                Result.success(true)
            } else {
                Result.failure(Exception("Falha ao recuperar token de sessão."))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(Exception("E-mail ou senha incorretos."))
        }
    }
}