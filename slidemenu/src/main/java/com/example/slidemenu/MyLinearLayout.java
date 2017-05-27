package com.example.slidemenu;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;

/**
 * Created by He on 2017/5/18.
 */

public class MyLinearLayout extends LinearLayout {

    public MyLinearLayout(Context context) {
        this(context,null);
    }

    public MyLinearLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    int mLeft;
    public void setMaxLeft(int maxLeft){
        mLeft = maxLeft;
    }

    MySlideMenu mySlideMenu;
    public void setMySlideMenu(MySlideMenu ms){
        mySlideMenu = ms;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(mLeft == getLeft()){
            if(mySlideMenu!=null){
                mySlideMenu.close();
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(mLeft == getLeft()){
            if(mySlideMenu!=null){
                mySlideMenu.close();
            }
            return true;
        }
        return false;
    }
}
