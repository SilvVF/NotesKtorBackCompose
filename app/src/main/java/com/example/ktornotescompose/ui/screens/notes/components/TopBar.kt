package com.example.ktornotescompose.ui.screens.notes.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.ktornotescompose.ui.screens.notes.NoteScreenEvent

@Composable
fun TopBar(
    isMenuExpanded: Boolean = false,
    onMenuIconClick: (Boolean) -> Unit,
    onLogoutClick: () -> Unit
) {
        TopAppBar(
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp),
        ) {
            Row(
                Modifier.fillMaxSize()
            ) {
                IconButton(
                    onClick = {
                        onMenuIconClick(isMenuExpanded)
                    },
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = null
                    )
                }
                DropdownMenu(
                    expanded = isMenuExpanded,
                    onDismissRequest = {
                        onMenuIconClick(isMenuExpanded)
                    }
                ) {
                    DropdownMenuItem(
                        onClick = {
                            onLogoutClick()
                        }
                    ) {
                        Text(
                            text = "Logout",
                            color = Color.Red,
                        )
                    }
                }
            }
        }
}