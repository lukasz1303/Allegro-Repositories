package com.lukasz.allegrorepositories.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


private const val BASE_URL = "https://api.github.com/"
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

interface GitHubApiService {
    @GET("orgs/allegro/repos?per_page=100")
    fun getGitHubRepositories(@Query("page") currentPage: Int): Deferred<List<NetworkGitHubRepository>>

    @GET("repos/allegro/{name}/languages")
    fun getAllLanguages(@Path("name")name :String): Deferred<Map<String?, Double?>?>
}

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

object GitHubApi {
    val retrofitServiceGitHub: GitHubApiService = retrofit.create(GitHubApiService::class.java)
}