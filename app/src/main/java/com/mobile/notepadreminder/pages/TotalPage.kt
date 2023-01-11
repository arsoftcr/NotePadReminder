package com.mobile.notepadreminder.pages

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.materialIcon
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
import com.mobile.notepadreminder.AlarmReceiver
import com.mobile.notepadreminder.data.Task
import com.mobile.notepadreminder.ui.theme.NoteTheme
import com.mobile.notepadreminder.ui.theme.taskText
import com.mobile.notepadreminder.viewmodels.TaskViewModel
import java.util.Calendar

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
        val showPopup= remember {
            mutableStateOf(false)
        }
        val msg= remember {
            mutableStateOf("")
        }
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

            val cal=Calendar.getInstance().apply {
                timeInMillis= task.time
            }
            Row(
                modifier=Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                val AMPM =if(cal.get(Calendar.AM_PM)==Calendar.AM){
                            "AM"
                        } else {
                            "PM"
                        }
                Text(
                    modifier = Modifier.padding(start = 24.dp),
                    text = "${task.date} : ${cal.get(Calendar.HOUR_OF_DAY)}: ${cal.get(Calendar.MINUTE)} $AMPM",
                    maxLines = 1,
                    color = taskText,
                    style = NoteTheme.typography.h1,
                    overflow = TextOverflow.Ellipsis,
                )
                IconButton(modifier = Modifier.padding(), onClick = {
                    vm.deleteTask(context,task.id)
                    val alarmMgr= context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

                    val intent= Intent(context, AlarmReceiver::class.java)
                    intent.action="alarma"
                    intent.putExtra("ALARMA_ID_STRING","${task.shuff}")
                    intent.putExtra("ALARMA_DESCRIPTION_STRING","${task.description}")
                    val pending=
                        PendingIntent.getBroadcast(context,task.shuff,intent, PendingIntent.FLAG_IMMUTABLE)
                    alarmMgr.cancel(pending)
                    vm.loadTask(context)
                    showPopup.value=true
                    msg.value="Recordatorio desactivado"
                }) {
                    Icon(
                        Icons.Filled.DeleteForever,
                        contentDescription = "Remove",
                        tint= Color(0xFFff4757),
                        modifier = Modifier.size(36.dp)
                    )
                }
            }
        }
        if (showPopup.value){
            PopupMessagePage(msg.value,400.dp,500.dp,showPopup,{
                showPopup.value=false
            },
                Icons.Filled.DeleteForever)
        }
    }
}

