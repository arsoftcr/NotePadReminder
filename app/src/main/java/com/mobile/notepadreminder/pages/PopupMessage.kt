package com.mobile.notepadreminder.pages

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.mobile.notepadreminder.ui.theme.taskText

@Composable
fun PopupMessagePage(message:String,
                     height: Dp,
                     width:Dp,
                     openDialog:MutableState<Boolean>,
                     onDismiss: ()->Unit,
                     icon: ImageVector
){
    Column(modifier = Modifier.fillMaxWidth()) {
        val dialogWidth = width/(1.3F)
        val dialogHeight = height/2

        if (openDialog.value) {
            Dialog(onDismissRequest = onDismiss) {
                // Draw a rectangle shape with rounded corners inside the dialog
                Box(
                    Modifier
                        .size(dialogWidth, dialogHeight)
                        .background(Color.White)
                        .padding(10.dp)){
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End) {
                            IconButton(onClick = {
                                openDialog.value = false
                            }) {
                                Icon(
                                    modifier=Modifier,
                                    imageVector = Icons.Filled.Close ,
                                    contentDescription ="closebtn", tint = Color.Red)
                            }
                        }
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier
                                .fillMaxWidth()
                                .verticalScroll(rememberScrollState())) {
                                Text(
                                    modifier = Modifier.fillMaxSize(),
                                    text = message,
                                    textAlign = TextAlign.Center,
                                    style = TextStyle(
                                        color = taskText,
                                        fontSize = 30.sp)
                                )
                            }
                            Icon(modifier= Modifier
                                .fillMaxWidth()
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
}

@SuppressLint("UnrememberedMutableState")
@Composable
@Preview
fun PreviewPopupMessagePage(){
    val state= mutableStateOf(true)
    PopupMessagePage(
        message = "test",
        height =400.dp ,
        width = 500.dp,
        openDialog = state,
        onDismiss = {  },
        icon = Icons.Filled.Done
    )
}