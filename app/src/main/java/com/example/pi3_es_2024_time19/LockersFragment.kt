package com.example.pi3_es_2024_time19

import android.content.Intent
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setBinding()
        binding.btnAddNovoArmario.setOnClickListener {
            val intent = Intent(context, RentLockerActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }

    private fun setBinding() {
        binding = FragmentLockersBinding.inflate(layoutInflater)
    }


}