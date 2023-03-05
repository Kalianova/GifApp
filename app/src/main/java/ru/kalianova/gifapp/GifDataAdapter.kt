package ru.kalianova.gifapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide

class GifPagerAdapter :
    PagingDataAdapter<GifDataObject, GifPagerAdapter.GifViewHolder>(GifComparator) {

    class GifViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image = itemView.findViewById<ImageView>(R.id.imageViewGifItem)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GifViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.gif_item, parent, false)
        return GifViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: GifViewHolder, position: Int) {
        val gif = getItem(position)

        val circularProgressDrawable = CircularProgressDrawable(holder.itemView.context)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()

        Glide.with(holder.itemView.context).load(gif!!.images.original.url)
            .placeholder(circularProgressDrawable)
            .into(holder.image)
        holder.itemView.setOnClickListener {
            onItemClick(position, holder.itemView.context)
        }
    }

    private fun onItemClick(position: Int, context: Context) {
        val intent = Intent(context, ActivityGifInfo::class.java)
        intent.putExtra("url", getItem(position)!!.images.original.url)
        intent.putExtra("title", getItem(position)!!.title)
        intent.putExtra("username", getItem(position)!!.username)
        intent.putExtra("date", getItem(position)!!.import_datetime)
        context.startActivity(intent)
    }

    object GifComparator : DiffUtil.ItemCallback<GifDataObject>() {
        override fun areItemsTheSame(oldItem: GifDataObject, newItem: GifDataObject): Boolean {
            return oldItem.images.original.url == newItem.images.original.url
        }

        override fun areContentsTheSame(oldItem: GifDataObject, newItem: GifDataObject): Boolean {
            return oldItem == newItem
        }
    }
}