package com.example.ktornotescompose.util

object Constants {

    val IGNORE_AUTH_URLS = listOf("/login", "/register")

    const val DATABASE_NAME = "notes_db"

    const val BASE_URL = "http://10.0.2.2:8001" //android uses this for the emulator

    const val ENCRYPTED_SHARED_PREF = "enc_shared_pref"

    const val KEY_LOGGED_IN_EMAIL = "KEY_LOGGED_IN_EMAIL"
    const val KEY_LOGGED_IN_PASSWORD = "KEY_LOGGED_IN_PASSWORD"
}