package com.bhadra.railwaybooking

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.bhadra.railwaybooking.utils.Constants

class SplashScreen : AppCompatActivity() {
    lateinit var sharedPreferences: SharedPreferences
    private val sharedPrefFileName = "SharedFile"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        sharedPreferences = this.getSharedPreferences(sharedPrefFileName, Context.MODE_PRIVATE)

        val isLogged = sharedPreferences.getString(Constants.KEY_NAME,"NA")
        Handler().postDelayed(Runnable {
            if(isLogged.equals("NA")){
                val intent = Intent(this,LoginActivity::class.java)
                startActivity(intent)
            }else{
                val intent = Intent(this,BookingActivity::class.java)
                startActivity(intent)
            }
            finish()
        }, 1400)


    }
}