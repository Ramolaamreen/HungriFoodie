package com.amreen.hungrifoodie.database
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "orders")
class OrderEntity (
    @PrimaryKey val resid:String,
    @ColumnInfo(name = "name") val name:String,
    @ColumnInfo(name = "cost_for_one") val cost_for_one:String
)