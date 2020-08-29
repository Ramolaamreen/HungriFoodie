package com.amreen.hungrifoodie.Activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
import com.google.gson.JsonArray
import kotlinx.android.synthetic.main.recycler_orderhistory_single_row.view.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.regex.Matcher
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {
    lateinit var Name: EditText
    lateinit var EmailAddress: EditText
    lateinit var mno: EditText
    lateinit var address: EditText
    lateinit var registerPassword: EditText
    lateinit var registerconfirmpassword: EditText
    lateinit var registerYourself: Button
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        title = "Register Yourself"
        sharedPreferences = getSharedPreferences("meals", Context.MODE_PRIVATE)
        Name = findViewById(R.id.Name)
        EmailAddress = findViewById(R.id.EmailAddress)
        mno = findViewById(R.id.mno)
        address = findViewById(R.id.address)
        registerPassword = findViewById(R.id.registerPassword)
        registerconfirmpassword = findViewById(R.id.registerconfirmpassword)
        registerYourself = findViewById(R.id.registerYourself)
        registerYourself.setOnClickListener() {
            val pname = Name.text.toString()
            val pmno = mno.text.toString()
            val initpassword = registerPassword.text.toString()//initial
            val ppassword = registerconfirmpassword.text.toString()//confirmm
            val paddress = address.text.toString()
            val pmail = EmailAddress.text.toString()
            val queue = Volley.newRequestQueue(this@RegisterActivity)
            val url = " http://13.235.250.119/v2/register/fetch_result "
            val jsonParams = JSONObject()
            jsonParams.put("name", Name.text.toString())
            jsonParams.put("mobile_number", mno.text.toString())
            jsonParams.put("password", registerPassword.text.toString())
            jsonParams.put("address", address.text.toString())
            jsonParams.put("email", EmailAddress.text.toString())
            val p = check(pname, pmail, pmno, paddress, initpassword, ppassword)
            if (p) {
                if (ConnectionManager().checkconnection(this@RegisterActivity)) {
                    val jsonRequest =
                        object : JsonObjectRequest(Method.POST, url, jsonParams, Response.Listener {
                            val data = it.getJSONObject("data")
                            val jsonObject = JSONObject()
                            val questions = jsonObject.optJSONArray("questions")
                            for(i in 0 until questions.length()){
                                val infoJsonObject = questions.getJSONObject(i)
                                val sno=infoJsonObject.getInt("sno")
                                val key=infoJsonObject.getString("key")
                            }
                            val success = data.getBoolean("success")
                            if (success) {//note
                                Toast.makeText(
                                    this@RegisterActivity,
                                    "Register Successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                                val infojsonObject = data.getJSONObject("data")
                                val name = infojsonObject.getString("name")
                                val email = infojsonObject.getString("email")
                                val mobile = infojsonObject.getString("mobile_number")
                                val address = infojsonObject.getString("address")
                                val userid=infojsonObject.getString("user_id")
                                sharedPreferences.edit().putString("user_id",userid).apply()
                                sharedPreferences.edit().putBoolean("loggedIn", true).apply()
                                sharedPreferences.edit().putString("name", name).apply()
                                sharedPreferences.edit().putString("mobile", mobile).apply()
                                sharedPreferences.edit().putString("email", email).apply()
                                sharedPreferences.edit().putString("place", address).apply()
                                val intent = Intent(this@RegisterActivity, HomeActivity::class.java)
                                startActivity(intent)


                            } else {
                                Toast.makeText(
                                    this@RegisterActivity,
                                    data.getString("errorMessage"),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }, Response.ErrorListener {
                            //here we handle error
                            Toast.makeText(
                                this@RegisterActivity,
                                "VolleyError $it",
                                Toast.LENGTH_SHORT
                            )
                                .show()
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
                    Toast.makeText(this@RegisterActivity, "no internet", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun check(
        pname: String,
        pmail: String,
        pmno: String,
        paddress: String,
        initpassword: String,
        ppassword: String
    ): Boolean {
        var validate: Boolean = true//(pname, pmail, pmno, paddress, initpassword, ppassword)
        var empty = ""
        if (pname == empty || pname.length < 3) {
            Toast.makeText(this@RegisterActivity, "Invalid name", Toast.LENGTH_SHORT).show()
            validate = false
            return false
        }
        if (pmail == empty || !emailValidator(pmail)) {
            validate = false
            Toast.makeText(this@RegisterActivity, "Invalid email Id", Toast.LENGTH_SHORT).show()
            return false
        }
        if (pmno == empty || pmno.length < 10) {
            validate = false
            Toast.makeText(this@RegisterActivity, "Invalid mobile number", Toast.LENGTH_SHORT)
                .show()
            return false

        }
        if (paddress == empty) {
            validate = false
            Toast.makeText(
                this@RegisterActivity,
                "delivery address cannot be empty",
                Toast.LENGTH_SHORT
            ).show()
            return false
        }
        if (initpassword == empty || initpassword.length < 4) {
            validate = false
            Toast.makeText(
                this@RegisterActivity,
                "password must be more than 4 digits",
                Toast.LENGTH_SHORT
            ).show()
            return false
        }
        if (ppassword != initpassword) {
            validate = false
            Toast.makeText(this@RegisterActivity, "passwords don't match", Toast.LENGTH_SHORT)
                .show()
        }
        return validate

    }
    fun emailValidator(pmail:String):Boolean{
        val pattern:Pattern
        val matcher:Matcher
        val emailPattern="^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
        pattern= Pattern.compile(emailPattern)
        matcher=pattern.matcher(pmail)
        return matcher.matches()
    }

}
