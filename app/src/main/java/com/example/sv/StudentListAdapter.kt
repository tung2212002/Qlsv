package com.example.sv

import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sv.R
import com.example.sv.data.Student

class StudentListAdapter(private val studentList: MutableList<Student>): RecyclerView.Adapter<StudentListAdapter.StudentViewHolder>() {

    var onClickItem: ((student: Student) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_student, parent, false)

        return StudentViewHolder(view)
    }

    override fun getItemCount(): Int {
        return studentList.size
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        holder.bind(studentList[position])
    }

    inner class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val email: TextView = itemView.findViewById(R.id.tvEmailStudent)
        private val name: TextView = itemView.findViewById(R.id.tvNameStudent)
        private val studentCode: TextView = itemView.findViewById(R.id.tvStudentCodeStudent)
        private lateinit var currentStudent: Student

        init {
            itemView.setOnClickListener{
                onClickItem?.invoke(currentStudent)
            }
        }

        fun bind(student: Student) {
            currentStudent = student
            email.text = generateEmail(student.name, student.studentCode)
            name.text = student.name
            studentCode.text = student.studentCode
        }
        private fun generateEmail(name: String, studentCode: String): String {
            val listString = name.split(" ")
            val first = listString.lastOrNull()?.lowercase()
            val mutableListString = listString.toMutableList()
            mutableListString.removeLast()
            val second = mutableListString.joinToString("") { it.firstOrNull()?.lowercase().toString() }
            studentCode.removeRange(0, 2)

            return "${first}.${second}${studentCode}@sis.hust.edu.vn"
        }
    }
}