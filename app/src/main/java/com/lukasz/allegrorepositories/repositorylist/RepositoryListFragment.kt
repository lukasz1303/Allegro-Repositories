package com.lukasz.allegrorepositories.repositorylist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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

        val manager = GridLayoutManager(activity,1)
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

        return binding.root
    }

    private fun updateRepositories(){
        val job = Job()
        CoroutineScope(job + Dispatchers.Main).launch {
            val result = viewModel.refreshDataFromRepositoryWithResult()
            if (result == 1) {
                Toast.makeText(context,
                    R.string.refresh_done_toast_message,
                    Toast.LENGTH_SHORT).show()
            }
            else if (result == 2) {
                Toast.makeText(context,
                    R.string.refresh_failed_toast_message,
                    Toast.LENGTH_SHORT).show()
            }
        }
        binding.swipeRefresh.isRefreshing = false
    }
}