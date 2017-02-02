package Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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

import Models.SimilerSeminaritem;
import creadigol.com.Meetto.MeettoApplication;
import creadigol.com.Meetto.R;
import creadigol.com.Meetto.SeminarDetail_Activity;


/**
 * Created by Creadigol on 07-09-2016.
 */
public class SimilerSeminar_Adapter extends RecyclerView.Adapter<SimilerSeminar_Adapter.SeminarViewHolder> {// Recyclerview will extend to
    //    private ArrayList<ImageslideItem> arrayList;
    private ArrayList<SimilerSeminaritem> similerSeminaritems;
    private Context context;
    View view;

    public SimilerSeminar_Adapter(Context context, ArrayList<SimilerSeminaritem> similerSeminar) {
        this.context = context;
        this.similerSeminaritems = similerSeminar;

    }


    @Override
    public SeminarViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.item_row_slide, parent, false);

        SeminarViewHolder outletViewHolder = new SeminarViewHolder(view);

        return outletViewHolder;

    }

    @Override
    public void onBindViewHolder(SeminarViewHolder holder, int position) {
        final SimilerSeminaritem model = similerSeminaritems.get(position);

        holder.seminarname.setText(model.getSeminar_name());

        MeettoApplication.getInstance().getImageLoader().displayImage(model.getSeminar_pic(), holder.ivseminarimage, getDisplayImageOptions());

        holder.ll_slider.setTag(position);
        holder.ll_slider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent I = new Intent(context, SeminarDetail_Activity.class);
                I.putExtra(SeminarDetail_Activity.EXTRA_KEY_SEMINAR_ID, model.getSeminar_id());
                I.putExtra("seminar_name", model.getSeminar_name());
                context.startActivity(I);
            }
        });

    }

    public DisplayImageOptions getDisplayImageOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.nophoto)
                .showImageForEmptyUri(R.drawable.nophoto)
                .showImageOnFail(R.drawable.nophoto)
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
        return similerSeminaritems.size();
    }


    public class SeminarViewHolder extends RecyclerView.ViewHolder {

        public TextView seminarname;
        public ImageView ivseminarimage;
        public LinearLayout ll_slider;

        public SeminarViewHolder(View itemView) {
            super(itemView);

            ll_slider = (LinearLayout) itemView.findViewById(R.id.ll_slider);
            seminarname = (TextView) itemView.findViewById(R.id.iv_title);
            ivseminarimage = (ImageView) itemView.findViewById(R.id.iv_image);
        }
    }

    public void modifyDataSet(ArrayList<SimilerSeminaritem> similerSeminaritems) {
        this.similerSeminaritems = similerSeminaritems;
        this.notifyDataSetChanged();
    }

}
