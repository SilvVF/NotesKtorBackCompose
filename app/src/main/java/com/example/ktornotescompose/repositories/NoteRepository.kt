package com.example.ktornotescompose.repositories

import android.app.Application
import android.provider.ContactsContract
import com.example.ktornotescompose.data.local.NoteDao
import com.example.ktornotescompose.data.local.entities.Note
import com.example.ktornotescompose.data.remote.NoteApi
import com.example.ktornotescompose.data.remote.requests.AccountRequest
import com.example.ktornotescompose.ui.navigation.UiEvent
import com.example.ktornotescompose.util.Resource
import com.example.ktornotescompose.util.checkForInternetConnection
import com.example.ktornotescompose.util.networkBoundResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NoteRepository @Inject constructor(
    private val noteDao: NoteDao,
    private val noteApi: NoteApi,
    private val context: Application
) {
    suspend fun loginUser(email: String, password: String): Resource<String>  {
        try {
            val response = noteApi.login(AccountRequest(email = email, password = password))
            if (response.isSuccessful && response.body()!!.successful) {
                response.body()?.message?.let {
                    return Resource.Success(it)
                }
            } else {
                return Resource.Error(response.message(), null)
            }
        }catch (e: Exception) {
            return Resource.Error("Couldn't connect to the server check internet", null)
        }
        return Resource.Error("Couldn't connect to the server check internet", null)
    }

    suspend fun registerUser(email: String, password: String): Resource<String>  {
        try {
            val response = noteApi.register(AccountRequest(email = email, password = password))
            if (response.isSuccessful) {
                response.body()?.message?.let {
                    return Resource.Success(it)
                }
            } else {
                return Resource.Error(response.message(), null)
            }
        }catch (e: Exception) {
            return Resource.Error("Couldn't connect to the server check internet", null)
        }
        return Resource.Error("Couldn't connect to the server check internet", null)
    }
    fun getAllNotes(): Flow<Resource<List<Note>>> {
        return networkBoundResource(
            query = {
                noteDao.getAllNotes()
            },
            fetch = {
                noteApi.getNotes()
            },
            saveFetchResult = { response ->
                if (response.isSuccessful) {
                    response.body()?.let { notes ->
                        noteDao.insertListNotes(notes)
                    }
                }
            },
            shouldFetch = {
                checkForInternetConnection(context)
            },
        )
    }
}