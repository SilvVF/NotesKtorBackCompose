package com.example.ktornotescompose.repositories

import android.app.Application
import com.example.ktornotescompose.data.local.NoteDao
import com.example.ktornotescompose.data.local.entities.LocallyDeletedNoteId
import com.example.ktornotescompose.data.local.entities.Note
import com.example.ktornotescompose.data.remote.NoteApi
import com.example.ktornotescompose.data.remote.requests.AccountRequest
import com.example.ktornotescompose.data.remote.requests.DeleteNoteRequest
import com.example.ktornotescompose.util.Resource
import com.example.ktornotescompose.util.checkForInternetConnection
import com.example.ktornotescompose.util.networkBoundResource
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class NoteRepositoryImpl (
    private val noteDao: NoteDao,
    private val noteApi: NoteApi,
    private val context: Application
): NoteRepository {
    override suspend fun loginUser(email: String, password: String): Resource<String>  {
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

    override suspend fun registerUser(email: String, password: String): Resource<String>  {
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
    override suspend fun insertNote(note: Note) {
        //insert note into backend server
        val response = try {
            noteApi.addNote(note)
        } catch (e: Exception) {
            null
        }
        //save the note locally in DB and set sync based on server response
        response?.let {
            if (response.isSuccessful && response.body() != null){
                noteDao.insert(note.apply { isSynced = true })
            }
        } ?: noteDao.insert(note.apply { isSynced = false })
    }
    override suspend fun insertNotes (noteList: List<Note>){
        noteList.forEach { insertNote(it) }
    }

    private var currentNotesResponse: Response<List<Note>>? = null
    override suspend fun syncNotes() {
        //get list of locally deleted notes
        val locallyDeletedNoteIds = noteDao.getLocallyDeletedNoteIds()
        //delete the notes from the server
        locallyDeletedNoteIds.forEach { id -> noteApi.deleteNote(DeleteNoteRequest(id.deletedNoteId)) }
        // insert all notes that where made locally to the server
        noteDao.getAllUnsyncedNotes().forEach { note -> insertNote(note) }
        //get the notes currently on the server after sync
        currentNotesResponse = noteApi.getNotes()
        //check the response
        currentNotesResponse?.body()?.let { notes ->
            //clear the cached notes in the database
            noteDao.deleteAllNotes()
            //insert the list of synced noted from the server
            insertNotes(notes.onEach { note -> note.isSynced = true })
        }
    }

    override fun getAllNotes(): Flow<Resource<List<Note>>> {
        return networkBoundResource(
            query = {
                noteDao.getAllNotes()
            },
            fetch = {
                syncNotes()
                currentNotesResponse
            },
            saveFetchResult = { response ->
                response?.body()?.let {
                    insertNotes(it.onEach { note -> note.isSynced = true })
                }
            },
            shouldFetch = {
                checkForInternetConnection(context)
            },
        )
    }
    override suspend fun getNoteById(id: String) = noteDao.getNoteById(id)

    private suspend fun deleteLocallyDeletedNoteId(deletedNoteId: String) {
        noteDao.deleteLocallyDeletedNoteId(deletedNoteId)
    }

    override suspend fun deleteNote(noteId: String) {
        //try and delete the note from the server and save the response
        val response = try {
            noteApi.deleteNote(DeleteNoteRequest(noteId))
        } catch (e: Exception) {
            null
        }
        //always delete the note locally
        noteDao.deleteNoteById(noteId)
        //check if the response was success
        if (response != null && response.isSuccessful) {
            //response was successful delete the note if it was saved in local list
            deleteLocallyDeletedNoteId(noteId)
            return
        }
        //couldn't connect to server save the noteId to be deleted later
        noteDao.insertLocallyDeletedNoteId(LocallyDeletedNoteId(noteId))
    }
}