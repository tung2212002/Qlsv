package com.example.sv

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sv.databinding.FragmentStudentListBinding
import com.example.sv.data.Student

class StudentListFragment: Fragment() {

    companion object{
        const val DETAIL_STUDENT_TYPE_KEY = "DETAIL_STUDENT_KEY"
        const val DETAIL_STUDENT_VALUE_KEY = "DETAIL_STUDENT_VALUE_KEY"
        const val CREATE_TYPE = "CREATE_TYPE"
        const val UPDATE_TYPE = "UPDATE_TYPE"
    }

    private lateinit var binding: FragmentStudentListBinding
    private lateinit var studentListAdapter: StudentListAdapter
    private val studentList = mutableListOf<Student>()
    private val viewModel: MainViewModel by activityViewModels {
        MainViewModel.MainViewModelFactory(
            (activity?.application as MyApplication).database
                .studentDao()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStudentListBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.option_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.create ->{
                navToAddStudent()
                true
            }
            else -> false
        }
        return true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        studentListAdapter = StudentListAdapter(studentList)
        studentListAdapter.onClickItem = {
            onClickItem(it)
        }
        binding.recyclerView.apply {
            adapter = studentListAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
        viewModel.allItems.observe(this.viewLifecycleOwner){
            if(studentList.isEmpty()){
                studentList.addAll(it.toMutableList())
            }
            else{
                studentList.clear()
                studentList.addAll(it.toMutableList())
            }
            studentListAdapter.notifyDataSetChanged()
        }
    }
    private fun onClickItem(student: Student){
        val bundle = Bundle()
        bundle.putString(DETAIL_STUDENT_TYPE_KEY, UPDATE_TYPE)
        bundle.putInt(DETAIL_STUDENT_VALUE_KEY, student.id)
        val addStudentFragment = AddStudentFragment()
        addStudentFragment.arguments = bundle
        val fragmentTransaction: FragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, addStudentFragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
    private fun navToAddStudent(){
        val bundle = Bundle()
        bundle.putString(DETAIL_STUDENT_TYPE_KEY, CREATE_TYPE)
        val addStudentFragment = AddStudentFragment()
        addStudentFragment.arguments = bundle
        val fragmentTransaction: FragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, addStudentFragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
}