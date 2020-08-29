package com.amreen.hungrifoodie.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.widget.Toolbar
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.amreen.hungrifoodie.R
import com.amreen.hungrifoodie.Fragment.*

import com.google.android.material.navigation.NavigationView



class HomeActivity : AppCompatActivity() {
    lateinit var drawerLayout: DrawerLayout
    lateinit var coordinatorLayout: CoordinatorLayout
    lateinit var toolbar: Toolbar
    lateinit var frameLayout: FrameLayout
    lateinit var navigationView: NavigationView
    lateinit var sharedPreferences: SharedPreferences
    lateinit var username:TextView
    lateinit var userphone:TextView
    var previousMenuItem:MenuItem?=null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        drawerLayout=findViewById(R.id.drawerlayout)
        coordinatorLayout=findViewById(R.id.coordinatorLayout)
        toolbar=findViewById(R.id.toolbar)
        frameLayout=findViewById(R.id.frame)
        navigationView=findViewById(R.id.navigationview)
        sharedPreferences = getSharedPreferences("meals", Context.MODE_PRIVATE)
        val view=navigationView.getHeaderView(0)
        sharedPreferences=getSharedPreferences("meals", Context.MODE_PRIVATE)
        username=view.findViewById(R.id.txtname)
        userphone=view.findViewById(R.id.txtphone)
        username.text=sharedPreferences.getString("name","Amreen")
        userphone.text=sharedPreferences.getString("mobile","7301868050")
        setUpToolbar()
        openHome()
        val actionBarDrawerToggle=ActionBarDrawerToggle(this@HomeActivity,drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        navigationView.setNavigationItemSelectedListener {
            if(previousMenuItem!=null){
                previousMenuItem?.isChecked=false

            }
            it.isCheckable=true
            it.isChecked=true
            previousMenuItem=it
            when(it.itemId){
                R.id.home ->{
                   openHome()
                    drawerLayout.closeDrawers()

                }
                R.id.favourites ->{
                    supportFragmentManager.beginTransaction().replace(
                        R.id.frame,
                       FavouritesFragment()
                    ).commit()
                    supportActionBar?.title="Favourites"
                    drawerLayout.closeDrawers()

                }
                R.id.orderhistory ->{
                    supportFragmentManager.beginTransaction().replace(
                        R.id.frame,
                        OrderhistoryFragment()
                    ).commit()
                    supportActionBar?.title="Order history"
                    drawerLayout.closeDrawers()

                }
                R.id.myprofile ->{
                    supportFragmentManager.beginTransaction().replace(
                        R.id.frame,
                       ProfileFragment()
                    ).commit()
                    supportActionBar?.title="Profile"
                    drawerLayout.closeDrawers()

                }
                R.id.faq ->{
                    supportFragmentManager.beginTransaction().replace(
                        R.id.frame,
                      FaqFragment()
                    ).commit()
                    supportActionBar?.title="Faq's"
                    drawerLayout.closeDrawers()

                }
                R.id.logout ->{
                    val dialog= AlertDialog.Builder(this@HomeActivity).setCancelable(false)
                    drawerLayout.closeDrawers()
                    dialog.setTitle("Logout")
                    dialog.setMessage("Are you sure you want to exit")
                    dialog.setPositiveButton("YES") { text, listener ->
                        sharedPreferences.edit().clear().apply()
                        val intent = Intent(this@HomeActivity,
                            LoginnActivity::class.java)
                        startActivity(intent)
                        finish()
                    }

                    dialog.setNegativeButton("No") { text, listener ->
                        openHome()
                    }
                    dialog.create()
                    dialog.show()

                }

            }
            return@setNavigationItemSelectedListener true
        }
    }

    fun setUpToolbar(){
        setSupportActionBar(toolbar)
        supportActionBar?.title="toolbar"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
     val id=item.itemId
        if(id==android.R.id.home)
          drawerLayout.openDrawer(GravityCompat.START)
        return super.onOptionsItemSelected(item)
    }
    fun openHome(){
        val fragment=HomeFragment()
        val transaction=supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame,fragment)
        transaction.commit()
        supportActionBar?.title="All Restaurants"
        navigationView.setCheckedItem(R.id.home)



    }

    override fun onBackPressed() {
        val flag=supportFragmentManager.findFragmentById(R.id.frame)
        when(flag){
            !is HomeFragment ->openHome()
            else->super.onBackPressed()
        }
    }
}
