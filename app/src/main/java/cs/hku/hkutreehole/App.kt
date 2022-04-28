package cs.hku.hkutreehole

import android.app.Application
import com.tencent.imsdk.v2.V2TIMSDKConfig
import com.tencent.qcloud.tim.uikit.TUIKit
import com.tencent.qcloud.tim.uikit.config.CustomFaceConfig
import com.tencent.qcloud.tim.uikit.config.GeneralConfig

/**
 * Application 入口
 *
 * 先初始化腾讯IM
 *  TUIKit.init
 */
class App : Application() {
    override fun onCreate() {
        super.onCreate()

        // 配置 Config，请按需配置
        val configs = TUIKit.getConfigs()
        configs.sdkConfig = V2TIMSDKConfig()
        configs.customFaceConfig = CustomFaceConfig()
        configs.generalConfig = GeneralConfig()
        TUIKit.init(this, 1400665466, configs)
    }
}