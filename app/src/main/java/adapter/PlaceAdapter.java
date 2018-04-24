package adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import model.Explore.Item_;
import model.Explore.Venue;
import victor.co.za.foursquareplaces.PhotosActivity;
import victor.co.za.foursquareplaces.R;


/**
 * Created by Victor on 2018/04/20.
 */


public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.ViewHolder> {

    private List<Item_> mItemList;
    private Context mContext;
    private String userPhotoUrl;
    private String tip;

    public PlaceAdapter(Context mContext, List<Item_> mItemList) {
        this.mContext = mContext;
        this.mItemList = mItemList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.place_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Item_ items = mItemList.get(position);

        final Venue venue = items.getVenue();

        Integer distance = venue.getLocation().getDistance();
        distance = distance / 80;

        if (items.getTips().size() != 0) {
            tip = items.getTips().get(0).getText();
            userPhotoUrl = items.getTips().get(0).getUser().getPhoto().getPrefix() + "80x80" + items.getTips().get(0).getUser().getPhoto().getSuffix();
        }

        if (venue.getPhotos().getCount()  ==  0) {
            holder.buttonPhotos.setEnabled(false);
        }else {
            holder.buttonPhotos.setEnabled(true);
        }

        if (venue.getPhotos().getCount()  > 1) {
            holder.buttonPhotos.setText(" "+String.valueOf(venue.getPhotos().getCount() + " Photos"));
        } else {
            holder.buttonPhotos.setText(" "+ String.valueOf(venue.getPhotos().getCount() + " Photo"));
        }

        String openStatus = "No value";
        if (venue.getHours() != null) {
            boolean isOpen = venue.getHours().getIsOpen();
            openStatus = "Closed";
            holder.textViewOpenNow.setTextColor(Color.RED);
            if (isOpen) {
                openStatus = "Open";
                holder.textViewOpenNow.setTextColor(Color.BLUE);
            }
        }

        holder.textViewOpenNow.setText(openStatus);

        holder.textViewPlaceName.setText(venue.getName());

        Double venueRating = venue.getRating();
        String category = venue.getCategories().get(0).getName();

        if (venueRating != null) {
            holder.ratingPlace.setRating(Float.parseFloat(String.valueOf(venueRating)));
        }

        holder.textViewCategory.setText(category);

        if (tip != null) {
            holder.textViewTip.setText(tip);
            if (!(userPhotoUrl == null)) {
                Glide.with(mContext)
                        .load(userPhotoUrl)
                        .into(holder.imageViewUser);
            }
        }

        String prefix = venue.getCategories().get(0).getIcon().getPrefix();
        String suffix = venue.getCategories().get(0).getIcon().getSuffix();

        Log.i("ImageLocation ", prefix + suffix.replace(".png","32.png"));

        String iconUrl = prefix + suffix.replace(".png","32.png");

        Glide.with(mContext)
                .load(iconUrl)
                .into(holder.imageViewIcon);


        holder.buttonPhotos.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, PhotosActivity.class);
            intent.putStringArrayListExtra("PhotoUrl", (ArrayList<String>) items.getPhotoUrl());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);

        });

        holder.textViewDistance.setText(String.valueOf(distance)+" min");
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewPlaceName,
                textViewCategory,
                textViewOpenNow,
                textViewDistance,
                textViewTip;

        ImageView imageViewIcon,
                imageViewUser;

        RatingBar ratingPlace;
        Button buttonPhotos;

        public ViewHolder(View itemView) {
            super(itemView);

            textViewPlaceName = itemView.findViewById(R.id.textViewPlaceName);
            textViewCategory = itemView.findViewById(R.id.textViewCategory);
            textViewOpenNow = itemView.findViewById(R.id.textViewOpenNow);
            textViewDistance = itemView.findViewById(R.id.textViewDistance);
            textViewTip = itemView.findViewById(R.id.textViewTip);

            imageViewIcon = itemView.findViewById(R.id.imageViewIcon);
            imageViewUser = itemView.findViewById(R.id.imageViewUser);
            ratingPlace = itemView.findViewById(R.id.ratingBarPlace);

            buttonPhotos = itemView.findViewById(R.id.buttonPhotos);

        }
    }
}
