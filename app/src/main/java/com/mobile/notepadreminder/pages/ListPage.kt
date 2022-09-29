package com.mobile.notepadreminder.pages

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.*
import com.mobile.notepadreminder.R
import com.mobile.notepadreminder.navigation.Screen
import com.mobile.notepadreminder.ui.theme.NoteTheme

@Composable
fun ListPage(navController: NavController){

    Scaffold(
        topBar = {

        },
        floatingActionButton = {

            FloatingActionButton(
                onClick = {
                    navController.navigate(Screen.add.route)
                },
                backgroundColor = Color.Blue,
                contentColor = Color.Blue,
                elevation = FloatingActionButtonDefaults.elevation(12.dp)
            ) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = "plus",
                    tint = Color.White
                )
            }
        }

    ){
        Column(modifier = Modifier
            .fillMaxSize()
            //.verticalScroll(rememberScrollState())
            .padding(10.dp),
            verticalArrangement = Arrangement.Center) {
            Text(modifier = Modifier.padding(start = 10.dp),
                text = "Resumen",
                color= Color.Black,
                textAlign = TextAlign.Center, style = NoteTheme.typography.bigTitle)
            CardTotals(title = "Tareas completadas", body ="Total de tareas completadas" , number ="0",
                backColor = Color(0xFF2c2c54), click = {
                    Log.d("cardtools","completas")
                })
            CardTotals(title = "Tareas en curso", body ="Total de tareas en curso" , number ="0",
                backColor = Color(0xFFff5252), click = {
                    Log.d("cardtools","curso")
                } )
            CardTotals(title = "Tareas", body ="Total de tareas" , number ="0",
                backColor = Color(0xFF34ace0), click = {
                    Log.d("cardtools","total")
                } )
            CardTotals(title = "Tareas de hoy", body ="Total de tareas de hoy" , number ="0",
                backColor = Color(0xFFccae62) , click = {
                    Log.d("cardtools","hoy")
                })
        }
    }
}


@Composable
fun CardTotals(title:String,body:String,number:String,backColor:Color,click:()->Unit){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(7.dp)
            .height(145.dp).clickable (onClick= click ),
        backgroundColor = backColor,
        elevation = 4.dp,
        shape = RoundedCornerShape(24.dp),
    ) {
        Box(Modifier.padding(16.dp)) {
            Column() {
                Text(
                    text = title,
                    color= Color.White,
                    style = NoteTheme.typography.h2
                )
                Text(
                    text = body,
                    color= Color.White,
                    style = NoteTheme.typography.subtitle
                )
                Text(
                    text = number,
                    color= Color.White,
                    style = NoteTheme.typography.bigTitle
                )
            }
        }
    }
}
