package com.mobile.notepadreminder.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Task")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    var title:String,
    var description:String,
    var date:String,
    var dateCreated:String,
    var time:Long,
    var iscompleted:Int
)