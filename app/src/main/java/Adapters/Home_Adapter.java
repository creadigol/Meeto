package Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;

import Models.HomeObject;
import Utils.PreferenceSettings;
import creadigol.com.Meetto.MeettoApplication;
import creadigol.com.Meetto.R;
import creadigol.com.Meetto.SeminarList_Activity;

/**
 * Created by Creadigol on 07-09-2016.
 */
public class Home_Adapter extends RecyclerView.Adapter<Home_Adapter.OutletViewHolder> {

    public Context context;
    View view;
    private ArrayList<HomeObject> cityListItems;
    public PreferenceSettings mPreferenceSettings;

    public Home_Adapter(Context context, ArrayList<HomeObject> cityListItems) {
        this.context = context;
        this.cityListItems = cityListItems;

    }

    @Override
    public OutletViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.grid_item, parent, false);
        mPreferenceSettings = MeettoApplication.getInstance().getPreferenceSettings();
        OutletViewHolder outletViewHolder = new OutletViewHolder(view);

        return outletViewHolder;
    }

    @Override
    public void onBindViewHolder(OutletViewHolder holder, int position) {
        final HomeObject model = cityListItems.get(position);

        // bitmap
        // setting title
        holder.tvcityName.setText(model.getCityname());
//        holder.ivimage.setImageResource(R.drawable.purplecity);
        MeettoApplication.getInstance().getImageLoader().displayImage(model.getCityimage(), holder.ivimage, getDisplayImageOptions());

        //** holder.cvOutlet.setTag(position);
        holder.rv_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent I = new Intent(context, SeminarList_Activity.class);
                I.putExtra("cityid", model.getCityid());
                I.putExtra("cityname",model.getCityname());
                Log.e("cityidadapter", "" + model.getCityid());
                context.startActivity(I);
            }
        });
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
           return cityListItems.size();
    }

    public class OutletViewHolder extends RecyclerView.ViewHolder {

        public TextView tvcityName;
        public ImageView ivimage;
        public RelativeLayout rv_city;

        public OutletViewHolder(View itemView) {
            super(itemView);

            rv_city= (RelativeLayout) itemView.findViewById(R.id.rv_city);
            tvcityName = (TextView) itemView.findViewById(R.id.tv_cityname);
            ivimage = (ImageView) itemView.findViewById(R.id.iv_cityimage);
        }
    }


}
