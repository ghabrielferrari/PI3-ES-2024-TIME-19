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
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.pi3_es_2024_time19.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var btnLogout: Item

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set the main action bar to be the toolbar view
        setSupportActionBar(binding.toolbar)

        // Create action bar drawer btn
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

//        binding.navigationView.setOnClickListener { menuItem ->
//            //  Handle Item Interaction
//            when(menuItem.id) {
//                R.id.btnLogout -> {
//                    val intent: Intent = Intent(this, LoginActivity::class.java)
//                    startActivity(intent)
//                }
//            }
//        }
//        val gson = Gson()
    }

    private fun setToolbarTitle(title: String) {
        binding.toolbar.setTitle(title)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.btnLogout -> {
                val intent: Intent = Intent(this, LoginActivity::class.java)
                Toast.makeText(this, "Logout!", Toast.LENGTH_LONG).show()
                startActivity(intent)
                return true
            }
        }
        return false
    }
}
