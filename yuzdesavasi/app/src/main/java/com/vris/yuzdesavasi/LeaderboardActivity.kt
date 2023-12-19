package com.vris.yuzdesavasi

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.sql.PreparedStatement

class LeaderboardActivity : AppCompatActivity() {
    private var connectSql = ConnectSql()
    lateinit var  userlogin: SharedPreferences
    lateinit var mAdView : AdView

    lateinit var  bottomSheetDialogOdul: BottomSheetDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaderboard)
        val itemList = mutableListOf<String>()
        val yourid=findViewById<TextView>(R.id.kullaniciadinText)

        val geriButon=findViewById<ImageView>(R.id.leadergeriButon)

        val bottomSheetViewOdul = layoutInflater.inflate(R.layout.odulaltmenu,null)
        bottomSheetDialogOdul = BottomSheetDialog(this)
        bottomSheetDialogOdul.setContentView(bottomSheetViewOdul)

        val odulbuton = findViewById<Button>(R.id.odullistesiButon)

        odulbuton.setOnClickListener {
            bottomSheetDialogOdul.show()
        }

        MobileAds.initialize(this) {}

        mAdView = findViewById(R.id.banner2)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        geriButon.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        window.statusBarColor = ContextCompat.getColor(this, R.color.turuncu)
        userlogin=getSharedPreferences("userlog", Context.MODE_PRIVATE)
        yourid.setText("ID: "+userlogin.getString("uid",""))

        val board=findViewById<RecyclerView>(R.id.siralama)

            val lider: PreparedStatement = connectSql.dbConn()
                ?.prepareStatement("select TOP 10 uid,lvl from ys ORDER BY lvl DESC ")!!


            val rs = lider.executeQuery()
            while (rs.next()) {

                itemList.add("ID:" + rs.getInt(1).toString() + "\n"+ "Seviye:" + rs.getInt(2).toString())


            }




            val adapter = MyAdapter(itemList)
        board.layoutManager=LinearLayoutManager(this)
        board.adapter=adapter
          



    }

    fun showToast(context: Context, message: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(context, message, duration).show()
    }
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.textView5)
    }
    class MyAdapter(private val itemList: List<String>) : RecyclerView.Adapter<MyViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.sira_layout, parent, false)
            return MyViewHolder(view)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val item = itemList[position]
            holder.textView.text = item
        }

        override fun getItemCount(): Int {
            return itemList.size
        }
    }
    fun loadbanner()
    {



        mAdView = findViewById(R.id.banner2)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
        mAdView.adListener = object: AdListener() {
            override fun onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            override fun onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }

            override fun onAdFailedToLoad(adError : LoadAdError) {
                // Code to be executed when an ad request fails.
            }

            override fun onAdImpression() {
                // Code to be executed when an impression is recorded
                // for an ad.
            }

            override fun onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            override fun onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }
        }
    }













}