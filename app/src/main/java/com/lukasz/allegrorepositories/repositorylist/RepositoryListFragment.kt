package com.lukasz.allegrorepositories.repositorylist

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.lukasz.allegrorepositories.R
import com.lukasz.allegrorepositories.adapters.GitHubRepositoryAdapter
import com.lukasz.allegrorepositories.adapters.GitHubRepositoryListener
import com.lukasz.allegrorepositories.databinding.FragmentRepositoryListBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class RepositoryListFragment : Fragment() {

    val viewModel: RepositoryListViewModel by lazy {
        ViewModelProvider(this).get(RepositoryListViewModel::class.java)
    }

    private lateinit var binding: FragmentRepositoryListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRepositoryListBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val manager = GridLayoutManager(activity, 1)
        binding.repositoryList.layoutManager = manager

        binding.repositoryList.addItemDecoration(
            DividerItemDecoration(
                binding.repositoryList.context,
                DividerItemDecoration.VERTICAL
            )
        )

        binding.repositoryList.adapter = GitHubRepositoryAdapter(GitHubRepositoryListener {
            viewModel.displayRepositoryDetails(it)
        })

        binding.swipeRefresh.setOnRefreshListener {
            updateRepositories()
        }

        viewModel.navigateToSelectedRepository.observe(viewLifecycleOwner, {
            if (null != it) {
                this.findNavController().navigate(
                    RepositoryListFragmentDirections.actionRepositoryListFragmentToDetailFragment(it)
                )
                viewModel.displayRepositoryDetailsComplete()
            }
        })

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.repository_list_menu, menu)
        val searchItem = menu.findItem(R.id.menu_search)
        val searchView = searchItem.actionView as SearchView
        val searchManager = requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))


        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                searchView.setQuery("", false)
                searchItem.collapseActionView()
                viewModel.setSearchedName("")
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    viewModel.setSearchedName(newText)
                }
                return false
            }
        })

        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun updateRepositories(){
        val job = Job()
        CoroutineScope(job + Dispatchers.Main).launch {
            val result = viewModel.refreshDataFromRepositoryWithResult()
            if (result == 1) {
                Toast.makeText(
                    context,
                    R.string.refresh_done_toast_message,
                    Toast.LENGTH_SHORT
                ).show()
            }
            else if (result == 2) {
                Toast.makeText(
                    context,
                    R.string.refresh_failed_toast_message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        binding.swipeRefresh.isRefreshing = false
    }
}