package com.tvapp.minimax

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.leanback.widget.Presenter
import androidx.core.content.ContextCompat

class CardPresenter : Presenter() {

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_item, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: Presenter.ViewHolder, item: Any) {
        val movie = item as Movie
        val holder = viewHolder as MovieViewHolder

        holder.title.text = movie.title
        holder.description.text = movie.description
        holder.image.setImageResource(movie.imageUrl)

        // 设置焦点和选中状态的颜色变化
        holder.itemView.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                holder.itemView.setBackgroundColor(
                    ContextCompat.getColor(holder.itemView.context, R.color.focused_background)
                )
            } else {
                holder.itemView.setBackgroundColor(
                    ContextCompat.getColor(holder.itemView.context, R.color.normal_background)
                )
            }
        }
    }

    override fun onUnbindViewHolder(viewHolder: Presenter.ViewHolder) {
        // 清理资源
    }

    private inner class MovieViewHolder(view: View) : Presenter.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.title)
        val description: TextView = view.findViewById(R.id.description)
        val image: ImageView = view.findViewById(R.id.image)
    }
}