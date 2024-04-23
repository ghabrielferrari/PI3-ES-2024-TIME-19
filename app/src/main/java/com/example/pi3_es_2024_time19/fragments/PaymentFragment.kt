package com.example.pi3_es_2024_time19.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.pi3_es_2024_time19.R
import com.example.pi3_es_2024_time19.activities.RegisterCardActivity
import com.example.pi3_es_2024_time19.databinding.FragmentPaymentBinding

class PaymentFragment : Fragment() {

    private lateinit var binding: FragmentPaymentBinding
    private lateinit var btnAddNewCard: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentPaymentBinding.inflate(
            inflater, container, false
        )

        // Initialize views here after inflating the layout
        btnAddNewCard = binding.btnAddNewCard

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnAddNewCard.setOnClickListener {
            startActivity(
                Intent(requireContext(), RegisterCardActivity::class.java)
            )
        }
    }
}
