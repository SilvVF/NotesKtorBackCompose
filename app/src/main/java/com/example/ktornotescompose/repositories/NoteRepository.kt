package com.example.ktornotescompose.repositories

import android.app.Application
import com.example.ktornotescompose.data.local.NoteDao
import com.example.ktornotescompose.data.remote.NoteApi
import com.example.ktornotescompose.data.remote.requests.AccountRequest
import com.example.ktornotescompose.ui.navigation.UiEvent
import com.example.ktornotescompose.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NoteRepository @Inject constructor(
    private val noteDao: NoteDao,
    private val noteApi: NoteApi,
    private val context: Application
) {

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
}