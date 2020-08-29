package com.amreen.hungrifoodie.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.amreen.hungrifoodie.R
import com.amreen.hungrifoodie.model.RestaurantItems

class RestaurantAdapter(val context: Context, val itemList:ArrayList<RestaurantItems>,val listener: OnItemClickListener):
    RecyclerView.Adapter<RestaurantAdapter.RestaurantItemViewHolder>() {
    class RestaurantItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemNo: TextView = view.findViewById(R.id.menu_number)
        val itemName: TextView = view.findViewById(R.id.menu_item_name)
        val itemCost: TextView = view.findViewById(R.id.menu_item_cost)
        val addButton: Button =view.findViewById(R.id.bt_add)
        val removeButton:Button=view.findViewById(R.id.bt_remove)

    }

    interface OnItemClickListener{
        fun onAddItemClick(restaurantItems: RestaurantItems)
        fun onRemoveItemClick(restaurantItems: RestaurantItems)
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RestaurantItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_menu_single_row, parent, false)
        return RestaurantItemViewHolder(
            view
        )
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(
        holder: RestaurantItemViewHolder,
        position: Int
    ) {
        var restaurant = itemList[position]
        holder.itemNo.text = (position+1).toString()
        holder.itemName.text = restaurant.name
        holder.itemCost.text = "₹." + restaurant.cost_for_one + "/person"
        holder.addButton.setOnClickListener {
            holder.addButton.visibility=View.GONE
            holder.removeButton.visibility=View.VISIBLE
            listener.onAddItemClick(restaurant)
        }
        holder.removeButton.setOnClickListener {
            holder.removeButton.visibility = View.GONE
            holder.addButton.visibility = View.VISIBLE
            listener.onRemoveItemClick(restaurant)
        }
    }
}
        //holder.removeButton.setOnClickListener {
          //  holder.removeButton.visibility = View.GONE
            //holder.addButton.visibility = View.VISIBLE
         //istener.onRemoveItemClick(restaurant)
        //}

