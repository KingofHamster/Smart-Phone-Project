package cs.hku.hkutreehole.ui.dashboard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cs.hku.hkutreehole.R;
import cs.hku.hkutreehole.adapter.PostAdapter;
import cs.hku.hkutreehole.comment.CommentBean;
import cs.hku.hkutreehole.comment.PostBean;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class DashboardFragment extends Fragment {

    private PostAdapter adapter;
    private ListView mListData;
    private List<PostBean> list;
    private int count;                    //记录评论ID
    private EditText hotwords;
    private LinearLayout wholes;
    private Button hides;
    private Button but_comment_cancel;
    private Button but_comment_send;
    private ImageButton ib_add_notes;
    private ImageButton search;
    private NoTouchLinearLayout edit_vg_lyt;
    private RelativeLayout wholedash;
    private RelativeLayout topbar;
    private EditText edit_comment;
    private EditText edit_title;
    private String nickname = "test user";        //nickname
    private String comment = "";
    private String searchwords = "";
    private String title = "";
    private String listposts = "http://175.178.42.68:8001/api/articles";
    private String addpost = "http://175.178.42.68:8001/api/article";
    private String searchpostu = "http://175.178.42.68:8001/api/article?";
    private boolean isloaded = false;
    private boolean anonymous = false;
    private CheckBox checkbox_anonymous;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        mListData = (ListView) root.findViewById(R.id.rv_content);
        checkbox_anonymous = (CheckBox)root.findViewById(R.id.checkbox_anonymous);
        hotwords = (EditText) root.findViewById(R.id.hotwords);
        edit_comment = (EditText)root.findViewById(R.id.edit_comment);
        edit_title = (EditText)root.findViewById(R.id.edit_title);
        wholes = (LinearLayout) root.findViewById(R.id.wholes);
        wholedash = (RelativeLayout) root.findViewById(R.id.wholedash);
        topbar = (RelativeLayout) root.findViewById(R.id.topbar);
        adapter = new PostAdapter(this.getContext(), getPostData(), R.layout.comment_item_list, handler,root);
        mListData.setAdapter(adapter);
        hides = (Button) root.findViewById(R.id.hides);
        but_comment_cancel = (Button) root.findViewById(R.id.but_comment_cancel);
        but_comment_send = (Button)root.findViewById(R.id.but_comment_send);
        TouchListener tl = new TouchListener();
        FocusChangeListener fl = new FocusChangeListener();
        ClickListener cl = new ClickListener();
        hides.setOnTouchListener(tl);
        but_comment_cancel.setOnClickListener(cl);
        but_comment_send.setOnClickListener(cl);
        hotwords.setOnFocusChangeListener(fl);
        hotwords.setOnClickListener(cl);
//        wholes.setOnTouchListener(tl);
        ib_add_notes = (ImageButton) root.findViewById(R.id.ib_add_notes);
        ib_add_notes.setOnClickListener(cl);
        search = (ImageButton) root.findViewById(R.id.search);
        search.setOnClickListener(cl);
        edit_vg_lyt = (NoTouchLinearLayout) root.findViewById(R.id.edit_vg_lyt);

        Intent intent = getActivity().getIntent();
        nickname = intent.getStringExtra("EmailAddress");

        hideSystemUI();
        return root;
    }


    public void onResume() {
        super.onResume();
        hideSystemUI();
    }

    public void getposts(String listposts, List<PostBean> list) {
        try {
            //我们请求的数据:
//            String data = "account=" + URLEncoder.encode(name, "UTF-8") +
//                    "&password=" + URLEncoder.encode(passwd, "UTF-8");

            //get请求的url
            URL url=new URL(listposts);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            //设置请求方式,请求超时信息
            conn.setRequestMethod("GET");
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);
//            //设置运行输入,输出:
//            conn.setDoOutput(true);
//            conn.setDoInput(true);
//            //Post方式不能缓存,需手动设置为false
//            conn.setUseCaches(false);
//
//            //设置请求的头信息
//            conn.setRequestProperty("staffid",StaffId );  //当前请求用户StaffId
//            conn.setRequestProperty("timestamp", ApiHelper.GetTimeStamp()); //发起请求时的时间戳（单位：毫秒）
//            conn.setRequestProperty("nonce", ApiHelper.GetRandom()); //发起请求时的随机数

            //开启连接
            conn.connect();
            InputStream inputStream=null;
            BufferedReader reader=null;
            //如果应答码为200的时候，表示成功的请求带了，这里的HttpURLConnection.HTTP_OK就是200
            if(conn.getResponseCode()==HttpURLConnection.HTTP_OK){
                //获得连接的输入流
                inputStream=conn.getInputStream();
                //转换成一个加强型的buffered流
                reader=new BufferedReader(new InputStreamReader(inputStream));
                //把读到的内容赋值给result
                String result = reader.readLine();
                JSONObject json_test = new JSONObject(result);
                Iterator<String> keys = json_test.keys();
                count = 0;
                while(keys.hasNext()){
                    String id = keys.next();
                    JSONObject jsonObject = json_test.getJSONObject(id);
                    //打印json 数据
                    PostBean bean = new PostBean();
                    bean.setId(count);
                    bean.setCommentNickname(jsonObject.get("title").toString());
                    bean.setCommentTime(jsonObject.get("createTime").toString());
                    bean.setCommnetAccount(jsonObject.get("id").toString());
                    bean.setCommentContent(jsonObject.get("text").toString());
                    list.add(bean);
                    Log.e("json", jsonObject.get("id").toString());
                    Log.e("json", jsonObject.get("title").toString());
                    Log.e("json", jsonObject.get("createTime").toString());
                    count++;
                }
                handler.sendMessage(handler.obtainMessage(2));
            }
            //关闭流和连接
            reader.close();
            inputStream.close();
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<PostBean> getPostData() {
        list = new ArrayList<>();
        new Thread(new Runnable(){
            @Override
            public void run() {
                getposts(listposts, list);
            }
        }).start();
        return list;
    }

    public void searchpost(String getposts) {
        try {
            //我们请求的数据:
            String data = "title=" + URLEncoder.encode(searchwords, "UTF-8");

            //get请求的url
            URL url=new URL(getposts + data);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            //设置请求方式,请求超时信息
            conn.setRequestMethod("GET");
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);
//            //设置运行输入,输出:
//            conn.setDoOutput(true);
//            conn.setDoInput(true);
//            //Post方式不能缓存,需手动设置为false
//            conn.setUseCaches(false);
//
//            //设置请求的头信息
//            conn.setRequestProperty("staffid",StaffId );  //当前请求用户StaffId
//            conn.setRequestProperty("timestamp", ApiHelper.GetTimeStamp()); //发起请求时的时间戳（单位：毫秒）
//            conn.setRequestProperty("nonce", ApiHelper.GetRandom()); //发起请求时的随机数

            //开启连接
            conn.connect();
            InputStream inputStream=null;
            BufferedReader reader=null;
            //如果应答码为200的时候，表示成功的请求带了，这里的HttpURLConnection.HTTP_OK就是200
            if(conn.getResponseCode()==HttpURLConnection.HTTP_OK){
                //获得连接的输入流
                inputStream=conn.getInputStream();
                //转换成一个加强型的buffered流
                reader=new BufferedReader(new InputStreamReader(inputStream));
                //把读到的内容赋值给result
                String result = reader.readLine();
                JSONObject temp = new JSONObject(result);
                Iterator<String> keys = temp.keys();
                count = 0;
                while(keys.hasNext()){
                    String id = keys.next();
                    JSONObject jsonObject = temp.getJSONObject(id);
                    //打印json 数据
                    PostBean bean = new PostBean();
                    bean.setId(count);
                    bean.setCommentNickname(jsonObject.get("title").toString());
                    bean.setCommentTime(jsonObject.get("createTime").toString());
                    bean.setCommnetAccount(jsonObject.get("id").toString());
                    bean.setCommentContent(jsonObject.get("text").toString());
                    list.add(bean);
                    Log.e("json", jsonObject.get("id").toString());
                    Log.e("json", jsonObject.get("title").toString());
                    Log.e("json", jsonObject.get("createTime").toString());
                    count++;
                }
                handler.sendMessage(handler.obtainMessage(2));
            }
            //关闭流和连接
            reader.close();
            inputStream.close();
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 2:
                    isloaded = true;
                    adapter.notifyDataSetChanged();
                    break;
                case 3:
                    list.clear();
                    new Thread(new Runnable(){
                        @Override
                        public void run() {
                            getposts(listposts, list);
                        }
                    }).start();
                    break;
                case 10:

                    break;
                case 11:

                    break;

            }

        }
    };

    private boolean isEditEmply() {
        comment = edit_comment.getText().toString().trim();
        title = edit_title.getText().toString().trim();
        if (comment.equals("")||title.equals("")) {
            Toast.makeText(getActivity().getApplicationContext(), "Parameter empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        edit_comment.setText("");
        edit_title.setText("");
        anonymous = checkbox_anonymous.isChecked();
        return true;
    }

    private boolean isSearchEmply() {
        searchwords = hotwords.getText().toString().trim();
        if (searchwords.equals("")) {
            Toast.makeText(getActivity().getApplicationContext(), "Search empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        hotwords.setText("");
        return true;
    }

    private final class ClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            InputMethodManager imm = (InputMethodManager)
                    edit_comment.getContext().getSystemService(INPUT_METHOD_SERVICE);
            InputMethodManager imm2 = (InputMethodManager)
                    hotwords.getContext().getSystemService(INPUT_METHOD_SERVICE);
            switch (view.getId()){
                case R.id.search:
                    if (isSearchEmply()) {        //判断用户是否输入内容
                        list.clear();
                        new Thread(new Runnable(){
                            @Override
                            public void run() {
                                searchpost(searchpostu);
                            }
                        }).start();
                        hides.setVisibility(View.GONE);
                        edit_vg_lyt.setVisibility(View.GONE);
                        ib_add_notes.setVisibility(View.VISIBLE);
                        imm2.hideSoftInputFromWindow(hotwords.getWindowToken(), 0);
                        hideSystemUI();
                    }
                    break;
                case R.id.but_comment_send:        //发表评论按钮
                    if (isEditEmply()) {        //判断用户是否输入内容
                        publishPost();
                        edit_vg_lyt.setVisibility(View.GONE);
                        ib_add_notes.setVisibility(View.VISIBLE);
                        imm.hideSoftInputFromWindow(edit_comment.getWindowToken(), 0);
                        hideSystemUI();
                        ib_add_notes.requestFocusFromTouch();
                        hides.setVisibility(View.GONE);
                    }
                    break;
                case R.id.but_comment_cancel:        //发表按钮
                    edit_vg_lyt.setVisibility(View.GONE);
                    ib_add_notes.setVisibility(View.VISIBLE);
                    imm.hideSoftInputFromWindow(edit_comment.getWindowToken(), 0);
                    hideSystemUI();
                    ib_add_notes.requestFocusFromTouch();
                    hides.setVisibility(View.GONE);
                    break;
                case R.id.hotwords:
                    sethidesheighthotwords();
                    hides.setVisibility(View.VISIBLE);
                    break;
                case R.id.ib_add_notes:
                    sethidesheightnewpost();
                    ib_add_notes.setVisibility(View.GONE);
                    gainfocus();
                    hides.setVisibility(View.VISIBLE);
                    edit_vg_lyt.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

    private final class TouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            InputMethodManager imm = (InputMethodManager)
                    hotwords.getContext().getSystemService(INPUT_METHOD_SERVICE);
            switch (view.getId()) {
                case R.id.hides:        //wholes
                    hides.setVisibility(View.GONE);
                    edit_vg_lyt.setVisibility(View.GONE);
                    ib_add_notes.setVisibility(View.VISIBLE);
                    imm.hideSoftInputFromWindow(hotwords.getWindowToken(), 0);
                    hideSystemUI();
                    break;
//                case R.id.hotwords:
//                    hides.setVisibility(View.VISIBLE);
//                    hotwords.requestFocus();//获取焦点
//                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//                    break;
            }
            return true;
        }
    }

    private final class FocusChangeListener implements View.OnFocusChangeListener {
        @Override
        public void onFocusChange(View view, boolean b) {
            if (view.getId()==R.id.hotwords&&b) {
                sethidesheighthotwords();
                hides.setVisibility(View.VISIBLE);
            }
        }
    }

    private void sethidesheighthotwords(){
        int height2 = topbar.getHeight();
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(hides.getLayoutParams());
        params.setMargins(0,height2,0,0);
        hides.setLayoutParams(params);
    }

    private void sethidesheightnewpost(){
        int height2 = edit_vg_lyt.getHeight();
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(hides.getLayoutParams());
        params.setMargins(0,height2,0,0);
        hides.setLayoutParams(params);
    }

    private void gainfocus(){
        InputMethodManager imm = (InputMethodManager)
                edit_title.getContext().getSystemService(INPUT_METHOD_SERVICE);
        edit_title.requestFocus();//获取焦点
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void addpost(String listposts) {
        try {
            //我们请求的数据:
//            String data = "account=" + URLEncoder.encode(name, "UTF-8") +
//                    "&password=" + URLEncoder.encode(passwd, "UTF-8");

            //get请求的url
            URL url=new URL(listposts);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            //设置请求方式,请求超时信息
            conn.setRequestMethod("POST");
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);
//            //设置运行输入,输出:
            conn.setDoOutput(true);
            conn.setDoInput(true);
//            //Post方式不能缓存,需手动设置为false
            conn.setUseCaches(false);
//
//            //设置请求的头信息
//            conn.setRequestProperty("staffid",StaffId );  //当前请求用户StaffId
//            conn.setRequestProperty("timestamp", ApiHelper.GetTimeStamp()); //发起请求时的时间戳（单位：毫秒）
//            conn.setRequestProperty("nonce", ApiHelper.GetRandom()); //发起请求时的随机数
            conn.setRequestProperty("Content-Type","application/json");

            //开启连接
            conn.connect();
            DataOutputStream out = new DataOutputStream(conn.getOutputStream());
            Log.d("commnet",comment);
            String content="{\"author\":\""+URLEncoder.encode(nickname,"UTF-8")+
                    "\",\"title\":\"" + title+
                    "\",\"text\":\"" + comment+
                    "\",\"anonymous\":\"" + URLEncoder.encode(String.valueOf(anonymous),"UTF-8")+
                    "\"}";
            Log.d("content",content);
            out.write(content.getBytes(StandardCharsets.UTF_8));
            out.flush();
            out.close();
            InputStream inputStream=null;
            BufferedReader reader=null;
            //如果应答码为200的时候，表示成功的请求带了，这里的HttpURLConnection.HTTP_OK就是200
            if(conn.getResponseCode()==HttpURLConnection.HTTP_OK){
                //获得连接的输入流
                inputStream=conn.getInputStream();
                //转换成一个加强型的buffered流
                reader=new BufferedReader(new InputStreamReader(inputStream));
                //把读到的内容赋值给result
                String result = reader.readLine();
                Log.d("result",result);
                handler.sendMessage(handler.obtainMessage(3));
            }
            //关闭流和连接
            reader.close();
            inputStream.close();
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void publishPost() {
        new Thread(new Runnable(){
            @Override
            public void run() {
                addpost(addpost);
            }
        }).start();
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
//        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
//        String str = formatter.format(curDate);
//        PostBean bean = new PostBean();
//        bean.setCommentNickname(title);
//        bean.setCommentTime(str);
//        bean.setCommnetAccount(nickname);
//        bean.setCommentContent(comment);
//        list.add(bean);
//        list.add(0, bean);//加载到list的最前面
//        count++;
//        handler.sendMessage(handler.obtainMessage(2));
    }

    private void hideSystemUI() {
        // Enables sticky immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE_STICKY.
        // Or for "regular immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE
        View decorView = getActivity().getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    private void showSystemUI() {
        View decorView = getActivity().getWindow().getDecorView();
        decorView.setSystemUiVisibility(

                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
    }

    @Override
    public void onDestroyView() {
        showSystemUI();
        super.onDestroyView();
    }
}
