package com.mobile.notepadreminder.pages

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
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
fun CompleteTaskPage(navController: NavController, vm: TaskViewModel = viewModel()) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFf1f2f6))
            .padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        val context = LocalContext.current
        LaunchedEffect(key1 = "completedlaunch") {
            vm.loadcomplete(context = context)
        }
        Text(
            text = "Total de tareas completadas: ${vm.list.value.size}",
            style = NoteTheme.typography.h1
        )
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(vm.list.value.size) { index ->
                val taskState = remember {
                    mutableStateOf(vm.list.value[index])
                }
                if (taskState.value.iscompleted == 1) {
                    CardComplete(task = taskState, vm = vm, context = context)
                    Divider(modifier = Modifier.padding(start = 24.dp, end = 24.dp))
                }
            }
        }
    }
}

@Composable
fun CardComplete(task: MutableState<Task>, vm: TaskViewModel, context: Context) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
    ) {

        Log.e("CardComplete", "CardComplete")

        Column() {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = task.value.iscompleted == 1,
                    onCheckedChange = {
                        Log.e("it", "it ${task.value.id} to ${it}")

                        Log.e("state", "procesar ${task.value.id} to ${task.value.iscompleted}")
                        vm.updateTask(context = context, task.value.id, 0)
                        vm.loadcomplete(context)
                    }
                )
                Text(
                    modifier = Modifier.padding(start = 24.dp, end = 24.dp),
                    text = task.value.description,
                    maxLines = 1,
                    color = taskText,
                    textDecoration = TextDecoration.LineThrough,
                    style = NoteTheme.typography.subtitle,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = Modifier.padding(start = 24.dp, end = 24.dp),
                    text = task.value.date,
                    maxLines = 1,
                    color = taskText,
                    style = NoteTheme.typography.h1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}


