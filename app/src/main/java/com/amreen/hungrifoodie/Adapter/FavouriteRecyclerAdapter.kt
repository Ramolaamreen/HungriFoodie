package com.amreen.hungrifoodie.Adapter

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.amreen.hungrifoodie.Activity.DescriptionActivity
import com.amreen.hungrifoodie.R

import com.amreen.hungrifoodie.database.ResEntity
import com.amreen.hungrifoodie.database.RestaurantDatabase
import com.squareup.picasso.Picasso

class FavouriteRecyclerAdapter(val context: Context, val resList: List<ResEntity>):RecyclerView.Adapter<FavouriteRecyclerAdapter.FavouriteViewHolder>() {
    class FavouriteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtFavResTitle: TextView = view.findViewById(R.id.txtFavResTitle)
        val txtFavPerPrice: TextView = view.findViewById(R.id.txtFavPerPrice)
        val txtFavResRating: TextView = view.findViewById(R.id.txtFavResRating)
        val imgFavResImage: ImageView = view.findViewById(R.id.imgFavResImage)
        val llContent: LinearLayout = view.findViewById(R.id.llFavContent)
        val heart: ImageButton = view.findViewById(R.id.fullheart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_favourite_single_row, parent, false)

        return FavouriteViewHolder(
            view
        )
    }

    override fun getItemCount(): Int {
        return resList.size
    }

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        val rest = resList[position]
        holder.txtFavResTitle.text = rest.resname
        holder.txtFavResRating.text = rest.resrating
        holder.txtFavPerPrice.text = "₹" + rest.costforone + "/person"
        Picasso.get().load(rest.resimg).error(R.drawable.defrestaurants).into(holder.imgFavResImage)
        holder.llContent.setOnClickListener {
            val intent =
                Intent(context, DescriptionActivity::class.java)//DescriptionActivity ki intent
            intent.putExtra("res_id", rest.resid)
            intent.putExtra("resname", rest.resname)

            context.startActivity(intent)

        }
        val resEntity =
            ResEntity(rest.resid, rest.resname, rest.resrating, rest.costforone, rest.resimg)
        val checkFav = HomeRecyclerAdapter.DBAsyncTask(
            context,
            resEntity,
            1
        ).execute()
        val isFav = checkFav.get()
        if (isFav) {
            holder.heart.setImageResource(R.drawable.ic_colorfullheart)
        } else {
            holder.heart.setImageResource(R.drawable.ic_coloremptyheart)

        }
        holder.heart.setOnClickListener() {
            holder.heart.setImageResource(R.drawable.ic_coloremptyheart)
            if (!HomeRecyclerAdapter.DBAsyncTask(
                    context,
                    resEntity,
                    1
                ).execute().get()) {
                val async = HomeRecyclerAdapter.DBAsyncTask(
                    context,
                    resEntity,
                    2
                ).execute()
                val result = async.get()
                if (result) {
                    Toast.makeText(context, "Added to favourites", Toast.LENGTH_SHORT).show()
                    holder.heart.setImageResource(R.drawable.ic_colorfullheart)

                } else {
                    Toast.makeText(context, "Error occured", Toast.LENGTH_SHORT).show()

                }

            } else {
                val async = HomeRecyclerAdapter.DBAsyncTask(
                    context,
                    resEntity,
                    3
                ).execute()
                val result = async.get()
                if (result) {
                    Toast.makeText(context, "Removed from favourites", Toast.LENGTH_SHORT).show()
                    holder.heart.setImageResource(R.drawable.ic_coloremptyheart)

                } else {
                    Toast.makeText(context, "Error occured", Toast.LENGTH_SHORT).show()


                }

            }

        }

    }


    class DBAsyncTask(val context: Context, val resEntity: ResEntity, val mode: Int) :
        AsyncTask<Void, Void, Boolean>() {
        val db = Room.databaseBuilder(context, RestaurantDatabase::class.java, "Res-db").build()
        override fun doInBackground(vararg params: Void?): Boolean {
            when (mode) {
                1 -> {
                    val resta: ResEntity? = db.resDao().getResById(resEntity.resid.toString())
                    db.close()
                    return resta != null
                }
                2 -> {
                    db.resDao().insertRes(resEntity)
                    db.close()
                    return true

                }
                3 -> {
                    db.resDao().deleteRes(resEntity)
                    db.close()
                    return true

                }
            }
            return false

        }

    }
}




