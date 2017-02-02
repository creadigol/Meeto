package Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;

import Models.Yourlist_item;
import creadigol.com.Meetto.MeettoApplication;
import creadigol.com.Meetto.R;


/**
 * Created by Creadigol on 12-09-2016.
 */
public class YourListing_Adapter extends RecyclerView.Adapter<YourListing_Adapter.MyViewHolder> {
    FragmentActivity a;
    Context context;
    ArrayList<Yourlist_item> yourlistItems;
    View.OnClickListener onClickListener;
    View .OnClickListener onclickdelete;
    View .OnClickListener onclickshow;

    public YourListing_Adapter(Context context, ArrayList<Yourlist_item> yourlistItems, View.OnClickListener onClickListener,  View .OnClickListener onclickshow,View .OnClickListener onclickdelete) {
        this.context = context;
        this.yourlistItems = yourlistItems;
        this.onClickListener=onClickListener;
        this.onclickdelete = onclickdelete;
        this.onclickshow= onclickshow;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custome_listing_activity, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Yourlist_item list = yourlistItems.get(position);

        holder.seminar_name.setText(list.getSeminar_name());
        holder.tagline.setText(list.getTagline());

        MeettoApplication.getInstance().getImageLoader().displayImage(list.getSeminar_image(), holder.imvlist, getDisplayImageOptions());

        holder.btn_edit.setTag(position);
        holder.btn_edit.setOnClickListener(onClickListener);
        holder.btn_show.setTag(position);
        holder.btn_show.setOnClickListener(onclickshow);
        holder.btn_delete.setTag(position);
        holder.btn_delete.setOnClickListener(onclickdelete);


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
        return yourlistItems.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView seminar_name, tagline;
        public Button btn_edit,btn_delete,btn_show;
        public ImageView imvlist;
        public LinearLayout listing;

        public MyViewHolder(View v) {
            super(v);

            seminar_name = (TextView) v.findViewById(R.id.seminar_name);
            tagline = (TextView) v.findViewById(R.id.tv_tagline);
            imvlist = (ImageView) v.findViewById(R.id.img_list_image);
            listing = (LinearLayout) v.findViewById(R.id.ll_listing);
            btn_edit = (Button) v.findViewById(R.id.buttonedit);
            btn_delete = (Button) v.findViewById(R.id.buttondelet);
            btn_show= (Button) v.findViewById(R.id.buttonshow);
        }


    }

    public void modifyDataSet(ArrayList<Yourlist_item> searchObjects) {
        this.yourlistItems = searchObjects;
        this.notifyDataSetChanged();
    }

}