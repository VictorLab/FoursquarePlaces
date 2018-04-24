package helper;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import listners.VenueCategoryListiner;
import victor.co.za.foursquareplaces.R;

/**
 * This class create the category layout the we later add to out MainActivity
 * Created by Victor on 2018/04/20.
 */


public class PlaceCategoryBuilder {

    private Activity activity;
    private boolean[] categoryList;
    private LinearLayout categoryContent;
    private String mCategory;

    private VenueCategoryListiner listiner;

    public PlaceCategoryBuilder(Activity activity, VenueCategoryListiner listiner) {
        this.activity = activity;
        this.listiner = listiner;
    }

    /**
     *
     * @return
     */
    public View createCategoryType() {

        LayoutInflater inflater = LayoutInflater.from(activity);
        categoryContent = (LinearLayout) inflater.inflate(
                R.layout.query_layout, null, false);

        final String[] queryType = activity.getResources().getStringArray(R.array.query_array);
        categoryList = new boolean[queryType.length];
        for (int i = 0; i < categoryList.length; i++) {

            final int index = i;
            final LinearLayout categoryLayout = getCategoryLayout(index);
            if (index == 0) {
                deactivateCategory(index, categoryLayout, false);
            } else {
                activateCategory(index, categoryLayout, false);
            }

            categoryLayout.setOnClickListener(view -> {
                for (int k = 0; k < queryType.length; k++) {
                    if (k == index) {
                        deactivateCategory(index, getCategoryLayout(index), true);
                        mCategory = queryType[index];
                        mCategory = mCategory.replace(" ", "").toLowerCase();
                        listiner.onVenueCategoryClick(mCategory);
                    } else {
                        activateCategory(k, getCategoryLayout(k), true);
                    }
                }

            });

            final TextView queryText = categoryLayout.findViewById(R.id.textViewQuery);
            queryText.setText(queryType[index]);
            mCategory = queryType[index];
        }
        return categoryContent;
    }


    private boolean checkQueries() {
        boolean thereIsAtLeastCategorySelected = false;
        for (int i = 0; i < categoryList.length && !thereIsAtLeastCategorySelected; i++) {
            if (categoryList[i]) {
                thereIsAtLeastCategorySelected = true;
            }
        }

        return thereIsAtLeastCategorySelected;
    }


    /**
     * deactivate the category button
     * @param index
     * @param categoryLayout
     * @param check
     */
    private void deactivateCategory(int index, LinearLayout categoryLayout, boolean check) {

        categoryList[index] = false;

        categoryLayout.setTag(false);

        categoryLayout.setBackgroundResource(R.drawable.xblue_grey_corners);

        TextView categoryText = categoryLayout.findViewById(R.id.textViewQuery);
        // int colour = ContextCompat.getColor(activity, R.color.black);
        categoryText.setTextColor(Color.BLACK);

        if (check) {
            checkQueries();
        }
    }

    /**
     * activate category button
     * @param index
     * @param categoryLayout
     * @param check
     */
    private void activateCategory(int index, LinearLayout categoryLayout, boolean check) {

        categoryList[index] = true;

        categoryLayout.setTag(true);

        categoryLayout.setBackgroundResource(R.drawable.xprimary_corners);

        TextView queryText = categoryLayout.findViewById(R.id.textViewQuery);
        // int color = ContextCompat.getColor(activity, R.color.);
        queryText.setTextColor(Color.WHITE);

        if (check) {
            checkQueries();
        }
    }

    /**
     *
     * @param i
     * @return
     */
    private LinearLayout getCategoryLayout(int i) {
        int id = categoryContent.getResources().getIdentifier(
                "query_" + i, "id", activity.getPackageName());
        return (LinearLayout) categoryContent.findViewById(id);
    }

    public String getmQuery() {
        return mCategory;
    }

    public void setmQuery(String mQuery) {
        this.mCategory = mQuery;
    }
}
