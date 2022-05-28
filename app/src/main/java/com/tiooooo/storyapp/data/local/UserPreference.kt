package com.tiooooo.storyapp.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.component.KoinComponent

class UserPreference(private val dataStore: DataStore<Preferences>) : KoinComponent {
    companion object {
        private val IS_LOGIN = booleanPreferencesKey("isLogin")
        private val TOKEN = stringPreferencesKey("token")
    }

    fun getToken(): Flow<String> =
        dataStore.data.map { it[TOKEN] ?: "" }

    suspend fun login(token: String) =
        dataStore.edit {
            it[TOKEN] = token
            it[IS_LOGIN] = true
        }

    suspend fun logout() =
        dataStore.edit {
            it[TOKEN] = ""
            it[IS_LOGIN] = false
        }

}