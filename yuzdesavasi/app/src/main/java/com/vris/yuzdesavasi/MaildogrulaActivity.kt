package com.vris.yuzdesavasi

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import java.sql.PreparedStatement

class MaildogrulaActivity : AppCompatActivity() {
    private var connectSql = ConnectSql()
    lateinit var  userlogin: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maildogrula)

        window.statusBarColor = ContextCompat.getColor(this, R.color.turuncu)

        val geridonButon=findViewById<ConstraintLayout>(R.id.geridonButon)
        userlogin=getSharedPreferences("userlog", Context.MODE_PRIVATE)
        val mailtxt=findViewById<TextView>(R.id.mailtxtchk)
val sendmail=findViewById<Button>(R.id.chkbtn)
        sendmail.setOnClickListener {

            if (mailkont(mailtxt.text.toString())==false) {
                val login: PreparedStatement = connectSql.dbConn()
                    ?.prepareStatement(
                        "update ys set mail=? where uname='" + userlogin.getString("uname","").toString() + "' and pass='"+userlogin.getString("pass","").toString() + "'")!!
                login.setString(1, mailtxt.text.toString())
                login.executeUpdate()

                val editor4: SharedPreferences.Editor = userlogin.edit()
                editor4.putString("mail", "1")


                editor4.commit()
                val editor: SharedPreferences.Editor = userlogin.edit()
                editor.putString("ulog", mailtxt.text.toString())

                editor.apply()


                val intent = Intent(this, MailSender::class.java)
                startActivity(intent)
                finish()
            }
            else
            {
                val context = applicationContext // Bu bağlamı uygun şekilde değiştirin
                 val message = "Bu mail adresi önceden kullanılmış."
                showToast(context, message)
            }

        }


        geridonButon.setOnClickListener{

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    fun  mailkont(mail:String) :Boolean
    {
        val login: PreparedStatement = connectSql.dbConn()
            ?.prepareStatement("select count(mail) from ys where mail='"+mail.toString()+"'")!!

        var kont=false


        val rs = login.executeQuery()

        while (rs.next()) {
           if( rs.getInt(1)>=1)
           {
               kont=true
           }
        }
        return kont
    }
    fun showToast(context: Context, message: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(context, message, duration).show()
    }

}