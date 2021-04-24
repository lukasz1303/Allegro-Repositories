package com.lukasz.allegrorepositories.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.lukasz.allegrorepositories.database.GitHubRepositoriesDatabase
import com.lukasz.allegrorepositories.database.asDomainModel
import com.lukasz.allegrorepositories.domain.GitHubRepository
import com.lukasz.allegrorepositories.network.GitHubApi
import com.lukasz.allegrorepositories.network.NetworkGitHubRepository
import com.lukasz.allegrorepositories.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GitHubReposRepository(private val database: GitHubRepositoriesDatabase) {

    private var _searchedName = MutableLiveData<String>()

    fun setSearchedName(name: String) {
        _searchedName.value = name
    }

    init {
        _searchedName.value = ""
    }

    val gitHubRepositories: LiveData<List<GitHubRepository>> = Transformations.switchMap(_searchedName) {
        Transformations.map(database.GithHubRepoitoryDao.getGitHubRepositories(_searchedName.value!!)) {
            it.asDomainModel()
        }
    }
    suspend fun refreshGitHubRepositories() {
        withContext(Dispatchers.IO) {
            var page = 1
            do {
                val gitHubRepositories =
                    GitHubApi.retrofitServiceGitHub.getGitHubRepositories(page).await()
                database.GithHubRepoitoryDao.insertAll(gitHubRepositories.asDatabaseModel())
                page++
            } while (gitHubRepositories.isNotEmpty())
        }
    }

    suspend fun refreshGitHubRepositoryLanguage(name: String): String {
        var languagesString = ""
        withContext(Dispatchers.IO) {
            val languages = GitHubApi.retrofitServiceGitHub.getAllLanguages(name).await()
            if (languages != null) {
                for (i in languages) {
                    languagesString += i.key + ":" + i.value + ","
                }
                Log.i("Refresh", languagesString)
                database.GithHubRepoitoryDao.updateLanguages(name, languagesString)
            }
        }
        return languagesString
    }
}