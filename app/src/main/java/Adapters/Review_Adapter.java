package Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;

import Models.Review_items;
import Utils.PreferenceSettings;
import creadigol.com.Meetto.MeettoApplication;
import creadigol.com.Meetto.R;


/**
 * Created by Creadigol on 12-09-2016.
 */
public class Review_Adapter extends RecyclerView.Adapter<Review_Adapter.MyViewHolder> {

    PreferenceSettings mPreferenceSettings;
    Context context;
    ArrayList<Review_items> reviewItemses;

    public Review_Adapter(Context context, ArrayList<Review_items> review_itemse) {
        this.context = context;
        this.reviewItemses = review_itemse;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custome_review, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        mPreferenceSettings = MeettoApplication.getInstance().getPreferenceSettings();
        return myViewHolder;

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        final Review_items list = reviewItemses.get(position);
        if (list.getType().equalsIgnoreCase("your_review")) {
            holder.name.setVisibility(View.GONE);
        } else if (list.getType().equalsIgnoreCase("user_review")) {
            holder.name.setVisibility(View.VISIBLE);
            holder.tv_username.setText(list.getUser_name());
        }
        holder.tv_name.setText(list.getSeminar_name());
        holder.tv_revew.setText(list.getReview());

        MeettoApplication.getInstance().getImageLoader().displayImage(list.getSeminar_pic(), holder.imvlist, getDisplayImageOptions());

        holder.viewlist.setTag(position);
//        holder.viewlist.setOnClickListener(onClickListener);
//
//        holder.viewlist.setOnLongClickListener(onLongClickListener);
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
        return reviewItemses.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_name, tv_username, tv_revew, username;
        public ImageView imvlist;
        public LinearLayout name;
        public CardView viewlist;

        public MyViewHolder(View v) {
            super(v);
            name = (LinearLayout) v.findViewById(R.id.name);
            tv_name = (TextView) v.findViewById(R.id.t_seminarname);
            username = (TextView) v.findViewById(R.id.username);
            imvlist = (ImageView) v.findViewById(R.id.img_review);
            tv_username = (TextView) v.findViewById(R.id.tv_revew_username);
            tv_revew = (TextView) v.findViewById(R.id.tv_revew);
            viewlist = (CardView) v.findViewById(R.id.cv_review);

        }


    }

    public void modifyDataSet(ArrayList<Review_items> searchObjects) {
        this.reviewItemses = searchObjects;
        this.notifyDataSetChanged();
    }

}