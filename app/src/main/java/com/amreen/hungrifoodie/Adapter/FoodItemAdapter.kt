package com.amreen.hungrifoodie.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.amreen.hungrifoodie.R
import com.amreen.hungrifoodie.model.NameCost

class FoodItemAdapter(val context: Context,val foodItemList:ArrayList<NameCost>):RecyclerView.Adapter<FoodItemAdapter.FoodItemViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FoodItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_itemname_single_row, parent, false)
        return FoodItemViewHolder(
            view
        )
    }

    override fun getItemCount(): Int {
     return foodItemList.size
    }

    override fun onBindViewHolder(holder: FoodItemViewHolder, position: Int) {
        var ob=foodItemList[position]
        holder.foodname.text=ob.name
        holder.foodcost.text="Rs."+ob.cost

    }
    class FoodItemViewHolder(view: View):RecyclerView.ViewHolder(view){
        val foodname: TextView =view.findViewById(R.id.foodname)
        val foodcost: TextView =view.findViewById(R.id.foodcost)

    }

}