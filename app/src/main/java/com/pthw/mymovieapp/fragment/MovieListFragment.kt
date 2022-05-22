package com.pthw.mymovieapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.pthw.mymovieapp.R
import com.pthw.mymovieapp.databinding.FragmentHomeBinding
import com.pthw.mymovieapp.databinding.FragmentMovieListBinding
import com.pthw.mymovieapp.databinding.ListItemMovieBinding
import com.pthw.mymovieapp.databinding.ListItemMovieGridBinding
import com.pthw.mymovieapp.mvvm.event.HomeUiEvent
import com.pthw.mymovieapp.mvvm.viewmodel.HomeViewModel
import com.pthw.mymovieapp.utils.AppConstants
import com.pthw.mymovieapp.utils.applySmartScrollListener
import com.pthw.mymovieapp.utils.fastadapter.SmartScrollListener
import com.pthw.mymovieapp.utils.fastadapter.bind
import com.pthw.mymovieapp.utils.fastadapter.update
import com.pthw.mymovieapp.utils.load
import com.pthw.mymovieapp.vos.MovieVO
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp

@AndroidEntryPoint
class MovieListFragment : MVVMFragment<HomeViewModel, HomeUiEvent>(),
    SmartScrollListener.OnSmartScrollListener {

    private val viewModel: HomeViewModel by viewModels()
    private var binding: FragmentMovieListBinding? = null

    private var listType = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        if (binding == null) {
            binding = FragmentMovieListBinding.inflate(inflater, container, false)

            setupUI()

            showLoadingDialog()
            when (listType) {
                AppConstants.TREND -> {
                    viewModel.getPopularMovieList()
                }
                AppConstants.TOPRATED -> {
                    viewModel.getTopRatedMovieList()
                }
                else -> {
                    viewModel.getUpComingMovieList()
                }
            }
        }
        return binding?.root
    }


    private fun setupUI() {
        listType = arguments?.getString(AppConstants.LIST_TYPE) ?: ""
        binding?.tvTitle?.text = listType
        binding?.ivBack?.setOnClickListener { popBackStack() }

        binding?.rvMovieGrid?.applySmartScrollListener(this)
        binding?.rvMovieGrid?.bind(
            emptyList(),
            R.layout.list_item_movie_grid
        ) { item: MovieVO, pos ->
            ListItemMovieGridBinding.bind(this).apply {
                ivMoviePoster.load(imageUrl + item.posterPath)
                tvMovieTitle.text = item.movieTitle
                tvMovieDate.text = item.releaseDate.split("-")[0]
                tvMovieRate.text = item.voteAverage.toString()

                setOnClickListener {
                    val bundle = Bundle()
                    bundle.putSerializable(AppConstants.MOVIE_DATA, item)
                    findNavController().navigate(
                        R.id.action_movieListFragment_to_movieDetailFragment,
                        bundle
                    )
                }
            }
        }?.layoutManager(GridLayoutManager(context, 3))
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
            is HomeUiEvent.SuccessMovieList -> {
                hideLoadingDialog()
                val list = event.item.toMutableList()
                binding?.rvMovieGrid?.update(list)
            }
        }
    }

    override fun renderError(message: String) {
        hideLoadingDialog()
    }


    override fun onListEndReach() {
        when (listType) {
            AppConstants.TREND -> {
                viewModel.onListEndReachPopularList()
            }
            AppConstants.TOPRATED -> {
                viewModel.onListEndReachTopRatedList()
            }
            else -> {
                viewModel.onListEndReachUpComingList()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}