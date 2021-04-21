package com.lukasz.allegrorepositories.repositorylist

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.lukasz.allegrorepositories.database.getDatabase
import com.lukasz.allegrorepositories.repository.GitHubReposRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RepositoryListViewModel(application: Application) : AndroidViewModel(application) {

    private val gitHubReposRepository = GitHubReposRepository(getDatabase(application))

    val gitHubRepositories = gitHubReposRepository.gitHubRepositories

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)


    init {
        refreshDataFromRepository()
    }

    private fun refreshDataFromRepository() {
        coroutineScope.launch {
            try {
                gitHubReposRepository.refreshGitHubRepositories()
                Log.i("RepositoryListViewModel", "Success: repositories retrieved")

            } catch (e: Exception) {
                Log.i("RepositoryListViewModel", "Failure: ${e.message}")
            }
        }
    }

}