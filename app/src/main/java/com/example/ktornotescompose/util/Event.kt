package com.example.ktornotescompose.util

open class Event<out T>(val content: T) {

    var hasBeenHandled: Boolean = false
        private set

    private fun getContentIfNotHandled() =
        if (hasBeenHandled) null
        else {
            hasBeenHandled = true
            content
        }

    fun peekContent() = content
}