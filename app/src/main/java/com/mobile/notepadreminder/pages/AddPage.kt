package com.mobile.notepadreminder.pages


import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.DatePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Warning
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
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.mobile.notepadreminder.AlarmReceiver
import com.mobile.notepadreminder.data.Task
import com.mobile.notepadreminder.ui.theme.NoteTheme
import com.mobile.notepadreminder.viewmodels.TaskViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.util.*

@RequiresApi(Build.VERSION_CODES.M)
@Composable
fun AddPage(navController: NavController, vm: TaskViewModel = viewModel()) {
    val scope= rememberCoroutineScope()
    val calendar=Calendar.getInstance().apply {
        timeInMillis=System.currentTimeMillis()
    }

    val title = remember {
        mutableStateOf("")
    }
    val description = remember {
        mutableStateOf("")
    }
    val year= remember {
        mutableStateOf(calendar.get(Calendar.YEAR))
    }

    val month= remember {
        mutableStateOf(calendar.get(Calendar.MONTH)+1)
    }

    val day= remember {
        mutableStateOf(calendar.get(Calendar.DAY_OF_MONTH))
    }

    val hour= remember {
        mutableStateOf(calendar.get(Calendar.HOUR_OF_DAY))
    }

    val minutes= remember {
        mutableStateOf(calendar.get(Calendar.MINUTE))
    }
    val showPopup= remember {
        mutableStateOf(false)
    }
    val msg= remember {
        mutableStateOf("")
    }
    val icon= remember {
        mutableStateOf(Icons.Filled.Done)
    }

    if (showPopup.value){

        PopupMessagePage(msg.value,400.dp,500.dp,showPopup.value,{
                                                                                            showPopup.value=false
        },
            icon.value)
    }
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Agregar Tarea",
            style = NoteTheme.typography.h1
        )

        RowOfThis(title, "Título", true)
        RowOfThis(description, "Descripción", false)
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            DatePick(context = context, year = year, month = month, day = day)
        }
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            TimePick(context = context, hours = hour, minutes = minutes)
        }

        Button(
            onClick = {
                if (title.value.isNullOrBlank() || description.value.isNullOrBlank()
                ) {
                    msg.value="Por favor ingrese todos los datos"
                    icon.value=Icons.Filled.Warning
                    showPopup.value=true
                    return@Button
                }
                scope.launch {

                    calendar.apply {
                        set(Calendar.YEAR,year.value)
                        set(Calendar.MONTH,(month.value-1))
                        set(Calendar.DAY_OF_MONTH,day.value)
                        set(Calendar.HOUR_OF_DAY,hour.value)
                        set(Calendar.MINUTE,minutes.value)
                    }

                    val shuff=(1..99999).shuffled().first()
                    vm.task.value.title = title.value
                    vm.task.value.description = description.value
                    vm.task.value.date = "${day.value}/${month.value}/${year.value}"
                    vm.task.value.time = calendar.timeInMillis
                    vm.task.value.dateCreated= getDateToday()
                    vm.task.value.shuff= shuff

                    vm.createTask(context, vm.task.value)
                    Log.e("tmepickerreceived","date and time  :${year.value}/${month.value}/${day.value} : ${hour.value}:${minutes.value}")


                    val alarmMgr= context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

                    val intent= Intent(context,AlarmReceiver::class.java)
                    intent.action="alarma"
                    intent.putExtra("ALARMA_ID_STRING","$shuff")
                    intent.putExtra("ALARMA_DESCRIPTION_STRING","${description.value}")
                    val pending=
                        PendingIntent.getBroadcast(context,shuff,intent, PendingIntent.FLAG_IMMUTABLE)

                    Log.e("time final","compare ${System.currentTimeMillis()}" +
                            " mytime: ${calendar.timeInMillis} " +
                            "hour: ${calendar.get(Calendar.HOUR_OF_DAY)} " +
                            "minute: ${calendar.get(Calendar.MINUTE)} " +
                            "year: ${calendar.get(Calendar.YEAR)} " +
                            "month: ${calendar.get(Calendar.MONTH)+1} " +
                            "day: ${calendar.get(Calendar.DAY_OF_MONTH)}")

                    alarmMgr?.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,calendar.timeInMillis,pending)
                    msg.value="Recordatorio activado"
                    icon.value=Icons.Filled.Done
                    showPopup.value=true
                    vm.resetTask()
                }

            },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Blue)
        ) {
            Text(text = "Guardar", color = Color.White, style = NoteTheme.typography.subtitle)
        }
    }
}



@Composable
fun RowOfThis(value: MutableState<String>, label: String, single: Boolean) {

    val focusManager = LocalFocusManager.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .border(BorderStroke(width = 1.dp, color = Color.Black)),
            singleLine = single,
            maxLines = 3,
            textStyle = NoteTheme.typography.body,
            value = value.value, onValueChange = {
                value.value = it
            }, label = {
                Text(
                    text = label, textAlign = TextAlign.Start,
                    color = Color.DarkGray,
                    style = NoteTheme.typography.subtitle
                )
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White),
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
            month.value=mMonth+1
            day.value=mDay
        }, year.value, month.value, day.value
    )

    mDatePickerDialog.datePicker.minDate=System.currentTimeMillis() - 1000

    Log.d("datepicker","${day.value}/${month.value}/${year.value}")
    Button(modifier = Modifier
        .padding(7.dp)
        .fillMaxWidth(0.5f), onClick = {
        mDatePickerDialog.show()
    }, colors = ButtonDefaults.buttonColors(backgroundColor = Color(0XFF227093))) {
        Text(text = "Seleccionar fecha",
            color = Color.White)
    }
    Text(text = "${day.value}/${month.value}/${year.value}", fontSize = 22.sp, color = Color(0XFF227093))
}



@Composable
fun TimePick(context: Context,hours:MutableState<Int>,minutes:MutableState<Int>) {

    val mTimePickerDialog = TimePickerDialog(
        context,
        {_, mHour : Int, mMinute: Int ->
            hours.value=mHour
            minutes.value=mMinute
        }, hours.value, minutes.value, false
    )

    Log.d("tmepicker","hour selected :${hours.value}: munute selected: ${minutes.value}")
    Button(modifier = Modifier
        .padding(7.dp)
        .fillMaxWidth(0.5f), onClick = {
        mTimePickerDialog.show()
    }, colors = ButtonDefaults.buttonColors(backgroundColor = Color(0XFF2c2c54))) {
        Text(text = "Seleccionar hora",
            color = Color.White)
    }
    Text(text = "${hours.value}:${minutes.value}", fontSize = 22.sp, color = Color(0XFF2c2c54))
}