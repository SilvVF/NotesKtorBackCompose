package com.example.ktornotescompose.data.remote

import com.example.ktornotescompose.data.local.entities.Note
import com.example.ktornotescompose.data.remote.requests.AccountRequest
import com.example.ktornotescompose.data.remote.requests.AddOwnerRequest
import com.example.ktornotescompose.data.remote.requests.DeleteNoteRequest
import com.example.ktornotescompose.data.remote.responses.SimpleResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface NoteApi {

    @POST("/register")
    suspend fun register(
        //makes sure retrofit parses this to JSON and send it to the KTOR server
        @Body registerRequest: AccountRequest
    ): Response<SimpleResponse>

    @POST("/login")
    suspend fun login(
        @Body loginRequest: AccountRequest
    ): Response<SimpleResponse>

    @POST("/addNote")
    suspend fun addNote(
        @Body note: Note
    ): Response<ResponseBody> //http status Code

    @POST("/deleteNote")
    suspend fun deleteNote(
        @Body deleteNoteRequest: DeleteNoteRequest
    ): Response<ResponseBody>

    @POST("/addOwnerToNote")
    suspend fun addOwnerToNote(
        @Body ownerRequest: AddOwnerRequest
    ): Response<SimpleResponse>

    @GET("/getNotes")
    suspend fun getNotes(): Response<List<Note>>
}