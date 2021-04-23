package com.lukasz.allegrorepositories.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.lukasz.allegrorepositories.database.GitHubRepositoriesDatabase.Companion.MIGRATION_1_2


@Dao
interface GithHubRepoitoryDao {
    @Query("select * from databasegithubrepository")
    fun getGitHubRepositories(): LiveData<List<DatabaseGitHubRepository>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll( videos: List<DatabaseGitHubRepository>)

}

@Database(entities = [DatabaseGitHubRepository::class], version = 2)
abstract class GitHubRepositoriesDatabase: RoomDatabase() {
    abstract val GithHubRepoitoryDao: GithHubRepoitoryDao

    companion object {
        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "ALTER TABLE databasegithubrepository "
                            + " ADD COLUMN description TEXT"
                )
                database.execSQL(
                    "ALTER TABLE databasegithubrepository "
                            + " ADD COLUMN html_url TEXT"
                )
                database.execSQL(
                    "ALTER TABLE databasegithubrepository "
                            + " ADD COLUMN created_at TEXT"
                )
                database.execSQL(
                    "ALTER TABLE databasegithubrepository "
                            + " ADD COLUMN updated_at TEXT"
                )
                database.execSQL(
                    "ALTER TABLE databasegithubrepository "
                            + " ADD COLUMN pushed_at TEXT"
                )
                database.execSQL(
                    "ALTER TABLE databasegithubrepository "
                            + " ADD COLUMN homepage TEXT"
                )
                database.execSQL(
                    "ALTER TABLE databasegithubrepository "
                            + " ADD COLUMN forks REAL"
                )
                database.execSQL(
                    "ALTER TABLE databasegithubrepository "
                            + " ADD COLUMN open_issues REAL"
                )
                database.execSQL(
                    "ALTER TABLE databasegithubrepository "
                            + " ADD COLUMN default_branch TEXT"
                )
            }
        }
    }
}

private lateinit var INSTANCE: GitHubRepositoriesDatabase

fun getDatabase(context: Context): GitHubRepositoriesDatabase {
    synchronized(GitHubRepositoriesDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                GitHubRepositoriesDatabase::class.java,
                "GitHubRepositories"
            ).addMigrations(MIGRATION_1_2)
                .build()
        }
    }
    return INSTANCE
}