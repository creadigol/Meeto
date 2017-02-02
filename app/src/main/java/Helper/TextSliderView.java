package Helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.daimajia.slider.library.SliderTypes.BaseSliderView;

import creadigol.com.Meetto.R;

/**
 * This is a slider with a description TextView.
 */
public class TextSliderView extends BaseSliderView {
    public TextSliderView(Context context) {
        super(context);
    }


    @Override
    public View getView() {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.seminar_image_slide, null);

        ImageView target = (ImageView) v.findViewById(R.id.slider_image);

        bindEventAndShow(v, target);
        return v;
    }
}
