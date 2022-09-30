package com.mobile.notepadreminder.pages

import android.content.ClipDescription
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
import androidx.compose.material.Divider
import androidx.compose.material.Text
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

        LazyColumn(modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            content = {
            items(items = vm.list.value, itemContent = {task->
                val taskState = remember {
                    mutableStateOf(task.iscompleted==1)
                }
                if (task.iscompleted==1){
                    CardComplete(state = taskState,
                        description = task.description,
                        date = task.date, idT = task.id,vm,context)
                }
            })
        })
    }
}

@Composable
fun CardComplete(state: MutableState<Boolean>,
                 description: String,
                 date:String,
                 idT:Int,vm: TaskViewModel,context: Context) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
    ) {
        Log.e("CardComplete", "CardComplete ${state.value} to ")
        if (state.value){
            Column() {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = state.value,
                        onCheckedChange = {
                            Log.e("it", "it ${state.value} to ${it}")
                            state.value=!state.value

                            if (state.value){
                                Log.e("conchanged","completed")
                                return@Checkbox
                            }
                            vm.updateTask(context =context,id=idT, completed = 0)
                        }
                    )
                    Text(
                        modifier = Modifier.padding(start = 24.dp, end = 24.dp),
                        text = description,
                        maxLines = 1,
                        color = taskText,
                        textDecoration = TextDecoration.LineThrough,
                        style = NoteTheme.typography.subtitle,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.padding(start = 24.dp, end = 24.dp),
                        text = date,
                        maxLines = 1,
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        style = NoteTheme.typography.h1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }

    }
}


