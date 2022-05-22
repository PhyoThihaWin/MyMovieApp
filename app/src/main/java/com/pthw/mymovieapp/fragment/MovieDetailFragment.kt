package com.pthw.mymovieapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pthw.mymovieapp.R
import com.pthw.mymovieapp.base.BaseFragment
import com.pthw.mymovieapp.databinding.FragmentHomeBinding
import com.pthw.mymovieapp.databinding.FragmentMovieDetailBinding
import com.pthw.mymovieapp.utils.AppConstants
import com.pthw.mymovieapp.utils.load
import com.pthw.mymovieapp.vos.MovieVO

class MovieDetailFragment : BaseFragment() {

    var movieVO: MovieVO? = null
    private var binding: FragmentMovieDetailBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        if (binding == null)
            binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
    }

    private fun setupUI() {
        binding?.ivBack?.setOnClickListener { popBackStack() }

        movieVO = arguments?.getSerializable(AppConstants.MOVIE_DATA) as MovieVO

        movieVO?.let {
            binding?.ivMoviePosterLandscape?.load(
                imageUrl + it.backdropPath,
                placeholder = 0,
                error = 0,
                centerCrop = true
            )
            binding?.tvMovieDate?.text = it.releaseDate
            binding?.tvMovieRate?.text = it.voteAverage.toString()

            binding?.tvMovieTitle?.text = it.movieTitle
            binding?.tvOverview?.text = it.overview
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}