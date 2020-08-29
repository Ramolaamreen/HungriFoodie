package com.amreen.hungrifoodie.Activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.amreen.hungrifoodie.R
import com.amreen.hungrifoodie.Activity.HomeActivity
import com.amreen.hungrifoodie.Activity.ForgotPassword

import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.amreen.hungrifoodie.util.ConnectionManager
import org.json.JSONObject

class LoginnActivity : AppCompatActivity() {
    lateinit var etMobileNumber: EditText
    lateinit var etPassword: EditText
    lateinit var btnLogin: Button/*widen scope of variables*/
    lateinit var Forgotpassword: TextView
    lateinit var register: TextView
    lateinit var sharedPreferences: SharedPreferences
    override fun onBackPressed() {
        finishAffinity()
        super.onBackPressed()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loginn)
        title = "Log In"
        sharedPreferences = getSharedPreferences("meals", Context.MODE_PRIVATE)
        login()
        etMobileNumber = findViewById(R.id.etMobileNumber)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        Forgotpassword = findViewById(R.id.Forgotpassword)
        register = findViewById(R.id.register)
        btnLogin.setOnClickListener {
            val mobileNum = etMobileNumber.text.toString()

            var passNum = etPassword.text.toString()
            val queue = Volley.newRequestQueue(this@LoginnActivity)
            val url = "http://13.235.250.119/v2/login/fetch_result/"
            val jsonParams = JSONObject()
            jsonParams.put("mobile_number", etMobileNumber.text.toString())
            jsonParams.put("password", etPassword.text.toString())
            val k = valid(mobileNum, passNum)
            if (k) {
                if (ConnectionManager().checkconnection(this@LoginnActivity)) {
                    val jsonRequest =
                        object : JsonObjectRequest(Method.POST, url, jsonParams, Response.Listener {
                            val data = it.getJSONObject("data")
                            val success = data.getBoolean("success")
                            if (success) {
                                Toast.makeText(this@LoginnActivity, "success", Toast.LENGTH_SHORT)
                                    .show()
                                val infojsonObject = data.getJSONObject("data")
                                val name = infojsonObject.getString("name")
                                val email = infojsonObject.getString("email")
                                val mobile = infojsonObject.getString("mobile_number")
                                val address = infojsonObject.getString("address")
                                val userid = infojsonObject.getString("user_id")
                                sharedPreferences.edit().putString("user_id", userid).apply()
                                sharedPreferences.edit().putBoolean("loggedIn", true).apply()
                                sharedPreferences.edit().putString("name", name).apply()
                                sharedPreferences.edit().putString("mobile", mobile).apply()
                                sharedPreferences.edit().putString("email", email).apply()
                                sharedPreferences.edit().putString("place", address).apply()
                                login()

                            } else {
                                Toast.makeText(
                                    this@LoginnActivity,
                                    data.getString("errorMessage"),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        }, Response.ErrorListener {
                            //here we handle error
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
                    Toast.makeText(this@LoginnActivity, "no internet", Toast.LENGTH_LONG).show()
                }
            }

            }
        }

        fun login() {
            val isLogin = sharedPreferences.getBoolean("loggedIn", false)
            if (isLogin) {
                val intent = Intent(this@LoginnActivity, HomeActivity::class.java)
                startActivity(intent)
            }
        }

        fun valid(mobileNum: String, passNum: String): Boolean {
            val o = ""
            var p = true
            if (mobileNum == o || passNum == o) {
                p = false
                Toast.makeText(
                    this@LoginnActivity,
                    "Password or mobile fields should not be empty",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (mobileNum.length < 10) {
                Toast.makeText(
                    this@LoginnActivity,
                    "Mobile number should be of 10 digits",
                    Toast.LENGTH_SHORT
                ).show()
                p = false
            }
            return p
        }

        fun forgotPassword(view: View) {
            val intent = Intent(this@LoginnActivity, ForgotPassword::class.java)
            startActivity(intent)
        }

        fun register(view: View) {
            val intent = Intent(this@LoginnActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
    }






