package com.example.newsapp


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class NewsAdapter( private val  listener:NewsItemClicked ) : RecyclerView.Adapter<ViewHolder>() {
    private val items:ArrayList<News> = ArrayList()
    override fun getItemCount(): Int {
       return items.size;
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
          val currentItem =  items[position];
          holder.titleView.text = currentItem.title
          holder.authorView.text = currentItem.author
          Glide.with(holder.itemView.context).load(currentItem.imageUrl).into(holder.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_news ,parent , false);
        var viewHolder = ViewHolder(view);
        view.setOnClickListener( View.OnClickListener {
              listener.onClick(items[viewHolder.adapterPosition]);
        })
        return viewHolder;
    }

    fun updateNews( updatenews:ArrayList<News>) {
        items.clear()
        items.addAll(updatenews)
        notifyDataSetChanged()
    }
}

class ViewHolder(items:View ): RecyclerView.ViewHolder(items) {
    var titleView:TextView = itemView.findViewById(R.id.title)
    var authorView:TextView = items.findViewById(R.id.author)
    var imageView:ImageView = items.findViewById(R.id.imageview)
}

interface NewsItemClicked {
    fun onClick( item:News ) {

    }
}