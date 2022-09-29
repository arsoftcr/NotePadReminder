package com.mobile.notepadreminder.data

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

@androidx.room.Database(entities = [Task::class], version = 1, exportSchema = false)
abstract class TaskDatabase : RoomDatabase() {
    abstract  fun taskDao():MyTaskDao

    companion object{
        @Volatile
        private  var INSTANCE:TaskDatabase?=null

        fun getDatabase(context: Context):TaskDatabase{
            val tempInstance= INSTANCE
            if (tempInstance!=null){
                return  tempInstance
            }
            synchronized(this){
                val instance= Room.databaseBuilder(context.applicationContext,TaskDatabase::class.java,"task.db").build()
                INSTANCE=instance
                return instance
            }
        }
    }
}