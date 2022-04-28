package cs.hku.hkutreehole.im

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cs.hku.hkutreehole.R
import cs.hku.hkutreehole.databinding.DialogSelectUserBinding


class UsersDialog(
    context: Context,
    private val onSelected: (User) -> Unit
) :
    BaseBottomSheetDialog(context) {
    private lateinit var binding: DialogSelectUserBinding
    private val adapter by lazy {
        UserAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogSelectUserBinding.inflate(LayoutInflater.from(context))
        setContentView(binding.root)
        binding.run {
            rvList.layoutManager = LinearLayoutManager(context)
            rvList.adapter = adapter
        }
    }

    private fun setInitData() {
       HttpUtil.getAllUser {
           adapter.setNewData(it)
       }
    }

    override fun show() {
        super.show()
        setInitData()
    }

    inner class UserAdapter() :
        RecyclerView.Adapter<UserAdapter.MyViewHolder>() {
        private var items:List<User>? = null

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            return MyViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_user, parent, false)
            )
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            items?.also {
                val item = it[position]
                holder.name.text = "${item.email} [${item.name}]"
                holder.itemView.setOnClickListener {
                    dismiss()
                    onSelected.invoke(item)
                }
            }

        }

        override fun getItemCount(): Int {
            return items?.size?:0
        }

        fun setNewData(it: List<User>?) {
            items = it
            notifyDataSetChanged()
        }

        inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val name: AppCompatTextView = itemView.findViewById(R.id.tv_name)
        }
    }
}