package com.amreen.hungrifoodie.Fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.amreen.hungrifoodie.R


class ProfileFragment : Fragment() {

    lateinit var textViewName:TextView
    lateinit var textViewEmail:TextView
    lateinit var textViewMobileNumber:TextView
    lateinit var textViewAddress:TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_profile, container, false)
        textViewName= view.findViewById(R.id.textViewName)
        textViewEmail= view.findViewById(R.id.textViewEmail)
        textViewMobileNumber  = view.findViewById(R.id.textViewMobileNumber)
        textViewAddress=view.findViewById(R.id.textViewAddress)
        val sharedPreferences = (activity as Context).getSharedPreferences("meals", Context.MODE_PRIVATE)
        textViewName.text=sharedPreferences.getString("name","")

        textViewEmail.text=sharedPreferences.getString("email","")
        textViewMobileNumber.text="+91-"+sharedPreferences.getString("mobile","")
        textViewAddress.text=sharedPreferences.getString("place","")
        return view
    }

}
