package com.renderson.desafiopicpay.presentation.contacts.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.renderson.desafiopicpay.R
import com.renderson.desafiopicpay.data.model.Contacts
import kotlinx.android.synthetic.main.items_contact_list.view.*

class ContactAdapter(private val contacts: List<Contacts>,
                     val onItemClickListener: ((contact: Contacts) -> Unit)
) : RecyclerView.Adapter<ContactAdapter.ContactsViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ContactsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.items_contact_list, parent, false)
        return ContactsViewHolder(itemView, onItemClickListener)
    }

    override fun getItemCount() = contacts.count()

    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {
        holder.bindView(contacts[position])
    }

    class ContactsViewHolder(
        itemView: View,
        private val onItemClickListener: ((contact: Contacts) -> Unit)
    ) : RecyclerView.ViewHolder(itemView) {

        private val image = itemView.contact_user_image
        private val name = itemView.contact_user_name
        private val userName = itemView.contact_user_name

        fun bindView(contacts: Contacts){
            userName.text = contacts.username
            name.text = contacts.name

            Glide.with(context)
                .load(contacts.img)
                .into(image)
        }
    }

}