package com.example.ktornotescompose.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.ktornotescompose.data.local.entities.LocallyDeletedNoteId
import com.example.ktornotescompose.data.local.entities.Note

@Database(
    entities = [Note::class, LocallyDeletedNoteId::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class NotesDatabase: RoomDatabase() {

    abstract fun noteDao(): NoteDao
}