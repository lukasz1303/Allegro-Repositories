package com.lukasz.allegrorepositories.network

import com.lukasz.allegrorepositories.database.DatabaseGitHubRepository
import com.lukasz.allegrorepositories.domain.GitHubRepository

class NetworkGitHubRepository(
    val name: String,
    val stargazers_count: Double?,
    val language: String?
)

fun List<NetworkGitHubRepository>.asDatabaseModel(): List<DatabaseGitHubRepository> {
    return map {
        DatabaseGitHubRepository(
            name = it.name,
            stargazers_count = it.stargazers_count,
            language = it.language
        )
    }
}

