package com.lukasz.allegrorepositories.repositorylist

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.lukasz.allegrorepositories.database.getDatabase
import com.lukasz.allegrorepositories.domain.GitHubRepository
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

    private val _repositoryEmpty = MutableLiveData<Boolean>()
    val repositoryEmpty: LiveData<Boolean>
        get() = _repositoryEmpty

    private val _navigateToSelectedRepository = MutableLiveData<GitHubRepository>()
    val navigateToSelectedRepository: LiveData<GitHubRepository>
        get() = _navigateToSelectedRepository

    fun displayRepositoryDetails(gitHubRepository: GitHubRepository) {
        _navigateToSelectedRepository.value = gitHubRepository
    }

    fun displayRepositoryDetailsComplete() {
        _navigateToSelectedRepository.value = null
    }

    fun setSearchedName(name: String) {
        gitHubReposRepository.setSearchedName(name)
    }

    fun setOrder(order: Int) {
        gitHubReposRepository.setOrder(order)
    }


    init {
        _repositoryEmpty.value = false
        val job = Job()
        CoroutineScope(job + Dispatchers.Main).launch {
            val result = refreshDataFromRepository()
            if (result == 1) {
                if (gitHubRepositories.value?.size ?: 0 > 0){
                    _repositoryEmpty.value = false
                }
            }
            else if (result == 2) {
                _repositoryEmpty.value = gitHubRepositories.value?.size ?: 0 <= 0
            }
        }

    }

    suspend fun refreshDataFromRepository(): Int {
        var res = 0
        coroutineScope.launch {
            res = try {
                gitHubReposRepository.refreshGitHubRepositories()
                if (gitHubRepositories.value?.size ?: 0 > 0){
                    _repositoryEmpty.value = false
                }
                Log.i("RepositoryListViewModel", "Success: repositories refreshed")
                1

            } catch (e: Exception) {
                Log.i("RepositoryListViewModel", "Refresh Failure: ${e.message}")
                2
            }
        }.join()
        return res
    }
}