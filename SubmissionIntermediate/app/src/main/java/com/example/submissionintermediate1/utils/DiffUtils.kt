package com.example.submissionintermediate1.utils

import androidx.recyclerview.widget.DiffUtil
import com.example.submissionintermediate1.data.response.Stories

class DiffUtils (
    private val oldList: List<Stories>,
    private val newList: List<Stories>
): DiffUtil.Callback(){

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val mOldList = oldList[oldItemPosition]
        val mNewList = newList[newItemPosition]

        return mOldList.id == mNewList.id && mOldList.name == mNewList.name && mOldList.description == mNewList.description && mOldList.photoUrl == mNewList.photoUrl
    }
}