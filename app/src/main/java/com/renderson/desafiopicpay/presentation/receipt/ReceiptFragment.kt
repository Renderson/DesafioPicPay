package com.renderson.desafiopicpay.presentation.receipt

import android.content.res.Resources
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.renderson.desafiopicpay.R
import com.renderson.desafiopicpay.data.model.Receipt
import com.renderson.desafiopicpay.presentation.util.dateFormat
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_complete_list.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class ReceiptFragment : BottomSheetDialogFragment() {

    private lateinit var receipt: Receipt

    private var bottomSheet: View? = null
    //private var bottomSheetPeekHeight = 0

    private fun newInstance(): ReceiptFragment? {
        return ReceiptFragment()
    }

    override fun getTheme(): Int {
        return R.style.Theme_MaterialComponents_DayNight_BottomSheetDialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater
            .inflate(R.layout.activity_complete_list, container, false)
        bottomSheet = view.findViewById(R.id.contentBottomSheet)

        receipt = arguments!!.getSerializable("transaction_detail") as Receipt
        Log.i("RECEIPT", receipt.id.toString())

        this.newInstance()

        return view
        /*bottomSheetPeekHeight = resources
            .getDimensionPixelSize(R.dimen.bottom_sheet_default_peek_height)*/
    }

    override fun onResume() {
        super.onResume()
        this.setUpBottomSheet()
        this.populateReceipt()
    }

    private fun populateReceipt() {
        val res: Resources = resources
        val (dateFormat, hourFormat) = dateFormat()

        complete_date.text = res.getString(R.string.txt_date_transaction, dateFormat, hourFormat)

        receipt.let { receipt ->
            Picasso.get()
                .load(receipt.destination_user.img)
                .placeholder(R.drawable.ic_account_default)
                .into(complete_profile)

            complete_user_id.text = receipt.destination_user.username
            complete_transaction_id.text =
                res.getString(R.string.txt_id_transaction, receipt.id.toString())

            val total = receipt.value
            val valueCurrency = res.getString(R.string.txt_currency_br, total)

            complete_value.text = valueCurrency
            complete_total_value.text = valueCurrency
        }
    }

    private fun setUpBottomSheet() {
        val bottomSheetBehavior: BottomSheetBehavior<*> = BottomSheetBehavior
            .from(view!!.parent as View)

        //bottomSheetBehavior.peekHeight = bottomSheetPeekHeight
        //bottomSheetBehavior.isHideable = false

        val childLayoutParams = bottomSheet?.layoutParams
        val displayMetrics = DisplayMetrics()

        requireActivity()
            .windowManager
            .defaultDisplay
            .getMetrics(displayMetrics)

        if (childLayoutParams != null) {
            childLayoutParams.height = displayMetrics.heightPixels * 85 / 100
        }

        bottomSheet?.layoutParams = childLayoutParams
    }
}