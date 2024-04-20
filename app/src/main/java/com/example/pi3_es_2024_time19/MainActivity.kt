package com.example.pi3_es_2024_time19

import android.content.ClipData.Item
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.replace
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.pi3_es_2024_time19.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var btnLogout: Item

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set the main action bar to be the toolbar view
        setSupportActionBar(binding.toolbar)

        // Create action bar drawer btn
        initializeActionBar()

        // Start in lockers fragment
        replaceFragment(LockersFragment())

        addNavbarListener()
//        val gson = Gson()
    }

    private fun setToolbarTitle(title: String) {
        binding.toolbar.setTitle(title)
    }

    private fun initializeActionBar() {
        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        // Add drawer btn listener and sync the state of the drawer (open/closed)
        binding.drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
    }

    private fun addNavbarListener() {
        binding.navBar.setOnItemSelectedListener {
            //  Handle Item Interaction
            when(it.itemId) {
                R.id.btnPageLockers -> replaceFragment(LockersFragment())
                R.id.btnPagePayment -> replaceFragment(PaymentFragment())
                R.id.btnPageMap -> replaceFragment(MapsFragment())
            }
            true
        }
    }

    private  fun replaceFragment(fragment: Fragment) {
        Toast.makeText(this, "Fragment Changed", Toast.LENGTH_SHORT).show()
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                binding.drawerLayout.openDrawer(GravityCompat.START)
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
