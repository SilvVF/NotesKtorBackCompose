package com.example.ktornotescompose

import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ktornotescompose.data.remote.BasicAuthInterceptor
import com.example.ktornotescompose.ui.navigation.Routes
import com.example.ktornotescompose.ui.screens.addeditnote.AddEditNoteScreen
import com.example.ktornotescompose.ui.screens.auth.AuthScreen
import com.example.ktornotescompose.ui.screens.notedetail.NoteDetailScreen
import com.example.ktornotescompose.ui.screens.notes.NoteScreen
import com.example.ktornotescompose.ui.theme.KtorNotesComposeTheme
import com.example.ktornotescompose.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity: ComponentActivity() {
    @Inject
    lateinit var basicAuthInterceptor: BasicAuthInterceptor
    @Inject
    lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KtorNotesComposeTheme {
                val navController = rememberNavController()
                val scaffoldState = rememberScaffoldState()
                Scaffold(
                    scaffoldState = scaffoldState
                ) { paddingValues ->
                    paddingValues.toString()
                    NavHost(
                        navController = navController,
                        startDestination = if (isLoggedIn()) Routes.NOTES_ROUTE else Routes.AUTH_ROUTE
                    ) {
                        composable(Routes.AUTH_ROUTE) {
                            AuthScreen(
                                scaffoldState = scaffoldState,
                                OnSuccessfulLogin = {
                                    navController.popBackStack()
                                    navController.navigate(Routes.NOTES_ROUTE)
                                }
                            )
                        }
                        composable(
                            route = Routes.NOTES_ROUTE,
                        ){
                            NoteScreen(
                                scaffoldState = scaffoldState,
                                onNoteClicked = { noteID ->
                                    navController.navigate(Routes.NOTE_DETAL_ROUTE + noteID)
                                },
                                onNavigate = { route ->
                                    navController.navigate(route)
                                },
                            )
                        }
                        composable(
                            route = Routes.NOTE_DETAL_ROUTE + "{noteID}",
                            arguments = listOf(navArgument("noteID") { type = NavType.StringType })
                        ) {
                            val id = it.arguments?.getString("noteID") ?: ""
                            NoteDetailScreen(
                                scaffoldState = scaffoldState, noteID = id,
                                navigateToEdit = { noteId ->
                                    navController.popBackStack()
                                    navController.navigate(Routes.ADD_EDIT_NOTE_ROUTE + noteId)
                                }
                            )
                        }
                        composable(
                            route = Routes.ADD_EDIT_NOTE_ROUTE+ "{noteID}",
                            arguments = listOf(navArgument("noteID") { type = NavType.StringType })
                        ){
                            val id = it.arguments?.getString("noteID") ?: ""
                            AddEditNoteScreen(
                                scaffoldState = scaffoldState,
                                noteId = id
                            )
                        }
                    }
                }
            }
        }
    }
    private fun isLoggedIn(): Boolean {
        val currEmail = sharedPref.getString(Constants.KEY_LOGGED_IN_EMAIL, Constants.NO_EMAIL) ?: Constants.NO_EMAIL
        val currPassword = sharedPref.getString(Constants.KEY_LOGGED_IN_PASSWORD, Constants.NO_PASSWORD) ?: Constants.NO_PASSWORD
        if (currEmail != Constants.NO_EMAIL && currPassword != Constants.NO_PASSWORD) {
            basicAuthInterceptor.password = currPassword
            basicAuthInterceptor.email = currEmail
            return true
        }
        return false
    }
}

