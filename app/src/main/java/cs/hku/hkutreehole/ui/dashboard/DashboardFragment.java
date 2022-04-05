package cs.hku.hkutreehole.ui.dashboard;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import cs.hku.hkutreehole.R;
import cs.hku.hkutreehole.adapter.PostAdapter;
import cs.hku.hkutreehole.comment.PostBean;

public class DashboardFragment extends Fragment {

    private PostAdapter adapter;
    private ListView mListData;
    private List<PostBean> list;
    private int count;                    //记录评论ID

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        mListData = (ListView) root.findViewById(R.id.rv_content);
        adapter = new PostAdapter(this.getContext(), getPostData(), R.layout.comment_item_list, handler);
        mListData.setAdapter(adapter);
        hideSystemUI();
        return root;
    }

    public void onResume() {
        super.onResume();
        hideSystemUI();
    }


    private List<PostBean> getPostData() {
        list = new ArrayList<>();
        count = 4;
        for (int i = 0; i < count; i++) {
            PostBean bean = new PostBean();
            bean.setId(i);
            bean.setCommentNickname("User2");
            bean.setCommentTime("13:" + i + "5");
            bean.setCommnetAccount("12345" + i);
            bean.setCommentContent("This is a test comment");
            list.add(bean);
        }
        return list;
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 10:

                    break;
                case 11:

                    break;

            }

        }
    };

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
