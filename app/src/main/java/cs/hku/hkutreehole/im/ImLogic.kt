package cs.hku.hkutreehole.im

import com.tencent.imsdk.v2.V2TIMManager
import com.tencent.imsdk.v2.V2TIMMessage
import com.tencent.imsdk.v2.V2TIMSendCallback
import com.tencent.qcloud.tim.uikit.TUIKit
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack
import com.tencent.qcloud.tim.uikit.utils.GenerateTestUserSig

/**
 * Des : 腾讯IM登录处理相关
 */
class ImLogic {
    companion object inst {
        @JvmField
        var loginStatus: Boolean = false

        //登录腾讯IM
        fun initIm(strUserName: String, callback: ImLogicCallback) {
            if (loginStatus) {
                callback.initSuccess()
                return
            }

            TUIKit.login(
                strUserName,
                GenerateTestUserSig.genTestUserSig(strUserName),
                object : IUIKitCallBack {
                    override fun onSuccess(data: Any?) {
                        LL.log("TUIKit.login onSuccess")
                        loginStatus = true
                        callback.initSuccess()
                    }

                    override fun onError(
                        module: String?,
                        errCode: Int,
                        errMsg: String?
                    ) {
                        LL.e("TUIKit.login onError $errMsg")
                    }
                })
        }

        fun sendTextMessage() {
            var msg =
                V2TIMManager.getMessageManager().createTextMessage("1212111");
            V2TIMManager.getMessageManager().sendMessage(
                msg,
                "1000",
                null,
                V2TIMMessage.V2TIM_PRIORITY_DEFAULT,
                false,
                null,
                object : V2TIMSendCallback<V2TIMMessage> {
                    override fun onError(code: Int, desc: String?) {

                    }

                    override fun onSuccess(t: V2TIMMessage?) {
                        LL.log("TUIKit.send onSuccess")
                    }

                    override fun onProgress(progress: Int) {

                    }

                })
        }
    }

    interface ImLogicCallback {
        fun initSuccess()
    }

}

