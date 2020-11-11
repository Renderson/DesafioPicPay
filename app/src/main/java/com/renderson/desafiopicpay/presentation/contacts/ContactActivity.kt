package com.renderson.desafiopicpay.presentation.contacts

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.renderson.desafiopicpay.R
import com.renderson.desafiopicpay.data.network.ApiService
import com.renderson.desafiopicpay.data.network.repository.ServiceApiDataSource
import com.renderson.desafiopicpay.presentation.ViewModelFactory
import com.renderson.desafiopicpay.presentation.transaction.TransactionActivity
import kotlinx.android.synthetic.main.activity_contact_main.textCollapsing
import kotlinx.android.synthetic.main.activity_contact_main.recycler
import kotlinx.android.synthetic.main.activity_search.search

class ContactActivity : AppCompatActivity() {

    private lateinit var viewModel: ContactsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_main)

        textCollapsing.text = getString(R.string.txt_contacts)
        //progressBar.visibility = View.VISIBLE

        viewModel = ViewModelProvider(
            viewModelStore,
            ViewModelFactory(ServiceApiDataSource(ApiService.serviceInterface))
        ).get(
            ContactsViewModel::class.java
        )

        if (savedInstanceState == null) {
            viewModel.getUsers()
        }

        getUsers(viewModel)
        showMessage(viewModel)
    }

    private fun getUsers(viewModel: ContactsViewModel) {

        viewModel.getUsersLiveData.observe(this, Observer {
            it?.let { users ->
                with(recycler) {
                    layoutManager = LinearLayoutManager(
                        this@ContactActivity,
                        RecyclerView.VERTICAL, false
                    )
                    setHasFixedSize(true)
                    adapter = ContactAdapter(users) { user ->
                        val intent = TransactionActivity.getStartIntent(this@ContactActivity, user)
                        this@ContactActivity.startActivity(intent)
                    }
                    searchListener(adapter as ContactAdapter)
                }
            }
        })
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
        searchCloseBtn.setColorFilter(
            ContextCompat.getColor(
                this@ContactActivity,
                R.color.color_white
            )
        )
        searchMagIcon.setColorFilter(
            ContextCompat.getColor(
                this@ContactActivity,
                R.color.color_white
            )
        )
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

    private fun clearSearchText() {
        val searchText: EditText? = search?.findViewById(R.id.search_src_text)
        searchText!!.clearFocus()
    }

    private fun showMessage(viewModel: ContactsViewModel) {
        //progressBar.visibility = View.GONE
        viewModel.message.observe(this, Observer { message ->
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        })
    }

    private fun removeObserve() {
        viewModel.getUsersLiveData.removeObservers(this)
        viewModel.message.removeObservers(this)
    }

    override fun onStop() {
        super.onStop()
        this.clearSearchText()
    }

    override fun onDestroy() {
        super.onDestroy()
        removeObserve()
    }
}
