package com.vris.yuzdesavasi

import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.gms.ads.rewarded.ServerSideVerificationOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.sql.PreparedStatement

import kotlinx.coroutines.CoroutineScope
import net.sourceforge.jtds.jdbc.DateTime
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {
    private var connectSql = ConnectSql()
    private var usemail = usemail()
    var pint: Int = 0
    lateinit var userlogin: SharedPreferences
    var rand: Int = 0
    lateinit var mAdView: AdView
    lateinit var bottomSheetDialog: BottomSheetDialog
    lateinit var bottomSheetDialogPromo: BottomSheetDialog

    private var mInterstitialAd: InterstitialAd? = null
    private var rewardedAd: RewardedAd? = null
    private final var TAG = "MainActivity"
    var main = 0
    val adRequest = AdRequest.Builder().build()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MobileAds.initialize(this) {}

        mAdView = findViewById(R.id.adView)
        //       val adRequest = AdRequest.Builder().build()


        mAdView.loadAd(adRequest)


        window.statusBarColor = ContextCompat.getColor(this, R.color.turuncu)

        RewardedAd.load(
            this,
            "ca-app-pub-7985415678818709/2261999584",
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {

                    rewardedAd = null
                    /*    rewardedInterstitialAd = ad
                    val options = ServerSideVerificationOptions.Builder()
                        .setCustomData("SAMPLE_CUSTOM_DATA_STRING")
                        .build()
                    rewardedAd.setServerSideVerificationOptions(options)*/
                }

                override fun onAdLoaded(ad: RewardedAd) {

                    rewardedAd = ad
                }
            })
        InterstitialAd.load(
            this,
            "ca-app-pub-7985415678818709/3264026428",
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {

                    mInterstitialAd = null
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {

                    mInterstitialAd = interstitialAd

                }
            })


        // loadbanner()


        val oyuntext = findViewById<TextView>(R.id.oyunText)





        userlogin = getSharedPreferences("userlog", Context.MODE_PRIVATE)
        val bottomSheetView = layoutInflater.inflate(R.layout.altmenu, null)
        bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(bottomSheetView)

        val bottomSheetViewPromo = layoutInflater.inflate(R.layout.promoaltmenu, null)
        bottomSheetDialogPromo = BottomSheetDialog(this)
        bottomSheetDialogPromo.setContentView(bottomSheetViewPromo)


        //Toast.makeText(this,userlogin.getString("mail","").toString(), Toast.LENGTH_SHORT).show()


        val playButton = findViewById<ImageView>(R.id.oynaView)

        val canButon = findViewById<ConstraintLayout>(R.id.canButon)
        val seviyeButon = findViewById<ConstraintLayout>(R.id.seviyeButon)
        val magazaButon = findViewById<ConstraintLayout>(R.id.magazaButon)
        val menuButon = findViewById<ConstraintLayout>(R.id.menuButon)
        val televizyoncekmi = findViewById<View>(R.id.televizyonacikView)
        val televizonac = findViewById<View>(R.id.televizyonView)
        val candurumtxt = findViewById<TextView>(R.id.candurumText)
        val lvldurumtxt = findViewById<TextView>(R.id.leveldurumText)
        val bildirim = findViewById<View>(R.id.bildirim)
        val reklambutton = findViewById<Button>(R.id.reklamButon)

        val mailup: Button = bottomSheetView.findViewById(R.id.button)
        val logoff: Button = bottomSheetView.findViewById(R.id.button4)
        val muzikackapa: Button = bottomSheetView.findViewById(R.id.button2)
        val promo: Button = bottomSheetView.findViewById(R.id.promoButon)

        val promokullan = bottomSheetViewPromo.findViewById<Button>(R.id.promokullanButon)
        val promotext = bottomSheetViewPromo.findViewById<TextView>(R.id.promocodeText)




        promokullan.setOnClickListener {

            try {


                val login: PreparedStatement = connectSql.dbConn()
                    ?.prepareStatement(
                        "select userid from promo where userid=" + userlogin.getString("uid","")?.toInt() + ""
                    )!!


                val rs = login.executeQuery()
                if (rs.next()) {

                    Toast.makeText(this, "Kullanılmış.", Toast.LENGTH_SHORT).show()
                } else {
                    login.close()
                    val promo: PreparedStatement = connectSql.dbConn()
                        ?.prepareStatement("select code from promocode where code='" + promotext.text.toString() + "'")!!
                    val rs2 = promo.executeQuery()
                    if (rs2.next()) {
                        val promoinsert: PreparedStatement = connectSql.dbConn()
                            ?.prepareStatement(
                                "insert into promo values('" + userlogin.getString(
                                    "uid",
                                    ""
                                )?.toInt() + "')"
                            )!!

                        promoinsert.executeUpdate()
                        promoinsert.close()
                        val promoinsert1: PreparedStatement = connectSql.dbConn()
                            ?.prepareStatement(
                                "update ys set health=health+10 where mail='" + userlogin.getString(
                                    "ulog",
                                    ""
                                ).toString() + "'"
                            )!!
                        promoinsert1.executeUpdate()

                        finish();
                        startActivity(getIntent());
                        Toast.makeText(this, "Toplandı.", Toast.LENGTH_SHORT).show()
                    }

                }
            } catch (e: Exception) {
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
            }
        }

        promo.setOnClickListener {
            bottomSheetDialog.dismiss()
            bottomSheetDialogPromo.show()
        }

        reklambutton.setOnClickListener {

            //  canreklam()
            rewardedAd?.let { ad ->
                ad.show(this, OnUserEarnedRewardListener { rewardItem ->
                    // Handle the reward.
                    val rewardAmount = rewardItem.amount
                    val rewardType = rewardItem.type
                    //  Log.d(TAG, "User earned the reward.")
                })
            } ?: run {
                //  Log.d(TAG, "The rewarded ad wasn't ready yet.")
                val context = applicationContext // Bu bağlamı uygun şekilde değiştirin
                val message = "Can toplama süren dolmadı."
                showToast(context, message)
            }
            rewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdClicked() {
                    // Called when a click is recorded for an ad.
                    Log.d(TAG, "Ad was clicked.")
                }

                override fun onAdDismissedFullScreenContent() {
                    // Called when ad is dismissed.
                    // Set the ad reference to null so you don't show the ad a second time.
                    Log.d(TAG, "Ad dismissed fullscreen content.")
                    rewardedAd = null
                    finish();
                    startActivity(getIntent());
                }

                override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                    // Called when ad fails to show.
                    Log.e(TAG, "Ad failed to show fullscreen content.")
                    rewardedAd = null
                }

                override fun onAdImpression() {
                    val candurumtxt = findViewById<TextView>(R.id.candurumText)
                    // Called when an impression is recorded for an ad.
                    val dailychkcan: PreparedStatement = connectSql.dbConn()
                        ?.prepareStatement(
                            "update ys set health=health+2 where mail='" + userlogin.getString(
                                "ulog",
                                ""
                            ).toString() + "'"
                        )!!
                    CoroutineScope(Dispatchers.IO).launch {
                        dailychkcan.executeUpdate()
                        withContext(Dispatchers.Main) {
                            val handler = Handler(Looper.getMainLooper())
                            lvlcan()
                            candurumtxt.setText("+2")
                            candurumtxt.visibility = View.VISIBLE
                            handler.postDelayed({

                                candurumtxt.visibility = View.INVISIBLE
                            }, 1000)
                        }
                    }


                }

                override fun onAdShowedFullScreenContent() {
                    // Called when ad is shown.
                    Log.d(TAG, "Ad showed fullscreen content.")
                }
            }

        }

        if (cankontrol() == false) {
            bildirim.visibility = View.INVISIBLE
        }

        muzikackapa.setOnClickListener {
            val context = applicationContext // Bu bağlamı uygun şekilde değiştirin
            val message = "Çok yakında geliyor."
            showToast(context, message)
        }

        if (userlogin.getString("mail", "").toString() == "1") {
            mailup.visibility = View.GONE
        }

        logoff.setOnClickListener {

            bottomSheetDialog.dismiss()
            val editor2: SharedPreferences.Editor = userlogin.edit()
            editor2.putString("ulog", null)

            editor2.apply()
            val editor1: SharedPreferences.Editor = userlogin.edit()
            editor1.putString("uname", null)

            editor1.apply()
            val editor3: SharedPreferences.Editor = userlogin.edit()
            editor3.putString("pass", null)

            editor3.apply()
            val editor4: SharedPreferences.Editor = userlogin.edit()
            editor4.putString("mail", null)

            editor4.apply()
            val intent = Intent(this, GirisActivity::class.java)
            startActivity(intent)
            finish()
        }

        mailup.setOnClickListener {
            bottomSheetDialog.dismiss()
            val intent = Intent(this, MaildogrulaActivity::class.java)
            startActivity(intent)
            finish()
        }


        val circleYuzde = findViewById<TextView>(R.id.circleYuzde)
        val progressBar = findViewById<ProgressBar>(R.id.circle)
        val leftright = findViewById<ImageView>(R.id.dondurmeButon)
        var say = 0
        leftright.setOnClickListener {
            if (say == 0) {
                progressBar.rotationY = 180f
                progressBar.progress = pint
                televizyoncekmi.visibility = View.INVISIBLE
                televizonac.visibility = View.VISIBLE
                progressBar.visibility = View.VISIBLE
                val animator = ValueAnimator.ofInt(0, pint)
                animator.duration = 1500
                animator.addUpdateListener { animation ->
                    progressBar.progress = animation.animatedValue as Int
                }

                animator.start()
                say++
            } else if (say == 1) {
                progressBar.rotationY = 0f
                progressBar.progress = pint
                televizyoncekmi.visibility = View.INVISIBLE
                televizonac.visibility = View.VISIBLE
                progressBar.visibility = View.VISIBLE
                val animator = ValueAnimator.ofInt(0, pint)
                animator.duration = 1500
                animator.addUpdateListener { animation ->
                    progressBar.progress = animation.animatedValue as Int
                }

                animator.start()
                say--
            }


        }
        //  progressBar.rotationY=180f

        progressBar.progress = pint
        progressBar.isIndeterminate = false

        lvlcan()
        oyuntext.text = "Şans: %" + pint

        canButon.setOnClickListener {

            if (cankontrol() == true) {

                try {
                    val dailychk: PreparedStatement = connectSql.dbConn()
                        ?.prepareStatement(
                            "update ys set lhdate=GETDATE() where mail='" + userlogin.getString(
                                "ulog",
                                ""
                            ).toString() + "'"
                        )!!
                    CoroutineScope(Dispatchers.IO).launch {


                        withContext(Dispatchers.Main) {
                            dailychk.executeUpdate()
                        }

                    }
                } catch (e: Exception) {
                    Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
                }

                val dailychkcan: PreparedStatement = connectSql.dbConn()
                    ?.prepareStatement(
                        "update ys set health=health+1 where mail='" + userlogin.getString(
                            "ulog",
                            ""
                        ).toString() + "'"
                    )!!
                CoroutineScope(Dispatchers.IO).launch {
                    dailychkcan.executeUpdate()
                    withContext(Dispatchers.Main) {
                        val handler = Handler(Looper.getMainLooper())
                        lvlcan()
                        candurumtxt.setText("+1")
                        candurumtxt.visibility = View.VISIBLE
                        handler.postDelayed({
                            bildirim.visibility = View.INVISIBLE
                            candurumtxt.visibility = View.INVISIBLE
                            if(userlogin.getInt("feedback",0)==0) {
                                showPopup()
                            }
                        }, 2000)
                    }
                }
            } else {
                Toast.makeText(this, "Henüz 1 saat dolmadı.", Toast.LENGTH_SHORT).show()
            }


        }
        seviyeButon.setOnClickListener {

        }
        magazaButon.setOnClickListener {
            /*   val context = applicationContext // Bu bağlamı uygun şekilde değiştirin
            val message = "Çok yakında geliyor."
            showToast(context, message)*/

            val intent = Intent(this, LeaderboardActivity::class.java)
            startActivity(intent)
            finish()
        }

        menuButon.setOnClickListener {


            bottomSheetDialog.show()

        }

        val cantext = findViewById<TextView>(R.id.canText)


        playButton.setOnClickListener {

            if (cantext.text != "0") {
                televizyoncekmi.visibility = View.INVISIBLE
                televizonac.visibility = View.VISIBLE
                progressBar.visibility = View.VISIBLE

                playButton.isEnabled = false
                leftright.isEnabled = false
                val randomInt = (0..100).random()
                rand = randomInt
                val randomInt2 = (0..5).random()


                val animator = ValueAnimator.ofInt(0, randomInt)
                var ms: Long = 10000
                //  val context = applicationContext // Bu bağlamı uygun şekilde değiştirin
                //  val message = ms.toString()
                // showToast(context, message)
                if (randomInt2 == 0) {
                    animator.duration = ms * 2
                } else if (randomInt2 == 1) {
                    animator.duration = ms + 4000
                } else if (randomInt2 == 2) {
                    animator.duration = ms - 4000
                } else if (randomInt2 == 3) {
                    animator.duration = ms / 2
                } else if (randomInt2 == 4) {
                    animator.duration = ms - 7000
                } else if (randomInt2 == 5) {
                    animator.duration = ms + 1000
                }


                animator.addUpdateListener { animation ->
                    progressBar.progress = animation.animatedValue as Int
                }
                animator.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        super.onAnimationEnd(animation)
                        // Burada aktivitenizi başlatın


                        if (randomInt <= pint) {
                            lvldurumtxt.visibility = View.VISIBLE
                            lvldurumtxt.setText("+1")
                            candurumtxt.visibility = View.VISIBLE
                            candurumtxt.setText("-1")

                            val chance: PreparedStatement = connectSql.dbConn()
                                ?.prepareStatement(
                                    "update ys set health=health-1,chance=chance-2,lvl=lvl+1 where mail='" + userlogin.getString(
                                        "ulog",
                                        ""
                                    ).toString() + "'"
                                )!!

                            chance.executeUpdate()

                            lvlcan()


                            /* progressBar.progressDrawable = resources.getDrawable(R.drawable.green)

                            progressBar.progress=randomInt-1+1+2-2*/


                            val handler = Handler(Looper.getMainLooper())
                            handler.postDelayed({
                                // progressBar.progressDrawable = resources.getDrawable(R.drawable.circle)
                                progressBar.progress = pint
                                val animator = ValueAnimator.ofInt(0, pint)
                                animator.duration = 3000
                                animator.addUpdateListener { animation ->
                                    progressBar.progress = animation.animatedValue as Int
                                }

                                animator.start()
                                val handler1 = Handler(Looper.getMainLooper())
                                handler1.postDelayed({
                                    //   circleYuzde.visibility = View.INVISIBLE
                                    televizyoncekmi.visibility = View.VISIBLE

                                    oyuntext.text = "Şans: %" + pint

                                    televizonac.visibility = View.INVISIBLE
                                    progressBar.visibility = View.INVISIBLE
                                    playButton.isEnabled = true
                                    leftright.isEnabled = true
                                    candurumtxt.visibility = View.INVISIBLE
                                    candurumtxt.setText("-1")
                                    lvldurumtxt.visibility = View.INVISIBLE
//loadiniad()

                                    val handler2 = Handler(Looper.getMainLooper())
                                    handler2.postDelayed({
                                        if (mInterstitialAd != null) {
                                            mInterstitialAd?.show(this@MainActivity)
                                        } else {
                                            //   Toast.makeText(this,"hazır değil", Toast.LENGTH_SHORT).show()

                                        }
                                        mInterstitialAd?.fullScreenContentCallback =
                                            object : FullScreenContentCallback() {
                                                override fun onAdClicked() {
                                                    // Called when a click is recorded for an ad.
                                                    Log.d(TAG, "Ad was clicked.")
                                                }

                                                override fun onAdDismissedFullScreenContent() {
                                                    // Called when ad is dismissed.
                                                    Log.d(TAG, "Ad dismissed fullscreen content.")

                                                    mInterstitialAd = null
                                                    finish();
                                                    startActivity(getIntent());
                                                }

                                                override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                                                    // Called when ad fails to show.
                                                    Log.e(
                                                        TAG,
                                                        "Ad failed to show fullscreen content."
                                                    )
                                                    mInterstitialAd = null
                                                }

                                                override fun onAdImpression() {
                                                    // Called when an impression is recorded for an ad.

                                                    Log.d(TAG, "Ad recorded an impression.")
                                                }

                                                override fun onAdShowedFullScreenContent() {
                                                    // Called when ad is shown.
                                                    Log.d(TAG, "Ad showed fullscreen content.")
                                                }
                                            }

                                    }, 1000)
                                }, 3000)
                            }, 5000)


                            val context = applicationContext // Bu bağlamı uygun şekilde değiştirin
                            val message = randomInt.toString()
                            // showToast(context, message)


                            //  progressBar.indeterminateDrawable.setColorFilter(Color.GREEN, android.graphics.PorterDuff.Mode.SRC_IN)
                            //  progressBar.getIndeterminateDrawable().setColorFilter(Color.GREEN,android.graphics.PorterDuff.Mode.MULTIPLY)
                            //      circleYuzde.setText("%" + pint)
                            oyuntext.text = "Kazandın! -%2 Şans"

                        } else if (randomInt > pint) {

                            candurumtxt.visibility = View.VISIBLE
                            candurumtxt.setText("-1")

                            val chance: PreparedStatement = connectSql.dbConn()
                                ?.prepareStatement(
                                    "update ys set health=health-1,chance=chance+2 where mail='" + userlogin.getString(
                                        "ulog",
                                        ""
                                    ).toString() + "'"
                                )!!

                            chance.executeUpdate()


                            lvlcan()

                            /* progressBar.progressDrawable = resources.getDrawable(R.drawable.red)

                                progressBar.progress=randomInt+1-3+2*/
                            val handler = Handler(Looper.getMainLooper())
                            handler.postDelayed({
                                // progressBar.progressDrawable = resources.getDrawable(R.drawable.circle)
                                progressBar.progress = pint
                                val animator = ValueAnimator.ofInt(0, pint)
                                animator.duration = 3000
                                animator.addUpdateListener { animation ->
                                    progressBar.progress = animation.animatedValue as Int
                                }

                                animator.start()
                                val handler1 = Handler(Looper.getMainLooper())
                                handler1.postDelayed({
                                    televizyoncekmi.visibility = View.VISIBLE
                                    //  circleYuzde.visibility = View.INVISIBLE
                                    televizonac.visibility = View.INVISIBLE

                                    oyuntext.text = "Şans: %" + pint

                                    progressBar.visibility = View.INVISIBLE
                                    playButton.isEnabled = true
                                    leftright.isEnabled = true
                                    candurumtxt.visibility = View.INVISIBLE
                                    // loadiniad()
                                    val handler2 = Handler(Looper.getMainLooper())
                                    handler2.postDelayed({
                                        if (mInterstitialAd != null) {
                                            mInterstitialAd?.show(this@MainActivity)
                                        } else {
                                            //   Toast.makeText(this,"hazır değil", Toast.LENGTH_SHORT).show()

                                        }
                                        mInterstitialAd?.fullScreenContentCallback =
                                            object : FullScreenContentCallback() {
                                                override fun onAdClicked() {
                                                    // Called when a click is recorded for an ad.
                                                    Log.d(TAG, "Ad was clicked.")
                                                }

                                                override fun onAdDismissedFullScreenContent() {
                                                    // Called when ad is dismissed.
                                                    Log.d(TAG, "Ad dismissed fullscreen content.")
                                                    mInterstitialAd = null
                                                    finish();
                                                    startActivity(getIntent());
                                                }

                                                override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                                                    // Called when ad fails to show.
                                                    Log.e(
                                                        TAG,
                                                        "Ad failed to show fullscreen content."
                                                    )
                                                    mInterstitialAd = null
                                                }

                                                override fun onAdImpression() {
                                                    // Called when an impression is recorded for an ad.
                                                    /*   finish();
                                                startActivity(getIntent());*/
                                                    Log.d(TAG, "Ad recorded an impression.")
                                                }

                                                override fun onAdShowedFullScreenContent() {
                                                    // Called when ad is shown.
                                                    Log.d(TAG, "Ad showed fullscreen content.")
                                                }
                                            }

                                    }, 1000)


                                }, 3000)

                            }, 5000)
                            // progressBar.indeterminateDrawable.setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN)
                            //  progressBar.indeterminateDrawable.setColorFilter(Color.RED, android.graphics.PorterDuff.Mode.SRC_IN)
                            val context = applicationContext // Bu bağlamı uygun şekilde değiştirin
                            val message = randomInt.toString()
                            //showToast(context, message)
                            oyuntext.text = "Kaybettin! +%2 Şans"

                            //    progressBar.indeterminateDrawable=resources.getDrawable(R.drawable.red)
                            // progressBar.getIndeterminateDrawable().setColorFilter(Color.RED,android.graphics.PorterDuff.Mode.MULTIPLY)
                            //  circleYuzde.setText("%" + pint)


                        }


                        //  progressBar.isIndeterminate = false


                    }
                })
                animator.start()


            } else {
                Toast.makeText(this, "Oynamak için cana ihtiyacın var.", Toast.LENGTH_SHORT).show()
            }
        }


    }

    fun lvlcan() {
        CoroutineScope(Dispatchers.IO).launch {
            val login: PreparedStatement = connectSql.dbConn()
                ?.prepareStatement("select lvl,health,chance,uid from ys where mail=?")!!
            login.setString(1, userlogin.getString("ulog", "").toString())
            val cantext = findViewById<TextView>(R.id.canText)
            val seviye = findViewById<TextView>(R.id.seviyeText)
            val oyuntext = findViewById<TextView>(R.id.oyunText)

            val rs = login.executeQuery()
            while (rs.next()) {
                withContext(Dispatchers.Main) {


                    cantext.text = rs.getInt(2).toString()
                    seviye.text = (rs.getInt(1)).toString()
                    pint = rs.getInt(3)

                    if (main == 0) {
                        oyuntext.text = "Şans: %" + rs.getInt(3).toString()
                        main++
                    }
                    val editor5: SharedPreferences.Editor = userlogin.edit()
                    editor5.putString("uid", rs.getInt(4).toString())

                    editor5.apply()
                }
            }
        }

    }

    fun loadbanner() {

        MobileAds.initialize(this) {}

        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
        mAdView.adListener = object : AdListener() {
            override fun onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            override fun onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
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

    fun cankontrol(): Boolean {
        var knt = false
        /*  val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({*/
        val dailychk: PreparedStatement = connectSql.dbConn()
            ?.prepareStatement(
                "select DATEDIFF(HOUR,(SELECT CONVERT(datetime, (select lhdate from ys where mail='" + userlogin.getString(
                    "ulog",
                    ""
                ).toString() + "' ))),GETDATE() )"
            )!!
        val kontoroldaily = dailychk.executeQuery()
        //     Toast.makeText(this,userlogin.getString("ulog","").toString()+".", Toast.LENGTH_SHORT).show()


        while (kontoroldaily.next()) {
            //showToast(this@MainActivity,kontoroldaily.getInt(1).toString(),Toast.LENGTH_LONG)
            //  Toast.makeText(this, , Toast.LENGTH_SHORT).show()
            if (kontoroldaily.getInt(1) >= 1) {
                knt = true
            }

        }
        // }, 100)

        if (knt == true) {
            return true

        } else
            return false


    }


    fun showToast(context: Context, message: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(context, message, duration).show()
    }

    fun loadiniad() {
        InterstitialAd.load(
            this,
            "ca-app-pub-3940256099942544/1033173712",
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {

                    mInterstitialAd = null
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {

                    mInterstitialAd = interstitialAd

                }
            })

        if (mInterstitialAd != null) {
            mInterstitialAd?.show(this)
        } else {
            Toast.makeText(this, "hazır değil", Toast.LENGTH_SHORT).show()

        }
    }

    fun canreklam() {
        RewardedAd.load(
            this,
            "ca-app-pub-3940256099942544/5224354917",
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {

                    rewardedAd = null
                    /*    rewardedInterstitialAd = ad
                    val options = ServerSideVerificationOptions.Builder()
                        .setCustomData("SAMPLE_CUSTOM_DATA_STRING")
                        .build()
                    rewardedAd.setServerSideVerificationOptions(options)*/
                }

                override fun onAdLoaded(ad: RewardedAd) {

                    rewardedAd = ad
                }
            })
        rewardedAd?.let { ad ->
            ad.show(this, OnUserEarnedRewardListener { rewardItem ->
                // Handle the reward.
                val rewardAmount = rewardItem.amount
                val rewardType = rewardItem.type
                Log.d(TAG, "User earned the reward.")
            })
        } ?: run {
            Log.d(TAG, "The rewarded ad wasn't ready yet.")
        }

    }

    private fun showPopup() {
        val dialogBuilder = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.popup, null)
        dialogBuilder.setView(view)

        val degerlendirText: TextView = view.findViewById(R.id.editTextTextMultiLine)
        val degerlendirButon = view.findViewById<Button>(R.id.button6)
        val kapatButon = view.findViewById<Button>(R.id.button7)

        val alertDialog = dialogBuilder.create()

        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        degerlendirButon.setOnClickListener {
            val name = degerlendirText.text.toString()

            try{
                var feedbacknt=userlogin.getInt("feedback",0)
                val editor5: SharedPreferences.Editor = userlogin.edit()
                editor5.putInt("feedback",2)
                editor5.apply()

                val promoinsert1: PreparedStatement = connectSql.dbConn()
                    ?.prepareStatement(
                        "insert into feedback values("+userlogin.getString("uid","")?.toInt()+",'"+degerlendirText.text.toString()+"',getdate())"
                    )!!
                promoinsert1.executeUpdate()

            alertDialog.dismiss()
            }
            catch (e:Exception){
                Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show()
            }
            }

        kapatButon.setOnClickListener {
            alertDialog.dismiss()
        }

            alertDialog.show()

        }

    }