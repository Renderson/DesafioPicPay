package com.renderson.desafiopicpay.presentation.contacts

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.renderson.desafiopicpay.R
import com.renderson.desafiopicpay.presentation.contacts.adapter.ContactAdapter
import kotlinx.android.synthetic.main.activity_contact_main.*
import kotlinx.android.synthetic.main.activity_search_user.*

class ContactActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_main)

        textCollapsing.text = getString(R.string.txt_contacts)
        //progressBar.visibility = View.VISIBLE

        val viewModel: ContactsViewModel = ViewModelProviders.of(this).get(
            ContactsViewModel::class.java
        )

        getValueViewModel(viewModel)
        showMessage(viewModel)
    }

    private fun getValueViewModel(viewModel: ContactsViewModel) {

        viewModel.contactsLiveData.observe(this, Observer {
            it?.let { users ->
                with(recycler) {
                    layoutManager = LinearLayoutManager(
                        this@ContactActivity,
                        RecyclerView.VERTICAL, false
                    )
                    setHasFixedSize(true)
                    adapter = ContactAdapter(users) { user ->
                        startActivity(Intent(this@ContactActivity, PaymentActivity::class.java))
                        this@ContactActivity.startActivity(intent)
                        Toast.makeText(
                            applicationContext,
                            "Contato selecionado: ${user.id}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    searchListener(adapter as ContactAdapter)
                }
            }
        })
        viewModel.getUsers()
    }

    private fun searchListener(adapter: ContactAdapter) {
        val contactSearch: SearchView = findViewById(R.id.searchUser)

        val searchContact: EditText? = searchUser?.findViewById(R.id.search_src_text)
        searchContact?.setTextColor(ContextCompat.getColor(this, R.color.color_white))
        searchContact?.setHintTextColor(ContextCompat.getColor(this, R.color.search_text))

        searchUser.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(query: String): Boolean {
                adapter.filter!!.filter(query)
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                adapter.filter!!.filter(query)
                return false
            }

        })

        contactSearch.setOnQueryTextFocusChangeListener { _, hasFocus ->
            this.changeFocusSearch(contactSearch, hasFocus)
        }
    }

    private fun changeFocusSearch(
        contactSearch: SearchView,
        hasFocus: Boolean
    ) {
        val iconClose: ImageView = searchUser.findViewById(R.id.search_close_btn)
        val iconSearch: ImageView = searchUser.findViewById(R.id.search_mag_icon)

        contactSearch.isSelected = hasFocus
        iconClose.setColorFilter(ContextCompat.getColor(this@ContactActivity, R.color.color_white))
        iconSearch.setColorFilter(ContextCompat.getColor(this@ContactActivity, R.color.color_white))
        contactSearch.setBackgroundResource(R.drawable.shape_button_able)

        contactSearch.isIconified = !hasFocus
        if (!hasFocus) {
            iconSearch.setColorFilter(
                ContextCompat.getColor(
                    this@ContactActivity,
                    R.color.search_text
                )
            )
            contactSearch.setBackgroundResource(R.drawable.shape_search_disable)
        }
    }

    private fun showMessage(viewModel: ContactsViewModel) {
        progressBar.visibility = View.GONE
        viewModel.message.observe(this, Observer { message ->
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        })
        viewModel.showMessage()
    }
}
