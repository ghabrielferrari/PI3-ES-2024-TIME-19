package com.example.pi3_es_2024_time19.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.pi3_es_2024_time19.R
import com.example.pi3_es_2024_time19.databinding.ActivityMainBinding
import com.example.pi3_es_2024_time19.fragments.AccountFragment
import com.example.pi3_es_2024_time19.fragments.LockersFragment
import com.example.pi3_es_2024_time19.fragments.MapsFragment
import com.example.pi3_es_2024_time19.fragments.PaymentFragment
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set the main action bar to be the toolbar view
        setSupportActionBar(binding.toolbar)

        // Create action bar drawer btn
        // initializeActionBar()

        // Start in lockers fragment
        replaceFragment(LockersFragment())

        addNavbarListener()
//        val gson = Gson()

        initFirebaseAuth()
        // Assign user
        user = auth.currentUser as FirebaseUser
        Toast.makeText(this, "UID=${user.uid}", Toast.LENGTH_SHORT).show()
    }

    private fun setToolbarTitle(title: String) {
        binding.toolbar.setTitle(title)
    }

//    private fun initializeActionBar() {
//        val actionBarDrawerToggle = ActionBarDrawerToggle(
//            this,
//            binding.drawerLayout,
//            binding.toolbar,
//            R.string.navigation_drawer_open,
//            R.string.navigation_drawer_close
//        )
//        // Add drawer btn listener and sync the state of the drawer (open/closed)
//        binding.drawerLayout.addDrawerListener(actionBarDrawerToggle)
//        actionBarDrawerToggle.syncState()
//    }

    private fun addNavbarListener() {
        binding.navBar.setOnItemSelectedListener {
            //  Handle Item Interaction
            when(it.itemId) {
                R.id.btnPageLockers -> replaceFragment(LockersFragment())
                R.id.btnPagePayment -> replaceFragment(PaymentFragment())
                R.id.btnPageMap -> replaceFragment(MapsFragment())
                R.id.btnAccount -> replaceFragment(AccountFragment())
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

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        if (item.itemId == android.R.id.home) {
//            if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
//                binding.drawerLayout.closeDrawer(GravityCompat.START)
//            } else {
//                binding.drawerLayout.openDrawer(GravityCompat.START)
//            }
//            return true
//        }
//        return super.onOptionsItemSelected(item)
//    }

//    override fun onBackPressed() {
//        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
//            binding.drawerLayout.closeDrawer(GravityCompat.START)
//        } else {
//            super.onBackPressed()
//        }
//    }

    private fun initFirebaseAuth() {
        // Initialize Firebase Auth
        auth = Firebase.auth
    }
}
