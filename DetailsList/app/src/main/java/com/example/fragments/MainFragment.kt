package com.example.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.adapters.RouteAdapter
import com.example.detailslist.DetailsActivity
import com.example.detailslist.R
import com.example.models.RouteItem
import org.json.JSONArray
import org.json.JSONTokener

class MainFragment : Fragment() {
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mExampleAdapter: RouteAdapter
    private lateinit var mExampleList: ArrayList<RouteItem>
    private lateinit var mRequestQueue: RequestQueue

    //Zmienne tabletowe
    private lateinit var mFragment: DetailFragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_main, container, false)
        mRecyclerView = view.findViewById(R.id.recycler_view)
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = LinearLayoutManager(activity)
        mExampleList = ArrayList()
        mRequestQueue = Volley.newRequestQueue(activity)
        parseJSON()

        return view
    }

    private fun parseJSON() {
        val url = "http://192.168.1.2/FinalApp/Api.php"
        val stringReq = StringRequest(
            Request.Method.GET, url,
            { response ->
                val strResp = response.toString()
                val jsonArray = JSONTokener(strResp).nextValue() as JSONArray

                for (i in 0 until jsonArray.length()) {
                    val hit = jsonArray.getJSONObject(i)

                    val id = hit.getInt("id")
                    val name = hit.getString("name")
                    val des = hit.getString("descript")
                    val dis = hit.getInt("distance")
                    val imgUrl = hit.getString("image")
                    val cordX = hit.getString("cordX")
                    val cordY = hit.getString("cordY")
                    val level = hit.getString("level")
                    if (level == "easy"){
                        mExampleList.add(RouteItem(id, name, des, dis, imgUrl, cordX.toDouble(), cordY.toDouble()))
                    }
                }
                mExampleAdapter = RouteAdapter(requireContext(), mExampleList)
                mRecyclerView.adapter = mExampleAdapter

                mExampleAdapter.setOnItemClickListener(object: RouteAdapter.OnItemClickListener{
                    override fun onItemClick(position: Int) {
                        val isTablet = resources.getBoolean(R.bool.isTablet)
                        if (isTablet){
                            val ft = parentFragmentManager.beginTransaction()
                            mFragment = DetailFragment.newInstance()
                            mFragment.sendData(mExampleList[position].mName,
                                mExampleList[position].mDescript,
                                mExampleList[position].mDistance.toString(),
                                mExampleList[position].mImage,
                                mExampleList[position].mCordX,
                                mExampleList[position].mCordY)
                            ft.replace(R.id.fragment_container, mFragment)
                            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            ft.addToBackStack(null)
                            ft.commit()
                        }
                        else{
                            val intent = Intent(activity, DetailsActivity::class.java)
                            intent.putExtra("name", mExampleList[position].mName)
                            intent.putExtra("description", mExampleList[position].mDescript)
                            intent.putExtra("distance", mExampleList[position].mDistance.toString())
                            intent.putExtra("img", mExampleList[position].mImage)
                            intent.putExtra("x", mExampleList[position].mCordX.toString())
                            intent.putExtra("y", mExampleList[position].mCordY.toString())
                            startActivity(intent)
                        }
                    }
                })
            },
            { Log.d("API", "Coś poszło nie tak...") })
        mRequestQueue.add(stringReq)
    }
}