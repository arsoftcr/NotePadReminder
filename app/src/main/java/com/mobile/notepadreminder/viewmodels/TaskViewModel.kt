package com.mobile.notepadreminder.viewmodels

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobile.notepadreminder.data.Task
import com.mobile.notepadreminder.data.TaskDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TaskViewModel(): ViewModel() {

     val tasks:List<Task> = listOf()
     var list= mutableStateOf(tasks)
    val goto= mutableStateOf(false)

    var task= mutableStateOf(Task(0,"","","",0,false))

     fun loadTask(context: Context){
        viewModelScope.launch {
            try{
                list.value= withContext(Dispatchers.IO){
                    TaskDatabase.getDatabase(context).taskDao().select()
                }
                if (list.value.isNotEmpty()){
                    goto.value=true
                }
                Log.d("loadTask()","tasks cargados correctamente ${list.value.count()}")
            }catch (err:java.lang.Exception){
                Log.d("ERROR:loadTask",err.toString())
            }
        }
    }


    fun createTask(context: Context,task: Task){
        viewModelScope.launch {
            try{
                TaskDatabase.getDatabase(context).taskDao().insert(task)
                Toast.makeText(context,"Tarea creada correctamente",Toast.LENGTH_LONG).show()
            }catch (err:Throwable){
                Log.d("ERROR:loadPeriods",err.message.toString())
            }
        }
    }

    fun resetTask(){
        task= mutableStateOf(Task(0,"","","",0,false))
    }
}