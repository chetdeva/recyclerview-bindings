package com.fueled.recyclerviewbindings.adapter

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup

import com.fueled.recyclerviewbindings.R
import com.fueled.recyclerviewbindings.databinding.ItemMainBinding
import com.fueled.recyclerviewbindings.databinding.ItemProgressBinding
import com.fueled.recyclerviewbindings.model.UserModel
import com.fueled.recyclerviewbindings.util.inflate

/**
 * @author chetansachdeva on 04/06/17
 */

class RVAdapter(private val list: MutableList<UserModel?>, private val listener: (UserModel?) -> Unit) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return if (null == list[position]) ITEM_PROGRESS
        else super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ITEM_PROGRESS) VHProgress(parent.inflate(R.layout.item_progress))
        else VHUser(parent.inflate(R.layout.item_main))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is VHUser) holder.bind(list[position], listener)
        else if (holder is VHProgress) holder.bind(true)
    }

    override fun getItemCount() = list.size

    /**
     * add list of items and notify
     */
    fun addAll(users: List<UserModel>) {
        list.addAll(users)
        notifyItemRangeChanged(users.size, list.size - 1)
    }

    /**
     * add an item and notify
     */
    fun add(user: UserModel?) {
        list.add(user)
        notifyItemInserted(list.size - 1)
    }

    /**
     * remove an item and notify
     */
    fun remove(position: Int) {
        list.removeAt(position)
        notifyItemRemoved(list.size)
    }

    /**
     * clear all items and notify
     */
    fun clear() {
        list.clear()
        notifyDataSetChanged()
    }

    /**
     * UserModel ViewHolder
     */
    internal class VHUser(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: ItemMainBinding = DataBindingUtil.bind(itemView)
        fun bind(user: UserModel?, listener: (UserModel?) -> Unit) {
            binding.user = user
            itemView.setOnClickListener { listener(user) }
        }
    }

    /**
     * Progress ViewHolder
     */
    internal class VHProgress(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: ItemProgressBinding = DataBindingUtil.bind(itemView)
        fun bind(isIndeterminate: Boolean) {
            binding.pb.isIndeterminate = isIndeterminate
        }
    }

    companion object {
        private val ITEM_PROGRESS = -1
    }
}
