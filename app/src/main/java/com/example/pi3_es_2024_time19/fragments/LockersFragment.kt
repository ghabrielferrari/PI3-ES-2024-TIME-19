package com.example.pi3_es_2024_time19.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.pi3_es_2024_time19.AdapterArmarioListItem
import com.example.pi3_es_2024_time19.RentLockerActivity
import com.example.pi3_es_2024_time19.databinding.FragmentLockersBinding
import com.example.pi3_es_2024_time19.models.Locker

class LockersFragment : Fragment() {

    private lateinit var binding: FragmentLockersBinding
    private lateinit var lockers: MutableList<Locker>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setBinding()
        setupRecyclerView()

        binding.btnAddNovoArmario.setOnClickListener {
            openRentLockerActivity()
        }

        setDummyListItems()
        bindAdapter()

        return binding.root
    }

    private fun setBinding() {
        binding = FragmentLockersBinding.inflate(layoutInflater)
    }

    private fun openRentLockerActivity() {
        val intent = Intent(context, RentLockerActivity::class.java)
        startActivity(intent)
    }

    private fun setupRecyclerView() {
        binding.rvArmarios.layoutManager = LinearLayoutManager(context)
        binding.rvArmarios.setHasFixedSize(true)
    }

    private fun setDummyListItems() {
        lockers = mutableListOf()
        val li1 = Locker("Armário 1", "Alugando", true)
//        val li2 = Locker("Armário 2", "Alugando", true)
        val li3 = Locker("Armário 3", "Livre", false)
//        val li4 = Locker("Armário 4", "Alugando", true)

        lockers.add(li1)
//        lockers.add(li2)
        lockers.add(li3)
//        lockers.add(li4)
    }

    private fun bindAdapter() {
        val adapter = AdapterArmarioListItem(lockers)
        binding.rvArmarios.adapter = adapter
    }
}