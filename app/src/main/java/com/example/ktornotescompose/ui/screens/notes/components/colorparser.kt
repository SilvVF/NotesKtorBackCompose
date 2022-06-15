package com.example.ktornotescompose.ui.screens.notes.components
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.isSpecified


object HexToJetpackColor {
    fun getColor(colorString: String): Color {
        return try {
            Color(android.graphics.Color.parseColor("#$colorString"))
        } catch (e: Exception) {
            Color.Cyan
        }
    }
}