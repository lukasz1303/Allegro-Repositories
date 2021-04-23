package com.lukasz.allegrorepositories.detail

import android.app.Application
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.lukasz.allegrorepositories.domain.GitHubRepository
import com.lukasz.allegrorepositories.repositorylist.RepositoryListFragmentDirections

class DetailViewModel(gitHubRepository: GitHubRepository, app: Application) : AndroidViewModel(app) {
    private val _selectedRepository = MutableLiveData<GitHubRepository>()
    val selectedRepository: LiveData<GitHubRepository>
        get() = _selectedRepository


    private val _navigateToGithubUrl = MutableLiveData<String>()
    val navigateToGithubUrl: LiveData<String>
        get() = _navigateToGithubUrl

    fun displayInWebBrowser(gitHubUrl: String) {
        _navigateToGithubUrl.value = gitHubUrl
    }

    fun displayInWebBrowserComplete() {
        _navigateToGithubUrl.value = null
    }

    init {
        _selectedRepository.value = gitHubRepository
    }
}