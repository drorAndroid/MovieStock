package com.dror.moviestock.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.dror.moviestock.R
import com.dror.moviestock.data.network.MoviesService
import com.dror.moviestock.model.Movie
import com.dror.moviestock.model.Status
import com.dror.moviestock.viewmodel.MoviesViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_details.*
import kotlinx.android.synthetic.main.fragment_main.*

class DetailsFragment : Fragment() {
    private val moviesViewModel: MoviesViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()
    }

    private fun observeViewModel() {
        moviesViewModel.currentMovie.observe(viewLifecycleOwner, Observer { movie ->
            movie?.let {
                updateMovieData(it)
            }
        })
    }

    private fun updateMovieData(movie: Movie) {
        Glide.with(requireActivity())
            .load(MoviesService.PHOTO_BASE_URL + movie.backdropPath)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .into(backdropImageView)
        movieNameTextView.text = movie.title
        movieDateTextView.text = movie.releaseDate
        movieVoteAverageTextView.text = movie.voteAverage.toString()
        movieVoteCountTextView.text = movie.voteCount.toString()
        movieOverviewTextView.text = movie.overview
    }

    companion object {
        @JvmStatic
        fun newInstance() = DetailsFragment()
    }
}