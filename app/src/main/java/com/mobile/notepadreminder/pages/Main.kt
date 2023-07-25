package com.mobile.notepadreminder.pages


import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.Absolute.Center
import androidx.compose.foundation.layout.Arrangement.Center
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.HorizontalAlignmentLine
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.os.bundleOf
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.airbnb.lottie.compose.*
import  com.mobile.notepadreminder.R
import com.mobile.notepadreminder.data.Task
import com.mobile.notepadreminder.data.TaskDatabase
import com.mobile.notepadreminder.navigation.Screen
import com.mobile.notepadreminder.ui.theme.NoteTheme
import com.mobile.notepadreminder.viewmodels.TaskViewModel
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import java.util.UUID

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainPage(navController: NavController, vm: TaskViewModel = viewModel()) {

    val context = LocalContext.current

    LaunchedEffect(key1 = "main") {

        vm.conteoCompletas(context)
        vm.conteoEnCurso(context)
        vm.conteodeHoy(context, getDateToday())
        vm.loadTask(context = context,0)

    }

    if (vm.goto.value) {
        vm.goto.value = false
        navController.navigate(Screen.lis.route){
            popUpTo(Screen.lis.route){
                inclusive=true
            }
        }
    }

    Scaffold(
        topBar = {

        },
        floatingActionButton = {

            FloatingActionButton(
                onClick = {
                    val entero:Int=0;
                    navController.navigate("add/$entero"){
                        popUpTo("add/$entero"){
                            inclusive=true
                        }
                    }
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

    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(2.dp), verticalAlignment = Alignment.CenterVertically
            ) {
                val composition by rememberLottieComposition(
                    LottieCompositionSpec
                        .RawRes(R.raw.task)
                )

                var isLottiePlaying by remember {
                    mutableStateOf(true)
                }
                var animationSpeed by remember {
                    mutableStateOf(1f)
                }

                // to control the lottie animation
                val lottieAnimation by animateLottieCompositionAsState(
                    // pass the composition created above
                    composition,
                    // Iterates Forever
                    iterations = LottieConstants.IterateForever,
                    // Lottie and pause/play
                    isPlaying = isLottiePlaying,
                    // Increasing the speed of change Lottie
                    speed = animationSpeed,
                    restartOnPlay = false
                )

                LottieAnimation(composition = composition, lottieAnimation)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "No hay tareas todavía",
                        textAlign = TextAlign.Center,
                        style = NoteTheme.typography.h1
                    )
                    Text(
                        text = "Por favor agregue una tarea",
                        textAlign = TextAlign.Center,
                        style = NoteTheme.typography.body
                    )
                    Button(onClick = {
                        val entero:Int=0;
                        navController.navigate("add/$entero"){
                            popUpTo("add/$entero"){
                                inclusive=true
                            }
                        }
                    }, colors = ButtonDefaults.buttonColors(backgroundColor = Color.Blue)) {
                        Text(
                            text = "Agregar Tarea",
                            color = Color.White,
                            style = NoteTheme.typography.subtitle
                        )
                    }
                }
            }
        }
    }
}