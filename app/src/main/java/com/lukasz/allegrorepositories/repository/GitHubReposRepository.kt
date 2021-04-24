package com.lukasz.allegrorepositories.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
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

    private var _order = MutableLiveData<Int>()

    fun setOrder(order: Int) {
        _order.value = order
    }

    init {
        _searchedName.value = ""
        _order.value = 1
    }


    private val combinedValues = MediatorLiveData<Pair<String?, Int?>>().apply {
        addSource(_searchedName) {
            value = Pair(it, _order.value)
        }
        addSource(_order) {
            value = Pair(_searchedName.value, it)
        }
    }


    val gitHubRepositories: LiveData<List<GitHubRepository>> = Transformations.switchMap(combinedValues) { pair ->
        Transformations.map(database.GithHubRepoitoryDao.getGitHubRepositories(pair.first!!, pair.second!!)) {
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