package com.mobile.notepadreminder.pages


import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.mobile.notepadreminder.AlarmReceiver
import com.mobile.notepadreminder.data.Task
import com.mobile.notepadreminder.navigation.Screen
import com.mobile.notepadreminder.ui.theme.NoteTheme
import com.mobile.notepadreminder.viewmodels.TaskViewModel
import kotlinx.coroutines.launch
import java.util.*

@RequiresApi(Build.VERSION_CODES.M)
@Composable
fun AddPage(navController: NavController,param:Int?, vm: TaskViewModel = viewModel()) {
    val scope= rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(key1 ="uniqueaddpage"){
        vm.loadTask(context, param ?: 0)
    }

    if (vm.showPopup.value){

        PopupMessagePage(vm.msg.value,400.dp,500.dp,vm.showPopup,{
            vm.showPopup.value=false
        },vm.icon.value)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(modifier= Modifier.fillMaxWidth()) {
            IconButton(modifier = Modifier.weight(0.2f),onClick = { navController.popBackStack() }) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "c1")
            }
            Text(modifier = Modifier.weight(0.8f),
                text = "Agregar Tarea",
                style = NoteTheme.typography.h1,
                textAlign = TextAlign.Start,
                fontSize = 34.sp, color = Color(0XFF3867d6)
            )
        }


        Divider(modifier= Modifier
            .padding(start = 7.dp, end = 7.dp)
            .height(1.dp))

        RowOfThis(vm.title, "Título", true,Icons.Filled.Title)
        RowOfThis(vm.description, "Descripción", false,Icons.Filled.Description)
        Row(modifier = Modifier
            .padding(top = 24.dp)
            .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            DatePick(context = context, year = vm.year, month = vm.month, day = vm.day)
        }
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            TimePick(context = context, hours =vm.hour, minutes = vm.minutes)
        }

        Button(modifier= Modifier
            .padding(top = 40.dp, start = 8.dp, end = 8.dp)
            .fillMaxWidth(),
            onClick = {
                if (vm.title.value.isNullOrBlank() || vm.description.value.isNullOrBlank()
                ) {
                    vm.msg.value="Por favor ingrese todos los datos"
                    vm.icon.value=Icons.Filled.Warning
                    vm.showPopup.value=true
                    return@Button
                }
                scope.launch {

                    if (param != null) {
                        vm.deleteTask(context,param)
                        val alarmMgr= context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

                        val intent= Intent(context, AlarmReceiver::class.java)
                        intent.action="alarma"
                        intent.putExtra("ALARMA_ID_STRING","${vm.task.value.shuff}")
                        intent.putExtra("ALARMA_DESCRIPTION_STRING","${vm.task.value.description}")
                        val pending=
                            PendingIntent.getBroadcast(context,vm.task.value.shuff,intent, PendingIntent.FLAG_IMMUTABLE)
                        alarmMgr.cancel(pending)
                    }


                    vm.calendar.apply {
                        set(Calendar.YEAR,vm.year.value)
                        set(Calendar.MONTH,(vm.month.value-1))
                        set(Calendar.DAY_OF_MONTH,vm.day.value)
                        set(Calendar.HOUR_OF_DAY,vm.hour.value)
                        set(Calendar.MINUTE,vm.minutes.value)
                    }

                    val shuff=(1..99999).shuffled().first()
                    vm.task.value.title = vm.title.value
                    vm.task.value.description = vm.description.value
                    vm.task.value.date = "${vm.day.value}/${vm.month.value}/${vm.year.value}"
                    vm.task.value.time = vm.calendar.timeInMillis
                    vm.task.value.dateCreated= getDateToday()
                    vm.task.value.shuff= shuff

                    vm.createTask(context, vm.task.value)

                    val alarmMgr= context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

                    val intent= Intent(context,AlarmReceiver::class.java)
                    intent.action="alarma"
                    intent.putExtra("ALARMA_ID_STRING","$shuff")
                    intent.putExtra("ALARMA_DESCRIPTION_STRING","${vm.description.value}")
                    val pending=
                        PendingIntent.getBroadcast(context,shuff,intent, PendingIntent.FLAG_IMMUTABLE)


                    alarmMgr?.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,vm.calendar.timeInMillis,pending)
                    vm.msg.value= if(param!=null&&param>0) "Recordatorio reconfigurado" else "Recordatorio activado"
                    vm.icon.value=Icons.Filled.Done
                    vm.showPopup.value=true
                    vm.resetTask()
                }

            },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(0XFF3867d6))
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                Text(text = "Guardar",
                    color = Color.White,
                    style = NoteTheme.typography.subtitle)
                Icon(imageVector = Icons.Filled.Save,
                    contentDescription ="gdi", tint = Color(0XFF25CCF7) )

            }
        }
    }

    if (vm.gotoList.value){
        vm.gotoList.value=false
        navController.navigate(Screen.lis.route){
            popUpTo(Screen.lis.route){
                inclusive=true
            }
        }
    }
}



