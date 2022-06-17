package com.example.ktornotescompose.ui.navigation

import androidx.compose.material.DismissState
import androidx.compose.material.ExperimentalMaterialApi
import com.example.ktornotescompose.data.local.entities.Note

//define events to send from view models to composable
sealed class UiEvent {
    //navigate to route using  this
    object Success: UiEvent()
    object NavigateUp: UiEvent()
    data class ShowSnackBar(val message: UiText): UiEvent()
    data class ShowSnackBarWithUndo (val message: UiText, val data: Note): UiEvent()
}
