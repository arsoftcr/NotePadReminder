package com.mobile.notepadreminder.pages

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.mobile.notepadreminder.data.Task
import com.mobile.notepadreminder.ui.theme.NoteTheme
import com.mobile.notepadreminder.ui.theme.Purple500
import com.mobile.notepadreminder.ui.theme.Purple700
import com.mobile.notepadreminder.ui.theme.Teal200
import com.mobile.notepadreminder.ui.theme.shapire
import com.mobile.notepadreminder.ui.theme.taskText
import com.mobile.notepadreminder.viewmodels.TaskViewModel
import kotlinx.coroutines.launch

@Composable
fun EnCursoTaskPage(navController: NavController, vm: TaskViewModel = viewModel()) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFf1f2f6))
            .padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        val context = LocalContext.current
        LaunchedEffect(key1 = "encursolaunch") {
            vm.loadencurso(context = context)
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            IconButton(modifier = Modifier.weight(0.2f),onClick = { navController.popBackStack() }) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "c1")
            }
            Text(modifier = Modifier.weight(0.8f),
                text = "Total de tareas en curso: ${vm.list.value.size}",
                style = NoteTheme.typography.h1
            )
        }


        LazyColumn(modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            content = {
                items(items = vm.list.value, itemContent = {task->
                    val taskState = remember {
                        mutableStateOf(task.iscompleted==1)
                    }
                    if (task.iscompleted==0){
                        CardEnCurso(state = taskState,
                            description = task.description,
                            date = task.date, idT = task.id,vm,context,navController)
                    }
                })
            })
    }
}

@Composable
fun CardEnCurso(state: MutableState<Boolean>,
                description: String,
                date:String,
                idT:Int,vm: TaskViewModel,context: Context,navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
    ) {
        if (!state.value){
            Column() {
              Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = state.value,
                        onCheckedChange = {

                            state.value=!state.value

                            if (!state.value){
                                return@Checkbox
                            }
                            vm.updateTask(context =context,id=idT, completed = 1)
                        }
                    )
                    Text(
                        modifier = Modifier.padding(start = 24.dp, end = 24.dp),
                        text = description,
                        maxLines = 1,
                        color = taskText,
                        textDecoration = TextDecoration.None,
                        style = NoteTheme.typography.subtitle,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier
                            .padding(start = 24.dp, end = 24.dp)
                            .weight(0.6f),
                        text = date,
                        maxLines = 1,
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        style = NoteTheme.typography.h1,
                        overflow = TextOverflow.Ellipsis
                    )

                   Button(onClick = {
                       navController.navigate("add/$idT"){
                           popUpTo("add/$idT"){
                               inclusive=true
                           }
                       }
                   },modifier = Modifier
                       .weight(0.4f), colors = ButtonDefaults.buttonColors(backgroundColor = shapire)
                   ) {
                        Text(text = "Modificar", color = Color.White)
                    }
                }


            }
        }

    }
}


