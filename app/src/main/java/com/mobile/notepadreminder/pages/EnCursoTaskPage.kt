package com.mobile.notepadreminder.pages

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.mobile.notepadreminder.data.Task
import com.mobile.notepadreminder.ui.theme.NoteTheme
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
        Text(
            text = "Total de tareas pendientes: ${vm.list.value.size}",
            style = NoteTheme.typography.h1
        )
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(vm.list.value.size) { index ->
                val task = vm.list.value[index]
                if (task.iscompleted==0){
                    CardEnCurso(task = task, vm = vm, context = context)
                    Divider(modifier = Modifier.padding(start = 24.dp, end = 24.dp))
                }
            }
        }
    }
}

@Composable
fun CardEnCurso(task: Task, vm: TaskViewModel, context: Context) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
    ) {
        val state = remember {
            mutableStateOf(false)
        }
        Column() {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = state.value,
                    onCheckedChange = {
                        state.value = it
                        if (state.value) {
                            Log.e("state", "procesar ${task.id} to ${state.value}")
                            vm.updateTask(context = context, task.id, 1)
                            vm.loadencurso(context)
                        }
                    }
                )
                Text(
                    modifier = Modifier.padding(start = 24.dp, end = 24.dp),
                    text = task.description,
                    maxLines = 1,
                    color = taskText,
                    style = NoteTheme.typography.subtitle,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                Text(
                    modifier = Modifier.padding(start = 24.dp, end = 24.dp),
                    text = task.date,
                    maxLines = 1,
                    color = taskText,
                    style = NoteTheme.typography.h1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}


