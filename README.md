# Allegro-Repositories
App for displaying list of allegro repository on GitHub.

# Launch
To launch the app install allegro-repositories.apk on physical device with Android version 5.0 (API level 21) or higher or run application in Android Studio emulator.

# Comments
Data are downloaded from GitHubApi and stored through repository in Room Database. List of Repositories uses a RecyclerView with clickListener to navigate to detail fragment where are displayed all details. There also are downloaded used programming languages for current repository. In the main fragment in the menu is the possibility to sort repositories in order of number of stars, date of last commit or alphabetical. In addition, on an action bar is SearchView to find a specific repository.
The application assumed that there's no limit in requests to GitHubApi, and it didn't resolve different responses from the server when error occurs. When the repository will be deleted on GitHub, or it was made private, it won't be deleted in the app up to time when storage of the app will be cleared.

# Future improvements
* Add support for lower Android API levels
* Handle different error responses from server and take appropriate action
* Delete repositories from database which are no longer available in GitHubApi


#Screenshots
![Alt text](/screenshots/repository_list.png?raw=true) &nbsp; &nbsp;![Alt text](/screenshots/detail_fragment.png?raw=true)  
![Alt text](/screenshots/searching.png?raw=true) &nbsp; &nbsp;![Alt text](/screenshots/not_found.png?raw=true)  