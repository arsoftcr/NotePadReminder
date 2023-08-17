package com.mobile.notepadreminder.viewmodels

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobile.notepadreminder.data.Task
import com.mobile.notepadreminder.data.TaskDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.log

class TaskViewModel(): ViewModel() {

    var calendar= Calendar.getInstance().apply {
        timeInMillis=System.currentTimeMillis()
    }
    val title = mutableStateOf("")

    val description =  mutableStateOf("")

    var year= mutableStateOf(calendar.get(Calendar.YEAR))

    var month= mutableStateOf(calendar.get(Calendar.MONTH))

    var day= mutableStateOf(calendar.get(Calendar.DAY_OF_MONTH))

    var hour=  mutableStateOf(calendar.get(Calendar.HOUR_OF_DAY))

    var minutes= mutableStateOf(calendar.get(Calendar.MINUTE))

    val showPopup= mutableStateOf(false)

    val msg=  mutableStateOf("")

    val icon=mutableStateOf(Icons.Filled.Done)

    val gotoList= mutableStateOf(false)


     val tasks:List<Task> = mutableListOf()
     var list= mutableStateOf(tasks)
    val completas= mutableStateOf(0)
    val total= mutableStateOf(0)
    val dehoy= mutableStateOf(0)
    val enCursoCount= mutableStateOf(0)
    val goto= mutableStateOf(false)

    var task= mutableStateOf(Task(0,"","","","",0,0, shuff = 0))

    init {
        gotoList.value = false
    }
    private  fun clean(){
        list.value= mutableListOf()
    }
     fun loadTask(context: Context,taskId:Int){
        viewModelScope.launch {
            try{
                clean()
                list.value= withContext(Dispatchers.IO){
                    TaskDatabase.getDatabase(context).taskDao().select()
                }
                total.value=list.value.count()
                if (taskId>0&&total.value>0){
                    val received= list.value.find { it.id==taskId }
                    if (received!=null){
                        task.value=received
                        title.value=received.title
                        description.value=received.description
                        val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                        val date = format.parse(received.date)
                        calendar= Calendar.getInstance().apply {
                            timeInMillis=received.time
                        }
                        year= mutableStateOf(calendar.get(Calendar.YEAR))

                        month= mutableStateOf(calendar.get(Calendar.MONTH))

                        day= mutableStateOf(calendar.get(Calendar.DAY_OF_MONTH))

                        hour=  mutableStateOf(calendar.get(Calendar.HOUR_OF_DAY))

                        minutes= mutableStateOf(calendar.get(Calendar.MINUTE))

                    }
                }
                if (list.value.isNotEmpty()){
                    goto.value=true
                }
            }catch (err:java.lang.Exception){
                Log.e("ERROR:loadTask",err.toString())
            }
        }
    }

    fun loadencurso(context: Context){
        viewModelScope.launch {
            try{
                clean()
                list.value= withContext(Dispatchers.IO){
                    TaskDatabase.getDatabase(context).taskDao().encurso()
                }
            }catch (err:java.lang.Exception){
                Log.e("ERROR:loadencurso",err.toString())
            }
        }
    }

    fun loadcomplete(context: Context){
        viewModelScope.launch {
            try{
                clean()
                list.value= withContext(Dispatchers.IO){
                    TaskDatabase.getDatabase(context).taskDao().completadas()
                }
            }catch (err:java.lang.Exception){
                Log.e("ERROR:loadcomplete",err.toString())
            }
        }
    }

    fun conteoCompletas(context: Context){
        viewModelScope.launch {
            try{
                completas.value= withContext(Dispatchers.IO){
                    TaskDatabase.getDatabase(context).taskDao().conteoCompletas()
                }
            }catch (err:java.lang.Exception){
                Log.e("ERROR:conteoCompletas",err.toString())
            }
        }
    }

    fun conteoEnCurso(context: Context){
        viewModelScope.launch {
            try{
                enCursoCount.value= withContext(Dispatchers.IO){
                    TaskDatabase.getDatabase(context).taskDao().conteoEnCurso()
                }
            }catch (err:java.lang.Exception){
                Log.e("ERROR:conteoCompletas",err.toString())
            }
        }
    }

    fun conteodeHoy(context: Context,date:String){
        viewModelScope.launch {
            try{
                dehoy.value= withContext(Dispatchers.IO){
                    TaskDatabase.getDatabase(context).taskDao().conteoDeHoy(date)
                }
            }catch (err:java.lang.Exception){
                Log.e("ERROR:conteodeHoy",err.toString())
            }
        }
    }

    fun loaddehoy(context: Context,date:String){
        viewModelScope.launch {
            try{
                clean()
                list.value= withContext(Dispatchers.IO){
                    TaskDatabase.getDatabase(context).taskDao().dehoy(date)
                }
            }catch (err:java.lang.Exception){
                Log.e("ERROR:loaddehoy",err.toString())
            }
        }
    }

    fun updateTask(context: Context,id:Int,completed:Int){
        viewModelScope.launch {
            try{
                TaskDatabase.getDatabase(context).taskDao().updateTask(id,completed)

            }catch (err:java.lang.Exception){
                Log.e("ERROR:updateTask",err.toString())
            }
        }
    }

    fun deleteTask(context: Context,id:Int){
        viewModelScope.launch {
            try{
                TaskDatabase.getDatabase(context).taskDao().deleteTask(id)

            }catch (err:java.lang.Exception){
                Log.e("ERROR:deleteTask",err.toString())
            }
        }
    }


    fun createTask(context: Context,task: Task){
        viewModelScope.launch {
            try{
                TaskDatabase.getDatabase(context).taskDao().insert(task)
            }catch (err:Throwable){
                Log.e("ERROR:loadPeriods",err.message.toString())
            }
        }
    }

    fun resetTask(){
        task= mutableStateOf(Task(0,"","","","",0,0, shuff = 0))
        gotoList.value = true
    }
}