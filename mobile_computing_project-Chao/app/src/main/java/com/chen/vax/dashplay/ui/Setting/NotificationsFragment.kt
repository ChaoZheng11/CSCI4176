package com.chen.vax.dashplay.ui.Setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.chen.vax.dashplay.R
import kotlinx.android.synthetic.main.fragment_settings.*

open class NotificationsFragment : Fragment() {

    private lateinit var notificationsViewModel: NotificationsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel =
            ViewModelProviders.of(this).get(NotificationsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_settings, container, false)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        memo.setOnClickListener {
            findNavController().navigate(R.id.memo2)
        }
        contact.setOnClickListener {
            findNavController().navigate(R.id.support)
        }
        priva.setOnClickListener{
            findNavController().navigate(R.id.privacy2)
        }



    }


}