package com.greenhand.cooperativework.utils

import android.content.Context
import android.widget.Toast

object ToastUtil {
     private var mToast:Toast?=null
   fun showMsg(context: Context,msg:String){
        if (mToast==null){
            mToast=Toast.makeText(context,msg,Toast.LENGTH_SHORT)
        }else{
            mToast?.setText(msg)
        }
        mToast?.show()
    }
}