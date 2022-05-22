package com.pthw.mymovieapp.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.pthw.mymovieapp.R
import com.pthw.mymovieapp.databinding.FragmentBrowseBinding
import com.pthw.mymovieapp.databinding.FragmentHomeBinding
import com.pthw.mymovieapp.databinding.ListItemMovieBinding
import com.pthw.mymovieapp.databinding.ListItemMovieGridSmallBinding
import com.pthw.mymovieapp.mvvm.event.BrowseUiEvent
import com.pthw.mymovieapp.mvvm.event.HomeUiEvent
import com.pthw.mymovieapp.mvvm.viewmodel.BrowseViewModel
import com.pthw.mymovieapp.mvvm.viewmodel.HomeViewModel
import com.pthw.mymovieapp.utils.*
import com.pthw.mymovieapp.utils.fastadapter.SmartScrollListener
import com.pthw.mymovieapp.utils.fastadapter.bind
import com.pthw.mymovieapp.utils.fastadapter.update
import com.pthw.mymovieapp.vos.MovieVO
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BrowseFragment : MVVMFragment<BrowseViewModel, BrowseUiEvent>(),
    SmartScrollListener.OnSmartScrollListener {

    private val viewModel: BrowseViewModel by viewModels()
    private var binding: FragmentBrowseBinding? = null

    var search = false
    var query = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        if (binding == null) {
            binding = FragmentBrowseBinding.inflate(inflater, container, false)

            setupUI()

            showLoadingDialog()
            viewModel.getPopularMovieList()

        }
        return binding?.root
    }


    private fun setupUI() {
        binding?.etSearchMovie?.afterTextChangedDelayed {
            if (it.length > 1) {
                binding?.ivClearText?.toVisible()

                // search api
                callSearchApi(it)

            } else {
                binding?.ivClearText?.toGone()

                callPopularApi()
            }

        }

        binding?.ivClearText?.setOnClickListener {
            binding?.etSearchMovie?.setText("")
            callPopularApi()
        }

        binding?.rvMovieGrid?.applySmartScrollListener(this)
        binding?.rvMovieGrid?.bind(
            emptyList(),
            R.layout.list_item_movie_grid_small
        ) { item: MovieVO, pos ->
            ListItemMovieGridSmallBinding.bind(this).apply {
                ivMoviePoster.load(imageUrl + item.posterPath)
                tvMovieTitle.text = item.movieTitle

                setOnClickListener {
                    val bundle = Bundle()
                    bundle.putSerializable(AppConstants.MOVIE_DATA, item)
                    findNavController().navigate(
                        R.id.action_browseFragment_to_movieDetailFragment,
                        bundle
                    )
                }
            }
        }?.layoutManager(GridLayoutManager(context, 3))


    }

    private fun callPopularApi() {
        search = false
        showLoadingDialog()
        viewModel.getPopularMovieList()
    }

    private fun callSearchApi(str: String) {
        search = true
        showLoadingDialog()
        query = str
        viewModel.getSearchMovieList(query = query)
    }

    override fun getOrCreateViewModel(): BrowseViewModel {
        return viewModel
    }

    override fun renderEvent(event: BrowseUiEvent) {
        when (event) {
            is BrowseUiEvent.Error -> {
                hideLoadingDialog()
                showErrorMessage(event.message)
            }
            is BrowseUiEvent.SuccessMovieList -> {
                hideLoadingDialog()
                val list = event.item.toMutableList()
                binding?.rvMovieGrid?.update(list)
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

    override fun onListEndReach() {
        if (search) viewModel.onListEndReachSearchList(query)
        else viewModel.onListEndReachPopularList()
    }


}