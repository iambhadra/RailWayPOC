package com.bhadra.railwaybooking.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bhadra.railwaybooking.R
import com.bhadra.railwaybooking.database.TrainDBData

class BookingHistoryAdapter(val historyData: List<TrainDBData>): RecyclerView.Adapter<BookingHistoryAdapter.BookingViewHolder>() {
    class BookingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val source : TextView = itemView.findViewById(R.id.tv_source)
         var destination : TextView = itemView.findViewById(R.id.tv_destination)
         var trainNum: TextView = itemView.findViewById(R.id.tv_train_number)
         var trainName: TextView = itemView.findViewById(R.id.tv_train_name)
         var numOfSeats: TextView = itemView.findViewById(R.id.tv_traveller_count)
         val travelDate : TextView = itemView.findViewById(R.id.tv_travel_date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.train_history_item,parent,false)
        return BookingViewHolder(view)
    }

    override fun getItemCount(): Int {
        return historyData.size
    }

    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        holder.trainName.text ="Train Name :  ${historyData.get(position).trainName}"
        holder.trainNum.text = "Train No. : ${historyData.get(position).trainNum}"
        holder.numOfSeats.text ="No. of Seats : ${historyData.get(position).numOfTicket}"
        holder.source.text = "Source : ${historyData.get(position).sourceName}"
        holder.destination.text = "Destination : ${historyData.get(position).destination}"
        holder.travelDate.text = "Travel Date : ${historyData.get(position).dateOfBooking}"
    }
}