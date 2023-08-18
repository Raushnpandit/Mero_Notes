package com.example.meronotes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var homeFragment: HomeFragment
    private lateinit var aboutFragment: AboutFragment
    private lateinit var activeFragment: Fragment
    private val fragmentManager: FragmentManager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        homeFragment = HomeFragment()
        aboutFragment = AboutFragment()

        activeFragment = homeFragment

        fragmentManager.beginTransaction().apply {
            add(R.id.fragmentContainer, aboutFragment, "2")
            hide(aboutFragment)
            add(R.id.fragmentContainer, homeFragment, "1")
        }.commit()

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.homeMenuItem -> {
                    fragmentManager.beginTransaction().hide(activeFragment).show(homeFragment).commit()
                    activeFragment = homeFragment
                    true
                }
                R.id.aboutMenuItem -> {
                    fragmentManager.beginTransaction().hide(activeFragment).show(aboutFragment).commit()
                    activeFragment = aboutFragment
                    true
                }
                else -> false
            }
        }
    }

    override fun onBackPressed() {
        val backStackEntryCount = supportFragmentManager.backStackEntryCount
        if (backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
            val currentFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
            if (currentFragment != null) {
                activeFragment = currentFragment
            }
        } else if (activeFragment != homeFragment) {
            supportFragmentManager.beginTransaction()
                .hide(activeFragment)
                .show(homeFragment)
                .commit()
            activeFragment = homeFragment
        } else {
            super.onBackPressed()
        }
    }
}