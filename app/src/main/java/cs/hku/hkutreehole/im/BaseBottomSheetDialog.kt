package cs.hku.hkutreehole.im

import android.content.Context
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import cs.hku.hkutreehole.R
import java.lang.Exception

open class BaseBottomSheetDialog(context: Context) :
    BottomSheetDialog(context, R.style.PickerBottomSheetDialog) {
    override fun onStart() {
        super.onStart()
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        behavior.isDraggable = true
    }

    override fun show() {
        try {
            super.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}