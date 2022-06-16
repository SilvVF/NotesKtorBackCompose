package com.example.ktornotescompose.ui.screens.notes.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.InspectableModifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ktornotescompose.data.local.entities.Note
import java.text.SimpleDateFormat
import java.util.*


@Composable
fun NoteItem(
    modifier: Modifier = Modifier.fillMaxWidth(),
    note: Note,
    onClick: () -> Unit
) {
    val dateFormat = SimpleDateFormat("dd.MM.yy, HH:mm", Locale.getDefault())
    Row(
        modifier = modifier
            .background(Color.DarkGray)
            .clickable {
                onClick()
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        Box(
            Modifier.fillMaxHeight().width(50.dp),
            contentAlignment = Alignment.TopStart
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .offset(x = (-26).dp),
            ) {

                val canvasWidth = size.width
                val canvasHeight = size.height

                drawArc(
                    HexToJetpackColor.getColor(note.color),
                    startAngle = -90f,
                    sweepAngle = 180f,
                    useCenter = true,
                    size = Size(canvasWidth, 2 * canvasHeight)
                )
            }
        }
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.5f)
            ) {
                Text(
                    text = note.title,
                    fontSize = 42.sp,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    color = Color.White
                )
                Text(
                    text = dateFormat.format(note.date),
                    color = Color.LightGray
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.7f)
                    .offset(x = 40.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.End
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Icon(
                        imageVector = if (note.isSynced)
                            Icons.Default.Check else Icons.Default.Close,
                        contentDescription = null,
                        tint = if (note.isSynced) Color.Green else Color.Red
                    )
                    Text(
                        text = stringResource(id = com.example.ktornotescompose.R.string.synced)
                    )
                }
            }
    }
}

