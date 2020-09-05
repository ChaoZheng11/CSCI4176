package com.chen.vax.dashplay.ui.Setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.chen.vax.dashplay.R

class Memo : Fragment() {

    private lateinit var notificationsViewModel: ShareVm

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel =
            ViewModelProviders.of(this).get(ShareVm::class.java)
        val root = inflater.inflate(R.layout.mem, container, false)

        return root
    }
}
