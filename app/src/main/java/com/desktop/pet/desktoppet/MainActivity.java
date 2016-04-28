package com.desktop.pet.desktoppet;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ImageButton showButton;
    private ImageButton hideButton;
    private ImageButton settingButton;
    private Intent desktopPetServiceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //制定首页布局
        setContentView(R.layout.main);
        //获取需要监听的按钮
        showButton = (ImageButton)findViewById(R.id.showButton);
        hideButton = (ImageButton)findViewById(R.id.hideButton);
        settingButton = (ImageButton)findViewById(R.id.settingButton);

        //给showButton设置监听器，用于启动DesktopPetService
        showButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //启动DesktopPetService
                desktopPetServiceIntent = new Intent(MainActivity.this,DesktopPetService.class);
                getApplication().startService(desktopPetServiceIntent);
                //Toast.makeText(MainActivity.this, "show button was clicked", Toast.LENGTH_SHORT).show();
            }
        });

        //给hideButton设置监听器，用于停止DesktopPetService
        hideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                desktopPetServiceIntent = new Intent(MainActivity.this,DesktopPetService.class);
                getApplication().stopService(desktopPetServiceIntent);
                //Toast.makeText(MainActivity.this, "hide button was clicked", Toast.LENGTH_SHORT).show();

            }
        });



        //给settingButton设置监听器，用于启动SettingActivity
        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent testIntent = new Intent(MainActivity.this,Test.class);
                startActivity(testIntent);
                Toast.makeText(MainActivity.this, "setting button was clicked", Toast.LENGTH_SHORT).show();
            }
        });










    }
}
