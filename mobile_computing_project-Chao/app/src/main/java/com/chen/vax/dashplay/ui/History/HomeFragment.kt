package com.chen.vax.dashplay.ui.History

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.chen.vax.dashplay.R
import com.chen.vax.dashplay.ui.Main.DashboardViewModel

class HomeFragment : Fragment(){

    private lateinit var homeViewModel: DashboardViewModel
    private lateinit var fm:FragmentManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel =
            ViewModelProviders.of(this).get(DashboardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_history, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)

        homeViewModel.write.observe(this, Observer {
            textView.text="History"
        })
        return root
    }



}