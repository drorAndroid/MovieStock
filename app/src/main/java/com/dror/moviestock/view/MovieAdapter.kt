package com.dror.moviestock.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.dror.moviestock.R
import com.dror.moviestock.data.network.MoviesService
import com.dror.moviestock.model.Movie
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_movie.view.*


class MovieAdapter(private val context: Context, private var movies: List<Movie>): RecyclerView.Adapter<MovieAdapter.ItemViewHolder>() {

    private val clickSubject = PublishSubject.create<Movie>()
    val clickEvent : Observable<Movie> = clickSubject

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nameTextView: TextView = itemView.nameTextView
        var dateTextView: TextView = itemView.dateTextView
        var voteTextView: TextView = itemView.voteTextView
        var movieImageView: ImageView = itemView.movieImageView
    }

    fun update(movies: List<Movie>) {
        this.movies = movies
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_movie, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val movie = movies.get(position)
        movie.let { currMovie ->
            holder.nameTextView.text = currMovie.title
            holder.dateTextView.text = currMovie.releaseDate
            holder.voteTextView.text = currMovie.voteAverage.toString()

            Glide.with(context)
                .load(MoviesService.PHOTO_BASE_URL + currMovie.posterPath)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(holder.movieImageView)

            holder.itemView.setOnClickListener {
                clickSubject.onNext(currMovie)
            }
        }
    }

    override fun getItemCount(): Int {
        return movies.size
    }
}