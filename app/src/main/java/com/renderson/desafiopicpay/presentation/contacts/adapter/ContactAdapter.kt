package com.renderson.desafiopicpay.presentation.contacts.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.renderson.desafiopicpay.R
import com.renderson.desafiopicpay.data.model.User
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.items_contact_list.view.*
import java.util.*
import kotlin.collections.ArrayList

class ContactAdapter(
    private var users: List<User>,
    private val onItemClickListener: ((user: User) -> Unit)
) : RecyclerView.Adapter<ContactAdapter.ContactsViewHolder>(), Filterable {

    private var usersList: List<User> = users

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ContactsViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.items_contact_list, parent, false)
        return ContactsViewHolder(itemView, onItemClickListener)
    }

    override fun getItemCount() = usersList.count()

    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {
        holder.bindView(usersList[position])
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

    override fun getFilter(): Filter? {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    usersList = users
                } else {
                    val filteredList: MutableList<User> =
                        ArrayList()
                    for (row in users) {
                        if (row.name.toLowerCase(Locale.ROOT).contains(charString.toLowerCase(Locale.ROOT)) ||
                            row.username.toLowerCase(Locale.ROOT).contains(charString.toLowerCase(
                                Locale.ROOT
                            )
                            )
                        ) {
                            filteredList.add(row)
                        }
                    }
                    usersList = filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = usersList
                return filterResults
            }

            override fun publishResults(
                charSequence: CharSequence,
                filterResults: FilterResults
            ) {
                usersList = filterResults.values as List<User>
                Log.i("LISTADAPTER", usersList.toString())
                notifyDataSetChanged()
            }
        }
    }
}