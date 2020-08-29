package com.amreen.hungrifoodie.Fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amreen.hungrifoodie.Adapter.OrderHistoryAdapter
import com.amreen.hungrifoodie.R
import com.amreen.hungrifoodie.model.OrderDetails
import com.amreen.hungrifoodie.util.ConnectionManager
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

/**
 * A simple [Fragment] subclass.
 */
class OrderhistoryFragment : Fragment() {
lateinit var recyclerhistory: RecyclerView
    lateinit var progresslayout:RelativeLayout
    lateinit var orderHistoryAdapter: OrderHistoryAdapter
    lateinit var ntgtoshowlayout:RelativeLayout
    lateinit var sharedPreferences: SharedPreferences
    var orderhistorylist = ArrayList<OrderDetails>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_orderhistory, container, false)
        recyclerhistory=view.findViewById(R.id.recyclerhistory)
        progresslayout=view.findViewById(R.id.progresslayout)
        ntgtoshowlayout=view.findViewById(R.id.ntgtoshowlayout)
        ntgtoshowlayout.visibility=View.GONE
        progresslayout.visibility=View.VISIBLE
        sharedPreferences = (activity as Context).getSharedPreferences("meals", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getString("user_id", null) as String
        val queue = Volley.newRequestQueue(activity as Context)
        val url="http://13.235.250.119/v2/orders/fetch_result/"+userId
        if (ConnectionManager().checkconnection(activity as Context)) {
            val jsonObjectRequest =
                object : JsonObjectRequest(Method.GET, url, null, Response.Listener {

                    val data = it.getJSONObject("data")
                    val success = data.getBoolean("success")
                    progresslayout.visibility = View.GONE
                    if (success) {
                        val resarray = data.getJSONArray("data")
                        if (resarray.length() == 0) {
                            ntgtoshowlayout.visibility = View.VISIBLE

                        } else {
                            for (i in 0 until resarray.length()) {
                                val orderobject = resarray.getJSONObject(i)
                                val fooditems = orderobject.getJSONArray("food_items")
                                val orderdetails = OrderDetails(
                                    orderobject.getString("restaurant_name"),
                                    orderobject.getString("order_placed_at"),
                                    fooditems
                                )
                                orderhistorylist.add(orderdetails)
                                if (orderhistorylist.isEmpty()) {
                                    ntgtoshowlayout.visibility = View.VISIBLE
                                } else {
                                    ntgtoshowlayout.visibility = View.GONE
                                }

                            }
                            if (activity != null) {
                                val layoutmanager = LinearLayoutManager(activity as Context)

                                orderHistoryAdapter=
                                   OrderHistoryAdapter(
                                        activity as Context,
                                        orderhistorylist
                                    )
                                recyclerhistory.adapter=orderHistoryAdapter
                                recyclerhistory.layoutManager = layoutmanager


                            }
                        }
                    }
                }, Response.ErrorListener {
                    Toast.makeText(activity as Context, "Volley error occured", Toast.LENGTH_SHORT)
                        .show()

                }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"
                        headers["token"] = "7e34038260fde7"
                        return headers
                    }
                }

            queue.add(jsonObjectRequest)
        }
        else{
            Toast.makeText(activity as Context,"No network",Toast.LENGTH_SHORT).show()
        }

        return view
    }

}
