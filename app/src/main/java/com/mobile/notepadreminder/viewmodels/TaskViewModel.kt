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
    val completas= mutableStateOf(0)
    val total= mutableStateOf(0)
    val dehoy= mutableStateOf(0)
    val enCursoCount= mutableStateOf(0)
    val goto= mutableStateOf(false)

    var task= mutableStateOf(Task(0,"","","","",0,0))

     fun loadTask(context: Context){
        viewModelScope.launch {
            try{
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
                list.value= withContext(Dispatchers.IO){
                    TaskDatabase.getDatabase(context).taskDao().encurso()
                }
                Log.d("loadencurso()","tasks cargados correctamente ${list.value.count()}")
            }catch (err:java.lang.Exception){
                Log.d("ERROR:loadencurso",err.toString())
            }
        }
    }

    fun loadcomplete(context: Context){
        viewModelScope.launch {
            try{
                list.value= withContext(Dispatchers.IO){
                    TaskDatabase.getDatabase(context).taskDao().completadas()
                }
                Log.d("loadcomplete()","tasks cargados correctamente ${list.value.count()}")
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
        task= mutableStateOf(Task(0,"","","","",0,0))
    }
}