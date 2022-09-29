package com.mobile.notepadreminder.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface MyTaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task:Task)

    @Query("select id,title,description,date,dateCreated,time,iscompleted from Task")
    suspend fun select():List<Task>

    @Query("select id,title,description,date,dateCreated,time,iscompleted from Task where iscompleted=1")
    suspend fun completadas():List<Task>

    @Query("select id,title,description,date,dateCreated,time,iscompleted from Task where iscompleted=0")
    suspend fun encurso():List<Task>

    @Query("select id,title,description,date,dateCreated,time,iscompleted from Task where dateCreated=:date")
    suspend fun dehoy(date:String):List<Task>

    @Query("update Task set iscompleted=:completed where id=:pid")
    suspend fun updateTask(pid:Int,completed:Int)

    @Query("select Count(*) from Task where iscompleted=1")
    suspend fun conteoCompletas():Int

    @Query("select Count(*) from Task where iscompleted=0")
    suspend fun conteoEnCurso():Int

    @Query("select Count(*) from Task where dateCreated=:date")
    suspend fun conteoDeHoy(date:String):Int
}