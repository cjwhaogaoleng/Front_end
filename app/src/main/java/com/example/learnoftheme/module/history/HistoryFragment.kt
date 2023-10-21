package com.example.learnoftheme.module.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.learnoftheme.R
import com.example.learnoftheme.databinding.FragmentHistoryBinding
import com.example.learnoftheme.module.maintwo.MainSharedViewModel

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class HistoryFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentHistoryBinding
    private lateinit var viewModel: MainSharedViewModel

    private lateinit var lv: ListView
    var history = ArrayList<String>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_history, container, false)
        binding.lifecycleOwner = this

        viewModel = ViewModelProvider(requireActivity()).get<MainSharedViewModel>(
            MainSharedViewModel::class.java
        )
        return binding.root
    }

    companion object {
        fun newInstance(param1: String, param2: String) =
            HistoryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        subscribe(view)
    }

    private fun initView(view: View) {
        lv = view.findViewById<ListView>(R.id.lv_history)
    }

    private fun subscribe(view: View) {
        history.add("There is no historical record yet.")
        viewModel.history.observe(viewLifecycleOwner) {
            if (viewModel.history.value != null) {
                println(true)
                history.clear()
                var size = viewModel.history.value!!.size
                for (i in size - 2 downTo if (size>21) size-21 else 0 ) {
                    history.add(viewModel.history.value!![i])
//                    history.add("33")
                }
            }
        }
        var adapter = MyListViewAdapter(view.context, history)
        lv.adapter = adapter
    }

}