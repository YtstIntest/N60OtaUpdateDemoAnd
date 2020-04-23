package com.example.n60otaupdatedemoand.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.n60otaupdatedemoand.R;


public class EmptyViewHelper {
    private Context context;
    private ImageView imageView;
    private TextView tipTextView;
    private View view;
    public EmptyViewHelper(Context context) {
        this.context = context;
        initView();
    }

    public void initView() {
        view = LayoutInflater.from(context).inflate(R.layout.view_empty,null);
        imageView = (ImageView) view.findViewById(R.id.imgIV);
        tipTextView = (TextView) view.findViewById(R.id.tipTV);
    }

    public View setContentView(int imgID, String tip){
        this.imageView.setBackgroundResource(imgID);
        this.tipTextView.setText(tip);
        return view;
    }
}
