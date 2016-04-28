package com.desktop.pet.desktoppet;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

//桌面悬浮窗口DesctopPetService类
public class DesktopPetService extends Service{

    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;
    //
    private boolean flag = true;//标记悬浮窗是否已经显示
    private LinearLayout desktop_pet;//悬浮窗布局
    private  ImageView desktop_pet_image;
    //desktop_pet布局文件内的图片以外的部分
    public LinearLayout linear;
    //悬浮窗是否是打开状态
    private boolean open = false ;


    public DesktopPetService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }


    @Override
    public void onCreate(){
        // TODO Auto-generated method stub
        //获取windowManager对象
        windowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        int screenWidth = windowManager.getDefaultDisplay().getWidth();
        //获取LayoutParams对象
        layoutParams = new WindowManager.LayoutParams();
        layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;// 系统提示window
        layoutParams.format = PixelFormat.TRANSLUCENT;// 支持透明
        //layoutParams.format = PixelFormat.RGBA_8888;//实现渐变效果需要
        layoutParams.flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;// 焦点
        layoutParams.flags |= WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;// 模态
        layoutParams.width = 550;//窗口的宽
        layoutParams.height = 400;//窗口的高
        layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        layoutParams.x = (screenWidth - 550) / 2;//窗口出现位置的偏移量
        layoutParams.y = 1000;//窗口出现位置的偏移量
        //layoutParams.alpha = 0.1f;//窗口的透明度
        LayoutInflater inflater = LayoutInflater.from(getApplication());
        //获取desktop_pet.xml布局文件
        desktop_pet = (LinearLayout) inflater.inflate(R.layout.desktop_pet, null);
        //获取desktop_pet布局文件内的图片
        desktop_pet_image = (ImageView) desktop_pet.findViewById(R.id.desktop_pet_image);
        //获取desktop_pet布局文件内的图片以外的部分
        linear = (LinearLayout)desktop_pet.findViewById(R.id.linear);

        //添加desktop_pet布局文件内的图片onTouch监听器
        desktop_pet_image.setOnTouchListener(new View.OnTouchListener() {
            int screenWidth = windowManager.getDefaultDisplay().getWidth();//屏幕宽度
            private float startX;//拖动开始之前悬浮窗的x位置
            private float startY;//拖动开始之前悬浮窗的y位置
            private float lastX;//上个MotionEvent的x位置
            private float lastY;//上个MotionEvent的y位置
            private float nowX;//这次MotionEvent的x位置
            private float nowY;//这次MotionEvent的y位置
            private float translateX;//每次拖动产生MotionEvent事件之后窗口所要移动的x轴距离
            private float translateY;//每次拖动产生MotionEvent事件的时候窗口所要移动的x轴距离
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int action = event.getAction();
                boolean ret = false;
                if (action == MotionEvent.ACTION_DOWN) {
                    lastX = event.getRawX();
                    lastY = event.getRawY();
                    startX = layoutParams.x;
                    startY = layoutParams.y;
                    if(open){
                        //隐藏表情以外区域
                        desktop_pet.removeAllViews();
                        desktop_pet.addView(desktop_pet_image);
                        //调整悬浮窗大小
                        layoutParams.width = 150;//窗口的宽
                        layoutParams.height = 150;//窗口的高
                        layoutParams.y += 250 ;
                        layoutParams.x = (int) (screenWidth - layoutParams.width) / 2; ;
                        //关闭状态
                        open = false;
                        //Toast.makeText(DesktopPetService.this, screenWidth+"--"+layoutParams.x, Toast.LENGTH_SHORT).show();
                        windowManager.updateViewLayout(desktop_pet, layoutParams);

                    }

                } else if (action == MotionEvent.ACTION_MOVE) {
                    nowX = event.getRawX();
                    nowY = event.getRawY();
                    //这次MotionEvent要移动的距离
                    translateX = (int) (nowX - lastX);
                    translateY = (int) (nowY - lastY);
                    layoutParams.x += translateX;
                    layoutParams.y += translateY;
                    //更新布局
                    windowManager.updateViewLayout(desktop_pet, layoutParams);
                    lastX = nowX;
                    lastY = nowY;
                } else if (action == MotionEvent.ACTION_UP) {
                    //跟开始位置比较，检测是否有明显的多动，不是的话返回false，继续执行onClick
                    boolean a = Math.abs(layoutParams.x - startX) < 5;
                    boolean b = Math.abs(layoutParams.y - startY) < 5;
                    //窗口xy移动距离小于我们期望的范围当做onClick，返回true继续执行onClick
                    if ( a&&b ) {
                        ret = false;
                    } else {
                        if (nowX <= (screenWidth /2) ) {
                            //0为屏幕最左边
                            layoutParams.x = 0;
                            //Toast.makeText(DesktopPetService.this,"在左边三分之一区域，左浮动" , Toast.LENGTH_SHORT).show();
                        }else if (nowX >= (screenWidth /2)  ){
                            //screenWidth-layoutParams.width为屏幕最右边
                            layoutParams.x = screenWidth-layoutParams.width;
                            //Toast.makeText(DesktopPetService.this,"在右边三分之一区域，右浮动" , Toast.LENGTH_SHORT).show();
                        }else{
                            //
                        }
                        ret = true;
                    }
                    //刷新布局
                    windowManager.updateViewLayout(desktop_pet, layoutParams);
                }
                return ret;
            }
        });

        desktop_pet_image.setOnClickListener(new View.OnClickListener() {
            int screenWidth = windowManager.getDefaultDisplay().getWidth();//屏幕宽度
            @Override
            public void onClick(View v) {
                if(!open){
                    //显示表情以外区域，中间对其
                    desktop_pet.removeAllViews();
                    desktop_pet.addView(linear);
                    desktop_pet.addView(desktop_pet_image);
                    //调整窗口
                    layoutParams.width = 550;//窗口的宽
                    layoutParams.height = 400;//窗口的高
                    layoutParams.x = (screenWidth - layoutParams.width) / 2;//窗口出现位置的偏移量
                    //250为除了图片之外的可视区域，相当于文字区域高度
                    layoutParams.y -= 250;
                    windowManager.updateViewLayout(desktop_pet, layoutParams);
                    open = true;

                }
                //v.getX();
                //v.getTranslationX();
                //v.getLeft();

                //Toast.makeText(DesktopPetService.this, "image", Toast.LENGTH_SHORT).show();
            }
        });

        super.onCreate();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        if (flag){
            flag = false;
            windowManager.addView(desktop_pet, layoutParams);
            open = true;
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        if(desktop_pet.getParent() != null){
            flag = true;
            windowManager.removeView(desktop_pet);
        }
        super.onDestroy();
    }


}
