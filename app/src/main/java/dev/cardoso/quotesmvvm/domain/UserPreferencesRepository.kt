package dev.cardoso.quotesmvvm.domain

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import java.io.IOException
val USER_DATASTORE = "preferencesquotesmvvm"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = USER_DATASTORE)

class UserPreferencesRepository(var context: Context) {



    companion object{
        val TOKEN = stringPreferencesKey("TOKEN")
        var theToken= ""
    }

    var token:Flow<String> = context.dataStore.data.catch { exception -> // 1
        // dataStore.data throws an IOException if it can't read the data
        if (exception is IOException) { // 2
           // emit(emptyPreferences())
            throw exception
        } else {
            throw exception
        }
    }
        .map{
                preferences->preferences[TOKEN]?:""
        }

    suspend fun saveTokenToDataStore(token: String){
        context.dataStore.edit{
            it[TOKEN]= token
        }
    }


    suspend fun getTokenFromDataStore(): Flow<String>{

        return context.dataStore.data
           .map{
                   preferences->
               preferences[TOKEN]?:""

           }
    }

    val exampleData = runBlocking { context.dataStore.data.map{
            preferences->
        preferences[dev.cardoso.quotesmvvm.domain.UserPreferencesRepository.Companion.TOKEN]?:""

    } }


}