package com.lukasz.allegrorepositories.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.lukasz.allegrorepositories.database.GitHubRepositoriesDatabase.Companion.MIGRATION_1_2
import com.lukasz.allegrorepositories.database.GitHubRepositoriesDatabase.Companion.MIGRATION_2_3


@Dao
interface GithHubRepoitoryDao {
    @Query("select * from databasegithubrepository where name like '%' || :name || '%' order by CASE WHEN :order = 1 THEN pushed_at END DESC, CASE WHEN :order = 0 THEN stargazers_count END DESC, CASE WHEN :order = 2 THEN lower(name) END")
    fun getGitHubRepositories(name: String, order: Int): LiveData<List<DatabaseGitHubRepository>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(videos: List<DatabaseGitHubRepository>)

    @Query("UPDATE DatabaseGitHubRepository SET allLanguages = :languages WHERE name = :name")
    fun updateLanguages(name: String, languages: String?): Int
}

@Database(entities = [DatabaseGitHubRepository::class], version = 3)
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
        val MIGRATION_2_3: Migration = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "ALTER TABLE databasegithubrepository "
                            + " ADD COLUMN allLanguages TEXT"
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
                .addMigrations(MIGRATION_2_3)
                .build()
        }
    }
    return INSTANCE
}