package com.example.my_test0

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main2.*

class Main2Activity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener{
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_home ->{
                var detailviewFragment = DetailviewFrament()
                //화면 트랜지 잭션 처리 부분 하기 위해서 supportFragment...->
                supportFragmentManager.beginTransaction().replace(R.id.main_content,detailviewFragment).commit()
                return true
            }
            R.id.action_search ->{
                var gridFragment = GridFragment()
                supportFragmentManager.beginTransaction().replace(R.id.main_content,gridFragment).commit()
                return true
            }
            R.id.action_add_photo ->{
                var alertFaragment = AlertFaragment()
                supportFragmentManager.beginTransaction().replace(R.id.main_content,alertFaragment).commit()
                return true
            }
            R.id.action_favorite_alarm ->{
                var alertFragmentActivity = AlertFaragment()
                supportFragmentManager.beginTransaction().replace(R.id.main_content,alertFragmentActivity).commit()
                return true
            }
            R.id.action_account ->{
                var userFragment = UserFragment()
                supportFragmentManager.beginTransaction().replace(R.id.main_content,userFragment).commit()
                return true
            }
        }
        return false

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        bottom_navigation.setOnNavigationItemSelectedListener(this)

    }
}
