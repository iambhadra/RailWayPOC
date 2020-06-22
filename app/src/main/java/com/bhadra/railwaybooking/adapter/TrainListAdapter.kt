package com.bhadra.railwaybooking.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bhadra.railwaybooking.R
import com.bhadra.railwaybooking.utils.TrainData

class TrainListAdapter(val trainClickListner:  TrainClickListener ) : RecyclerView.Adapter<TrainListAdapter.TrainViewHolder>() {
    var TrainData = arrayListOf<TrainData>(
        TrainData("Rajdhani Exp",123467),
        TrainData("Sapthagiri Exp",428108),
        TrainData("Tirumal Super Fast",234871),
        TrainData("Circar Exp",174881),
        TrainData("Grant Trunk",879389),
        TrainData("Howrah Mail",982716),
        TrainData("Bhuvaneswar Exp",876231),
        TrainData("EastCoast",987612)
    )

    class TrainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val Name :TextView = itemView.findViewById(R.id.tv_train_name)
            val Number :TextView =itemView.findViewById(R.id.tv_train_number)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.train_item, parent, false)
        return TrainViewHolder(view)
    }

    override fun getItemCount(): Int {
        return TrainData.size
    }

    override fun onBindViewHolder(holder: TrainViewHolder, position: Int) {
        holder.Name.text = TrainData.get(position).trainName
        holder.Number.text = TrainData.get(position).trainNum.toString()
        holder.Number.setOnClickListener {
            trainClickListner.onTrainClicked(holder.Number.text.toString(),holder.Name.text.toString())
        }
    }

    interface TrainClickListener{
        public fun onTrainClicked(trainNumber: String,trainName:String)
    }
}