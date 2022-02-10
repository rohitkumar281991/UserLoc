package com.cartrack.userlocation.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.cartrack.userlocation.R
import com.cartrack.userlocation.data.api.model.UserInfoRepositoryList
import com.cartrack.userlocation.databinding.CardviewBinding
import com.cartrack.userlocation.databinding.ClickListener


class RecyclerViewUserDataAdapter(private val onClickListener: ClickListener) :
    RecyclerView.Adapter<RecyclerViewUserDataAdapter.UserDataViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<UserInfoRepositoryList>() {
        override fun areItemsTheSame(
            oldItem: UserInfoRepositoryList,
            newItem: UserInfoRepositoryList
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: UserInfoRepositoryList,
            newItem: UserInfoRepositoryList
        ): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    fun submitList(list: MutableList<UserInfoRepositoryList>) = differ.submitList(list)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserDataViewHolder {
        val binding: CardviewBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.cardview,
            parent,
            false
        )
        return UserDataViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    class UserDataViewHolder(@NonNull val binding: CardviewBinding) :
        ViewHolder(binding.root)

    override fun onBindViewHolder(holder: UserDataViewHolder, position: Int) {

        val item = differ.currentList[position]
        holder.itemView.apply {
            holder.binding.cvName.text = item.name
            holder.binding.cvUserName.text = item.username
            holder.binding.cvEmail.text = item.email
            holder.binding.cvCity.text = item.address?.city
            holder.binding.cvWebsite.text = item.website
            holder.binding.cvCompany.text = item.company?.name
        }
        holder.itemView.setOnClickListener {
            onClickListener.onRecyclerViewClick(item)
        }
    }

    class OnClickListener(val clickListener: (data: UserInfoRepositoryList) -> Unit) {
        fun onClick(data: UserInfoRepositoryList) = clickListener(data)
    }
}