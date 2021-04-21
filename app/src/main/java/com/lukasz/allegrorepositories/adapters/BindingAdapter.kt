package com.lukasz.allegrorepositories.adapters

import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lukasz.allegrorepositories.domain.GitHubRepository
import java.math.BigDecimal
import java.math.MathContext
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import kotlin.math.ceil
import kotlin.math.log10

@BindingAdapter("repositoryList")
fun bindRecyclerView(recyclerView: RecyclerView, gitHubRepositories: List<GitHubRepository>?) {
    val adapter = recyclerView.adapter as GitHubRepositoryAdapter
    adapter.submitList(gitHubRepositories)
}

@BindingAdapter("stargazersCount")
fun reformatStarGazersCount(textView: TextView, stargazers_count: Double?) {
    val suffix: String
    var roundedStargazersCount: Double = stargazers_count ?: 0.0
    when (stargazers_count?.toInt()){
        in 0..999 -> suffix = ""
        in 1000..999999 -> {
            suffix = "k"
            roundedStargazersCount /= 1000.0
        }
        in 1000000..999999999 -> {
            suffix = "M"
            roundedStargazersCount /= 1000000.0
        }
        else -> {
            suffix = "B"
            roundedStargazersCount /= 1000000000.0
        }
    }

    if (roundedStargazersCount.toInt().compareTo(roundedStargazersCount) == 0){
        val text = String.format("%.0f", roundedStargazersCount) + suffix
        textView.text = text
    } else {
        when (ceil(log10(roundedStargazersCount)).toInt()) {
            1 -> {
                val text = String.format("%.2f", roundedStargazersCount) + suffix
                textView.text = text
            }
            else -> {
                val text = String.format("%.1f", roundedStargazersCount) + suffix
                textView.text = text
            }
        }
    }
}