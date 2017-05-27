package com.example.slidemenu;

import android.animation.ArgbEvaluator;
import android.animation.FloatEvaluator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * Created by He on 2017/5/17.
 */

public class MySlideMenu extends FrameLayout {
    ViewDragHelper dragHelper;
    View main,menu;
    int maxLeft;

    public MySlideMenu(@NonNull Context context) {
        this(context,null);
    }

    public MySlideMenu(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MySlideMenu(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        dragHelper = ViewDragHelper.create(this,callb);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        menu = getChildAt(0);
        main = getChildAt(1);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        maxLeft = (int) (getMeasuredWidth()*0.6f);
        listener.onMax(maxLeft);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        boolean result = dragHelper.shouldInterceptTouchEvent(ev);
        return result;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        dragHelper.processTouchEvent(event);
        return true;
    }

    ViewDragHelper.Callback callb = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return true;
        }
        @Override
        public int getViewHorizontalDragRange(View child) {
            return 1;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            if(child==main){
                left = fixLeft(left);
            }
            return left;
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            if(changedView==menu){
                menu.layout(0,0,menu.getMeasuredWidth(),menu.getMeasuredHeight());
                int newLeft = main.getLeft()+dx;
                newLeft = fixLeft(newLeft);
                main.layout(newLeft,main.getTop(),newLeft+main.getMeasuredWidth(),main.getMeasuredHeight());
            }
            float fraction = main.getLeft()*1f/maxLeft;
            execAnimation(fraction);
            if(listener!=null){
                listener.onDragging(fraction);
                if(main.getLeft()==maxLeft){
                    listener.onOpen();
                }else if(main.getLeft()==0){
                    listener.onClose();
                }
            }
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            if(main.getLeft()>maxLeft/2){
                open();
            }else{
                close();
            }
        }
    };
    FloatEvaluator floatEvaluator = new FloatEvaluator();
    ArgbEvaluator argbEvaluator = new ArgbEvaluator();
    private void execAnimation(float fraction) {
        main.setScaleX(floatEvaluator.evaluate(fraction,1,0.8));
        main.setScaleY(floatEvaluator.evaluate(fraction,1,0.8));
        menu.setScaleX(floatEvaluator.evaluate(fraction, 0.3f, 1f));
        menu.setScaleY(floatEvaluator.evaluate(fraction, 0.3f, 1f));
        menu.setTranslationX(floatEvaluator.evaluate(fraction,-menu.getMeasuredWidth()/2,0));
        Drawable background = getBackground();
        if(background!=null){
            int color = (int) argbEvaluator.evaluate(fraction, Color.BLACK,Color.TRANSPARENT);
            background.setColorFilter(color, PorterDuff.Mode.SRC_OVER);
        }
    }

    public void close() {
        dragHelper.smoothSlideViewTo(main, 0, 0);
        ViewCompat.postInvalidateOnAnimation(MySlideMenu.this);
    }

    private void open() {
        dragHelper.smoothSlideViewTo(main,maxLeft,0);
        ViewCompat.postInvalidateOnAnimation(MySlideMenu.this);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if(dragHelper.continueSettling(true)){
            ViewCompat.postInvalidateOnAnimation(MySlideMenu.this);
        }
    }

    private int fixLeft(int newLeft) {
        if(newLeft>maxLeft){
            newLeft = maxLeft;
        }else if(newLeft<0){
            newLeft = 0;
        }
        return newLeft;
    }

    public interface OnSlideListener{
        void onDragging(float fraction);
        void onOpen();
        //关闭的回调
        void onClose();
        void onMax(int max);
    }
    private OnSlideListener listener;
    public void setOnSlideListener(OnSlideListener listener){
        this.listener = listener;
    }
    public void toggle(){
        if(main.getLeft()==0){
            //点击的时候应该打开
            open();
        }else if(main.getLeft()==maxLeft){
            close();
        }
    }
}
