package com.dev.zaidi.Utils

import android.view.View

/**
 * Created by Eric on 12/18/2017.
 */

interface AdvancedOnclickRecyclerListener {
    fun onClickListener(position: Int)

    fun onLongClickListener(position: Int)

    fun onCheckedClickListener(position: Int)

    fun onMoreClickListener(position: Int)

    fun onClickListener(adapterPosition: Int, view: View)
    fun onSwipe(adapterPosition: Int, direction: Int)

    fun onEditTextChanged(position: Int, idTime: Int, idType: Int, editable: View)

    // fun onDeleteListener( adapterPosition: Int);
}
