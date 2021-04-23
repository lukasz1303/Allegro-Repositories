package com.lukasz.allegrorepositories.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lukasz.allegrorepositories.domain.GitHubRepository

@Entity
data class DatabaseGitHubRepository constructor(
        @PrimaryKey
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
        val default_branch: String?)

fun List<DatabaseGitHubRepository>.asDomainModel(): List<GitHubRepository> {
    return map {
        GitHubRepository(
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
                default_branch =  it.default_branch)
    }
}