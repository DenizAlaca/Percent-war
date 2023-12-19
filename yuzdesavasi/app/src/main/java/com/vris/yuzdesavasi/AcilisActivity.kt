package com.vris.yuzdesavasi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.core.content.ContextCompat

class AcilisActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_acilis)

        window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrim)

        Handler().postDelayed({
            val intent = Intent(this, TutorialActivity::class.java)
            startActivity(intent)
            finish()

        },1000)

    }
}