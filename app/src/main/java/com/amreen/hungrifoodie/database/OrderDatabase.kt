package com.amreen.hungrifoodie.database

import androidx.room.Database
import androidx.room.RoomDatabase
@Database(entities = [OrderEntity::class],version=1)//class orderentity
abstract class OrderDatabase:RoomDatabase() {
    abstract fun orderDao():OrderDao


}