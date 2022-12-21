package com.example.submissionintermediate1.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class Token constructor(
    private val dataStore: DataStore<Preferences>
) {

    private val tokenKey = stringPreferencesKey(key)

    fun getToken(): Flow<String?>{
        return dataStore.data.map { preferences ->
            preferences[tokenKey]
        }
    }

    suspend fun saveToken(token: String){
        dataStore.edit{
            it[tokenKey] = token
        }
    }

    suspend fun deleteAuthToken(){
        dataStore.edit {
            it.clear()
        }
    }
    companion object{
        const val key: String = "token"

        @Volatile
        private var INSTANCE: Token? = null
        fun getInstance(dataStore: DataStore<Preferences>): Token{
            return INSTANCE?: synchronized(this){
                val instance = Token(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}