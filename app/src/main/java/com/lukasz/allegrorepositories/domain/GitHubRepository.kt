package com.lukasz.allegrorepositories.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GitHubRepository(
        val name: String,
        val stargazers_count: Double?,
        val language: String?
) :Parcelable