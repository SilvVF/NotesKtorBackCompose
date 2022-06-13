package com.example.ktornotescompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ktornotescompose.ui.navigation.Routes
import com.example.ktornotescompose.ui.screens.addeditnote.AddEditNoteScreen
import com.example.ktornotescompose.ui.screens.auth.AuthScreen
import com.example.ktornotescompose.ui.screens.notedetail.NoteDetailScreen
import com.example.ktornotescompose.ui.screens.notes.NoteScreen
import com.example.ktornotescompose.ui.theme.KtorNotesComposeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KtorNotesComposeTheme {

                val navController = rememberNavController()
                val scaffoldState = rememberScaffoldState()
                Scaffold(
                    scaffoldState = scaffoldState
                ) {
                    NavHost(navController = navController, startDestination = Routes.AUTH_ROUTE) {
                        composable(Routes.AUTH_ROUTE) {
                            AuthScreen(
                                scaffoldState = scaffoldState,
                                OnSuccessfulLogin = {
                                    navController.popBackStack()
                                    navController.navigate(Routes.NOTES_ROUTE)
                                }
                            )

                        }
                        composable(Routes.NOTES_ROUTE){
                            NoteScreen(scaffoldState = scaffoldState)
                        }
                        composable(Routes.NOTE_DETAL_ROUTE){
                            NoteDetailScreen(scaffoldState = scaffoldState)
                        }
                        composable(Routes.ADD_EDIT_NOTE_ROUTE){
                            AddEditNoteScreen(scaffoldState = scaffoldState)
                        }
                    }
                }
            }
        }
    }
}

