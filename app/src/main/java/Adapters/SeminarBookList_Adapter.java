package Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;

import Models.SeminarBookListitem;
import creadigol.com.Meetto.MeettoApplication;
import creadigol.com.Meetto.R;


/**
 * Created by Creadigol on 12-09-2016.
 */
public class SeminarBookList_Adapter extends RecyclerView.Adapter<SeminarBookList_Adapter.MyViewHolder> {
    FragmentActivity a;
    Context context;
    ArrayList<SeminarBookListitem> yourlistItems;
    View.OnClickListener onClickListener;



    public SeminarBookList_Adapter(Context context, ArrayList<SeminarBookListitem> yourlistItems,    View.OnClickListener onClickListener
    ) {
        this.context = context;
        this.yourlistItems = yourlistItems;
        this.onClickListener=onClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custome_seminarbooklist_activity, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final SeminarBookListitem list = yourlistItems.get(position);

        holder.username.setText(list.getUser_name());
        holder.email.setText(list.getEmail());
        holder.totalseat.setText(list.getTotal_seat());
        Log.e("status ","=="+list.getStatus());
        if(list.getStatus().equalsIgnoreCase(" declined")){
            holder.status.setTextColor(Color.BLUE);
            holder.status.setText(list.getStatus());
        }else if(list.getStatus().equalsIgnoreCase("accepted")){
            holder.status.setTextColor(context.getResources().getColor(R.color.colorgreen));
            holder.status.setText(list.getStatus());
        }else{
            holder.status.setTextColor(Color.RED);
            holder.status.setText(list.getStatus());
        }
        MeettoApplication.getInstance().getImageLoader().displayImage(list.getUser_image(), holder.imvlist, getDisplayImageOptions());

    holder.listing.setTag(position);
    holder.listing.setOnClickListener(onClickListener);
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
        public TextView username,email,totalseat,status;
        public ImageView imvlist;
        public LinearLayout listing;

        public MyViewHolder(View v) {
            super(v);

            username = (TextView) v.findViewById(R.id.user_name);
            email = (TextView) v.findViewById(R.id.email);
            imvlist = (ImageView) v.findViewById(R.id.img_book);
            listing = (LinearLayout) v.findViewById(R.id.ll_Booklist);
            totalseat= (TextView) v.findViewById(R.id.total_seat);
            status= (TextView) v.findViewById(R.id.status);
        }


    }

    public void modifyDataSet(ArrayList<SeminarBookListitem> searchObjects) {
        this.yourlistItems = searchObjects;
        this.notifyDataSetChanged();
    }

}