package com.amreen.hungrifoodie.Activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.amreen.hungrifoodie.R


import com.amreen.hungrifoodie.database.OrderDatabase
import com.amreen.hungrifoodie.database.OrderEntity
import com.amreen.hungrifoodie.model.FoodItem
import com.amreen.hungrifoodie.util.ConnectionManager
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject

class PlaceOrder : AppCompatActivity() {
    lateinit var placeorder:Button
    lateinit var orderfrom: TextView
    lateinit var toolbar: Toolbar
    lateinit var recyclerplace:RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter: com.amreen.hungrifoodie.Adapter.PlaceOrderAdapter
    lateinit var sharedPreferences: SharedPreferences
    var sum=0
    val orderlist=ArrayList<FoodItem>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_order)
        orderfrom=findViewById(R.id.orderfrom)
        placeorder=findViewById(R.id.placeorder)
        toolbar=findViewById(R.id.toolbar)
        recyclerplace=findViewById(R.id.recyclerplace)
        sharedPreferences = getSharedPreferences("meals", Context.MODE_PRIVATE)
        layoutManager=LinearLayoutManager(this)

        setUpToolbar()
        val dbRestaurantList=
            RetriveOrders(
                applicationContext
            ).execute().get()
        recyclerAdapter= com.amreen.hungrifoodie.Adapter.PlaceOrderAdapter(
            this@PlaceOrder,
            dbRestaurantList
        )
        recyclerplace.adapter=recyclerAdapter
        recyclerplace.layoutManager=layoutManager
        val resname=intent.getStringExtra("resName")
        orderfrom.text="Order from: $resname"
        val gson = Gson()
        val orderlistt = gson.toJson(dbRestaurantList)
        val jsonArray = JSONArray(orderlistt)
        for (i in 0 until jsonArray.length()) {
            val data = jsonArray.getJSONObject(i)
            val cost = data.getString("cost_for_one")
            sum+= cost.toInt()
            val itemlist = FoodItem(
                data.getString("resid")
            )
            orderlist.add(itemlist)
        }
        val itemList = gson.toJson(orderlist)
        val itemArray = JSONArray(itemList)
        val t="Place order(Total: Rs.$sum)"
        placeorder.text=t
        placeorder.setOnClickListener() {
            val queue = Volley.newRequestQueue(this@PlaceOrder)
            val url = "http://13.235.250.119/v2/place_order/fetch_result/"
            val jsonParams = JSONObject()
            jsonParams.put("user_id", sharedPreferences.getString("user_id", ""))
            jsonParams.put("restaurant_id", intent.getStringExtra("res_id"))
            jsonParams.put("total_cost", sum.toString())
            jsonParams.put("food", itemArray)
            if (ConnectionManager().checkconnection(this@PlaceOrder)) {
                val jsonRequest =
                    object : JsonObjectRequest(Method.POST, url, jsonParams, Response.Listener {
                        val data = it.getJSONObject("data")
                        val success = data.getBoolean("success")
                        if (success) {
                            val clear = DeleteOrders(
                                applicationContext
                            ).execute().get()
                            Toast.makeText(this@PlaceOrder, "success", Toast.LENGTH_SHORT).show()
                            val intent= Intent(this@PlaceOrder, OverActivity::class.java)
                            startActivity(intent)

                        } else {
                            Toast.makeText(
                                this@PlaceOrder,
                                "Some error occured",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    }, Response.ErrorListener {
                        Toast.makeText(this@PlaceOrder, "Volley error", Toast.LENGTH_SHORT).show()
                    }) {
                        override fun getHeaders(): MutableMap<String, String> {
                            val headers = HashMap<String, String>()
                            headers["Content-type"] = "application/json"
                            headers["token"] = "7e34038260fde7"
                            return headers

                        }
                    }
                queue.add(jsonRequest)
            } else {
                Toast.makeText(this@PlaceOrder, "no network", Toast.LENGTH_LONG).show()
            }
        }


    }
    class RetriveOrders(val context:Context):AsyncTask<Void,Void,List<OrderEntity>>(){
        override fun doInBackground(vararg params: Void?): List<OrderEntity> {
            val db= Room.databaseBuilder(context,OrderDatabase::class.java,"orders-db").build()
            return db.orderDao().getAllOrders()
        }

    }
    fun setUpToolbar(){
       setSupportActionBar(toolbar)
        supportActionBar?.title="My Cart"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }
    class DeleteOrders(val context: Context):AsyncTask<Void,Void,Boolean>(){
        override fun doInBackground(vararg params: Void?): Boolean {
            val db = Room.databaseBuilder(context, OrderDatabase::class.java, "orders-db").build()
            db.orderDao().deleteOrders()
            db.close()
            return true
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onBackPressed()
        return super.onOptionsItemSelected(item)
    }
}
