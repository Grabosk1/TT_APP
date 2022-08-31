package com.example.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentTransaction
import com.example.detailslist.R
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton


class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val edit =  view?.findViewById<EditText?>(R.id.editTextTextPersonName)

        val button = view.findViewById<Button?>(R.id.button)
        button.setOnClickListener {
            val sharedPreferences: SharedPreferences = this.requireActivity().getSharedPreferences("shared", Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor =  sharedPreferences.edit()
            val phone_number = edit!!.text.toString().toInt()
            editor.putInt("number", phone_number)
            editor.apply()
            editor.commit()

        }
        return view
    }

    @SuppressLint("SetTextI18n")
    override fun onStart() {
        super.onStart()


    }
}