package com.mobile.notepadreminder.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MyTaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task:Task)

    @Query("select id,title,description,date,time,iscompleted from Task")
    suspend fun select():List<Task>
}