package com.example.ktornotescompose.data.local.entities

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import java.util.*

@Entity(tableName = "notes")
data class Note(
    val title: String,
    val content: String,
    val date: Long, //will be a timestamp
    val owners: List<String>, //multiple emails of people allowed to edit
    val color: String, // hex value
    @PrimaryKey(autoGenerate = false)
    val id: String = UUID.randomUUID().toString(),
    @Expose(deserialize = false, serialize = false) //make sure that wont be included with any requests while parsing with retrofit
    var isSynced: Boolean = false // is it synced with the server
)
