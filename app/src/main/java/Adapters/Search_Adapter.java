package Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import Models.Searchitem;
import creadigol.com.Meetto.R;


/**
 * Created by Creadigol on 12-09-2016.
 */
public class Search_Adapter extends RecyclerView.Adapter<Search_Adapter.MyViewHolder> {
    Context context;
    ArrayList<Searchitem> searchitems;
    View.OnClickListener onClickListener;

    public Search_Adapter(Context context, ArrayList<Searchitem> searchitems,View.OnClickListener onClickListener) {
        this.context = context;
        this.searchitems = searchitems;
        this.onClickListener=onClickListener;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custome_searching_activity, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Searchitem search= searchitems.get(position);

        holder.seminar_name.setText(search.getSearch_name());
        holder.type.setText(search.getSearch_type());


//        MeettoApplication.getInstance().getImageLoader().displayImage(search.getSearch_image(), holder.imvlist, getDisplayImageOptions());

       holder.rv_search.setTag(position);
        holder.rv_search.setOnClickListener(onClickListener);
    }

    /*public DisplayImageOptions getDisplayImageOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisk(true).resetViewBeforeLoading(true)
                .showImageForEmptyUri(R.drawable.bigbang)
                .showImageOnFail(R.drawable.bigbang)
                .showImageOnLoading(R.drawable.bigbang)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new RoundedBitmapDisplayer(0))
                .build();
        return options;
    }*/

    @Override
    public int getItemCount() {
        return searchitems.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView seminar_name, tagline,type;
        public Button btnlist;
        public ImageView imvlist;
        public RelativeLayout rv_search;

        public MyViewHolder(View v) {
            super(v);

            seminar_name = (TextView) v.findViewById(R.id.tv_search_name);
//            tagline = (TextView) v.findViewById(R.id.tv_address);
            type = (TextView) v.findViewById(R.id.tv_type);
            imvlist = (ImageView) v.findViewById(R.id.iv_search);
            rv_search = (RelativeLayout) v.findViewById(R.id.rl_listing);
        }


    }

    public void modifyDataSet(ArrayList<Searchitem> search) {
        this.searchitems = search;
        this.notifyDataSetChanged();
    }

}