package com.renderson.desafiopicpay.presentation.contacts.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.renderson.desafiopicpay.R
import com.renderson.desafiopicpay.data.model.User
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.items_contact_list.view.*

class ContactAdapter(
    private val users: List<User>,
    private val onItemClickListener: ((user: User) -> Unit)
) : RecyclerView.Adapter<ContactAdapter.ContactsViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ContactsViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.items_contact_list, parent, false)
        return ContactsViewHolder(itemView, onItemClickListener)
    }

    override fun getItemCount() = users.count()

    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {
        holder.bindView(users[position])
    }

    class ContactsViewHolder(
        itemView: View,
        private val onItemClickListener: ((user: User) -> Unit)
    ) : RecyclerView.ViewHolder(itemView) {

        private val image = itemView.contact_user_image
        private val name = itemView.contact_user_name
        private val username = itemView.contact_user_id

        fun bindView(users: User) {
            username.text = users.username
            name.text = users.name

            Picasso.get()
                .load(users.img)
                .into(image)

            itemView.setOnClickListener {
                onItemClickListener.invoke(users)
            }
        }
    }
}