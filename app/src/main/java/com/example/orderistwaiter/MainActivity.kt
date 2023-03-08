package com.example.orderistwaiter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val database = FirebaseDatabase.getInstance()
//        val myRef = database.getReference("message12")
//        myRef.setValue("Helloorasdld!")

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            navController = navHostFragment.navController
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)
        setupWithNavController(bottomNavigationView, navController)

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.destination_order -> {
                    navController.popBackStack(R.id.destination_order, false)
                    navController.navigate(R.id.destination_order)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.destination_history -> {
                    navController.popBackStack(R.id.destination_history, false)
                    navController.navigate(R.id.destination_history)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.destination_serve -> {
                    navController.popBackStack(R.id.destination_serve, false)
                    navController.navigate(R.id.destination_serve)
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }

    }



}