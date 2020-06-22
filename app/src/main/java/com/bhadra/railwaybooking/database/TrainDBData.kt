package com.bhadra.railwaybooking.database

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "TrainDBData")
class TrainDBData(
    @PrimaryKey @ColumnInfo (name ="number")val trainNum :String,
    @ColumnInfo(name ="source") val sourceName:String?,
    @ColumnInfo(name="destination") val destination:String?,
    @ColumnInfo(name="name") val trainName:String?,
     /* */@ColumnInfo (name="date")
    // @NonNull
     val dateOfBooking:String?,
    @ColumnInfo(name="ticketCount") val numOfTicket:String?
)