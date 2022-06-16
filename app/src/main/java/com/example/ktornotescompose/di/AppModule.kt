package com.example.ktornotescompose.di


import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.ktornotescompose.data.local.NoteDao
import com.example.ktornotescompose.data.local.NotesDatabase
import com.example.ktornotescompose.data.remote.BasicAuthInterceptor
import com.example.ktornotescompose.data.remote.NoteApi
import com.example.ktornotescompose.repositories.NoteRepository
import com.example.ktornotescompose.repositories.NoteRepositoryImpl
import com.example.ktornotescompose.util.Constants.BASE_URL
import com.example.ktornotescompose.util.Constants.DATABASE_NAME
import com.example.ktornotescompose.util.Constants.ENCRYPTED_SHARED_PREF
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNotesDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
            context,
            NotesDatabase::class.java,
            DATABASE_NAME
        ).build()

    @Provides
    @Singleton
    fun provideNoteDao(db: NotesDatabase) = db.noteDao()

    @Provides
    @Singleton
    fun provideBasicAuthInterceptor(): BasicAuthInterceptor {
        return BasicAuthInterceptor()
    }

    @Provides
    @Singleton
    fun provideNoteApi(
        basicAuthInterceptor: BasicAuthInterceptor
    ): NoteApi {
        val client = OkHttpClient.Builder()
            .addInterceptor(basicAuthInterceptor)
            .build()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(NoteApi::class.java)
    }

    @Provides
    @Singleton
    fun provideEncryptedSharedPreferences(
        @ApplicationContext context: Context
    ): SharedPreferences {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        return EncryptedSharedPreferences.create(
            context,
            ENCRYPTED_SHARED_PREF,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    @Provides
    @Singleton
    fun provideNoteRepository(
        noteDao: NoteDao,
        noteApi: NoteApi,
        context: Application
    ): NoteRepository {
        return NoteRepositoryImpl(
            noteDao = noteDao,
            noteApi = noteApi,
            context = context
        )
    }
}