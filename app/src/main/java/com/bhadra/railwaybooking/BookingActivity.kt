package com.bhadra.railwaybooking

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bhadra.railwaybooking.adapter.BookingHistoryAdapter
import com.bhadra.railwaybooking.adapter.TrainListAdapter
import com.bhadra.railwaybooking.database.TrainDBData
import com.bhadra.railwaybooking.database.TrainDataBase
import kotlinx.android.synthetic.main.activity_booking.*
import kotlinx.android.synthetic.main.train_list_dialog.*
import kotlinx.android.synthetic.main.train_list_dialog.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class BookingActivity : AppCompatActivity(), TrainListAdapter.TrainClickListener {
    lateinit var alertDialog: AlertDialog
    val myCalendar = Calendar.getInstance()
    lateinit var trainName: String
    lateinit var sharedPreferences: SharedPreferences
    lateinit var sharedPrefEditor: SharedPreferences.Editor
    private val sharedPrefFileName = "SharedFile"
    lateinit var trainHistoryData: List<TrainDBData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking)
        sharedPreferences = this.getSharedPreferences(sharedPrefFileName, Context.MODE_PRIVATE)
        btn_book.setOnClickListener {
            if (validate()) {

                GlobalScope.launch {
                    insertTrainData()
                }
                showSucessDialog(
                    etv_travel_date.text.toString(),
                    etv_num_of_travellers.text.toString()
                )
            } else {
                Toast.makeText(
                    this@BookingActivity,
                    "Please fill all the fields",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        val scheduleDate =
            OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, monthOfYear)
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val dateFormat = "dd/MM/yyyy"
                val simpleDateFormat = SimpleDateFormat(dateFormat)
                etv_travel_date.setText(simpleDateFormat.format(myCalendar.time))
            }

        etv_travel_date.setOnClickListener(View.OnClickListener {
            DatePickerDialog(
                this@BookingActivity, scheduleDate,
                myCalendar[Calendar.YEAR], myCalendar[Calendar.MONTH],
                myCalendar[Calendar.DAY_OF_MONTH]
            ).show()
        })

        tv_train_list.setOnClickListener {
            showTrainList()
        }
    }

    private fun validate(): Boolean {
        if (etv_source.text == null || etv_source.text.toString().isEmpty()) {
            return false
        }
        if (etv_destination.text == null || etv_destination.text.toString().isEmpty()) {
            return false
        }
        if (etv_train_number.text == null || etv_train_number.text.toString().isEmpty()) {
            return false
        }
        if (etv_num_of_travellers.text == null || etv_num_of_travellers.text.toString().isEmpty()) {
            return false
        }
        if (etv_travel_date.text == null || etv_travel_date.text.toString().isEmpty()) {
            return false
        }
        return true
    }

    private suspend fun insertTrainData() {
        val trainNum = etv_train_number.text.toString()
        val source = etv_source.text.toString()
        val destination = etv_destination.text.toString()
        val numOfTravellers = etv_num_of_travellers.text.toString()
        val date = etv_travel_date.text.toString()
        val trainDao = TrainDataBase.getDatabase(application).TrainDao()
        trainDao?.let { dao ->
            withContext(Dispatchers.IO) {
                trainDao.insertTrainData(
                    TrainDBData(
                        trainNum,
                        source,
                        destination,
                        trainName,
                        date,
                        numOfTravellers
                    )
                )
            }
        }


    }

    private fun showSucessDialog(date: String, numOfTravellers: String) {
        val dialogBuilder =
            AlertDialog.Builder(this)
        dialogBuilder
            .setCancelable(false)
            .setMessage(
                "Booking Completed for $trainName for $numOfTravellers passengers " +
                        "on $date.\n Have a safe journey!!!"
            )
        dialogBuilder
            .setPositiveButton(
                "Okay",
                DialogInterface.OnClickListener { dialog, which ->
                    etv_train_number.setText("")
                    etv_source.setText("")
                    etv_destination.setText("")
                    etv_num_of_travellers.setText("")
                    etv_travel_date.setText("")
                    dialog.cancel()
                })
        val alert = dialogBuilder.create()
        alert.setTitle("Success")
        alert.show()

    }

    private fun showTrainList() {

        val dialogBuilder =
            AlertDialog.Builder(this@BookingActivity)
        val inflater =
            this@BookingActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(R.layout.train_list_dialog, null)
        view.tv_events_title.setText("List of Trains")
        val imv_close_webcastalert =
            view.findViewById<ImageView>(R.id.imv_close)
        val rv_trainList: RecyclerView = view.findViewById(R.id.rv_train_list)
        rv_trainList.apply {
            adapter = TrainListAdapter(this@BookingActivity)
            layoutManager = LinearLayoutManager(this@BookingActivity)
        }

        imv_close_webcastalert.setOnClickListener { alertDialog.dismiss() }
        dialogBuilder.setView(view)
        alertDialog = dialogBuilder.create()
        alertDialog.show()
    }

    private suspend fun showBookHistory() {
        val trainDao = TrainDataBase.getDatabase(application).TrainDao()


        withContext(Dispatchers.IO) {
            trainHistoryData = trainDao.getAllData()
        }.let {
            withContext(Dispatchers.Main) {
                showHistoryDialog()
            }
        }


    }

    private suspend fun showHistoryDialog() {
        if (trainHistoryData == null || trainHistoryData.size > 0) {
            val dialogBuilder =
                AlertDialog.Builder(this@BookingActivity)
            val inflater =
                this@BookingActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view: View = inflater.inflate(R.layout.train_list_dialog, null)
            view.tv_events_title.setText("Booking History Details")
            val imv_close_webcastalert =
                view.findViewById<ImageView>(R.id.imv_close)
            val rv_trainList: RecyclerView = view.findViewById(R.id.rv_train_list)
            rv_trainList.apply {
                adapter = BookingHistoryAdapter(historyData = trainHistoryData)
                layoutManager = LinearLayoutManager(this@BookingActivity)
            }

            imv_close_webcastalert.setOnClickListener { alertDialog.dismiss() }
            dialogBuilder.setView(view)
            alertDialog = dialogBuilder.create()
            alertDialog.show()
        }else{
            Toast.makeText(this@BookingActivity,"Empty History",Toast.LENGTH_LONG).show()
        }
    }

    override fun onTrainClicked(trainNumber: String, tName: String) {
        alertDialog.dismiss()
        etv_train_number.setText(trainNumber)
        trainName = tName
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> {
                sharedPrefEditor = sharedPreferences.edit()
                sharedPrefEditor.clear()
                if (sharedPrefEditor.commit()) {
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this@BookingActivity, "Try Again", Toast.LENGTH_LONG).show()
                }
                return true
            }
            R.id.booking_history -> {
                GlobalScope.launch {
                    showBookHistory()
                }
             //   Toast.makeText(this@BookingActivity, "Show History", Toast.LENGTH_LONG).show()
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }
}