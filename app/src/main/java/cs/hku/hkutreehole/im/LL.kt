package cs.hku.hkutreehole.im

import android.util.Log

class LL {
    companion object {
        fun log(message: String?) {
            if (message != null) {
                Log.d("SPP_log", message)
            }
        }

        fun e(s: String?) {
            if (s != null) {
                Log.e("SPP_log", s)
            }
        }
    }
}