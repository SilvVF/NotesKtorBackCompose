package com.example.ktornotescompose.ui.screens.notes.components

import androidx.compose.foundation.background
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ktornotescompose.ui.screens.notes.NoteScreenEvent
import kotlinx.coroutines.flow.collect

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
                    },
                    Modifier.background(Color.White)
                ) {
                    DropdownMenuItem(
                        onClick = {
                            onLogoutClick()
                        },
                    ) {
                        Text(
                            text = "Logout",
                            color = Color.Red,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
}