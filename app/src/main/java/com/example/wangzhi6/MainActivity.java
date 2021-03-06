package com.example.wangzhi6;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.wangzhi6.Functions.WriteEvent;
import com.example.wangzhi6.Functions.Write_note;
import com.example.wangzhi6.Sql.UserData;
import com.example.wangzhi6.Sql.UserDo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;
import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;

public class MainActivity extends AppCompatActivity{

    private Toolbar t;
    private ListView ls;
    private FabSpeedDial fabSpeedDial;

    //监听ListView上下滑动
    private int mTouchSlop;
    private float mFirstY;
    private float mCurrentY;
    private int direction;
    private boolean mShow = true;

    private MyAdpter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //注册EventBus
        // EventBus.getDefault().register(this);

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }



        fabSpeedDial = (FabSpeedDial) findViewById(R.id.mini_fab);

//        fabSpeedDial.setMenuListener(new SimpleMenuListenerAdapter() {
//            @Override
//            public boolean onPrepareMenu(NavigationMenu navigationMenu) {
//                return true;
//            }
//        });

        fabSpeedDial.setMenuListener(new SimpleMenuListenerAdapter() {
            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {

                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.riji_write:
                        Toast.makeText(MainActivity.this, "创建链接", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, Write_note.class));
                        break;
//                    case R.id.activity_setting:
//                        Toast.makeText(MainActivity.this, "设置", Toast.LENGTH_SHORT).show();
//                        break;
//                    case R.id.search:
//                        Toast.makeText(MainActivity.this, "搜索", Toast.LENGTH_SHORT).show();
//                        break;
                }
                return false;
            }
        });


        ls = (ListView) findViewById(R.id.list_item);
        fillList(ls);
        //监听listview上下滑动
        mTouchSlop = ViewConfiguration.get(this).getScaledTouchSlop();

        ls.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mFirstY = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        mCurrentY = event.getY();
                        if (mCurrentY - mFirstY > mTouchSlop) {
                            direction = 0;// down
                        } else if (mFirstY - mCurrentY > mTouchSlop) {
                            direction = 1;// up
                        }
                        if (direction == 1) {

                            //上滑todo
                            fabSpeedDial.hide();

                        } else if (direction == 0) {

                            //下滑todo
                            fabSpeedDial.show();
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                }
                return false;
            }
        });
    }


    //接收消息
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onEventMainThread(WriteEvent event) {

        String msg =event.getmMsg();
        Log.d("harvic", msg);
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        fillList(ls);
    }

    //EventBus的使用--反注册
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        EventBus.getDefault().unregister(this);
//    }

    //刷新数据库
    private void refreshListView(){
        fillList(ls);
    }



    //list列表中实现加载数据
    private void fillList(ListView lv) {
        //List<UserData> list = new ArrayList<UserData>();

        //Sqlite数据库测试
        final UserDo userDo = new UserDo(MainActivity.this);
        // userDo.addSql();

        final List<UserData> list = userDo.readSql();
        //先判断数据库中有没有数据
        if (list == null) {
            startActivity(new Intent(MainActivity.this,Write_note.class));

        } else {

            //标题集合
            List<String> titltlist = new ArrayList<>();
            //备注集合
            List<String> remarklist = new ArrayList<>();
            //icon链接集合
            List<String> url_iconlist = new ArrayList<>();
            //链接集合
            List<String> urllist = new ArrayList<>();
            //id集合
            final List<Integer> idlist = new ArrayList<>();
            System.out.println("ID的集合："+idlist.toString());

            //获取ID
            System.out.println("获取ID" + userDo.getCount());
            for (Iterator iterator = list.iterator(); iterator.hasNext(); ) {
                UserData userdata = (UserData) iterator.next();
                titltlist.add(userdata.getTitle());
                remarklist.add(userdata.getRemark());
                url_iconlist.add(userdata.getUrl_icon());
                urllist.add(userdata.getUrl());
                idlist.add(userdata.getId());
//                contentlist.add(userdata.getContent());
//                timelist.add(userdata.getTime());
            }

            System.out.println("ID的集合："+idlist.toString());

            adapter  = new MyAdpter(this, list, titltlist,remarklist,url_iconlist,urllist);
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



                    //根据position获取list的对应位置
                    UserData myUrl = list.get(position);
                    Uri uri = Uri.parse(myUrl.getUrl());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);

////                  传递一个url对象到MYWeb中
//                    Bundle bundle = new Bundle();
//                    Intent intent = new Intent();
// //改
//                    intent.setClass(MainActivity.this, MyWeb.class);
//                    bundle.putSerializable("myUrl", myUrl);
//                    intent.putExtras(bundle);
//                    startActivity(intent);

                    Toast.makeText(MainActivity.this, "item" + position, Toast.LENGTH_SHORT).show();
                }
            });

            //list长按删除
            lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                    final int id_delete = idlist.get(position);
                    System.out.println("此处的id:"+id_delete);
                    //设置对话框，确认是否删除
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("提醒")
                            .setMessage("你确定要删除此链接吗？")
                            .setNegativeButton("取消",null)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //执行删除指定的日记
                                    userDo.deletSql(id_delete);
                                    //刷新日记本
                                    refreshListView();
                                }
                            }).show();

                    return true;
                }
            });

            adapter.setOnImageListner(new MyAdpter.CallBack() {
                @Override
                public void onImageBackListner(View v, int position) {
                    Toast.makeText(MainActivity.this, "image" + position, Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
}

