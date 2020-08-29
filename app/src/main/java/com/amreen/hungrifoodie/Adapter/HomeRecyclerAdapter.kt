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
import com.amreen.hungrifoodie.model.Res
import java.util.ArrayList
import com.amreen.hungrifoodie.database.ResEntity
import com.amreen.hungrifoodie.database.RestaurantDatabase
import com.squareup.picasso.Picasso

class HomeRecyclerAdapter(
    val context:Context,
    val itemList: ArrayList<Res>
) :RecyclerView.Adapter<HomeRecyclerAdapter.HomeViewHolder>(){
    class HomeViewHolder(view: View):RecyclerView.ViewHolder(view){
        val txtResName: TextView = view.findViewById(R.id.txtResName)
        val emptyheart:ImageButton=view.findViewById(R.id.emptyheart)
        val txtPerPersonPrice: TextView = view.findViewById(R.id.txtPerPersonPrice)
        val txtResRating: TextView = view.findViewById(R.id.txtResRating)
        val imgResImage: ImageView = view.findViewById(R.id.imgResImage)
        val llContent: LinearLayout = view.findViewById(R.id.llContent)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.recycler_dashboard_single_row,parent,false)
        return HomeViewHolder(
            view
        )
    }

    override fun getItemCount(): Int {
       return itemList.size
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val rest = itemList[position]
        holder.txtResName.text = rest.resName
        holder.txtPerPersonPrice.text = "₹" + rest.costforone + "/person"
        holder.txtResRating.text = rest.resRating
        Picasso.get().load(rest.resImage).error(R.drawable.defrestaurants).into(holder.imgResImage)
        holder.llContent.setOnClickListener {
            val intent = Intent(context, DescriptionActivity::class.java)//DescriptionActivity ki intent
            intent.putExtra("res_id", rest.resid)
            intent.putExtra("resname", rest.resName)

            context.startActivity(intent)

        }
        val resEntity =
            ResEntity(rest.resid, rest.resName, rest.resRating, rest.costforone, rest.resImage)
        val checkFav = DBAsyncTask(
            context,
            resEntity,
            1
        ).execute()
        val isFav = checkFav.get()
        if (isFav) {
            holder.emptyheart.setImageResource(R.drawable.ic_colorfullheart)
        } else {
            holder.emptyheart.setImageResource(R.drawable.ic_coloremptyheart)

        }



        holder.emptyheart.setOnClickListener {
            holder.emptyheart.setImageResource(R.drawable.ic_coloremptyheart)
            if (!DBAsyncTask(
                    context,
                    resEntity,
                    1
                ).execute().get()) {
                val async = DBAsyncTask(
                    context,
                    resEntity,
                    2
                ).execute()
                val result = async.get()
                if (result) {
                    Toast.makeText(context, "Added to favourites", Toast.LENGTH_SHORT).show()
                    holder.emptyheart.setImageResource(R.drawable.ic_colorfullheart)

                } else {
                    Toast.makeText(context, "Error occured", Toast.LENGTH_SHORT).show()

                }

            } else {
                val async = DBAsyncTask(
                    context,
                    resEntity,
                    3
                ).execute()
                val result = async.get()
                if (result) {
                    Toast.makeText(context, "Removed from favourites", Toast.LENGTH_SHORT).show()
                    holder.emptyheart.setImageResource(R.drawable.ic_coloremptyheart)

                } else {
                    Toast.makeText(context, "Error occured", Toast.LENGTH_SHORT).show()


                }

            }

        }
    }
        class DBAsyncTask(val context: Context,val resEntity: ResEntity,val mode:Int):
            AsyncTask<Void, Void, Boolean>(){
            val db= Room.databaseBuilder(context,RestaurantDatabase::class.java,"Res-db").build()
            override fun doInBackground(vararg params: Void?): Boolean {
                when(mode){
                    1->{
                        val resta:ResEntity?=db.resDao().getResById(resEntity.resid.toString())
                            db.close()
                            return resta!=null
                    }
                    2->{
                        db.resDao().insertRes(resEntity)
                        db.close()
                        return true

                    }
                    3->{
                        db.resDao().deleteRes(resEntity)
                        db.close()
                        return true

                    }
                }
                return false

            }

        }
    }
