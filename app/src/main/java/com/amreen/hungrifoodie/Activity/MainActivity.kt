package com.amreen.hungrifoodie.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.content.Intent
import com.amreen.hungrifoodie.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val timeoutsplash:Long = 3000//1SEC
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title="HungriFoodie"
        Handler().postDelayed({
            val intent = Intent(this@MainActivity,LoginnActivity::class.java)
            startActivity(intent)
            finish()
        },timeoutsplash)

    }


}
