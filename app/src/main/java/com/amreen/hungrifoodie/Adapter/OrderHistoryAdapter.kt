package com.amreen.hungrifoodie.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amreen.hungrifoodie.R
import com.amreen.hungrifoodie.model.NameCost
import com.amreen.hungrifoodie.model.OrderDetails
import kotlin.collections.ArrayList

class OrderHistoryAdapter (val context: Context,
 val orderhistorylist: ArrayList<OrderDetails>
) :
RecyclerView.Adapter<OrderHistoryAdapter.OrderHistoryViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OrderHistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_orderhistory_single_row, parent, false)
        return OrderHistoryViewHolder(
            view
        )
    }

    override fun getItemCount(): Int {
      return orderhistorylist.size
    }

    override fun onBindViewHolder(
        holder: OrderHistoryViewHolder,
        position: Int
    ) {
        val orderhistoryobject=orderhistorylist[position]
        holder.txtresname.text=orderhistoryobject.restaurant_name
        holder.txtdate.text=orderhistoryobject.order_placed_at.substring(0,8)

        setRecyclerView(holder.recyclerView,orderhistoryobject)
    }
    class OrderHistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val txtresname: TextView =view.findViewById(R.id.restaurantname)
        val txtdate:TextView=view.findViewById(R.id.date)
        val recyclerView:RecyclerView=view.findViewById(R.id.recyclerorder)

    }

    fun setRecyclerView(recyclerView: RecyclerView,orderhistoryobject:OrderDetails){
        val foodItemList= arrayListOf<NameCost>()
        for(i in 0 until orderhistoryobject.fooditems.length()){
            val infoJson=orderhistoryobject.fooditems.getJSONObject(i)
            val foodItems=NameCost(

                infoJson.getString("name"),
                infoJson.getString("cost")
            )
            foodItemList.add(foodItems)
        }
        val layoutManager= LinearLayoutManager(context)
        val foodItemAdapter=
           FoodItemAdapter(context, foodItemList)
        recyclerView.adapter=foodItemAdapter
        recyclerView.layoutManager=layoutManager

    }
    }

