package com.amreen.hungrifoodie.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.amreen.hungrifoodie.R
import com.amreen.hungrifoodie.database.OrderEntity

class PlaceOrderAdapter(val context: Context, val orderList: List<OrderEntity>):
    RecyclerView.Adapter<PlaceOrderAdapter.PlaceOrderViewHolder>()  {
    class PlaceOrderViewHolder(view: View): RecyclerView.ViewHolder(view){
         val itemname:TextView=view.findViewById(R.id.item)
        val itemcost:TextView=view.findViewById(R.id.cost)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceOrderViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.recycler_placeorder_single_row,parent,false)
        return PlaceOrderViewHolder(
            view
        )

    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    override fun onBindViewHolder(holder: PlaceOrderViewHolder, position: Int) {
        var hotel=orderList[position]
        holder.itemname.text=hotel.name
        holder.itemcost.text="Rs."+hotel.cost_for_one

    }


}