package com.example.sv.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "students")
data class Student(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "studentCode")
    val studentCode: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "dateOfBirth")
    val dateOfBirth: String,
    @ColumnInfo(name = "address")
    val address: String,
)