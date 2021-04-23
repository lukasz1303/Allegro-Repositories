package com.lukasz.allegrorepositories.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GitHubRepository(
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
) :Parcelable