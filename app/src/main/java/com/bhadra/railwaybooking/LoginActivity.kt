package com.bhadra.railwaybooking

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bhadra.railwaybooking.database.TrainDBData
import com.bhadra.railwaybooking.database.TrainDataBase
import com.bhadra.railwaybooking.utils.Constants
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {
    lateinit var sharedPreferences:SharedPreferences
    lateinit var sharedPrefEditor: SharedPreferences.Editor
    private val sharedPrefFileName = "SharedFile"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        sharedPreferences = this.getSharedPreferences(sharedPrefFileName,Context.MODE_PRIVATE)
        btn_submit.setOnClickListener {
            if(validate()){
                if (saveToPreference()){
                    GlobalScope.launch {
                        eraseDBData()
                    }
                    val intent = Intent(this@LoginActivity,BookingActivity::class.java)
                    startActivity(intent)
                }
            }else{
                Toast.makeText(this@LoginActivity,"Please fill all the fields",Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun saveToPreference() :Boolean{
        sharedPrefEditor = sharedPreferences.edit()
        sharedPrefEditor.putString(Constants.KEY_NAME,etv_name.text.toString())
        sharedPrefEditor.putString(Constants.KEY_EMAIL,etv_email_address.text.toString())
        sharedPrefEditor.putString(Constants.KEY_PASSWORD,etv_password.text.toString())
        return sharedPrefEditor.commit()
    }
    private suspend fun eraseDBData(){
        val trainDao = TrainDataBase.getDatabase(application).TrainDao()
        trainDao?.let { dao ->
            withContext(Dispatchers.IO){
                trainDao.deleteData()
            }
        }
    }
    private fun validate(): Boolean {
        if(etv_name.text == null || etv_name.text.toString().isEmpty()){
            return false;
        }
        if(etv_email_address == null || etv_email_address.text.toString().isEmpty()){
            return false;
        }
        if(etv_password == null || etv_password.text.toString().isEmpty()){
            return false;
        }
        return true;
    }
}

/*
fun main(array: Array<String>){
    lateinit var vri :String
    println(vri)
}*/
