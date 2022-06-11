package com.example.ktornotescompose.ui.screens.auth.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.ktornotescompose.R

@Composable
fun AuthHeader(
    modifier: Modifier = Modifier,
    textSize: Int = 28,
    textColor: Color = Color.White
) {
   Box(
       modifier = modifier,
       contentAlignment = Alignment.Center
   ) {
       Text(
           text = stringResource(id = R.string.login_existing_account),
           fontSize = textSize.sp,
           color = textColor,
           fontWeight = FontWeight.SemiBold,
           textAlign = TextAlign.Center
       )
   }
}