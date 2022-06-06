package com.example.ktornotescompose.data.remote.requests

data class AddOwnerRequest(
    val noteID: String,
    val owner: String
)