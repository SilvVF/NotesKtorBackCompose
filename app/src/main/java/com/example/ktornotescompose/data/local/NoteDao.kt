package com.example.ktornotescompose.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.ktornotescompose.data.local.entities.LocallyDeletedNoteId
import com.example.ktornotescompose.data.local.entities.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: Note)

    @Query("""
            DELETE
            FROM notes
            WHERE id = :id
        """)
    suspend fun deleteNoteById(id: String)

    @Query("DELETE FROM notes WHERE isSynced = 1") // 1 = true in SQL
    suspend fun deleteAllSyncedNotes()

    @Query("SELECT * FROM notes WHERE id = :id LIMIT 1")
    fun observeNoteById(id: String): LiveData<Note>

    @Query("SELECT * FROM notes WHERE id = :id LIMIT 1")
    suspend fun getNoteById(id: String): Note?

    @Query("SELECT * FROM notes ORDER BY date DESC")
    fun getAllNotes(): Flow<List<Note>>

    @Query("SELECT * FROM notes WHERE isSynced = 0")
    suspend fun getAllUnsyncedNotes(): List<Note>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListNotes(listNote: List<Note>)

    @Query("SELECT * FROM locally_deleted_note_ids")
    suspend fun getLocallyDeletedNoteIds(): List<LocallyDeletedNoteId>

    @Query("DELETE FROM locally_deleted_note_ids WHERE deletedNoteId = :deletedNoteId")
    suspend fun deleteLocallyDeletedNoteId(deletedNoteId: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocallyDeletedNoteId(locallyDeletedNoteId: LocallyDeletedNoteId)
}