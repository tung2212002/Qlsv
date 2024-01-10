package com.example.sv

import android.app.Application
import com.example.sv.data.StudentDatabase

class MyApplication: Application() {
    val database: StudentDatabase by lazy { StudentDatabase.getDatabase(this) }
}