package com.vris.yuzdesavasi

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ContentView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialog


import java.util.zip.Inflater
import com.vris.yuzdesavasi.R.id.btngiris
import com.vris.yuzdesavasi.R.id.ziyaretcimailText
import java.sql.PreparedStatement
import java.text.SimpleDateFormat
import java.util.Date


class GirisActivity : AppCompatActivity() {
    lateinit var  userlogin: SharedPreferences
    lateinit var  bottomSheetDialog:BottomSheetDialog
    private var connectSql = ConnectSql()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_giris)

        window.statusBarColor = ContextCompat.getColor(this, R.color.turuncu)

        val ziyaretcigirisButon=findViewById<Button>(R.id.ziyaretcigirisButon)
        val girisButon=findViewById<Button>(R.id.girisButon)


        val bottomSheetView = layoutInflater.inflate(R.layout.girisaltmenu ,null)
        bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(bottomSheetView)


        userlogin=getSharedPreferences("userlog",Context.MODE_PRIVATE)
        var log=userlogin.getString("ulog","")



       // bottomSheetDialog.setContentView(findViewById<ConstraintLayout>(R.id.background))


       /*  bottomSheetDialog = BottomSheetDialog(
            this@GirisActivity, R.style.BottomSheetDialogTheme
        )
        val bottomSheetView = LayoutInflater.from(applicationContext).inflate(
            R.layout.girisaltmenu,
            findViewById<ConstraintLayout>(R.id.background)
        )
        bottomSheetDialog.dismiss()
        bottomSheetDialog.setContentView(bottomSheetView)

        bottomSheetDialog.show()*/



        val submit:Button=bottomSheetView.findViewById(R.id.btngiris)
        val unametxt:TextView=bottomSheetView.findViewById(R.id.kullaniciadiText)
        val password:TextView=bottomSheetView.findViewById(R.id.sifreText)
        val email1:TextView=findViewById(R.id.ziyaretcimailText)

if (log!="")
{
    val intent = Intent(this, MainActivity::class.java)
    startActivity(intent)
    finish()

}

