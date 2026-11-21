package com.example.energiainteligente.security

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class SessionManager(context: Context) {

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
        context,
        "secure_session_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun salvarTokenSessao(token: String) {
        sharedPreferences.edit().putString("SESSION_TOKEN", token).apply()
    }

    fun isUsuarioLogado(): Boolean {
        return sharedPreferences.getString("SESSION_TOKEN", null) != null
    }

    fun limparSessao() {
        sharedPreferences.edit().remove("SESSION_TOKEN").apply()
    }
}