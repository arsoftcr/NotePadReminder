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
import java.util.*

class TaskViewModel(): ViewModel() {

    val calendar= Calendar.getInstance().apply {
        timeInMillis=System.currentTimeMillis()
    }
    val title = mutableStateOf("")

    val description =  mutableStateOf("")

    val year= mutableStateOf(calendar.get(Calendar.YEAR))

    val month= mutableStateOf(calendar.get(Calendar.MONTH))

    val day= mutableStateOf(calendar.get(Calendar.DAY_OF_MONTH))

    val hour=  mutableStateOf(calendar.get(Calendar.HOUR_OF_DAY))

    val minutes= mutableStateOf(calendar.get(Calendar.MINUTE))

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
     fun loadTask(context: Context){
        viewModelScope.launch {
            try{
                clean()
                list.value= withContext(Dispatchers.IO){
                    TaskDatabase.getDatabase(context).taskDao().select()
                }
                total.value=list.value.count()
                if (list.value.isNotEmpty()){
                    goto.value=true
                }
                Log.d("loadTask()","tasks cargados correctamente ${list.value.count()}")
            }catch (err:java.lang.Exception){
                Log.d("ERROR:loadTask",err.toString())
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
                Log.d("loadencurso()","loadencurso ${list.value.count()}")
            }catch (err:java.lang.Exception){
                Log.d("ERROR:loadencurso",err.toString())
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
                Log.d("loadcomplete()","loadcomplete ${list.value.count()}")
            }catch (err:java.lang.Exception){
                Log.d("ERROR:loadcomplete",err.toString())
            }
        }
    }

    fun conteoCompletas(context: Context){
        viewModelScope.launch {
            try{
                completas.value= withContext(Dispatchers.IO){
                    TaskDatabase.getDatabase(context).taskDao().conteoCompletas()
                }
                Log.d("conteoCompletas()","conteoCompletass ${completas.value}")
            }catch (err:java.lang.Exception){
                Log.d("ERROR:conteoCompletas",err.toString())
            }
        }
    }

    fun conteoEnCurso(context: Context){
        viewModelScope.launch {
            try{
                enCursoCount.value= withContext(Dispatchers.IO){
                    TaskDatabase.getDatabase(context).taskDao().conteoEnCurso()
                }
                Log.d("conteoCompletas()","conteoEnCurso ${enCursoCount.value}")
            }catch (err:java.lang.Exception){
                Log.d("ERROR:conteoCompletas",err.toString())
            }
        }
    }

    fun conteodeHoy(context: Context,date:String){
        viewModelScope.launch {
            try{
                dehoy.value= withContext(Dispatchers.IO){
                    TaskDatabase.getDatabase(context).taskDao().conteoDeHoy(date)
                }
                Log.d("conteodeHoy()","conteodeHoy ${dehoy.value}")
            }catch (err:java.lang.Exception){
                Log.d("ERROR:conteodeHoy",err.toString())
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
                Log.d("loaddehoy()","tasks cargados correctamente ${list.value.count()}")
            }catch (err:java.lang.Exception){
                Log.d("ERROR:loaddehoy",err.toString())
            }
        }
    }

    fun updateTask(context: Context,id:Int,completed:Int){
        viewModelScope.launch {
            try{
                TaskDatabase.getDatabase(context).taskDao().updateTask(id,completed)

                Log.d("updateTask()","updateTask id $id completed : $completed")
            }catch (err:java.lang.Exception){
                Log.d("ERROR:updateTask",err.toString())
            }
        }
    }

    fun deleteTask(context: Context,id:Int){
        viewModelScope.launch {
            try{
                TaskDatabase.getDatabase(context).taskDao().deleteTask(id)

                Log.d("deleteTask()","deleteTask id $id")
            }catch (err:java.lang.Exception){
                Log.d("ERROR:deleteTask",err.toString())
            }
        }
    }


    fun createTask(context: Context,task: Task){
        viewModelScope.launch {
            try{
                TaskDatabase.getDatabase(context).taskDao().insert(task)
            }catch (err:Throwable){
                Log.d("ERROR:loadPeriods",err.message.toString())
            }
        }
    }

    fun resetTask(){
        task= mutableStateOf(Task(0,"","","","",0,0, shuff = 0))
        gotoList.value = true
    }
}