package com.mobile.notepadreminder.data

import androidx.lifecycle.LiveData

class Repository(private val task:MyTaskDao) {
    var readAll:List<Task> = TODO()

    suspend fun getAll(){
        readAll=task.select()
    }

    suspend fun add(ptask: Task){
        task.insert(ptask)
    }
}