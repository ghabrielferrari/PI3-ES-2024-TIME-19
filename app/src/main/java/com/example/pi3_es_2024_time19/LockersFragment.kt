package com.example.pi3_es_2024_time19

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pi3_es_2024_time19.databinding.FragmentLockersBinding

/**
 * A simple [Fragment] subclass.
 * Use the [LockersFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LockersFragment : Fragment() {

    private lateinit var binding: FragmentLockersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private fun setBinding() {
        binding = FragmentLockersBinding.inflate(layoutInflater)
    }


}