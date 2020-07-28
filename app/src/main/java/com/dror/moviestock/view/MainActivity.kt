package com.dror.moviestock.view

import android.app.ActionBar
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.dror.moviestock.R
import com.dror.moviestock.model.Status
import com.dror.moviestock.viewmodel.MoviesViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val moviesViewModel: MoviesViewModel by viewModels()
    private lateinit var pagerAdapter: MoviesPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pagerAdapter = MoviesPagerAdapter(supportFragmentManager)
        viewPager.adapter = pagerAdapter

        val actionBar: ActionBar? = actionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        observeViewModel()
    }

    private fun observeViewModel() {
        moviesViewModel.movies.observe(this, Observer { response ->
            when (response.status) {
                Status.LOADING -> {
                    progressBar.visibility = View.VISIBLE
                }
                Status.ERROR -> {
                    progressBar.visibility = View.GONE
                    response.errorMessage?.let { errorMessage ->
                        val alertDialogBuilder = AlertDialog.Builder(this)
                        alertDialogBuilder.apply {
                            setTitle("Error")
                            setMessage(errorMessage)
                            setPositiveButton("OK") { dialogInterface, _ ->
                                dialogInterface.dismiss()
                            }
                            show()
                        }
                    }
                }
                else -> {
                    //success
                    progressBar.visibility = View.GONE
                }
            }

        })

        moviesViewModel.currentMovie.observe(this, Observer { movie ->
            movie?.let {
                viewPager.currentItem = pagerAdapter.getFragmentPosition(DetailsFragment::class)
            }
        })
    }

    override fun onBackPressed() {
        val mainFragmentPosition = pagerAdapter.getFragmentPosition(MainFragment::class)
        if(viewPager.currentItem == mainFragmentPosition) {
            super.onBackPressed()
        }
        else {
            viewPager.currentItem = mainFragmentPosition
        }
    }
}