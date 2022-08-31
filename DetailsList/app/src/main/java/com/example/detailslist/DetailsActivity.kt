package com.example.detailslist

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.fragments.DetailFragment

class DetailsActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_activity)

        val frag: DetailFragment =
            supportFragmentManager.findFragmentById(R.id.detailFragment) as DetailFragment

        val name = intent.getStringExtra("name")
        val description = intent.getStringExtra("description")
        val distance = intent.getStringExtra("distance")
        val image = intent.getStringExtra("img")
        val geoX = intent.getStringExtra("x")?.toDouble()!!
        val geoY = intent.getStringExtra("y")?.toDouble()!!

        frag.sendData(name!!, description!!, distance!!, image!!, geoX, geoY)

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.custom_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.Search -> {
                openTourWebPage()
                true
            }
            else -> {

                true
            }
        }
    }
    private fun openTourWebPage(){
        val url = "https://www.snowcard.co.uk/blog/ten-best-treks-europe"
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    }
}