package com.tiooooo.core.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.tiooooo.core.model.UserModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.component.KoinComponent

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class UserPreference(val context: Context) : KoinComponent {
    companion object {
        private val IS_LOGIN = booleanPreferencesKey("isLogin")
        private val TOKEN = stringPreferencesKey("token")
        private val NAME_KEY = stringPreferencesKey("name")
        private val EMAIL_KEY = stringPreferencesKey("email")
    }

    fun getToken(): Flow<String> =
        context.dataStore.data.map { it[TOKEN] ?: "" }

    fun isLogin(): Flow<Boolean> =
        context.dataStore.data.map { it[IS_LOGIN] ?: false }

    suspend fun login(user: UserModel) =
        context.dataStore.edit {
            it[NAME_KEY] = user.name
            it[EMAIL_KEY] = user.email
            it[TOKEN] = user.accessToken
            it[IS_LOGIN] = true
        }

    fun getUser(): Flow<UserModel> {
        return context.dataStore.data.map { preferences ->
            UserModel(
                preferences[NAME_KEY] ?: "",
                preferences[EMAIL_KEY] ?: "",
                preferences[TOKEN] ?: "",
                preferences[IS_LOGIN] ?: false
            )
        }
    }

    suspend fun logout() =
        context.dataStore.edit {
            it[TOKEN] = ""
            it[IS_LOGIN] = false
        }

}