package com.amreen.hungrifoodie.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.amreen.hungrifoodie.R
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.amreen.hungrifoodie.util.ConnectionManager
import org.json.JSONObject

class ResetPassword : AppCompatActivity() {
    lateinit var otp: EditText
    lateinit var newpassword: EditText
    lateinit var confirmpassword: EditText
    lateinit var btnsubmit: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)
        val ss: String? = intent.getStringExtra("mobile")
        title = "Reset password"
        otp = findViewById(R.id.otp)
        newpassword = findViewById(R.id.newpassword)
        confirmpassword = findViewById(R.id.confirmpassword)
        btnsubmit = findViewById(R.id.btnsubmit)
        btnsubmit.setOnClickListener() {
            val otpsent = otp.text.toString()
            val passwordreset = newpassword.text.toString()
            val confirmreset = confirmpassword.text.toString()
            val queue = Volley.newRequestQueue(this@ResetPassword)
            val url = "http://13.235.250.119/v2/reset_password/fetch_result "
            val jsonParams = JSONObject()
            jsonParams.put("mobile_number", ss.toString())
            jsonParams.put("password", newpassword.text.toString())
            jsonParams.put("otp", otp.text.toString())
            val q = check(otpsent, passwordreset, confirmreset)
            if (q) {
                if (ConnectionManager().checkconnection(this@ResetPassword)) {
                    val jsonRequest =
                        object : JsonObjectRequest(Method.POST, url, jsonParams, Response.Listener {
                            val data = it.getJSONObject("data")
                            val success = data.getBoolean("success")
                            if (success) {
                                Toast.makeText(
                                    this@ResetPassword,
                                    "Password has been successfully changed",
                                    Toast.LENGTH_LONG
                                )
                                    .show()
                                val intent = Intent(this@ResetPassword, LoginnActivity::class.java)
                                startActivity(intent)

                            } else {
                                Toast.makeText(
                                    this@ResetPassword,
                                    data.getString("errorMessage"),
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }, Response.ErrorListener {
                            Toast.makeText(
                                this@ResetPassword,
                                "some error occured.Please try again",
                                Toast.LENGTH_SHORT
                            ).show()
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
                    Toast.makeText(this@ResetPassword, "no network", Toast.LENGTH_LONG).show()

                }
            }
        }
    }

    fun check(
        otpsent: String,
        passwordreset: String,
        confirmreset: String
    ): Boolean {
        //(otpsent,passwordreset,confirmreset){
        var validate=true
     if(otpsent.length<4){
         if(passwordreset!=confirmreset){
             Toast.makeText(this@ResetPassword,"Passwords don't match",Toast.LENGTH_SHORT).show()
             Toast.makeText(this@ResetPassword,"invalid OTP",Toast.LENGTH_SHORT).show()
             validate=false
         }
     }
        else if(otpsent.length<4){
            if(passwordreset==confirmreset){
                Toast.makeText(this@ResetPassword,"invalid OTP",Toast.LENGTH_SHORT).show()
                validate=false
            }
        }
        else if(otpsent.length==4){
            if(passwordreset!=confirmreset){
                Toast.makeText(this@ResetPassword,"Passwords don't match",Toast.LENGTH_SHORT).show()
                validate=false
            }
        }
        else if(otpsent.length==4){
         if(passwordreset==confirmreset){
             validate=true

         }
     }


        return validate
    }
}




