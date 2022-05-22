package com.pthw.mymovieapp.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.pthw.mymovieapp.R
import com.pthw.mymovieapp.databinding.FragmentHomeBinding
import com.pthw.mymovieapp.databinding.ListItemMovieBinding
import com.pthw.mymovieapp.databinding.ListItemMovieLargeBinding
import com.pthw.mymovieapp.databinding.ListItemMovieSmallBinding
import com.pthw.mymovieapp.mvvm.event.HomeUiEvent
import com.pthw.mymovieapp.mvvm.viewmodel.HomeViewModel
import com.pthw.mymovieapp.utils.AppConstants
import com.pthw.mymovieapp.utils.fastadapter.bind
import com.pthw.mymovieapp.utils.fastadapter.update
import com.pthw.mymovieapp.utils.load
import com.pthw.mymovieapp.utils.toVisible
import com.pthw.mymovieapp.vos.MovieVO
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : MVVMFragment<HomeViewModel, HomeUiEvent>() {

    private val viewModel: HomeViewModel by viewModels()
    private var binding: FragmentHomeBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        if (binding == null) {
            binding = FragmentHomeBinding.inflate(inflater, container, false)

            setupUI()

            //--api call
            showLoadingDialog()
            viewModel.getHomeMovie()
            viewModel.fetchHomeMovie()
        }

        return binding?.root
    }


    private fun setupUI() {
        binding?.rvTrending?.bind(
            emptyList(),
            R.layout.list_item_movie
        ) { item: MovieVO, pos ->
            ListItemMovieBinding.bind(this).apply {
                ivMoviePoster.load(imageUrl + item.posterPath)
                tvMovieTitle.text = item.movieTitle
                tvMovieDate.text = item.releaseDate
                tvMovieRate.text = item.voteAverage.toString()

                setOnClickListener {
                    val bundle = Bundle()
                    bundle.putSerializable(AppConstants.MOVIE_DATA, item)
                    findNavController().navigate(
                        R.id.action_homeFragment_to_movieDetailFragment,
                        bundle
                    )
                }
            }
        }?.layoutManager(LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false))

        binding?.rvTopRated?.bind(
            emptyList(),
            R.layout.list_item_movie_small
        ) { item: MovieVO, pos ->
            ListItemMovieSmallBinding.bind(this).apply {
                ivMoviePoster.load(imageUrl + item.posterPath)
                tvMovieTitle.text = item.movieTitle

                setOnClickListener {
                    val bundle = Bundle()
                    bundle.putSerializable(AppConstants.MOVIE_DATA, item)
                    findNavController().navigate(
                        R.id.action_homeFragment_to_movieDetailFragment,
                        bundle
                    )
                }
            }
        }?.layoutManager(LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false))

        binding?.rvUpcoming?.bind(
            emptyList(),
            R.layout.list_item_movie_large
        ) { item: MovieVO, pos ->
            ListItemMovieLargeBinding.bind(this).apply {
                ivMoviePoster.load(imageUrl + item.backdropPath)
                tvMovieTitle.text = item.movieTitle
                tvMovieDate.text = item.releaseDate
                tvMovieRate.text = item.voteAverage.toString()

                setOnClickListener {
                    val bundle = Bundle()
                    bundle.putSerializable(AppConstants.MOVIE_DATA, item)
                    findNavController().navigate(
                        R.id.action_homeFragment_to_movieDetailFragment,
                        bundle
                    )
                }
            }
        }


        //-- seemore
        binding?.tvTrendSeeMore?.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(AppConstants.LIST_TYPE, AppConstants.TREND)
            findNavController().navigate(
                R.id.action_homeFragment_to_movieListFragment,
                bundle
            )
        }
        binding?.tvTopRatedSeeMore?.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(AppConstants.LIST_TYPE, AppConstants.TOPRATED)
            findNavController().navigate(
                R.id.action_homeFragment_to_movieListFragment,
                bundle
            )
        }
        binding?.tvUpcomingSeeMore?.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(AppConstants.LIST_TYPE, AppConstants.UPCOMING)
            findNavController().navigate(
                R.id.action_homeFragment_to_movieListFragment,
                bundle
            )
        }
    }


    override fun getOrCreateViewModel(): HomeViewModel {
        return viewModel
    }

    override fun renderEvent(event: HomeUiEvent) {
        when (event) {
            is HomeUiEvent.Error -> {
                hideLoadingDialog()
                showErrorMessage(event.message)
            }
            is HomeUiEvent.SuccessHomeMovie -> {
                hideLoadingDialog()

                binding?.layoutLinear?.toVisible()

                val trendList = event.item.trendingList
                val latestList = event.item.topRatedList
                val upcomingList = event.item.upcomingList
                binding?.rvTrending?.update(trendList.toMutableList())
                binding?.rvTopRated?.update(latestList.toMutableList())
                binding?.rvUpcoming?.update(
                    upcomingList.subList(0, upcomingList.size / 2).toMutableList()
                )
            }

        }
    }

    override fun renderError(message: String) {
        hideLoadingDialog()
    }


    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}