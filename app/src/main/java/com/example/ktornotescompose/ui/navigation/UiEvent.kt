package com.example.ktornotescompose.ui.navigation

//define events to send from view models to composable
sealed class UiEvent {
    //navigate to route using  this
    object Success: UiEvent()
    object NavigateUp: UiEvent()
    data class ShowSnackBar(val message: UiText): UiEvent()
}
