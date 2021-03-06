package cs.hku.hkutreehole.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cs.hku.hkutreehole.MainActivity;
import cs.hku.hkutreehole.R;
import cs.hku.hkutreehole.comment.PostBean;
import cs.hku.hkutreehole.comment.ReplyBean;
import cs.hku.hkutreehole.ui.dashboard.CommentFragment;
import cs.hku.hkutreehole.ui.dashboard.NoScrollListView;


/**
 * Created by Admin on 2016/5/27.
 */
public class PostAdapter extends BaseAdapter {
    private int resourceId;
    private Context context;
    private Handler handler;
    private List<PostBean> list;
    private LayoutInflater inflater;
    private ViewHolder mholder = null;
    private Map<Integer, Boolean> isVisible;
    private LinearLayout whole;
    private View root;

    private Animation animation;//动画效果的

    public PostAdapter(Context context, List<PostBean> list
            , int resourceId, Handler handler, View root) {
        this.list = list;
        this.context = context;
        this.handler = handler;
        this.resourceId = resourceId;
        this.root = root;
        inflater = LayoutInflater.from(context);
        setPaise();
    }

    public void addMap(int id) {
        isVisible.put(id, false);
    }

    private void setPaise() {
        isVisible = new HashMap<Integer, Boolean>();
        for (int i = 0; i < list.size(); i++) {
//规避ID重复的风险
            addMap(list.get(i).getId());
        }
    }



    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        PostBean bean = list.get(position);
//		ViewHolder mholder = null;
        if (convertView == null) {
            mholder = new ViewHolder();
            convertView = inflater.inflate(resourceId, null);
            mholder.commentNickname = (TextView)
                    convertView.findViewById(R.id.comment_name);
            mholder.commentItemTime = (TextView)
                    convertView.findViewById(R.id.comment_time);
            mholder.commentItemContent = (TextView)
                    convertView.findViewById(R.id.comment_content);
            mholder.replyList = (NoScrollListView)
                    convertView.findViewById(R.id.no_scroll_list_reply);
            mholder.mPraiseText = (TextView)
                    convertView.findViewById(R.id.img_zan_num);
            mholder.replyBut = (Button)
                    convertView.findViewById(R.id.but_comment_reply);
            mholder.mDelect = (Button)
                    convertView.findViewById(R.id.but_comment_delect);
            whole = (LinearLayout) convertView.findViewById(R.id.whole);

            convertView.setTag(mholder);
        } else {
            mholder = (ViewHolder) convertView.getTag();
        }

        mholder.commentNickname.setText(bean.getCommentNickname());
        mholder.commentItemTime.setText(bean.getCommentTime());
        mholder.commentItemContent.setText(bean.getCommentContent());
        mholder.mDelect.setVisibility(View.GONE);
        mholder.replyBut.setVisibility(View.GONE);


        final ReplyAdapter adapter = new ReplyAdapter(context, bean.getReplyList(), R.layout.reply_item);
        mholder.replyList.setAdapter(adapter);
        TextviewClickListener tcl = new TextviewClickListener(position);
        mholder.replyBut.setOnClickListener(tcl);
        mholder.mPraiseText.setOnClickListener(tcl);
        mholder.mDelect.setOnClickListener(tcl);
        whole.setOnClickListener(tcl);
        return convertView;
    }


    private final class ViewHolder {
        public TextView commentNickname;            //评论人昵称
        public TextView commentItemTime;            //评论时间
        public TextView commentItemContent;         //评论内容
        public NoScrollListView replyList;          //评论回复列表
        public TextView mPraiseText;                //点赞
        public Button replyBut;                     //回复
        public Button mDelect;                      //删除评论

    }

    /**
     * 获取回复评论
     */
    public void getReplyComment(ReplyBean bean, int position) {
        List<ReplyBean> rList = list.get(position).getReplyList();
        rList.add(rList.size(), bean);
    }

    /**
     * 事件点击监听器
     */
    private final class TextviewClickListener implements View.OnClickListener {
        private int position;

        public TextviewClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.but_comment_reply:
                    handler.sendMessage(handler.obtainMessage(10, position));
                    break;
                case R.id.lyt_comment_zan:
                    if (isVisible.get(list.get(position).getId())){
                        isVisible.put(list.get(position).getId(), false);
                    }else {
                        isVisible.put(list.get(position).getId(), true);
                    }
                    notifyDataSetChanged();
                    break;
                case R.id.but_comment_delect:
                    handler.sendMessage(handler.obtainMessage(11, position));
                    break;
                case R.id.whole:
                    Activity activity = (Activity) context;
                    root.findViewById(R.id.wholes).setVisibility(View.GONE);
                    root.findViewById(R.id.ib_add_notes).setVisibility(View.GONE);
                    activity.getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.wholedash, new CommentFragment(), null)
                            .addToBackStack(null)
                            .commit();
                    Intent intent = ((Activity) context).getIntent();
                    intent.putExtra("id",list.get(position).getCommnetAccount());
                    break;
            }
        }
    }

}