var kontro:Boolean=false
        submit.setOnClickListener {

            bottomSheetDialog.dismiss()

            if(unametxt.text.toString()==""|| password.text.toString()=="")
            {

                Toast.makeText(this, "Lütfen bilgilerini eksiksiz gir.", Toast.LENGTH_SHORT).show()
            }
            else
            {
                val login: PreparedStatement = connectSql.dbConn()
                    ?.prepareStatement("select mail,uname,pass,uid from ys where uname=? and pass=?")!!
                login.setString(1,unametxt.text.toString())
                login.setString(2,password.text.toString())



                val rs = login.executeQuery()

                    while (rs.next()) {

                        val editor2: SharedPreferences.Editor = userlogin.edit()
                        editor2.putString("ulog", rs.getString(1))

                        editor2.apply()
                        val editor1: SharedPreferences.Editor = userlogin.edit()
                        editor1.putString("uname", rs.getString(2))

                        editor1.apply()
                        val editor3: SharedPreferences.Editor = userlogin.edit()
                        editor3.putString("pass", rs.getString(3))

                        editor3.apply()
                        val editor5: SharedPreferences.Editor = userlogin.edit()
                        editor5.putInt("uid", rs.getInt(4))

                        editor5.apply()
                        val editor4: SharedPreferences.Editor = userlogin.edit()
                        editor4.putString("mail", "1")

                        editor4.apply()

 kontro=true

                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()


                    }
                if (kontro==false)
                {
                    Toast.makeText(
                        this,
                        "Bilgilerini kontrol et.",
                        Toast.LENGTH_LONG
                    ).show()
                }



            }







        }

        ziyaretcigirisButon.setOnClickListener {
            if (email1.text.toString() == ""|| email1.text.toString() == null) {

                Toast.makeText(this, "Mail veya misafir adı girmelisin.", Toast.LENGTH_SHORT).show()
            } else {


                val email = email1.text.toString()
                var chkUsr: Boolean = false
                var startvalue = 0
                var counter = 1
                var specialchar = 0
                var dot = 0
                var allspecialchar = 0
                var trchar = 0
                var endvalue = email1.length()
                var str = email.toString()
                while (startvalue != endvalue) {


                    if (str.substring(startvalue, counter) == "@") {
                        specialchar++
                    }
                    if (str.substring(startvalue, counter) == ".") {
                        dot++
                    }
                    if (str.substring(startvalue, counter) == "İ" || str.substring(
                            startvalue,
                            counter
                        ) == "ç" || str.substring(startvalue, counter) == "Ç" || str.substring(
                            startvalue,
                            counter
                        ) == "ı" || str.substring(startvalue, counter) == "Ö" || str.substring(
                            startvalue,
                            counter
                        ) == "ğ" || str.substring(startvalue, counter) == "Ğ" || str.substring(
                            startvalue,
                            counter
                        ) == "Ö" || str.substring(startvalue, counter) == "ö" || str.substring(
                            startvalue,
                            counter
                        ) == "Ü" || str.substring(startvalue, counter) == "ü" || str.substring(
                            startvalue,
                            counter
                        ) == "Ş" || str.substring(startvalue, counter) == "ş"
                    ) {
                        trchar++; }
                    if (str.substring(startvalue, counter) == "*" || str.substring(
                            startvalue,
                            counter
                        ) == "/" || str.substring(startvalue, counter) == "-" || str.substring(
                            startvalue,
                            counter
                        ) == "+" || str.substring(startvalue, counter) == "!" || str.substring(
                            startvalue,
                            counter
                        ) == "'" || str.substring(startvalue, counter) == "$" || str.substring(
                            startvalue,
                            counter
                        ) == "%" || str.substring(startvalue, counter) == "#" || str.substring(
                            startvalue,
                            counter
                        ) == "^" || str.substring(startvalue, counter) == "&" || str.substring(
                            startvalue,
                            counter
                        ) == "(" || str.substring(startvalue, counter) == ")"
                    ) {
                        allspecialchar++; }
                    startvalue = counter
                    counter++
                }

                if (trchar == 0 && allspecialchar == 0 && dot >= 1 && specialchar == 1) {
                    val chkuser: PreparedStatement = connectSql.dbConn()
                        ?.prepareStatement("select mail from ys where mail=?")!!
                    chkuser.setString(1, email.toString())

                    val rs = chkuser.executeQuery()
                    while (rs.next()) {
                        if (rs.getString(1) != email.toString()) {
                            chkUsr = false
                        } else {
                            chkUsr = true
                        }
                    }
                    if (chkUsr == true) {
                        Toast.makeText(
                            this,
                            "Bu mail adresi ile kayıt bulunmaktadır.",
                            Toast.LENGTH_LONG
                        ).show()

                    }
                    if (chkUsr == false) {
                        var uname = rastgeleKelimeUret(10)
                        var pass = rastgeleKelimeUret(8)
                        val usuario: PreparedStatement =
                            connectSql.dbConn()
                                ?.prepareStatement("insert into ys(mail,uname,pass) values (?,?,?)")!!

                        usuario.setString(1, email.toString())
                        usuario.setString(2, uname.toString())
                        usuario.setString(3, pass.toString())
                        usuario.executeUpdate()
                        val editor2: SharedPreferences.Editor = userlogin.edit()
                        editor2.putString("ulog", email.toString())

                        editor2.apply()
                        val editor1: SharedPreferences.Editor = userlogin.edit()
                        editor1.putString("uname", uname.toString())

                        editor1.apply()
                        val editor3: SharedPreferences.Editor = userlogin.edit()
                        editor3.putString("pass", pass.toString())

                        editor3.apply()
                        val editor4: SharedPreferences.Editor = userlogin.edit()
                        editor4.putString("mail", "1")

                        editor4.apply()



                        val intent = Intent(this, MailSender::class.java)
                        startActivity(intent)
                        finish()

                    }

                } else {
                    var uname = rastgeleKelimeUret(10)
                    var pass = rastgeleKelimeUret(8)
                    val usuario: PreparedStatement =
                        connectSql.dbConn()
                            ?.prepareStatement("insert into ys(mail,uname,pass) values (?,?,?)")!!

                    usuario.setString(1, email.toString())
                    usuario.setString(2, uname.toString())
                    usuario.setString(3, pass.toString())
                    usuario.executeUpdate()
                    val editor2: SharedPreferences.Editor = userlogin.edit()
                    editor2.putString("ulog", email.toString())

                    editor2.apply()
                    val editor1: SharedPreferences.Editor = userlogin.edit()
                    editor1.putString("uname", uname.toString())

                    editor1.apply()
                    val editor3: SharedPreferences.Editor = userlogin.edit()
                    editor3.putString("pass", pass.toString())

                    editor3.apply()

                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()


                }
            }


        }
        girisButon.setOnClickListener {

            bottomSheetDialog.show()
        }
    }
    fun createDialog(){

    }
    fun rastgeleKelimeUret(harfSayisi: Int): String {
        val rastgele = java.util.Random()
        val harfSemboller = "abcdefghijklmnopqrstuvwxyz1234567890*-+#"

        val kelime = StringBuilder(harfSayisi)
        for (i in 0 until harfSayisi) {
            val rastgeleIndex = rastgele.nextInt(harfSemboller.length)
            val rastgeleHarf = harfSemboller[rastgeleIndex]
            kelime.append(rastgeleHarf)
        }


        return kelime.toString()
    }
}

