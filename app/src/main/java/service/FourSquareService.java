package service;


import io.reactivex.Observable;
import model.Explore.Explore;
import model.Photo.Photos;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Victor on 2018/04/20.
 */

public interface FourSquareService {

	@GET("venues/explore/")
    Observable<Explore> requestExplore(
            @Query("client_id") String client_id,
            @Query("client_secret") String client_secret,
            @Query("v") String v,
            @Query("ll") String ll,
            @Query("limit") int limit,
            @Query("query") String query,
            @Query("sortByDistance") int sortByDistance);

	@GET("venues/{venue_id}/photos/")
    Observable<Photos> requestPhotos(
            @Path("venue_id") String venue_id,
            @Query("client_id") String client_id,
            @Query("client_secret") String client_secret,
            @Query("v") String v);

}
