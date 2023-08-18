package com.example.meronotes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView

class SplashScreen : AppCompatActivity() {

    private val SPLASH_TIME_OUT:Long = 3000 // 3 seconds

    private lateinit var imgSplash : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        imgSplash = findViewById(R.id.imgSplash)

        Handler().postDelayed({
            val intent = Intent(this@SplashScreen, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, SPLASH_TIME_OUT)
    }
}