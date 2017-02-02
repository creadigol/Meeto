package Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;

import Models.Wishlist_item;
import Utils.PreferenceSettings;
import creadigol.com.Meetto.MeettoApplication;
import creadigol.com.Meetto.R;


/**
 * Created by Creadigol on 12-09-2016.
 */
public class Wishlist_Adapter extends RecyclerView.Adapter<Wishlist_Adapter.MyViewHolder> {

    PreferenceSettings mPreferenceSettings;
    String seminarid;
    Context context;
    ArrayList<Wishlist_item> wishlist_items;
    View.OnClickListener onClickListener;
    View.OnLongClickListener onLongClickListener;

    public Wishlist_Adapter(Context context, ArrayList<Wishlist_item> yourlistItems, View.OnClickListener onClickListener, View.OnLongClickListener onLongClickListener) {
        this.context = context;
        this.wishlist_items = yourlistItems;
        this.onClickListener = onClickListener;
        this.onLongClickListener = onLongClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custome_wishlist_activity, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        mPreferenceSettings = MeettoApplication.getInstance().getPreferenceSettings();
        return myViewHolder;

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        final Wishlist_item list = wishlist_items.get(position);

        holder.tv_name.setText(list.getSeminar_name());
        holder.tv_date.setText(list.getDate());

        if (list.getBooked_seat() != null && list.getTotal_seat() != null) {
            holder.tv_seats.setText(list.getBooked_seat() + "/" + list.getTotal_seat());
        } else {
            holder.tv_seats.setVisibility(View.GONE);
        }

        MeettoApplication.getInstance().getImageLoader().displayImage(list.getSeminar_image(), holder.imvlist, getDisplayImageOptions());

        holder.viewlist.setTag(position);
        holder.viewlist.setOnClickListener(onClickListener);

        holder.viewlist.setOnLongClickListener(onLongClickListener);
    }

    public DisplayImageOptions getDisplayImageOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisk(true).resetViewBeforeLoading(true)
                .showImageForEmptyUri(R.drawable.nophoto)
                .showImageOnFail(R.drawable.nophoto)
                .showImageOnLoading(R.drawable.nophoto)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new RoundedBitmapDisplayer(0))
                .build();
        return options;
    }

    @Override
    public int getItemCount() {
        return wishlist_items.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_name, tv_date, tv_seats;
        public ImageView imvlist;
        public CardView viewlist;

        public MyViewHolder(View v) {
            super(v);
            tv_name = (TextView) v.findViewById(R.id.tv_wishlistname);
            tv_date = (TextView) v.findViewById(R.id.tv_wishlistdate);
            tv_seats = (TextView) v.findViewById(R.id.tv_seats);
            imvlist = (ImageView) v.findViewById(R.id.img_wishlist_image);
            viewlist = (CardView) v.findViewById(R.id.viewwishlist);

        }


    }

    public void modifyDataSet(ArrayList<Wishlist_item> searchObjects) {
        this.wishlist_items = searchObjects;
        this.notifyDataSetChanged();
    }

}