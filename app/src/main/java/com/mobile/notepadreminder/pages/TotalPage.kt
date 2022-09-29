package com.mobile.notepadreminder.pages

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.mobile.notepadreminder.data.Task
import com.mobile.notepadreminder.ui.theme.NoteTheme
import com.mobile.notepadreminder.ui.theme.taskText
import com.mobile.notepadreminder.viewmodels.TaskViewModel

@Composable
fun TotalPage(navController: NavController, vm: TaskViewModel = viewModel()) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFf1f2f6))
            .padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        val context = LocalContext.current
        LaunchedEffect(key1 = "totallaunch") {
            vm.loadTask(context = context)
        }
        Text(
            text = "Total de tareas: ${vm.list.value.size}",
            style = NoteTheme.typography.h1
        )
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(vm.list.value.size) { index ->

                val task = vm.list.value[index]

                CardTotal(task = task, vm = vm, context = context)
                Divider(modifier = Modifier.padding(start = 24.dp, end = 24.dp))

            }
        }
    }
}

@Composable
fun CardTotal(task: Task, vm: TaskViewModel, context: Context) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
    ) {
        val state = remember {
            mutableStateOf(task.iscompleted==1)
        }
        Log.e("CardComplete", "CardComplete")

        Column() {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = state.value,
                    onCheckedChange = {
                        Toast.makeText(context,"No se puede realizar la operación que desea esta ventana",Toast.LENGTH_LONG).show() }
                )
                Text(
                    modifier = Modifier.padding(start = 24.dp, end = 24.dp),
                    text = task.description,
                    maxLines = 1,
                    color = taskText,
                    textDecoration= if (task.iscompleted==1){ TextDecoration.LineThrough} else {TextDecoration.None},
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