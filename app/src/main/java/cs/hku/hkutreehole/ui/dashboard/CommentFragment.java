package cs.hku.hkutreehole.ui.dashboard;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import android.app.Fragment;

import org.json.JSONException;
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

import cs.hku.hkutreehole.R;
import cs.hku.hkutreehole.adapter.CommentAdapter;
import cs.hku.hkutreehole.comment.CommentBean;
import cs.hku.hkutreehole.comment.PostBean;
import cs.hku.hkutreehole.comment.ReplyBean;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class CommentFragment extends Fragment {

    private ListView mListData;
    private LinearLayout mLytCommentVG;
    private NoTouchLinearLayout mLytEdittextVG;
    private EditText mCommentEdittext;
    private TextView edit_title;
    private Button mSendBut;
    private Button mCancelBut;
    private List<CommentBean> list;
    private CommentAdapter adapter;
    private int count;                    //记录评论ID
    private String comment = "";        //记录对话框中的内容
    private String title = "";
    private String nickname = "test user";        //nickname
    private String user = "";
    private String id = "";
    private String content = "";
    private String getposts = "http://175.178.42.68:8001/api/article?";
    private String getcomment = "http://175.178.42.68:8001/api/comment?";
    private String addpost = "http://175.178.42.68:8001/api/article";
    private String addcomment = "http://175.178.42.68:8001/api/comment";
    private int position;                //记录回复评论的索引
    private boolean isReply;            //是否是回复，true代表回复
    private boolean anonymous = false;
    private boolean isEdit = false;
    private Button hides;
    private Button button_edit;
    private LinearLayout ll_comment_test;
    private TextView author;
    private TextView text;
    private TextView creationtime;
    private CheckBox checkbox_anonymous;
    JSONObject json_test = new JSONObject();



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_comment, container, false);
        checkbox_anonymous = (CheckBox)root.findViewById(R.id.checkbox_anonymous);
        edit_title = (TextView) root.findViewById(R.id.edit_title);
        author = (TextView) root.findViewById(R.id.author);
        creationtime = (TextView) root.findViewById(R.id.creationtime);
        text = (TextView) root.findViewById(R.id.text);
        mListData = (ListView) root.findViewById(R.id.list_data);
        mLytCommentVG = (LinearLayout) root.findViewById(R.id.comment_vg_lyt);
        ll_comment_test = (LinearLayout) root.findViewById(R.id.ll_comment_test);
        mLytEdittextVG = (NoTouchLinearLayout) root.findViewById(R.id.edit_vg_lyt);
        mCommentEdittext = (EditText) root.findViewById(R.id.edit_comment);
        mSendBut = (Button) root.findViewById(R.id.but_comment_send);
        button_edit = (Button)root.findViewById(R.id.button_edit);
        mCancelBut = (Button) root.findViewById(R.id.but_comment_cancel);
        hides = (Button) root.findViewById(R.id.hides);
        ClickListener cl = new ClickListener();
        mSendBut.setOnClickListener(cl);
        mLytCommentVG.setOnClickListener(cl);
        mCancelBut.setOnClickListener(cl);
        hides.setOnClickListener(cl);
        button_edit.setOnClickListener(cl);
        Log.d("TAGOFTHEAPP","initial");
        adapter = new CommentAdapter(getActivity().getApplicationContext(), getCommentData(), R.layout.comment_item_list, handler);
        mListData.setAdapter(adapter);

        Intent intent = getActivity().getIntent();
        user = intent.getStringExtra("EmailAddress");
        id = intent.getStringExtra("id");
        Log.d("id",id);
        new Thread(new Runnable(){
            @Override
            public void run() {
                getinfo(getposts);
            }
        }).start();

        hideSystemUI();
        return root;
    }

    public CommentFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static CommentFragment newInstance(String param1, String param2) {
        CommentFragment fragment = new CommentFragment();
        Bundle args = new Bundle();
        args.putString("ARG_PARAM1", param1);
        args.putString("ARG_PARAM2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    private void initViews(@NonNull LayoutInflater inflater,ViewGroup container) {
        View root = inflater.inflate(R.layout.fragment_comment, container, false);

    }

    public void getinfo(String getposts) {
        try {
            //我们请求的数据:
            String data = "id=" + URLEncoder.encode(id, "UTF-8");

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
                json_test = new JSONObject(result);
                //打印json 数据
                handler.sendMessage(handler.obtainMessage(1));
                Log.e("json", json_test.get("id").toString());
                Log.e("json", json_test.get("title").toString());
                Log.e("json", json_test.get("createTime").toString());
            }
            //关闭流和连接
            reader.close();
            inputStream.close();
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getcomments(String getcomment, List<CommentBean> list) {
        try {
            //我们请求的数据:
            String data = "id=" + URLEncoder.encode(id, "UTF-8");
//                    "&password=" + URLEncoder.encode(passwd, "UTF-8");
            //get请求的url
            URL url=new URL(getcomment + data);
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
                while (keys.hasNext()){
                    String id = keys.next();
                    JSONObject jsonObject = json_test.getJSONObject(id);
                    //打印json 数据
                    CommentBean bean = new CommentBean();
                    bean.setId(count);
                    bean.setCommentNickname(jsonObject.get("author").toString());
                    if(jsonObject.getBoolean("anonymous"))
                        bean.setCommentNickname("Anonymous Author");
                    bean.setCommentTime(jsonObject.get("createTime").toString());
                    bean.setCommnetAccount(jsonObject.get("author").toString());
                    bean.setCommentContent(jsonObject.get("text").toString());
                    bean.setAnonymous(jsonObject.getBoolean("anonymous"));
                    bean.setReplyList(getReplyData());
                    if(jsonObject.get("author")==user)
                        bean.setSame(TRUE);
                    else
                        bean.setSame(FALSE);
                    list.add(bean);
                    Log.e("json", jsonObject.get("author").toString());
                    Log.e("json", jsonObject.get("text").toString());
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

    private List<CommentBean> getCommentData() {
        list = new ArrayList<>();
        new Thread(new Runnable(){
            @Override
            public void run() {
                getcomments(getcomment, list);
            }
        }).start();
        return list;
    }

    private List<ReplyBean> getReplyData() {
        List<ReplyBean> replyList = new ArrayList<>();
        return replyList;
    }

    /**
     * 显示或隐藏输入法
     */
    private void onFocusChange(boolean hasFocus) {
        final boolean isFocus = hasFocus;
        (new Handler()).postDelayed(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            public void run() {
                InputMethodManager imm = (InputMethodManager)
                        mCommentEdittext.getContext().getSystemService(INPUT_METHOD_SERVICE);
                if (isFocus) {
                    //显示输入法
                    if(isEdit)
                        mCommentEdittext.requestFocus();//获取焦点
                    else
                        mCommentEdittext.requestFocus();//获取焦点
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                } else {
                    //隐藏输入法
                    imm.hideSoftInputFromWindow(mCommentEdittext.getWindowToken(), 0);
                    mLytCommentVG.setVisibility(View.VISIBLE);
                    mLytEdittextVG.setVisibility(View.GONE);
                }
            }
        }, 100);
    }

    /**
     * 点击屏幕其他地方收起输入法
     */

    /**
     * 隐藏或者显示输入框
     */
    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            /**
             *这堆数值是算我的下边输入区域的布局的，
             * 规避点击输入区域也会隐藏输入区域
             */

            v.getLocationInWindow(leftTop);
            int left = leftTop[0] - 50;
            int top = leftTop[1] - 50;
            int bottom = top + v.getHeight() + 300;
            int right = left + v.getWidth() + 120;
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断对话框中是否输入内容
     */
    private boolean isEditEmply() {
        comment = mCommentEdittext.getText().toString().trim();
        if (comment.equals("")) {
            Toast.makeText(getActivity().getApplicationContext(), "Haven't written", Toast.LENGTH_SHORT).show();
            return false;
        }
        mCommentEdittext.setText("");
        anonymous = checkbox_anonymous.isChecked();
        checkbox_anonymous.setChecked(false);
        return true;
    }

    private boolean isEditButtonEmply() {
        comment = mCommentEdittext.getText().toString().trim();
        title = edit_title.getText().toString().trim();
        if (comment.equals("")||title.equals("")) {
            Toast.makeText(getActivity().getApplicationContext(), "Parameters empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        mCommentEdittext.setText("");
        edit_title.setText("");
        edit_title.setVisibility(View.GONE);
        anonymous = checkbox_anonymous.isChecked();
        checkbox_anonymous.setChecked(false);
        return true;
    }

    public void addcomment(String listposts) {
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
            String content="{\"articleID\":\""+URLEncoder.encode(id,"UTF-8")+
                    "\",\"text\":\"" + comment+
                    "\",\"author\":\"" + user+
                    "\",\"anonymous\":" + URLEncoder.encode(String.valueOf(anonymous),"UTF-8")+
                    "}";
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
            }
            //关闭流和连接
            reader.close();
            inputStream.close();
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发表评论
     */
    private void publishComment() {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);

        CommentBean bean = new CommentBean();
        bean.setId(count);
        bean.setCommentNickname(user);
        bean.setCommentTime(str);
        bean.setCommnetAccount(user);
        bean.setCommentContent(comment);
        bean.setSame(TRUE);
        list.add(0, bean);//加载到list的最前面
        adapter.addMap(count);
        count++;
        handler.sendMessage(handler.obtainMessage(3));
        adapter.notifyDataSetChanged();
    }

    private void DelectComment(int postion) {
        list.remove(postion);
        adapter.notifyDataSetChanged();
    }


    /**
     * 回复评论
     */
    private void replyComment() {
        ReplyBean bean = new ReplyBean();
        bean.setId(count + 10);
        bean.setCommentNickname(list.get(position).getCommentNickname());
        bean.setReplyNickname(user);
        bean.setReplyContent(comment);
        adapter.getReplyComment(bean, position);
        adapter.notifyDataSetChanged();
    }


    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 1:
                    try {
                        if(json_test.getBoolean("anonymous"))
                            nickname = "Anonymous Author";
                        else
                            nickname = json_test.get("author").toString();
                        author.setText(json_test.get("title").toString());
                        text.setText(nickname + '\n' + json_test.get("text"));
                        content = json_test.get("text").toString();
                        creationtime.setText(json_test.get("createTime").toString());
                        if(user.equals(nickname))
                            button_edit.setVisibility(View.VISIBLE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    adapter.notifyDataSetChanged();
                    break;
                case 3:
                    new Thread(new Runnable(){
                        @Override
                        public void run() {
                            addcomment(addcomment);
                        }
                    }).start();
                    break;
                case 4:
                    heightchange();
                    break;
                case 10:
                    isReply = true;
                    position = (Integer) msg.obj;
                    mLytCommentVG.setVisibility(View.GONE);
                    mLytEdittextVG.setVisibility(View.VISIBLE);
                    onFocusChange(true);
                    hides.setVisibility(View.VISIBLE);
                    handler.sendMessage(handler.obtainMessage(4));
                    break;
                case 11:
                    isReply = false;
                    position = (Integer) msg.obj;
                    DelectComment(position);
                    break;

            }

        }
    };

    private void changetoedit()
    {
        isEdit = true;
        isReply = false;
        hides.setVisibility(View.VISIBLE);
        mLytEdittextVG.setVisibility(View.VISIBLE);
        mLytCommentVG.setVisibility(View.GONE);
        onFocusChange(true);
        edit_title.setVisibility(View.VISIBLE);
        edit_title.setText(author.getText());
        mCommentEdittext.setText(content);
        mSendBut.setText("Edit");
        handler.sendMessage(handler.obtainMessage(4));
    }
    private void changetoreply()
    {
        isEdit = false;
        edit_title.setVisibility(View.GONE);
        edit_title.setText("");
        mCommentEdittext.setText("");
        mSendBut.setText("Reply");
    }

    public void editpost(String listposts) {
        try {
            //我们请求的数据:
//            String data = "account=" + URLEncoder.encode(name, "UTF-8") +
//                    "&password=" + URLEncoder.encode(passwd, "UTF-8");

            //get请求的url
            URL url=new URL(listposts);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            //设置请求方式,请求超时信息
            conn.setRequestMethod("PUT");
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
            String content="{\"id\":\""+URLEncoder.encode(id,"UTF-8")+
                    "\",\"text\":\"" + comment+
                    "\",\"anonymous\":" + URLEncoder.encode(String.valueOf(anonymous),"UTF-8")+
                    "}";
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

    private final class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.but_comment_send:        //发表评论按钮
                    if (isEdit) {
                        if(isEditButtonEmply()) {
                            changetoreply();
                            hideSystemUI();
                            hides.setVisibility(View.GONE);
                            mLytCommentVG.setVisibility(View.VISIBLE);
                            mLytEdittextVG.setVisibility(View.GONE);
                            author.setText(title);
                            text.setText(nickname + '\n' + comment);
                            content = comment;
                            onFocusChange(false);
                            new Thread(new Runnable(){
                                @Override
                                public void run() {
                                    editpost(addpost);
                                }
                            }).start();
                        }
                    }
                    else if (isEditEmply()) {        //判断用户是否输入内容
                        if (isReply) {
                            replyComment();
                        }
                        else {
                            publishComment();
                        }
                        hideSystemUI();
                        hides.setVisibility(View.GONE);
                        mLytCommentVG.setVisibility(View.VISIBLE);
                        mLytEdittextVG.setVisibility(View.GONE);
                        onFocusChange(false);
                    }

                    break;
                case R.id.but_comment_cancel:        //发表按钮
                    hideSystemUI();
                    changetoreply();
                    hides.setVisibility(View.GONE);
                    mLytCommentVG.setVisibility(View.VISIBLE);
                    mLytEdittextVG.setVisibility(View.GONE);
                    onFocusChange(false);
                    break;
                case R.id.comment_vg_lyt:        //底部评论按钮

                    isReply = false;
                    hides.setVisibility(View.VISIBLE);
                    mLytEdittextVG.setVisibility(View.VISIBLE);
                    mLytCommentVG.setVisibility(View.GONE);
                    onFocusChange(true);
                    handler.sendMessage(handler.obtainMessage(4));
                    break;
                case R.id.button_edit:
                    changetoedit();
                    break;
                case R.id.hides:
                    changetoreply();
                    hides.setVisibility(View.GONE);
                    hideSystemUI();
                    mLytCommentVG.setVisibility(View.VISIBLE);
                    mLytEdittextVG.setVisibility(View.GONE);
                    onFocusChange(false);
                    break;
            }
        }
    }

    private void heightchange() {
        int height2 = ll_comment_test.getHeight();
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(hides.getLayoutParams());
        params.setMargins(0,height2,0,0);
        hides.setLayoutParams(params);
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