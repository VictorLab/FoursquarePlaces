package helper;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import adapter.PlaceAdapter;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import listners.FindPlaceListiner;
import model.Explore.Group;
import model.Explore.Item_;
import model.Photo.Photos_;
import service.ApiUtil;
import victor.co.za.foursquareplaces.R;

/**
 * Created by Victor on 2018/04/20.
 */

public class PlaceHelper {

    private Activity activity;
    private ProgressBar progressBar;
    private int numberOfRequestVenue = 10;
    private String geoLocation;
    private List<Item_> itemList;
    private RecyclerView recycleViewPlaces;
    private Disposable disposable;
    private final String TAG = this.getClass().getSimpleName();
    private String category = "cafe";
    private LinearLayout queryWrapper;
    private FindPlaceListiner listiner;

    public PlaceHelper(Activity activity, FindPlaceListiner listiner) {
        this.activity = activity;
        this.listiner = listiner;
        init();
    }

    private void init() {

        recycleViewPlaces = activity.findViewById(R.id.recycleViewPlaces);
        progressBar = activity.findViewById(R.id.progressBar);
        queryWrapper = activity.findViewById(R.id.queryWrapper);

    }

    public void getPlaces() {
        progressBar.setVisibility(View.VISIBLE);
        queryWrapper.setVisibility(View.GONE);
        disposable = ApiUtil.getRetrofitService().requestExplore(
                Constants.CLIENT_ID,
                Constants.CLIENT_SECRET,
                Constants.API_VERSION,
                geoLocation,
                numberOfRequestVenue,
                category,
                1
        ).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(explore -> {

                    List<Group> groupList = explore.getResponse().getGroups();

                    itemList = groupList.get(0).getItems();

                    getPhoto(itemList);

                }, throwable -> {
                    progressBar.setVisibility(View.GONE);
                    listiner.onFindPlaceError();
                    Log.e(TAG, throwable.getMessage());
                });
    }


    /**
     * @param items
     */
    private void getPhoto(List<Item_> items) {
        Observable.fromIterable(items)
                .flatMap(item_ -> {
                    return Observable.zip(
                            Observable.just(item_.getVenue().getId()),
                            ApiUtil.getRetrofitService().requestPhotos(
                                    item_.getVenue().getId(),
                                    Constants.CLIENT_ID,
                                    Constants.CLIENT_SECRET,
                                    Constants.API_VERSION),
                            (s, photos) -> {

                                Photos_ photos_ = photos.getResponse().getPhotos();

                                int numberOfPhotos = photos_.getCount();

                                item_.getVenue().getPhotos().setCount(numberOfPhotos);

                                ArrayList<String> photoUrlList = new ArrayList<>();

                                numberOfPhotos = (numberOfPhotos < 9) ? numberOfPhotos : 9;

                                for (int j = 0; j < numberOfPhotos; j++) {
                                    String prefix = photos_.getItems().get(j).getPrefix();
                                    String suffix = photos_.getItems().get(j).getSuffix();
                                    String imageThumbnailUrl = prefix + "150x150" + suffix;
                                    Log.v("imageThumbnailUrl", imageThumbnailUrl);
                                    photoUrlList.add(imageThumbnailUrl);
                                    item_.setPhotoUrl(photoUrlList);
                                }

                                return photos;
                            }
                    );
                })
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((objects, throwable) -> {
                    // Images

                    if (itemList != null) {

                        PlaceAdapter adapter = new PlaceAdapter(activity, itemList);
                        recycleViewPlaces.setAdapter(adapter);
                        progressBar.setVisibility(View.GONE);
                        queryWrapper.setVisibility(View.VISIBLE);
                        listiner.onFindPlaceSuccess();

                    }
                });

    }

    public void dispose() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    public void setGeoLocation(String geoLocation) {
        this.geoLocation = geoLocation;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
