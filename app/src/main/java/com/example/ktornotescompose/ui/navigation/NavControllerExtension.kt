package com.example.ktornotescompose.ui.navigation

import androidx.navigation.NavController

//allows the callback from ui screens to be able to navigate to the next composable
//from the outside
fun NavController.navigate(event: UiEvent.Success){
    this.navigate(event)
}