package com.example.learnoftheme.module.maintwo


import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.learnoftheme.R
import com.example.learnoftheme.databinding.ActivityMainTwoBinding
import com.example.learnoftheme.module.history.HistoryFragment
import com.example.learnoftheme.module.keyboard.KeyboardFragment


class MainActivityTwo : AppCompatActivity() {

    //                    ViewModelProviders.of(this).get(SharedViewModel::class.java)
    private lateinit var binding: ActivityMainTwoBinding
    private lateinit var viewModel: MainSharedViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main_two)
        viewModel = ViewModelProvider(this).get(MainSharedViewModel::class.java)

        binding.lifecycleOwner = this
        binding.mainTwoClick = this

        initPager()
        subscribe()
    }



    private fun initPager() {
        var keyboard = KeyboardFragment.newInstance("", "")
        var history = HistoryFragment.newInstance("", "")
        var data = arrayListOf<Fragment>()
        data.add(keyboard)
        data.add(history)

        var myViewPagerAdapter = MyViewPagerAdapter(supportFragmentManager, lifecycle, data)

        binding.vpMain.adapter = myViewPagerAdapter


    }


        private fun subscribe() {
            viewModel.input.observe(this
            ) {
                println("input has changed to " +viewModel.input.value )

                binding.tvProcess.text = viewModel.input.value }
            viewModel.result.observe(this) {

                println("result has changed " + viewModel.result.value)
                binding.tvResult.text = viewModel.result.value
                binding.tvResult.text = viewModel.result.value

            }
        }
}