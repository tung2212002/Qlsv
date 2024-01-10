package com.example.sv

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.sv.databinding.FragmentAddStudentBinding
import com.example.sv.data.Student
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*

class AddStudentFragment : Fragment() {
    private lateinit var binding: FragmentAddStudentBinding
    private val viewModel: MainViewModel by activityViewModels {
        MainViewModel.MainViewModelFactory(
            (activity?.application as MyApplication).database
                .studentDao()
        )
    }
    private var isCreateType = true
    private var id: Int? = null
    private var selectedStudent: Student? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val type = arguments?.getString(StudentListFragment.DETAIL_STUDENT_TYPE_KEY)
        id = arguments?.getInt(StudentListFragment.DETAIL_STUDENT_VALUE_KEY)
        isCreateType = type == StudentListFragment.CREATE_TYPE
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddStudentBinding.inflate(inflater, container, false)
        initView()
        return binding.root
    }

    private fun initView() {
        binding.saveAction.setOnClickListener {
            if (validate()) {
                addNewStudent()
            }
        }
        binding.updateAction.setOnClickListener{
            if(validate()){
                updateStudent()
            }
        }
        binding.deleteAction.setOnClickListener{
            deleteStudent()
        }

        binding.itemDateOfBirth.inputType = InputType.TYPE_NULL
        binding.itemDateOfBirth.setOnClickListener {
            showDatePickerDialog()
        }

        val provinces = readRawTextFile()
        val arrayAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, provinces)
        binding.itemAddress.setAdapter(arrayAdapter)

        if (isCreateType) {
            binding.deleteAction.visibility = View.GONE
            binding.updateAction.visibility = View.GONE
        } else {
            binding.saveAction.visibility = View.GONE
        }

        id?.let {
            if (id != 0) {
                viewModel.retrieveItem(it).observe(this.viewLifecycleOwner) { student ->
                    selectedStudent = student
                    showDetailStudent(student)
                }
            }
        }
    }

    private fun showDetailStudent(student: Student) {
        binding.itemStudentCode.setText(student.studentCode)
        binding.itemAddress.setText(student.address)
        binding.itemStudentName.setText(student.name)
        binding.itemDateOfBirth.setText(student.dateOfBirth)
    }

    private fun validate(): Boolean {
        val studentCode = binding.itemStudentCode.text.toString()
        val name = binding.itemStudentName.text.toString()
        val address = binding.itemAddress.text.toString()
        val dateOfBirth = binding.itemDateOfBirth.text.toString()

        if (studentCode.isEmpty() || name.isEmpty() || address.isEmpty() || dateOfBirth.isEmpty()) {
            Toast.makeText(requireContext(), "Vui lòng nhập đủ trường", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun addNewStudent() {
        val studentCode = binding.itemStudentCode.text.toString()
        val name = binding.itemStudentName.text.toString()
        val address = binding.itemAddress.text.toString()
        val dateOfBirth = binding.itemDateOfBirth.text.toString()

        viewModel.addNewItem(studentCode, name, address, dateOfBirth)

        requireActivity().supportFragmentManager.popBackStack()
    }

    private fun updateStudent() {

        val studentCode = binding.itemStudentCode.text.toString()
        val name = binding.itemStudentName.text.toString()
        val address = binding.itemAddress.text.toString()
        val dateOfBirth = binding.itemDateOfBirth.text.toString()

        viewModel.updateItem(Student(id = selectedStudent!!.id ,studentCode = studentCode, name = name, dateOfBirth = dateOfBirth, address = address))

        requireActivity().supportFragmentManager.popBackStack()
    }

    private fun deleteStudent(){
        viewModel.deleteItem(selectedStudent!!)
        requireActivity().supportFragmentManager.popBackStack()
    }


    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            DatePickerDialog.OnDateSetListener { view: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, monthOfYear, dayOfMonth)

                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val formattedDate = dateFormat.format(selectedDate.time)

                binding.itemDateOfBirth.setText(formattedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.show()
    }

    private fun readRawTextFile(): List<String> {
        val inputStream: InputStream = requireContext().resources.openRawResource(R.raw.province)
        val byteArrayOutputStream = ByteArrayOutputStream()

        try {
            var i: Int
            i = inputStream.read()
            while (i != -1) {
                byteArrayOutputStream.write(i)
                i = inputStream.read()
            }
            inputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return byteArrayOutputStream.toString().split("\n")
    }
}