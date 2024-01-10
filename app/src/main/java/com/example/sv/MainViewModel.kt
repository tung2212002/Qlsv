package com.example.sv

import androidx.lifecycle.*
import com.example.sv.data.Student
import com.example.sv.data.StudentDao
import kotlinx.coroutines.launch

class MainViewModel(private val studentDao: StudentDao): ViewModel() {

    val allItems: LiveData<List<Student>> = studentDao.getStudents().asLiveData()

    fun addNewItem(studentCode: String, name: String, dateOfBirth: String, address: String) {
        val newItem = Student(studentCode = studentCode, name = name, dateOfBirth = dateOfBirth, address = address)
        insertItem(newItem)
    }

    private fun insertItem(item: Student) {
        viewModelScope.launch {
            studentDao.insert(item)
        }
    }

    fun deleteItem(item: Student) {
        viewModelScope.launch {
            studentDao.delete(item)
        }
    }
    fun retrieveItem(id: Int): LiveData<Student> {
        return studentDao.getStudent(id).asLiveData()
    }
    fun updateItem(student: Student){
        viewModelScope.launch {
            studentDao.update(student)
        }
    }

    class MainViewModelFactory(private val studentDao: StudentDao) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(studentDao) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}