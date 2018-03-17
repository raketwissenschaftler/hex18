package com.treecio.hexplore.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.treecio.hexplore.R
import kotlinx.android.synthetic.main.activity_people.*
import com.treecio.hexplore.R.id.rv
import android.support.v7.widget.LinearLayoutManager
import com.raizlabs.android.dbflow.list.FlowQueryList
import com.raizlabs.android.dbflow.sql.language.SQLite
import com.treecio.hexplore.R.id.rv
import com.treecio.hexplore.model.User
import com.treecio.hexplore.model.UserAdapter
import com.treecio.hexplore.model.User_Table


class PeopleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_people)

        rv.setHasFixedSize(true)
        val llm = LinearLayoutManager(this)
        rv.layoutManager = llm

        val users: FlowQueryList<User> = SQLite.select().from(User::class.java).flowQueryList()

        val adapter = UserAdapter(users)
        rv.adapter = adapter

        users.close()
    }


    override fun onDestroy() {
        super.onDestroy()

    }
}
