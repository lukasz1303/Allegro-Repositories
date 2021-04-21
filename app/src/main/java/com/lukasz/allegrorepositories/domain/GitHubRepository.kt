package com.lukasz.allegrorepositories.domain

data class GitHubRepository(
        val name: String,
        val stargazers_count: Double?,
        val language: String?
)