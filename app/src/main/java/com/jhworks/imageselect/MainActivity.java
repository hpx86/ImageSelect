package com.jhworks.imageselect;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.jhworks.library.ImageSelector;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE = 2;

    private TextView mResultText;
    private RadioGroup mChoiceMode, mShowCamera;
    private EditText mRequestNum;
    private EditText mImageSpanCount;
    private Toolbar mToolbar;

    private ArrayList<String> mSelectPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle(R.string.app_name);
        mToolbar.setNavigationIcon(R.mipmap.ic_launcher);

        mResultText = (TextView) findViewById(R.id.result);
        mChoiceMode = (RadioGroup) findViewById(R.id.choice_mode);
        mShowCamera = (RadioGroup) findViewById(R.id.show_camera);
        mRequestNum = (EditText) findViewById(R.id.request_num);
        mImageSpanCount = (EditText) findViewById(R.id.image_span_count);

        mChoiceMode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if (checkedId == R.id.multi) {
                    mRequestNum.setEnabled(true);
                } else {
                    mRequestNum.setEnabled(false);
                    mRequestNum.setText("");
                }
            }
        });

        View button = findViewById(R.id.button);
        if (button != null) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pickImage();
                }
            });
        }

    }

    private void pickImage() {
        boolean showCamera = mShowCamera.getCheckedRadioButtonId() == R.id.show;
        int maxNum = 9;

        if (!TextUtils.isEmpty(mRequestNum.getText())) {
            try {
                maxNum = Integer.valueOf(mRequestNum.getText().toString());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        int imageSpanCount = 3;
        if (!TextUtils.isEmpty(mImageSpanCount.getText())) {
            try {
                imageSpanCount = Integer.valueOf(mImageSpanCount.getText().toString());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        ImageSelector selector = ImageSelector.create();
        if (mChoiceMode.getCheckedRadioButtonId() == R.id.single) {
            selector.single();
        } else {
            selector.multi();
        }
        selector.origin(mSelectPath)
                .showCamera(showCamera)
                .count(maxNum)
                .spanCount(imageSpanCount)
                .start(MainActivity.this, REQUEST_IMAGE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                mSelectPath = data.getStringArrayListExtra(ImageSelector.EXTRA_RESULT);
                StringBuilder sb = new StringBuilder();
                for (String p : mSelectPath) {
                    sb.append(p);
                    sb.append("\n");
                }
                mResultText.setText(sb.toString());
            }
        }
    }
}
