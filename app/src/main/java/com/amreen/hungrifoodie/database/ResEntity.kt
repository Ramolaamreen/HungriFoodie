package com.amreen.hungrifoodie.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "restaurants")
data class ResEntity(
    @PrimaryKey val resid:String,
    @ColumnInfo(name = "res_name") val resname:String,
    @ColumnInfo(name = "res_rating") val resrating:String,
    @ColumnInfo(name="cost_one")   val costforone:String,
    @ColumnInfo(name="res_img") val resimg:String

)