package com.amreen.hungrifoodie.Activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room //import androidx.room.Room
import com.amreen.hungrifoodie.R
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.amreen.hungrifoodie.database.OrderDatabase
import com.amreen.hungrifoodie.database.OrderEntity
//import com.internshala.bookhub.database.BookDatabase
//import com.internshala.bookhub.database.BookEntity
import com.amreen.hungrifoodie.model.RestaurantItems
import com.amreen.hungrifoodie.util.ConnectionManager

class DescriptionActivity : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var progressLayout: RelativeLayout
    lateinit var proceedToCart: Button
    lateinit var progressBar: ProgressBar
    lateinit var toolbar: Toolbar
    lateinit var restaurantAdapter: com.amreen.hungrifoodie.Adapter.RestaurantAdapter
    var orderList = arrayListOf<RestaurantItems>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)
        val resId = intent.getStringExtra("res_id")
        val title = intent.getStringExtra("resname")
        toolbar=findViewById(R.id.toolbardesc)
        progressBar=findViewById(R.id.progressBar)
        recyclerView = findViewById(R.id.recyclerdesc)
        layoutManager = LinearLayoutManager(this@DescriptionActivity)
        proceedToCart = findViewById(R.id.bt_proceedCart)
        progressLayout = findViewById(R.id.progressLayout)
        proceedToCart.visibility = View.GONE
        progressLayout.visibility = View.VISIBLE
        progressBar.visibility=View.VISIBLE
        setUpToolbar()
        val queue = Volley.newRequestQueue(this@DescriptionActivity)
        val url = "http://13.235.250.119/v2/restaurants/fetch_result/" + resId
        //api for res list
        if (ConnectionManager().checkconnection(this@DescriptionActivity)) {
            var itemList = arrayListOf<RestaurantItems>(RestaurantItems("1", "chicken", "150"))
            val jsonRequest = object : JsonObjectRequest(Method.GET, url, null, Response.Listener {
                val data = it.getJSONObject("data")
                val success = data.getBoolean("success")
                if (success) {

                    progressLayout.visibility = View.GONE
                    val itemData = data.getJSONArray("data")
                    for (i in 0 until itemData.length()) {
                        val infoJsonObject = itemData.getJSONObject(i)
                        val resItem = RestaurantItems(
                            infoJsonObject.getString("id"),
                            infoJsonObject.getString("name"),
                            infoJsonObject.getString("cost_for_one")
                        )
                        itemList.add(resItem)
                    }
                    itemList.removeAt(0)
                    val restaurantAdapter =
                        com.amreen.hungrifoodie.Adapter.RestaurantAdapter(
                            this@DescriptionActivity,
                            itemList,
                            object :
                                com.amreen.hungrifoodie.Adapter.RestaurantAdapter.OnItemClickListener {
                                override fun onAddItemClick(restaurantItems: RestaurantItems) {
                                    orderList.add(restaurantItems)
                                    val orderItems = OrderEntity(
                                        restaurantItems.id,
                                        restaurantItems.name,
                                        restaurantItems.cost_for_one
                                    )
                                    val async =
                                        ItemsOfCart(
                                            applicationContext,
                                            orderItems,
                                            1
                                        ).execute().get()
                                    if (orderList.size > 0) {
                                        proceedToCart.visibility = View.VISIBLE
                                    }
                                }

                                override fun onRemoveItemClick(restaurantItems: RestaurantItems) {
                                    orderList.remove(restaurantItems)
                                    val orderItems = OrderEntity(
                                        restaurantItems.id,
                                        restaurantItems.name,
                                        restaurantItems.cost_for_one
                                    )
                                    val async =
                                        ItemsOfCart(
                                            applicationContext,
                                            orderItems,
                                            2
                                        ).execute().get()
                                    if (orderList.isEmpty()) {
                                        proceedToCart.visibility = View.GONE
                                    }

                                }
                            })
                    recyclerView.adapter = restaurantAdapter
                    recyclerView.layoutManager = layoutManager
                } else {
                    Toast.makeText(
                        this@DescriptionActivity,
                        "Some error occured",
                        Toast.LENGTH_SHORT
                    ).show()

                }

            }, Response.ErrorListener {
                Toast.makeText(this@DescriptionActivity, "Volley error", Toast.LENGTH_SHORT).show()
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
            Toast.makeText(this@DescriptionActivity, "No internet", Toast.LENGTH_LONG).show()
        }
        proceedToCart.setOnClickListener {
            val intent= Intent(this@DescriptionActivity,
                PlaceOrder::class.java)
            intent.putExtra("res_id",resId)
            intent.putExtra("resName",title)
            startActivity(intent)

        }

    }
    fun setUpToolbar(){
        setSupportActionBar(toolbar)
        supportActionBar?.title=intent.getStringExtra("resname")
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onBackPressed()
        return super.onOptionsItemSelected(item)
    }
    override fun onBackPressed() {
        if(orderList.size>0) {
            val dialog = AlertDialog.Builder(this@DescriptionActivity).setCancelable(false)
            dialog.setTitle("Confirmation")
            dialog.setMessage("Items will be reset.Do you want to proceed")
            dialog.setPositiveButton("YES") { text, listener ->
                val async =
                    ItemsOfCart(
                        applicationContext,
                        OrderEntity("", "", ""),
                        3
                    ).execute().get()
                super.onBackPressed()

            }
            dialog.setNegativeButton("No") { text, listener ->
                //No action
            }
            dialog.create()
            dialog.show()
        }
        else{
            super.onBackPressed()
        }
    }
    class ItemsOfCart(context: Context,val orderItems:OrderEntity,val mode:Int):
        AsyncTask<Void,Void,Boolean>() {
        val db = Room.databaseBuilder(context, OrderDatabase::class.java, "orders-db").build()
        override fun doInBackground(vararg params: Void?): Boolean {
            when (mode) {
                1 -> {
                    db.orderDao().insertOrder(orderItems)
                    db.close()
                    return true
                }
                2 -> {
                    db.orderDao().deleteOrder(orderItems)
                    db.close()
                    return true
                }
                3 -> {
                    db.orderDao().deleteOrders()
                    db.close()
                    return true
                }
            }
            return false

        }
    }

}





