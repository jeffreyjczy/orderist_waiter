package com.example.orderistwaiter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.orderistwaiter.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(Order())


        binding.bottomNavigationView.setOnItemSelectedListener {

            when(it.itemId){
                R.id.order -> replaceFragment(Order())
                R.id.history -> replaceFragment(History())
                R.id.serve -> replaceFragment(Serve())

                else -> {


                }
            }
            true
        }

    }

    private fun replaceFragment(fragment : Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout,fragment)
        fragmentTransaction.commit()
    }


}