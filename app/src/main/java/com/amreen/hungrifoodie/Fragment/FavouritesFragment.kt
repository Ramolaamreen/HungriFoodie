package com.amreen.hungrifoodie.Fragment

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.amreen.hungrifoodie.Adapter.FavouriteRecyclerAdapter
import com.amreen.hungrifoodie.R

import com.amreen.hungrifoodie.database.ResEntity
import com.amreen.hungrifoodie.database.RestaurantDatabase

/**
 * A simple [Fragment] subclass.
 */
class FavouritesFragment : Fragment() {
    lateinit var recyclerFavourite: RecyclerView
    //lateinit var progressLayout: RelativeLayout
    //lateinit var progressBar: ProgressBar
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter: FavouriteRecyclerAdapter
    lateinit var ntgtoshowlayout:RelativeLayout
     var dbRestaurantList= listOf<ResEntity>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_favourites, container, false)
        recyclerFavourite = view.findViewById(R.id.recyclerfavourites)

        ntgtoshowlayout=view.findViewById(R.id.ntgtoshowlayout)
        ntgtoshowlayout.visibility=View.GONE
        layoutManager = GridLayoutManager(activity as Context, 2)
        dbRestaurantList=
            RetrieveFavourites(
                activity as Context
            ).execute().get()
        if(dbRestaurantList.isEmpty()){
            ntgtoshowlayout.visibility=View.VISIBLE

        }
        else{
            if(activity!=null){

                recyclerAdapter=
                    FavouriteRecyclerAdapter(
                        activity as Context,
                        dbRestaurantList
                    )
                recyclerFavourite.adapter=recyclerAdapter
                recyclerFavourite.layoutManager=layoutManager
            }

        }
        return view
    }

    class RetrieveFavourites(val context: Context) : AsyncTask<Void, Void, List<ResEntity>>() {

        override fun doInBackground(vararg p0: Void?): List<ResEntity> {
            val db =
                Room.databaseBuilder(context, RestaurantDatabase::class.java, "Res-db").build()

            return db.resDao().getAllRes()
        }

    }
}
