package com.vris.yuzdesavasi

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat

class TutorialActivity : AppCompatActivity() {
     lateinit var  userlogin: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial)

        window.statusBarColor = ContextCompat.getColor(this, R.color.turuncu)

        //txt
        var txt1=findViewById<TextView>(R.id.tutoText)


        //txt

        val skip=findViewById<Button>(R.id.skipButon)
        userlogin=getSharedPreferences("userlog", Context.MODE_PRIVATE)
        var log1=userlogin.getString("ulog","")


        var log=userlogin.getString("tut","")
        if (log=="")
        {
            txt1.setText(R.string.tuto)
            var say=0
            skip.setOnClickListener {
                val editor2: SharedPreferences.Editor = userlogin.edit()
                editor2.putString("tut", "1")

                editor2.apply()

                val intent = Intent(this, GirisActivity::class.java)
                startActivity(intent)
                finish()
            }

        }
        else if (log1=="")
        {
            val intent = Intent(this, GirisActivity::class.java)
            startActivity(intent)
            finish()
        }
        else if(log1!="")
        {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}