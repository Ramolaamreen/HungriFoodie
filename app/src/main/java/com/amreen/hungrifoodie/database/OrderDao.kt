package com.amreen.hungrifoodie.database
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
@Dao
interface OrderDao {
    @Insert
    fun insertOrder(orderEntity: OrderEntity)
    @Delete
    fun deleteOrder(orderEntity: OrderEntity)
    @Query("DELETE FROM orders")
    fun deleteOrders()
    @Query("SELECT * FROM orders")
    fun getAllOrders():List<OrderEntity>

}