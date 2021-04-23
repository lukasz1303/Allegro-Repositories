package com.lukasz.allegrorepositories.network

import com.lukasz.allegrorepositories.database.DatabaseGitHubRepository
import com.lukasz.allegrorepositories.domain.GitHubRepository

class NetworkGitHubRepository(
    val name: String,
    val stargazers_count: Double?,
    val language: String?,
    val description: String?,
    val html_url: String?,
    val created_at: String?,
    val updated_at: String?,
    val pushed_at: String?,
    val homepage: String?,
    val forks: Double?,
    val open_issues: Double?,
    val default_branch: String?
)

fun List<NetworkGitHubRepository>.asDatabaseModel(): List<DatabaseGitHubRepository> {
    return map {
        DatabaseGitHubRepository(
            name = it.name,
            stargazers_count = it.stargazers_count,
            language = it.language,
            description = it.description,
            html_url= it.html_url,
            created_at = it.created_at,
            updated_at = it.updated_at,
            pushed_at = it.pushed_at,
            homepage = it.homepage,
            forks = it.forks,
            open_issues = it.open_issues,
            default_branch =  it.default_branch
        )
    }
}

