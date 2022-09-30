package com.mobile.notepadreminder.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.mobile.notepadreminder.ui.theme.taskText

@Composable
fun PopupMessagePage(message:String,
                     height: Dp,
                     width:Dp,
                     openDialog:Boolean,
                     onDismiss: ()->Unit,
                     icon: ImageVector
){
    Column(modifier = Modifier.fillMaxWidth()) {
        val dialogWidth = width/(1.3F)
        val dialogHeight = height/2

        if (openDialog) {
            Dialog(onDismissRequest = onDismiss) {
                // Draw a rectangle shape with rounded corners inside the dialog
                Box(
                    Modifier
                        .size(dialogWidth, dialogHeight)
                        .background(Color.White)
                        .padding(10.dp)){
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(text = message,
                            style = TextStyle(color = taskText,
                                fontSize = 30.sp)
                        )
                        Icon(modifier=Modifier.fillMaxWidth()
                            .size(150.dp),
                            imageVector= icon,
                            contentDescription = "icon",
                            tint = Color.Blue)
                    }
                }
            }
        }

    }
}