package com.example.simple_chat_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.cometchat.pro.core.AppSettings
import com.cometchat.pro.core.CometChat
import com.cometchat.pro.exceptions.CometChatException
import android.content.ContentValues.TAG
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.cometchat.pro.models.User
import com.example.simple_chat_app.utils.SimpleChatConstants

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initCometChat()
        initViews()
    }

    private fun initViews() {
        val userIdEditText:EditText = findViewById(R.id.userIdEditText)
        val submitButton:Button = findViewById(R.id.submitButton)
        submitButton.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?) {
                val UID:String=userIdEditText.toString()
                val authKey:String=SimpleChatConstants.AUTH_KEY

                if (CometChat.getLoggedInUser() == null) {
                    CometChat.login(UID,authKey, object : CometChat.CallbackListener<User>() {
                        override fun onSuccess(p0: User?) {
                            redirectToGroupListScreen()
                        }

                        override fun onError(p0: CometChatException?) {
                            Log.d(TAG, "Login failed with exception: " +  p0?.message)
                        }

                    })
                }else{
                    // User already logged in
                }
            }
        })
    }

    private fun redirectToGroupListScreen() {
        GroupListActivity.start(this)
    }

    private fun initCometChat() {
        val appSettings = AppSettings.AppSettingsBuilder().subscribePresenceForAllUsers().setRegion(SimpleChatConstants.REGION).build()

        CometChat.init(this,SimpleChatConstants.APP_ID,appSettings, object : CometChat.CallbackListener<String>() {
            override fun onSuccess(p0: String?) {
                Log.d(TAG, "Initialization completed successfully")
            }

            override fun onError(p0: CometChatException?) {
                Log.d(TAG, "Initialization failed with exception: " + p0?.message)
            }

        })
    }
}