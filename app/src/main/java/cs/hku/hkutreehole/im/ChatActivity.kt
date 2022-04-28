package cs.hku.hkutreehole.im

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tencent.imsdk.v2.V2TIMConversation
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo
import com.tencent.qcloud.tim.uikit.modules.chat.layout.inputmore.InputMoreActionUnit
import cs.hku.hkutreehole.databinding.ActivityChatBinding

/**
 * 聊天页
 * 传入ChatInfo （对方信息）
 */
class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding

    companion object {
        fun toChat(context: Context, id: String?, name: String?) {
            val chatInfo = ChatInfo()
            chatInfo.type = V2TIMConversation.V2TIM_C2C
            chatInfo.id = id
            chatInfo.chatName = name ?: ""
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("info", chatInfo)
            context.startActivity(intent)
        }
    }

    var mChatInfo: ChatInfo? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        // 从布局文件中获取聊天面板
        // 单聊面板的默认 UI 和交互初始化
        mChatInfo = intent.getSerializableExtra("info") as ChatInfo?
        binding.run {
            chatLayout.chatInfo = mChatInfo
            chatLayout.initDefault()
            chatLayout.inputLayout.disableSendFileAction(true)
            chatLayout.inputLayout.disableVideoRecordAction(true)
            val action = InputMoreActionUnit()
            action.setOnClickListener {
                chatLayout.inputLayout.hideSoftInput()
            }
            chatLayout.inputLayout.addAction(action)
            chatLayout.messageLayout.avatarSize = intArrayOf(40, 40)
        }

    }
}