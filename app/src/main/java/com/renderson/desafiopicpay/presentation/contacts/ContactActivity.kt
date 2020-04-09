package com.renderson.desafiopicpay.presentation.contacts

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.renderson.desafiopicpay.R
import com.renderson.desafiopicpay.presentation.contacts.adapter.ContactAdapter
import kotlinx.android.synthetic.main.activity_contact_main.*

class ContactActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_main)

        textCollapsing.text = "Contacts"
        progressBar.visibility = View.VISIBLE

        val viewModel: ContactsViewModel = ViewModelProviders.of(this).get(
            ContactsViewModel::class.java
        )

        viewModel.contactsLiveData.observe(this, Observer {
            it?.let { users ->
                with(recycler) {
                    layoutManager = LinearLayoutManager(
                        this@ContactActivity,
                        RecyclerView.VERTICAL, false
                    )
                    setHasFixedSize(true)
                    adapter = ContactAdapter(users) { user ->
                        Toast.makeText(
                            applicationContext,
                            "Contato selecionado: ${user.id}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    progressBar.visibility = View.GONE
                }
            }
        })
        viewModel.getUsers()
    }
}
