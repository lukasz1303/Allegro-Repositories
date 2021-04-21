package com.lukasz.allegrorepositories.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lukasz.allegrorepositories.databinding.RepositoryListItemBinding
import com.lukasz.allegrorepositories.domain.GitHubRepository

class GitHubRepositoryAdapter :ListAdapter<GitHubRepository, GitHubRepositoryAdapter.GitHubRepositoryViewHolder>(GitHubRepositoryDiffCallback()) {

    class GitHubRepositoryViewHolder(private val binding: RepositoryListItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(gitHubRepository: GitHubRepository){
            binding.viewHolder = gitHubRepository
            binding.executePendingBindings()
        }
    }

    class GitHubRepositoryDiffCallback: DiffUtil.ItemCallback<GitHubRepository>() {
        override fun areItemsTheSame(oldItem: GitHubRepository, newItem: GitHubRepository): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: GitHubRepository, newItem: GitHubRepository): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GitHubRepositoryViewHolder {
        return GitHubRepositoryViewHolder(RepositoryListItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: GitHubRepositoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}