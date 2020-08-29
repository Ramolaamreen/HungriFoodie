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

class ForgotPassword : AppCompatActivity() {
    lateinit var etMobileNumberforgot: EditText
    lateinit var etMailforgot: EditText
    lateinit var btnNext: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        title="Forgot password"
        etMobileNumberforgot = findViewById(R.id.etMobileNumberforgot)
        etMailforgot = findViewById(R.id.etMailforgot)
        btnNext = findViewById(R.id.btnNext)
        btnNext.setOnClickListener() {
            val mobileforgot = etMobileNumberforgot.text.toString()
            val mailforgot = etMailforgot.text.toString()
            val queue = Volley.newRequestQueue(this@ForgotPassword)
            val url = "http://13.235.250.119/v2/forgot_password/fetch_result "
            val jsonParams = JSONObject()
            jsonParams.put("mobile_number", etMobileNumberforgot.text.toString())
            jsonParams.put("email", etMailforgot.text.toString())
            if (ConnectionManager().checkconnection(this@ForgotPassword)) {
                val jsonRequest =
                    object : JsonObjectRequest(Method.POST, url, jsonParams, Response.Listener {
                        val data = it.getJSONObject("data")
                        val success = data.getBoolean("success")
                        if (success) {
                            Toast.makeText(
                                this@ForgotPassword,
                                "OTP has been sent to registered mail",
                                Toast.LENGTH_LONG
                            )
                                .show()
                            val intent=Intent(this@ForgotPassword,
                                ResetPassword::class.java)
                            intent.putExtra("mobile",mobileforgot)
                            startActivity(intent)

                        }
                        else{
                            Toast.makeText(this@ForgotPassword,data.getString("errorMessage"),Toast.LENGTH_LONG).show()
                        }

                    }, Response.ErrorListener {
                        Toast.makeText(
                            this@ForgotPassword,
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
                Toast.makeText(this@ForgotPassword, "no network", Toast.LENGTH_LONG).show()
            }

        }
    }

}