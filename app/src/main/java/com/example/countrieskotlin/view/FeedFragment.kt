package com.example.countrieskotlin.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.countrieskotlin.R
import com.example.countrieskotlin.adapter.CountryAdapter
import com.example.countrieskotlin.viewmodel.FeedViewModel
import kotlinx.android.synthetic.main.fragment_feed.*


class FeedFragment : Fragment() {

    private lateinit var viewModel : FeedViewModel
    private val countryAdapter = CountryAdapter(arrayListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(FeedViewModel::class.java)
        viewModel.refreshData()

        rv_countryList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = countryAdapter
        }

        swipeRefresh()
        observeLiveData()
    }

    private fun swipeRefresh() {
        swipeRefreshLayout.setOnRefreshListener {
            rv_countryList.visibility = View.GONE
            countryError.visibility = View.GONE
            countryLoading.visibility = View.VISIBLE
            viewModel.refreshFromAPI()
            swipeRefreshLayout.isRefreshing = false
        }
    }


    private fun observeLiveData() {
        viewModel.countries.observe(viewLifecycleOwner, Observer { countryList ->
            countryList?.let {
                rv_countryList.visibility = View.VISIBLE
                countryAdapter.updateCountryList(countryList)
            }
        })

        viewModel.countryError.observe(viewLifecycleOwner, Observer { error ->
            error?.let {
                if(it) {
                    countryError.visibility = View.VISIBLE
                    rv_countryList.visibility = View.GONE
                }
                else {
                    countryError.visibility = View.GONE
                }
            }
        })

        viewModel.countryLoading.observe(viewLifecycleOwner, Observer { loading ->
            loading?.let {
                if(it) {
                    countryLoading.visibility = View.VISIBLE
                    rv_countryList.visibility = View.GONE
                    countryError.visibility = View.GONE
                }
                else {
                    countryLoading.visibility = View.GONE
                }
            }
        })
    }


}