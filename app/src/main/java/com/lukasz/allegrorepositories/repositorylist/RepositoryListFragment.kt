package com.lukasz.allegrorepositories.repositorylist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.lukasz.allegrorepositories.adapters.GitHubRepositoryAdapter
import com.lukasz.allegrorepositories.databinding.FragmentRepositoryListBinding

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

        binding.repositoryList.adapter = GitHubRepositoryAdapter()

        return binding.root
    }
}