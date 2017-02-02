package creadigol.com.Meetto;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import Utils.PreferenceSettings;

/**
 * Created by ravi on 27-10-2016.
 */

public class Language_Selection_activity extends AppCompatActivity implements View.OnClickListener {
    LinearLayout ll_english,ll_japanese,ll_setlng;
    TextView skip,tv_eng,tv_jp,tv_submited;
    String launguge;
    PreferenceSettings mPreferenceSettings;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        ll_setlng=(LinearLayout)findViewById(R.id.ll_setlng);
        tv_eng=(TextView) findViewById(R.id.rb_english);
        tv_jp=(TextView) findViewById(R.id.rb_japanese);

        skip=(TextView)findViewById(R.id.skip_lng);

        tv_submited=(TextView)findViewById(R.id.tv_submited);

        skip.setOnClickListener(this);
        ll_setlng.setOnClickListener(this);

        mPreferenceSettings=MeettoApplication.getInstance().getPreferenceSettings();

        tv_eng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPreferenceSettings.setLUNGAUGE(false);
                MeettoApplication.language("en");
                Intent intent = new Intent(Language_Selection_activity.this, Splash_Activity.class);
                intent.putExtra("Login", "true");
                intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
        tv_jp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mPreferenceSettings.setLUNGAUGE(true);
                MeettoApplication.language("ja");
                Intent intent = new Intent(Language_Selection_activity.this, Splash_Activity.class);
                intent.putExtra("Login", "true");
                intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

            }
        });
    }



    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.skip_lng:
                finish();
                break;

        }

    }
}
