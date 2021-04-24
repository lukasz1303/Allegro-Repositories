package com.lukasz.allegrorepositories.detail

import android.app.Application
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.lukasz.allegrorepositories.database.getDatabase
import com.lukasz.allegrorepositories.domain.GitHubRepository
import com.lukasz.allegrorepositories.repository.GitHubReposRepository
import com.lukasz.allegrorepositories.repositorylist.RepositoryListFragmentDirections
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class DetailViewModel(gitHubRepository: GitHubRepository, app: Application) : AndroidViewModel(app) {
    private val _selectedRepository = MutableLiveData<GitHubRepository>()
    val selectedRepository: LiveData<GitHubRepository>
        get() = _selectedRepository

    private val _languages = MutableLiveData<String>()
    val languages: LiveData<String>
        get() = _languages


    private val gitHubReposRepository = GitHubReposRepository(getDatabase(app))
    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)


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

    init {
        refreshDetailDataFromRepository()
    }

    private fun refreshDetailDataFromRepository() {
        coroutineScope.launch {
            try {
                selectedRepository.value?.name?.let {
                    _languages.value = gitHubReposRepository.refreshGitHubRepositoryLanguage(
                        it
                    )
                }
                Log.i("DetailViewModel", "Success: languages retrieved")

            } catch (e: Exception) {
                Log.i("DetailViewModel", "Failure: ${e.message}")
            }
        }
    }
}