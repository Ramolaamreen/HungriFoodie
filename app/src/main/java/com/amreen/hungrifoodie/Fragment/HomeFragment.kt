package com.amreen.hungrifoodie.Fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amreen.hungrifoodie.model.Res
import com.amreen.hungrifoodie.util.ConnectionManager
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import android.app.Activity
import android.app.AlertDialog
import android.provider.Settings
import android.content.Intent
import android.view.*
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.core.app.ActivityCompat
import com.amreen.hungrifoodie.Adapter.HomeRecyclerAdapter
import com.amreen.hungrifoodie.R
import org.json.JSONException
import java.util.Collections.sort
import kotlin.Comparator
import kotlin.collections.HashMap


class HomeFragment : Fragment() {
    lateinit var recyclerHome: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter: HomeRecyclerAdapter
    val resList = arrayListOf<Res>()
    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar
    var ratingComparator = Comparator<Res>{res1, res2 ->

        if (res1.resRating.compareTo(res2.resRating, true) == 0) {
            // sort according to name if rating is same
            res1.resName.compareTo(res2.resName, true)
        } else {
            res1.resRating.compareTo(res2.resRating, true)
        }
    }
    var costComparator = Comparator<Res>{res1, res2 ->

        if (res1.costforone.compareTo(res2.costforone, true) == 0) {
            // sort according to name if cost is same
            res1.resName.compareTo(res2.resName, true)
        } else {
            res1.costforone.compareTo(res2.costforone, true)
        }
    }
    var costComparatorl= Comparator<Res>{res1, res2 ->
        if(res1.costforone.compareTo(res2.costforone,true)==0){
            res1.resName.compareTo(res2.resName, true)
        }

        else{
            res2.costforone.compareTo(res1.costforone,true)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        setHasOptionsMenu(true)
        recyclerHome = view.findViewById(R.id.recyclerHome)
        layoutManager = LinearLayoutManager(activity)
        progressLayout = view.findViewById(R.id.progressLayout)
        progressBar = view.findViewById(R.id.progressBar)
        progressLayout.visibility = View.VISIBLE


        val queue = Volley.newRequestQueue(activity as Context)
        val url = "http://13.235.250.119/v2/restaurants/fetch_result/"
        if (ConnectionManager().checkconnection(activity as Context)) {
            val jsonObjectRequest =
                object : JsonObjectRequest(Method.GET, url, null, Response.Listener {
                    try{
                        progressLayout.visibility = View.GONE
                    val data1 = it.getJSONObject("data")
                    val success = data1.getBoolean("success")
                    if (success) {
                        val data = data1.getJSONArray("data")
                        for (i in 0 until data.length()) {
                            val resJsonObject = data.getJSONObject(i)
                            val resObject = Res(
                                resJsonObject.getString("id"),
                                resJsonObject.getString("name"),
                                resJsonObject.getString("rating"),
                                resJsonObject.getString("cost_for_one"),
                                resJsonObject.getString("image_url")
                            )
                            resList.add(resObject)
                            recyclerAdapter =
                                HomeRecyclerAdapter(
                                    activity as Context,
                                    resList
                                )
                            recyclerHome.adapter = recyclerAdapter
                            recyclerHome.layoutManager = layoutManager


                        }

                    } else {
                        Toast.makeText(
                            activity as Context,
                            "Some error occured",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                }
                    catch (e: JSONException){
                        Toast.makeText(activity as Context, "Some unexpected error occurred!", Toast.LENGTH_SHORT).show()

                    }

                }, Response.ErrorListener {
                    if(activity!=null) {
                        Toast.makeText(
                            activity as Context,
                            "Volley error occurred!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"
                        headers["token"] = "7e34038260fde7"
                        return headers
                    }


                }
            queue.add(jsonObjectRequest)

        } else {
            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection is not Found")
            dialog.setPositiveButton("Open Settings") { text, listener ->
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                activity?.finish()
            }
            dialog.setNegativeButton("Exit") {text, listener ->
                ActivityCompat.finishAffinity(activity as Activity)
            }
            dialog.create()
            dialog.show()
        }

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater?.inflate(R.menu.menu_dashboard,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item?.itemId
        if (id == R.id.action_sort) {
            sort(resList, ratingComparator)
            resList.reverse()
            item.isCheckable=true
            item.isChecked=true
        } else if (id == R.id.sortcostforone) {
            sort(resList, costComparator)
            resList.reverse()
            item.isCheckable=true
            item.isChecked=true
        }
        else if(id== R.id.sortcostforonel){
            sort(resList,costComparatorl)
            resList.reverse()
            item.isCheckable=true
            item.isChecked=true

        }

        recyclerAdapter.notifyDataSetChanged()
        return super.onOptionsItemSelected(item)
    }

}

