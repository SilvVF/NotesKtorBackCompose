package com.example.ktornotescompose.ui.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.ColorPickerController
import com.github.skydoves.colorpicker.compose.HsvColorPicker

@Composable
fun ColorPickerDialog(
    modifier: Modifier = Modifier,
    controller: ColorPickerController,
    onColorChange: (ColorEnvelope) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = { onDismiss() }
    ) {
        HsvColorPicker(
            modifier = modifier,
            controller = controller,
            onColorChanged = {
                onColorChange(it)
            }
        )
    }
}