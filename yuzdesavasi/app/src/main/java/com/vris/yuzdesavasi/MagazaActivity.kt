package com.vris.yuzdesavasi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat

class MagazaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_magaza)

        window.statusBarColor = ContextCompat.getColor(this, R.color.turuncu)

        val geriButon=findViewById<ConstraintLayout>(R.id.geriButon)

        geriButon.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

}