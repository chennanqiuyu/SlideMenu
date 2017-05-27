package com.example.slidemenu;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.CycleInterpolator;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.menu_listview)
    ListView menuListview;
    @BindView(R.id.iv_head)
    ImageView ivHead;
    @BindView(R.id.main_listview)
    ListView mainListview;
    @BindView(R.id.slideMenu)
    MySlideMenu slideMenu;
    @BindView(R.id.mylinerlayout)
    MyLinearLayout mylinerlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mylinerlayout.setMySlideMenu(slideMenu);
        mainListview.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, Constant.NAMES));
        menuListview.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, Constant.sCheeseStrings) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                textView.setTextColor(Color.WHITE);
                return textView;
            }
        });
        slideMenu.setOnSlideListener(new MySlideMenu.OnSlideListener() {
            @Override
            public void onDragging(float fraction) {
                ivHead.setRotationY(720 * fraction);
            }

            @Override
            public void onOpen() {
                Toast.makeText(MainActivity.this, "打开", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onClose() {
                Toast.makeText(MainActivity.this, "关闭!!!!", Toast.LENGTH_SHORT).show();
                ViewCompat.animate(ivHead).translationX(40).setInterpolator(new CycleInterpolator(4)).setDuration(800).start();
            }

            @Override
            public void onMax(int max) {
                mylinerlayout.setMaxLeft(max);
            }
        });
    }

    @OnClick(R.id.iv_head)
    public void onViewClicked() {

        slideMenu.toggle();
    }
}
