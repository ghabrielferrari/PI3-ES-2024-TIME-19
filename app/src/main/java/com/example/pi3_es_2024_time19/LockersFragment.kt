package com.example.pi3_es_2024_time19

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pi3_es_2024_time19.databinding.FragmentLockersBinding

class LockersFragment : Fragment() {

    private lateinit var binding: FragmentLockersBinding
    private lateinit var rentedLockers: MutableList<Locker>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupViewBinding()
        prepareRecycleView()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lockers, container, false)
    }
    private fun setupViewBinding() {
        binding = FragmentLockersBinding.inflate(layoutInflater)
    }

    private fun prepareRecycleView() {
        binding.rvArmarios.layoutManager = LinearLayoutManager(context)
        binding.rvArmarios.setHasFixedSize(false)
    }

}