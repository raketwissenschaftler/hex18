package com.treecio.hexplore

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.treecio.hexplore.network.NetworkClient
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var callbackManager: CallbackManager
    private lateinit var networkClient: NetworkClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        networkClient = NetworkClient(this)
        callbackManager = CallbackManager.Factory.create()

        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                networkClient.register(loginResult.accessToken)
            }

            override fun onCancel() {
            }

            override fun onError(exception: FacebookException) {
                Toast.makeText(this@MainActivity, "There was a problem", Toast.LENGTH_SHORT).show()
            }
        })

        btn_login.setOnClickListener { LoginManager.getInstance().logInWithReadPermissions(this@MainActivity, Arrays.asList("public_profile", "user_friends")) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
}
