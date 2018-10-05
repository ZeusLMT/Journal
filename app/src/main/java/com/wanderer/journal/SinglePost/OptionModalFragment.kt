package com.wanderer.journal.SinglePost

import android.content.Context
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wanderer.journal.R
import kotlinx.android.synthetic.main.bottom_sheet.view.*

class OptionModalFragment: BottomSheetDialogFragment() {
    private lateinit var optionModalListener: OptionModalListener

    interface OptionModalListener{
        fun onOptionClick(id: String)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.bottom_sheet, container, false)
        view.delete_text.setOnClickListener {
            optionModalListener.onOptionClick("delete")
        }
        view.share_text.setOnClickListener {
            optionModalListener.onOptionClick("share")
        }
        view.wiki_text.setOnClickListener {
            optionModalListener.onOptionClick("wiki")
            dismiss()
        }
        view.gallery_text.setOnClickListener {
            optionModalListener.onOptionClick("gallery")
        }

        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        Log.d("OptionModal", "onAttach")
        try{
            Log.d("OptionModal", "try onAttach")
            optionModalListener = context as OptionModalListener
        } catch (e: ClassCastException){
            //Activity fail to implement the interface
            Log.d("OptionModal", "failOnAttach")
            throw ClassCastException(context.toString() + " must implement OptionModalListener")
        }
    }
}