@Composable
fun RowOfThis(value: MutableState<String>, label: String, single: Boolean,icon:ImageVector) {

    val focusManager = LocalFocusManager.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            singleLine = single,
            maxLines = 1,
            textStyle = NoteTheme.typography.body,
            value = value.value, onValueChange = {
                value.value = it
            }, label = {
                Text(
                    text = label, textAlign = TextAlign.Start,
                    color = Color(0XFF2c2c54),
                    style = NoteTheme.typography.subtitle
                )
            }, trailingIcon = {
                Icon(imageVector = icon, contentDescription = "itil",
                    tint = Color(0XFF2c2c54))},
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.White,
                focusedLabelColor = Color.Gray,
            focusedIndicatorColor = Color.Gray),
            keyboardActions = KeyboardActions(onNext =
            { focusManager.moveFocus((FocusDirection.Next)) })
        )
    }
}


@Composable
fun DatePick(context: Context, year: MutableState<Int>,month: MutableState<Int>,day: MutableState<Int>) {

    val mDatePickerDialog = DatePickerDialog(
        context,{d,mYear,mMonth,mDay ->
            year.value=mYear
            month.value=mMonth
            day.value=mDay
        }, year.value, month.value, day.value
    )

    mDatePickerDialog.datePicker.minDate=System.currentTimeMillis() - 1000

    Column(modifier = Modifier.fillMaxWidth(),
    horizontalAlignment = Alignment.CenterHorizontally) {
        Button(modifier = Modifier
            .padding(7.dp), onClick = {
            mDatePickerDialog.show()
        }, colors = ButtonDefaults.buttonColors(backgroundColor = Color(0XFF2c2c54))) {
            Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                Icon(imageVector = Icons.Filled.DateRange,
                    contentDescription ="wdi", tint = Color(0XFF3867d6) )
                Text(text = "Seleccionar fecha",
                    color = Color.White,
                    style = NoteTheme.typography.subtitle)
            }
        }
        Text(modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = "${day.value}/${month.value}/${year.value}",
            fontSize = 32.sp, color = Color.Gray,
            style = NoteTheme.typography.subtitle)
    }

}



@Composable
fun TimePick(
    context: Context,
    hours: MutableState<Int>,
    minutes: MutableState<Int>
) {

    val mTimePickerDialog = TimePickerDialog(
        context,
        {_, mHour : Int, mMinute: Int ->
            hours.value=mHour
            minutes.value=mMinute
        }, hours.value, minutes.value, false
    )

    Column(modifier = Modifier.fillMaxWidth(),
    horizontalAlignment = Alignment.CenterHorizontally) {
        Button(modifier = Modifier
            .padding(7.dp), onClick = {
            mTimePickerDialog.show()
        }, colors = ButtonDefaults.buttonColors(backgroundColor = Color(0XFF2c2c54))) {
            Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                Icon(imageVector = Icons.Filled.Timer, contentDescription ="hhfdi",
                    tint = Color(0XFF3867d6) )
                Text(text = "Seleccionar hora",
                    color = Color.White,
                    style = NoteTheme.typography.subtitle)
            }
        }
        Text(modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,text = "${hours.value}:${minutes.value}",
            fontSize = 32.sp,color= Color.Gray,
            style = NoteTheme.typography.subtitle)
    }
}
