package com.ef.smallstar.smallstar;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Test2Activity extends AppCompatActivity {

    Intent intent,intent1;
    ImageView image1,image2,image3,image4,imageAnswer;
    float lastX ,lastY;
    float firstX,firstY;
    Point point,point1,point2;
    private int containerWidth;
    private int containerHeight;
    boolean first ;
    Mylistner mylistner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toothbrush);
        overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
        initView();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus){
            WindowManager wm = this.getWindowManager();
            containerWidth = wm.getDefaultDisplay().getWidth();
            containerHeight = wm.getDefaultDisplay().getHeight();
        }
    }

    private void initView() {
        first = true;
        mylistner = new Mylistner();
        image1 = (ImageView) findViewById(R.id.image1);
        image2 = (ImageView) findViewById(R.id.image2);
        image3 = (ImageView) findViewById(R.id.image3);
        image4 = (ImageView) findViewById(R.id.image4);
        imageAnswer = (ImageView) findViewById(R.id.iv_answer);
        intent = new Intent(this,MainActivity.class);
        intent1 = new Intent(this,Test3Activity.class);
        image1.setOnTouchListener(mylistner);
        image2.setOnTouchListener(mylistner);
        image3.setOnTouchListener(mylistner);
        image4.setOnTouchListener(mylistner);
    }
    public void showHome(View view) {
        startActivity(intent);
        finish();
    }

    class Mylistner implements View.OnTouchListener {

        @Override
        public boolean onTouch(final View view, MotionEvent motionEvent) {

                switch (motionEvent.getActionMasked()){
                    case MotionEvent.ACTION_DOWN:
                        if (first){
                            firstX = view.getX();
                            firstY = view.getY();
                            System.out.println(firstX+"这是最开始的位置"+firstY);
                        }
                        lastX = motionEvent.getRawX();
                        lastY = motionEvent.getRawY();

                        startScaleAnimator(view, 1.f, 1.2f);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float distanceX = lastX - motionEvent.getRawX();
                        float distanceY =lastY - motionEvent.getRawY();

                        float nextX = view.getX() - distanceX;
                        float nextY = view.getY() - distanceY;

                        //不能移动到屏幕外面
                        if (nextY < 0){
                            nextY = 0;
                        }else if (nextY > containerHeight - view.getHeight()){
                            nextY  =containerHeight - view.getHeight();
                        }
                        if (nextX < 0)
                            nextX = 0;
                        else if (nextX > containerWidth - view.getWidth())
                            nextX = containerWidth - image1.getWidth();

                        //用动画来移动
                        ObjectAnimator y = ObjectAnimator.ofFloat(view,"y",view.getY(),nextY);
                        ObjectAnimator x = ObjectAnimator.ofFloat(view,"x",view.getX(),nextX);
                        AnimatorSet animatorSet = new AnimatorSet();
                        animatorSet.playTogether(x,y);
                        animatorSet.setDuration(0);
                        animatorSet.start();

                        lastY = motionEvent.getRawY();
                        lastX = motionEvent.getRawX();
                        break;
                    case MotionEvent.ACTION_UP:
                        image1.setEnabled(false);
                        image2.setEnabled(false);
                        image3.setEnabled(false);
                        image4.setEnabled(false);
                        point1 = getViewLocation(view);
                        point2 = getViewLocation(imageAnswer);
                        float distance = getDistanceBetween2Points(point1,point2);
                        System.out.println("distance:"+distance);

                        if (distance<150){
                            float w = view.getWidth();
                            float h = view.getHeight();
                            x = ObjectAnimator.ofFloat(view,"x", point2.x - w / 2);
                            y = ObjectAnimator.ofFloat(view,"y", point2.y - h / 2-15);
                            //滑到空框里面,并且缩小
                            showAnimator(x,y);
                            startScaleAnimator(view, 1.2f, .6f).addListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator anim) {

                                }

                                @Override
                                public void onAnimationEnd(Animator anim) {

                                    if (view.getId() == image1.getId()){
                                        //等一秒跳转到第二题
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {

                                                startActivity(intent1);
                                                finish();
                                            }
                                        },500);
                                    }else{
                                        new Handler().postDelayed(new Runnable(){

                                            public void run() {

                                                //播放摆动的动画,然后回到开始的位置
                                                TranslateAnimation animation = new TranslateAnimation(7, -7, 0, 0);
                                                animation.setInterpolator(new OvershootInterpolator());
                                                animation.setDuration(100);
                                                animation.setRepeatCount(5);
                                                animation.setRepeatMode(Animation.REVERSE);
                                                view.startAnimation(animation);
                                                animation.setAnimationListener(new Animation.AnimationListener() {
                                                    @Override
                                                    public void onAnimationStart(Animation animation) {

                                                    }

                                                    @Override
                                                    public void onAnimationEnd(Animation animation) {
                                                        //放大,回到开始位置
                                                        //回去
                                                        showAnimator(ObjectAnimator.ofFloat(view,"y",view.getY(),firstY),ObjectAnimator.ofFloat(view,"x",view.getX(),firstX));
                                                        startScaleAnimator(view, 1.f, 1.2f);
                                                        image1.setEnabled(true);
                                                        image2.setEnabled(true);
                                                        image3.setEnabled(true);
                                                        image4.setEnabled(true);
                                                    }

                                                    @Override
                                                    public void onAnimationRepeat(Animation animation) {

                                                    }
                                                });

                                            }

                                        }, 500);


                                    }

                                }

                                @Override
                                public void onAnimationCancel(Animator animation) {

                                }

                                @Override
                                public void onAnimationRepeat(Animator animation) {

                                }
                            });




                        }else{
                            System.out.println("这是要回位的店startx:"+firstX+"starty:"+firstY);
                            y = ObjectAnimator.ofFloat(view,"y",view.getY(),firstY);
                            x = ObjectAnimator.ofFloat(view,"x",view.getX(),firstX);
                            //回去
                            x.addListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    view.setEnabled(true);
                                }

                                @Override
                                public void onAnimationCancel(Animator animation) {

                                }

                                @Override
                                public void onAnimationRepeat(Animator animation) {

                                }
                            });
                            showAnimator(x,y);

                            startScaleAnimator(view, 1.2f, 1.f);
                        }
                        break;
                }

                return true;
        }
    }

    /**
     * 缩放动画
     * @param view
     * @param startValue
     * @param endValue
     * @return
     */
    private ObjectAnimator startScaleAnimator(View view, float startValue, float endValue) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", startValue, endValue);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", startValue, endValue);
        AnimatorSet scaleSet = new AnimatorSet();
        scaleSet.playTogether(scaleX, scaleY);
        scaleSet.setDuration(200);
        scaleSet.start();
        return scaleX;
    }

    /**
     * 得到一个点的坐标
     * @param view
     */
    public Point getViewLocation(View view){
        int[] textLocation = new int[2];
        view.getLocationOnScreen(textLocation);
        point = new Point(textLocation[0] + view.getWidth() / 2, textLocation[1] + view.getHeight() / 2);
        System.out.println("x:"+textLocation[0]+"y:"+textLocation[1]);
        return point;
    }

    /**
     * As meaning of method name.
     * 获得两点之间的距离
     * @param p0
     * @param p1
     * @return
     */
    public static float getDistanceBetween2Points(Point p0, Point p1) {
        float distance = (float) Math.sqrt(Math.pow(p0.y - p1.y, 2) + Math.pow(p0.x - p1.x, 2));
        System.out.println("移动的坐标"+p0.x+p0.y);
        System.out.println("空的坐标"+p1.x+p1.y);
        return distance;
    }


    public void showAnimator(ObjectAnimator x,ObjectAnimator y){

        AnimatorSet animatorSet1 = new AnimatorSet();
        animatorSet1.playTogether(x,y);
        animatorSet1.setDuration(300);
        x.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                image1.setEnabled(true);
                image2.setEnabled(true);
                image3.setEnabled(true);
                image4.setEnabled(true);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet1.start();
    }

    @Override
    public void onBackPressed() {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("You will leave the game!")
                .setCancelText("Yes,exit!")
                .setConfirmText("No,cancel!")
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        startActivity(intent);
                        finish();
                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                    }
                })
                .show();
    }
}
