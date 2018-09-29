package com.wanderer.journal.SinglePost

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import com.wanderer.journal.R

class DeleteDialogFragment: DialogFragment() {
    internal lateinit var dialogListener: DeleteDialogListener

    interface DeleteDialogListener{
        fun onDeletePositiveClick(dialog: DialogFragment)
        fun onDeleteNegativeClick(dialog: DialogFragment)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage(R.string.delete_dialog_message)
                    .setTitle(R.string.delete_dialog_title)
                    .setPositiveButton(R.string.delete_dialog_confirm, DialogInterface.OnClickListener { dialog, id ->
                        //confirm
                    })
                    .setNegativeButton(R.string.delete_dialog_cancel, DialogInterface.OnClickListener{dialog, id ->
                        //Cancel
                    })
            builder.create()
        } ?: throw IllegalStateException("Activity cant be null")
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try{
            dialogListener = context as DeleteDialogListener
        } catch (e: ClassCastException){
            //Activity fail to implement the interface
            throw ClassCastException(context.toString() + " must implement DialogListener")
        }
    }
}