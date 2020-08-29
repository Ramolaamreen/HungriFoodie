package com.amreen.hungrifoodie.model

import org.json.JSONArray

data class OrderDetails(
    val restaurant_name: String,
    val order_placed_at: String,
    val fooditems: JSONArray
)