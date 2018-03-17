package com.treecio.hexplore

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.facebook.CallbackManager
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.FacebookCallback
import com.facebook.login.LoginManager
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import com.facebook.AccessToken

class MainActivity : AppCompatActivity() {

    private lateinit var callbackManager:CallbackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val loggedIn = AccessToken.getCurrentAccessToken() == null
        if(loggedIn) {

        }

        callbackManager = CallbackManager.Factory.create()

        LoginManager.getInstance().registerCallback(callbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(loginResult: LoginResult) {
                    }

                    override fun onCancel() {
                    }

                    override fun onError(exception: FacebookException) {
                    }
                })

        setContentView(R.layout.activity_main)

        btn_login.setOnClickListener { LoginManager.getInstance().logInWithReadPermissions(this@MainActivity, Arrays.asList("public_profile", "user_friends")) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
}
