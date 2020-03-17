package com.example.wangzhi6.Functions;

/**
 * 写日记功能实现----by MiChong
 */

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wangzhi6.Parse.GetHtmlTitle;
import com.example.wangzhi6.Parse.MainContract;
import com.example.wangzhi6.Parse.ParsePresenter;
import com.example.wangzhi6.R;
import com.example.wangzhi6.Sql.UserDo;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.regex.Pattern;

import de.greenrobot.event.EventBus;

public class Write_note extends AppCompatActivity implements MainContract.IMainView {

    private EditText ain_url;
    private Button ain_parse;
    private ImageButton ain_icon;
    private EditText ain_title;
    private EditText ain_remark;
    private Button ain_add;

    private String st_title;
    private String st_icon_url = "1";



    private LoadingDialog dialog;
    private GetHtmlTitle getHtmlTitle;
    private ProgressDialog progressDialog;//圆形进度条
    private ParsePresenter parsePresenter; //


    /**
     * 链接保存成功
     */
    private Handler handler = new Handler(){
        public void handleMessage(android.os.Message msg) {

            super.handleMessage(msg);
            if(msg.what == 0){
                dialog.success();
                dialog.dismiss();
                finish();
                EventBus.getDefault().post(new WriteEvent("保存成功！！！"));
            }
        };
    };
    private UserDo userDo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

/////////允许访问网页
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

//        Toolbar mToolbarTb = (Toolbar) findViewById(R.id.id_tool_bar3);
//        mToolbarTb.setTitle("添加日记");
//        setSupportActionBar(mToolbarTb);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        mToolbarTb.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //设置返回事件
//                onBackPressed();
//            }
//        });

//        write_title = (EditText) findViewById(R.id.write_title);
//        write_content = (EditText) findViewById(R.id.write_content);
        ain_url = (EditText)findViewById(R.id.ain_url);
        ain_parse = (Button) findViewById(R.id.ain_parse);
        ain_title = (EditText)findViewById(R.id.ain_title);
        ain_remark =  (EditText)findViewById(R.id.ain_remark);
        ain_icon = (ImageButton) findViewById(R.id.ain_icon);
        ain_add = (Button) findViewById(R.id.ain_add);
       //成功界面
        dialog = new LoadingDialog(this);
        //连接数据库
        userDo = new UserDo(Write_note.this);
        //执行编辑链接
        getHtmlTitle = new GetHtmlTitle();
        parsePresenter = new ParsePresenter(this);
        org.greenrobot.eventbus.EventBus.getDefault().register(this); //获取到这个单例的 EventBus 对象
        ain_parse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pattern pattern = Patterns.WEB_URL;
                final String url = ain_url.getText().toString();

                System.out.println("网址:"+url);
                if (TextUtils.isEmpty(url) || !pattern.matcher(url).matches()) {
                    Toast.makeText(getApplicationContext(), "请输入有效网址", Toast.LENGTH_SHORT).show();
                } else {
                    parsePresenter.parseIcon(url);
                    String allHtml = getHtmlTitle.getHtmlSource(ain_url.getText().toString());
                    st_title = getHtmlTitle.getTitle(allHtml);
                    System.out.println(st_title);
                    if(st_title.equals("")){ st_title = ain_title.getText().toString();}
                    ain_title.setText(st_title);


                }
            }
        });

        ain_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = ain_title.getText().toString();
                String remark = ain_remark.getText().toString();
                String url_icon = st_icon_url  ;
                System.out.println("图标网址："+st_icon_url);
                String url = ain_url.getText().toString();
                //获取时间
//                Date date = new Date();
//                SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
                //System.out.println(sf.format(date));
//                if (!title.equals("") && !url_icon.equals("") && !url.equals("")) {
                if ( !url.equals("")) {
                    userDo.addSql(title,remark,url_icon,url);
                    dialog.show();
                    handler.sendEmptyMessageDelayed(0, 2000);

                }
                else {

                    Toast.makeText(Write_note.this, "网址不能为空!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }




    //结束activity的一个生命周期就调用这个方法
    @Override
    protected void onDestroy() {
        super.onDestroy();
        parsePresenter.unsubscribe(); //主动解除订阅
        org.greenrobot.eventbus.EventBus.getDefault().unregister(this);
    }
    //订阅者方法
    public static class MessageEvent {
        public final String message;

        public MessageEvent(String message) {
            this.message = message;
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        Toast.makeText(this, event.message, Toast.LENGTH_SHORT).show();
    }
    @Subscribe
    public void handleSomethingElse(Object event){
        Toast.makeText(this, event.toString(), Toast.LENGTH_SHORT).show();
    }


//    @Override
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.ain_parse:
//                Pattern pattern = Patterns.WEB_URL;
//                final String url = ain_url.getText().toString();
//                System.out.println("网址:"+url);
//                if (TextUtils.isEmpty(url) || !pattern.matcher(url).matches()) {
//                    Toast.makeText(getApplicationContext(), "请输入有效网址", Toast.LENGTH_SHORT).show();
//                } else {
//                    parsePresenter.parseIcon(url);
//                    String allHtml = getHtmlTitle.getHtmlSource(url);
//                    st_title = getHtmlTitle.getTitle(allHtml);
//                }
//                break;
////            case R.id.image_icon:
////                Uri uri = Uri.parse("https://www.baidu.com");
////                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
////                startActivity(intent);
////                break;
//            default:
//                break;
//        }
//    }


    @Override
    public void showProgress() {
        if(progressDialog == null){
            progressDialog = new ProgressDialog(this);
        }
        progressDialog.show();
    }

    @Override
    public void hideProgress() {
        if(progressDialog != null && progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }

    @Override
    public void onParseSuccess(String url) {
        Toast.makeText(this,url, Toast.LENGTH_SHORT).show();
        if(!TextUtils.isEmpty(url)){
            Picasso.get().load(url).into(ain_icon);
            st_icon_url = url;
        }
        else {
            ain_icon.setImageResource(R.drawable.keai);
            st_icon_url = "1";
        }


    }

    @Override
    public void onParseError(Throwable t) {
        Toast.makeText(this,t.getMessage(), Toast.LENGTH_SHORT).show();
        ain_icon.setImageResource(R.drawable.keai);
        st_icon_url = "1";

    }
}
