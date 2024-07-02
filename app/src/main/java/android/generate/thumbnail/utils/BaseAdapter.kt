package android.generate.thumbnail.utils

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

//import androidx.recyclerview.widget.RecyclerView


class BaseAdapter<T> : RecyclerView.Adapter<BaseAdapter.BaseViewHolder<T>>() {
    private var listOfItems: List<T> = ArrayList()
    private var holderBinding: HolderBinding<T>? = null
    private var viewHolder: ViewHolder? = null

    fun setListOfItems(listOfItems: List<T>) {
        this.listOfItems = listOfItems
        notifyDataSetChanged()
    }

    fun setHolderBinding(holderBinding: HolderBinding<T>) {
        this.holderBinding = holderBinding
    }

    fun setViewHolder(viewHolder: ViewHolder) {
        this.viewHolder = viewHolder
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<T> {
        return BaseViewHolder(viewHolder!!.create(parent), holderBinding!!)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<T>, position: Int) {
        holder.bind(listOfItems[position], position)
    }

    override fun getItemCount(): Int {
        return listOfItems.size
    }

    interface HolderBinding<T> {
        fun bind(item: T, view: View, postion: Int)
    }

    interface ViewHolder {
        fun create(parent: ViewGroup): View
    }

    class BaseViewHolder<T>(private val view: View, private val expression: HolderBinding<T>) :
        RecyclerView.ViewHolder(view) {

        fun bind(item: T, postion: Int) {
            expression.bind(item, view, postion)
        }
    }
}
