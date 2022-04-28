package cs.hku.hkutreehole.ui.notifications

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tencent.imsdk.v2.V2TIMConversation
import com.tencent.imsdk.v2.V2TIMConversationResult
import com.tencent.imsdk.v2.V2TIMManager
import com.tencent.imsdk.v2.V2TIMValueCallback
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo
import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationListLayout
import com.tencent.qcloud.tim.uikit.modules.conversation.base.ConversationInfo
import cs.hku.hkutreehole.databinding.FragmentNotificationsBinding
import cs.hku.hkutreehole.im.ChatActivity
import cs.hku.hkutreehole.im.LL
import cs.hku.hkutreehole.im.UsersDialog

/**
 * 腾讯IM
 * 会话列表Fragment
 */
class NotificationsFragment : Fragment(), V2TIMValueCallback<V2TIMConversationResult?>,
    ConversationListLayout.OnItemLongClickListener {
    private var binding: FragmentNotificationsBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val notificationsViewModel =
            ViewModelProvider(this).get(
                NotificationsViewModel::class.java
            )
        binding = FragmentNotificationsBinding.inflate(
            inflater,
            container,
            false
        )
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.run {
            buttonNewMessage.setOnClickListener {
                showUsersDialog()
            }

        }
    }

    /**
     * 弹出用户列表，选择新的会话
     */
    private fun showUsersDialog() {
        activity?.also { act ->
            UsersDialog(act) { user ->
                ChatActivity.toChat(act, user.email, user.email)
            }.show()
        }
    }


    private fun initConversationLayout() {
        if (binding?.conversationLayout == null) {
            return
        }
        val conversationLayout = binding!!.conversationLayout
        // 初始化聊天面板
        conversationLayout.initDefault()
        // 从 ConversationLayout 获取会话列表
        val listLayout: ConversationListLayout = conversationLayout.conversationList
        listLayout.setItemTopTextSize(16) // 设置 item 中 top 文字大小

        listLayout.setItemBottomTextSize(12) // 设置 item 中 bottom 文字大小

        listLayout.setItemDateTextSize(10) // 设置 item 中 timeline 文字大小
        listLayout.setItemDateTextSize(10) // 设置 item 中 timeline 文字大小
        listLayout.disableItemUnreadDot(false) // 设置 item 是否不显示未读红点，默认显示
        // 长按弹出菜单
        listLayout.setOnItemClickListener { view, position, conversationInfo ->
            val chatInfo = ChatInfo()
            chatInfo.type =
                if (conversationInfo.isGroup) V2TIMConversation.V2TIM_GROUP else V2TIMConversation.V2TIM_C2C
            chatInfo.id = conversationInfo.id
            chatInfo.chatName = conversationInfo.title

            val intent = Intent(activity, ChatActivity::class.java)
            intent.putExtra("info", chatInfo)
            startActivity(intent)
        }
        listLayout.setOnItemLongClickListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onError(code: Int, desc: String?) {
        LL.e("getConversationListSize onError -$code, $desc")

    }

    override fun onSuccess(t: V2TIMConversationResult?) {
        LL.e("getConversationListSize onSuccess ")

    }

    override fun OnItemLongClick(view: View?, position: Int, messageInfo: ConversationInfo?) {

    }

    private fun getConversationListSize() {
        V2TIMManager.getConversationManager()
            .getConversationList(0, 20, this)

    }

    override fun onResume() {
        super.onResume()
        initConversationLayout()
        getConversationListSize()
    }

}