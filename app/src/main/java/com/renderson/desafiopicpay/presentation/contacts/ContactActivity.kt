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
import kotlinx.android.synthetic.main.activity_search.*

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
        val searchView: SearchView = findViewById(R.id.search)

        val searchText: EditText? = search?.findViewById(R.id.search_src_text)
        searchText?.setTextColor(ContextCompat.getColor(this, R.color.color_white))
        searchText?.setHintTextColor(ContextCompat.getColor(this, R.color.search_text))

        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(query: String): Boolean {
                adapter.filter!!.filter(query)
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                adapter.filter!!.filter(query)
                return false
            }

        })

        searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
            this.changeStylesSearch(searchView, hasFocus)
        }
    }

    private fun changeStylesSearch(
        searchView: SearchView,
        hasFocus: Boolean
    ) {
        val searchCloseBtn: ImageView = search.findViewById(R.id.search_close_btn)
        val searchMagIcon: ImageView = search.findViewById(R.id.search_mag_icon)

        searchView.isSelected = hasFocus
        searchCloseBtn.setColorFilter(ContextCompat.getColor(this@ContactActivity, R.color.color_white))
        searchMagIcon.setColorFilter(ContextCompat.getColor(this@ContactActivity, R.color.color_white))
        searchView.setBackgroundResource(R.drawable.shape_search_able)

        searchView.isIconified = !hasFocus
        if (!hasFocus) {
            searchMagIcon.setColorFilter(
                ContextCompat.getColor(
                    this@ContactActivity,
                    R.color.search_text
                )
            )
            searchView.setBackgroundResource(R.drawable.shape_search_disable)
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
