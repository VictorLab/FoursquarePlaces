package adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import victor.co.za.foursquareplaces.FullViewPhotoActivity;
import victor.co.za.foursquareplaces.R;

/**
 * Created by Victor on 2018/04/19.
 */

public class PhotoAdapter  extends ArrayAdapter<String> {

    private Context mContext;
    private int layoutResourceId;
    private ArrayList<String> photos = new ArrayList<>();

    public PhotoAdapter(Context mContext, int layoutResourceId, ArrayList<String> photos) {
        super(mContext, layoutResourceId, photos);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.photos = photos;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        final ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
          //  holder.titleTextView = (TextView) row.findViewById(R.id.grid_item_title);
            holder.imageView = (ImageView) row.findViewById(R.id.imageViewPhoto);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        final String item = photos.get(position);

        Glide.with(mContext)
                .load(item)
                .into((holder.imageView));

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), FullViewPhotoActivity.class);

                int[] screenLocation = new int[2];
                holder.imageView.getLocationOnScreen(screenLocation);

                //Pass the image title and url to DetailsActivity
                intent.putExtra("left", screenLocation[0]).
                        putExtra("top", screenLocation[1]).
                        putExtra("width", holder.imageView.getWidth()).
                        putExtra("height", holder.imageView.getHeight()).
                        putExtra("photo", photos.get(position));

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);
            }
        });

        return row;
    }

    static class ViewHolder {
        TextView titleTextView;
        ImageView imageView;
    }
}
