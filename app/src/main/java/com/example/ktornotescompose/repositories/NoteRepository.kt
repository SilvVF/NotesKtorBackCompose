package com.example.ktornotescompose.repositories

import com.example.ktornotescompose.data.local.entities.LocallyDeletedNoteId
import com.example.ktornotescompose.data.local.entities.Note
import com.example.ktornotescompose.util.Resource
import kotlinx.coroutines.flow.Flow

interface NoteRepository {

    suspend fun loginUser(email: String, password: String): Resource<String>

    suspend fun registerUser(email: String, password: String): Resource<String>

    suspend fun insertNote(note: Note)

    suspend fun insertNotes(noteList: List<Note>)

    fun getAllNotes(): Flow<Resource<List<Note>>>

    suspend fun getNoteById(id: String): Note?

    suspend fun deleteNote(noteId: String)

    suspend fun syncNotes()

    suspend fun addOwnerToNote(noteId: String, owner: String): Resource<String>
}