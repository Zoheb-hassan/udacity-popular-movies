package movies.nano.udacity.com.udacitypopularmovies.utility;

import movies.nano.udacity.com.udacitypopularmovies.BuildConfig;

/**
 * Created by Zoheb Syed on 23-12-2015.
 */
public interface RequestConstants {

    final String apiBasePath = "http://api.themoviedb.org/3/discover/movie";
    final String sortBy = "sort_by";
    final String popDesc = "popularity.desc";
    final String voteDesc = "vote_average.desc";
    final String apiKey = "api_key";
    final String key = BuildConfig.APIKEY;

    final String posterBasePath = "http://image.tmdb.org/t/p/";
    final String imageResolution = "w342";


}
