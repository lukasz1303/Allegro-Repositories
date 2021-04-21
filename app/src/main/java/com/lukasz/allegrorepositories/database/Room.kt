package com.lukasz.allegrorepositories.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface GithHubRepoitoryDao {
    @Query("select * from databasegithubrepository")
    fun getGitHubRepositories(): LiveData<List<DatabaseGitHubRepository>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll( videos: List<DatabaseGitHubRepository>)

}

@Database(entities = [DatabaseGitHubRepository::class], version = 1)
abstract class GitHubRepositoriesDatabase: RoomDatabase() {
    abstract val GithHubRepoitoryDao: GithHubRepoitoryDao
}

private lateinit var INSTANCE: GitHubRepositoriesDatabase

fun getDatabase(context: Context): GitHubRepositoriesDatabase {
    synchronized(GitHubRepositoriesDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                GitHubRepositoriesDatabase::class.java,
                "GitHubRepositories"
            ).build()
        }
    }
    return INSTANCE
}