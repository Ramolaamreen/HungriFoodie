package com.amreen.hungrifoodie.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.amreen.hungrifoodie.Activity.HomeActivity
import com.amreen.hungrifoodie.R

class OverActivity : AppCompatActivity() {
    lateinit var buttonok:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_over)
        buttonok=findViewById(R.id.buttonok)
        buttonok.setOnClickListener(){
            val intent= Intent(this@OverActivity, HomeActivity::class.java)
            startActivity(intent)

        }

    }
    override fun onBackPressed() {

    }
}
