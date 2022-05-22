package com.pthw.mymovieapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pthw.mymovieapp.R
import com.pthw.mymovieapp.base.BaseFragment
import com.pthw.mymovieapp.databinding.FragmentHomeBinding
import com.pthw.mymovieapp.databinding.FragmentProfileBinding


class ProfileFragment : BaseFragment() {

    var binding: FragmentProfileBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        if (binding == null) {
            binding = FragmentProfileBinding.inflate(inflater, container, false)
        }

        binding?.ivBack?.setOnClickListener { popBackStack() }
        return binding?.root
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}