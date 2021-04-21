package com.lukasz.allegrorepositories.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.lukasz.allegrorepositories.database.GitHubRepositoriesDatabase
import com.lukasz.allegrorepositories.database.asDomainModel
import com.lukasz.allegrorepositories.domain.GitHubRepository
import com.lukasz.allegrorepositories.network.GitHubApi
import com.lukasz.allegrorepositories.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GitHubReposRepository(private val database: GitHubRepositoriesDatabase) {

    val gitHubRepositories: LiveData<List<GitHubRepository>> = Transformations.map(database.GithHubRepoitoryDao.getGitHubRepositories()) {
        it.asDomainModel()
    }

    suspend fun refreshGitHubRepositories() {
        withContext(Dispatchers.IO) {
            val gitHubRepositories = GitHubApi.retrofitServiceGitHub.getGitHubRepositories(1).await()
            database.GithHubRepoitoryDao.insertAll(gitHubRepositories.asDatabaseModel())

        }
    }
}