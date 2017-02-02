package Fragments;
import android.support.v4.app.Fragment;
import android.os.Bundle;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.ArrayList;

import Adapters.Review_Adapter;
import Models.Review_items;
import Models.Review_object_item;
import creadigol.com.Meetto.R;

/**
 * Created by Creadigol on 17-10-2016.
 */
public class Review_Fragment extends Fragment {

        static  private Review_object_item review_object;
        private TAB_TYPE tabType;
        Review_Adapter review_adapter;
         ArrayList<Review_items> review_itemses = new ArrayList<>();
        public static String EXTRA_KEY_MY_TAG_OBJECT = "myTagObject";
        public static String EXTRA_KEY_TAB_TYPE = "tabType";
        public static  enum TAB_TYPE {YOURREVIEW,USERRIVIEW}

        public static Review_Fragment newInstance(Review_object_item myTagsListObject, TAB_TYPE tab_type) {
            Review_Fragment  myTagFragment = new Review_Fragment ();
            Bundle bundle = new Bundle();
            review_object=myTagsListObject;
            bundle.putSerializable(EXTRA_KEY_MY_TAG_OBJECT, myTagsListObject);
            bundle.putSerializable(EXTRA_KEY_TAB_TYPE, tab_type);
            myTagFragment.setArguments(bundle);
            return myTagFragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

//            review_object = (Review_object_item) getArguments().getSerializable(
//                    EXTRA_KEY_MY_TAG_OBJECT);
            tabType = (TAB_TYPE) getArguments().getSerializable(EXTRA_KEY_TAB_TYPE);
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.custome_review_activity, container, false);

            ArrayList<Review_items> myTagsObjects = getTagList();
            if(myTagsObjects != null && myTagsObjects.size() > 0){
                RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rv_recycle);
                recyclerView.setVisibility(View.VISIBLE);
                recyclerView.setHasFixedSize(true);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                review_adapter = new Review_Adapter(getActivity(),myTagsObjects);
                recyclerView.setAdapter(review_adapter);
                // Add the sticky headers decoration
            } else {
                TextView tvNoRecordFound = (TextView) view.findViewById(R.id.tv_no_record_found);
                tvNoRecordFound.setVisibility(View.VISIBLE);
                if (tabType == TAB_TYPE.YOURREVIEW) {
                    tvNoRecordFound.setText(getResources().getString(R.string.review_msg));
                } else if (tabType == TAB_TYPE.USERRIVIEW) {
                    tvNoRecordFound.setText(getResources().getString(R.string.review_msg_2));
                }
            }
            return view;
        }
    public ArrayList<Review_items> getTagList() {


        if (tabType == TAB_TYPE.YOURREVIEW) {
            for (Review_items review_items: review_object.getreviewlist()) {
                if (review_items.getType().equalsIgnoreCase(getResources().getString(R.string.review_res)))
                    review_itemses.add(review_items);
            }
        } else if (tabType == TAB_TYPE.USERRIVIEW) {
            for (Review_items review_items : review_object.getreviewlist()) {
                if (review_items.getType().equalsIgnoreCase(getResources().getString(R.string.review_res_2)))
                    review_itemses.add(review_items);
            }
        }
        return review_itemses;
    }
}
