package cs.hku.hkutreehole.ui.dashboard;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import android.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cs.hku.hkutreehole.R;
import cs.hku.hkutreehole.adapter.CommentAdapter;
import cs.hku.hkutreehole.comment.CommentBean;
import cs.hku.hkutreehole.comment.ReplyBean;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class CommentFragment extends Fragment {

    private ListView mListData;
    private LinearLayout mLytCommentVG;
    private NoTouchLinearLayout mLytEdittextVG;
    private EditText mCommentEdittext;
    private Button mSendBut;
    private Button mCancelBut;
    private List<CommentBean> list;
    private CommentAdapter adapter;
    private int count;                    //记录评论ID
    private String comment = "";        //记录对话框中的内容
    private String nickname = "test user";        //nickname
    private String author = "test user";        //nickname
    private int position;                //记录回复评论的索引
    private boolean isReply;            //是否是回复，true代表回复



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_comment, container, false);
        mListData = (ListView) root.findViewById(R.id.list_data);
        mLytCommentVG = (LinearLayout) root.findViewById(R.id.comment_vg_lyt);
        mLytEdittextVG = (NoTouchLinearLayout) root.findViewById(R.id.edit_vg_lyt);
        mCommentEdittext = (EditText) root.findViewById(R.id.edit_comment);
        mSendBut = (Button) root.findViewById(R.id.but_comment_send);
        mCancelBut = (Button) root.findViewById(R.id.but_comment_cancel);
        ClickListener cl = new ClickListener();
        mSendBut.setOnClickListener(cl);
        mLytCommentVG.setOnClickListener(cl);
        mCancelBut.setOnClickListener(cl);
        Log.d("TAGOFTHEAPP","initial");
        adapter = new CommentAdapter(getActivity().getApplicationContext(), getCommentData(), R.layout.comment_item_list, handler);
        mListData.setAdapter(adapter);
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

    private List<CommentBean> getCommentData() {
        list = new ArrayList<>();
        count = 4;
        for (int i = 0; i < count; i++) {
            CommentBean bean = new CommentBean();
            bean.setId(i);
            bean.setCommentNickname("User2");
            bean.setCommentTime("13:" + i + "5");
            bean.setCommnetAccount("12345" + i);
            bean.setCommentContent("This is a test comment");
            bean.setReplyList(getReplyData());
            list.add(bean);
        }
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
        return true;
    }

    /**
     * 发表评论
     */
    private void publishComment() {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM月/dd HH:mm:ss ");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);

        CommentBean bean = new CommentBean();
        bean.setId(count);
        bean.setCommentNickname(nickname);
        bean.setCommentTime(str);
        bean.setCommnetAccount("12345" + count);
        bean.setCommentContent(comment);
        list.add(0, bean);//加载到list的最前面
        adapter.addMap(count);
        count++;
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
        bean.setReplyNickname(nickname);
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
                case 10:
                    isReply = true;
                    position = (Integer) msg.obj;
                    mLytCommentVG.setVisibility(View.GONE);
                    mLytEdittextVG.setVisibility(View.VISIBLE);
                    onFocusChange(true);
                    break;
                case 11:
                    isReply = false;
                    position = (Integer) msg.obj;
                    DelectComment(position);
                    break;

            }

        }
    };

    private final class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.but_comment_send:        //发表评论按钮
                    if (isEditEmply()) {        //判断用户是否输入内容
                        if (isReply) {
                            replyComment();
                        } else {
                            publishComment();
                        }
                        hideSystemUI();
                        mLytCommentVG.setVisibility(View.VISIBLE);
                        mLytEdittextVG.setVisibility(View.GONE);
                        onFocusChange(false);
                    }
                    break;
                case R.id.but_comment_cancel:        //发表按钮
                    hideSystemUI();
                    mLytCommentVG.setVisibility(View.VISIBLE);
                    mLytEdittextVG.setVisibility(View.GONE);
                    onFocusChange(false);
                    break;
                case R.id.comment_vg_lyt:        //底部评论按钮
                    isReply = false;
                    mLytEdittextVG.setVisibility(View.VISIBLE);
                    mLytCommentVG.setVisibility(View.GONE);
                    onFocusChange(true);
                    break;
            }
        }
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