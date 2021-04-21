package com.lukasz.allegrorepositories.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lukasz.allegrorepositories.domain.GitHubRepository

@Entity
data class DatabaseGitHubRepository constructor(
        @PrimaryKey
        val name: String,
        val stargazers_count: Double?,
        val language: String?)

fun List<DatabaseGitHubRepository>.asDomainModel(): List<GitHubRepository> {
    return map {
        GitHubRepository(
                name = it.name,
                stargazers_count = it.stargazers_count,
                language = it.language)
    }
}