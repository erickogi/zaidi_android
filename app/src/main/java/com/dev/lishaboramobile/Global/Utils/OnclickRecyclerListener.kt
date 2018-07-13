package com.dev.lishaboramobile.Global.Utils

import android.view.View

/**
 * Created by Eric on 12/18/2017.
 */

interface OnclickRecyclerListener {
    fun onClickListener(position: Int)

    fun onLongClickListener(position: Int)

    fun onCheckedClickListener(position: Int)

    fun onMoreClickListener(position: Int)

    fun onClickListener(adapterPosition: Int, view: View)

    // fun onDeleteListener( adapterPosition: Int);
}
