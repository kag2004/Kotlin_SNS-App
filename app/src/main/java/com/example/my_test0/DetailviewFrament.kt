package com.example.my_test0

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class DetailviewFrament : Fragment (){
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return LayoutInflater.from(inflater.context).inflate(R.layout.fragment_detail,container,false)
    }
}