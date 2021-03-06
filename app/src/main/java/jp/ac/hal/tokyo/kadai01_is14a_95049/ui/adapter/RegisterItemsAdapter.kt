package jp.ac.hal.tokyo.kadai01_is14a_95049.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import jp.ac.hal.tokyo.kadai01_is14a_95049.R
import jp.ac.hal.tokyo.kadai01_is14a_95049.data.RegisterItem
import jp.ac.hal.tokyo.kadai01_is14a_95049.data.RegisteredItemDb
import jp.ac.hal.tokyo.kadai01_is14a_95049.databinding.FragmentRegisterItemDetailViewBinding
import jp.ac.hal.tokyo.kadai01_is14a_95049.ui.DetailPageDirections
import jp.ac.hal.tokyo.kadai01_is14a_95049.ui.RegisterPageMode

class RegisterItemsAdapter(private val itemList: MutableList<RegisterItem>) :
    RecyclerView.Adapter<RegisterItemsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val dataBinding: FragmentRegisterItemDetailViewBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.fragment_register_item_detail_view,
                parent, false
            )
        return ViewHolder(dataBinding.root, dataBinding, parent.context)
    }

    override fun onBindViewHolder(holder: ViewHolder, index: Int) {
        holder.binding.apply {
            val item = itemList[index]

            position = index + 1
            total = itemCount
            model = item
            categoryString = holder.context.getString(item.category.res)

            duplicateCommand = View.OnClickListener {
                it.findNavController().navigate(
                    DetailPageDirections.actionDetailToRegister(item.id, RegisterPageMode.Copy)
                )
            }

            editCommand = View.OnClickListener {
                it.findNavController().navigate(
                    DetailPageDirections.actionDetailToRegister(item.id, RegisterPageMode.Edit)
                )
            }

            deleteCommand = View.OnClickListener {
                RegisteredItemDb.getRegisterItemDb()!!.registerItemDao() // remove from repo
                    .delete(holder.binding.model as RegisterItem)
                itemList.removeAt(index) // remote from cache
                notifyItemRemoved(index) // refresh
            }
        }
    }

    override fun getItemCount() = itemList.size

    data class ViewHolder(
        val itemView: View,
        var binding: FragmentRegisterItemDetailViewBinding,
        val context: Context
    ) :
        RecyclerView.ViewHolder(itemView)
}
