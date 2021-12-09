package com.example.eurisko_challenge.RecycleView

import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eurisko_challenge.Models.Result
import com.example.eurisko_challenge.R
import com.squareup.picasso.Picasso

class MostPopularViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    var imageContent = view.findViewById<ImageView>(R.id.contentImage)
    var contentTitle = view.findViewById<TextView>(R.id.contentTitle)
    var contentDate = view.findViewById<TextView>(R.id.contentDate)


}

class MostPopularViewAdapter(private var contents: List<Result>, private val listener: OnClickListener) : RecyclerView.Adapter<MostPopularViewHolder>() {

    interface OnClickListener{
        fun onItemClick(url: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MostPopularViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.most_popular_content, parent, false)
        return MostPopularViewHolder(view)
    }

    override fun onBindViewHolder(holder: MostPopularViewHolder, position: Int) {
        if(contents.isNotEmpty()){
            val content = contents[position]
            if(content.media.size > 0){
                Picasso.with(holder.imageContent.context)
                    .load(content.media[0].media_metadata[0].url)
                    .placeholder(R.drawable.ic_firstname)
                    .error(R.drawable.ic_firstname)
                    .into(holder.imageContent)
            }

            holder.contentTitle.setText(content.title)
            holder.contentDate.setText(content.published_date)
            holder.view.setOnClickListener{
                listener.onItemClick(content.url)
            }
        }
    }

    override fun getItemCount(): Int {
        return contents.size
    }

    fun loadNewData(newContents: List<Result>){
        contents = newContents
        notifyDataSetChanged()
    }


}