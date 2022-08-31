package com.example.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.adapters.RouteAdapter.ExampleViewHolder
import com.example.detailslist.R
import com.example.models.RouteItem
import com.squareup.picasso.Picasso


open class RouteAdapter(private val mContext: Context, private val mExampleList: ArrayList<RouteItem>) :
    RecyclerView.Adapter<ExampleViewHolder>() {
    private lateinit var mListener: OnItemClickListener

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExampleViewHolder {
        val v = LayoutInflater.from(mContext).inflate(R.layout.example_item, parent, false)
        return ExampleViewHolder(v, mListener)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ExampleViewHolder, position: Int) {
        val currentItem = mExampleList[position]

        val imageUrl: String = currentItem.mImage
        val creatorName: String = currentItem.mName
        val distance: Int = currentItem.mDistance
        holder.mTextViewCreator.text = creatorName
        holder.mTextViewDistance.text = "Dystans: $distance km"

        Picasso.get().load(imageUrl).fit().centerInside().into(holder.mImageView)
    }

    override fun getItemCount(): Int {
        return mExampleList.size
    }

    inner class ExampleViewHolder(itemView: View, listener: OnItemClickListener) : RecyclerView.ViewHolder(itemView) {
        var mImageView: ImageView = itemView.findViewById(R.id.image_view)
        var mTextViewCreator: TextView = itemView.findViewById(R.id.text_view_creator)
        var mTextViewDistance: TextView = itemView.findViewById(R.id.text_view_distance)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }
}