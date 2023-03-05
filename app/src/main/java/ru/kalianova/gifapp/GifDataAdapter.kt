package ru.kalianova.gifapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide

class GifDataAdapter(private val gifs: List<GifDataObject>, val context: Context) : RecyclerView.Adapter<GifDataAdapter.GifViewHolder>(){
    class GifViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val image = itemView.findViewById<ImageView>(R.id.imageViewGifItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GifViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.gif_item, parent, false)
        return GifViewHolder(itemView)
    }

    override fun getItemCount() = gifs.size

    override fun onBindViewHolder(holder: GifViewHolder, position: Int) {
        val gif = gifs[position]

        val circularProgressDrawable = CircularProgressDrawable(context)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()

        Glide.with(context).load(gif.images.original.url).placeholder(circularProgressDrawable).into(holder.image)
        holder.itemView.setOnClickListener{
            onItemClick(position)
        }
    }

    private fun onItemClick(position: Int){
        val intent = Intent(context, ActivityGifInfo::class.java)
        intent.putExtra("url", gifs[position].images.original.url)
        intent.putExtra("title", gifs[position].title)
        intent.putExtra("username", gifs[position].username)
        intent.putExtra("date", gifs[position].import_datetime)
        context.startActivity(intent)
    }
}