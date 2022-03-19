package com.codepath.apps.restclienttemplate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.codepath.apps.restclienttemplate.models.Tweet
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers

class ComposeActivity : AppCompatActivity() {
    lateinit var btnTweet: Button
    lateinit var etTweetCompose: EditText
    lateinit var client: TwitterClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compose)

        etTweetCompose = findViewById(R.id.et_tweet_compose)
        btnTweet = findViewById(R.id.btn_tweet)
        client = TwitterApplication.getRestClient(this)
        btnTweet.setOnClickListener{
            //grab content of et
            val tweetContent = etTweetCompose.text.toString()

            //make sure tweet is not empty
            if(tweetContent.isEmpty()){
                Toast.makeText(this,"Empty tweet not allowed!", Toast.LENGTH_SHORT).show()
            }
            //make sure tweet is under character count
            if(tweetContent.length > 140){
                Toast.makeText(this,"Tweet is too long! Limit is a 140 characters!", Toast.LENGTH_SHORT).show()

            } else {
                //make api call
                client.publishTweet(tweetContent,object: JsonHttpResponseHandler(){


                    override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                        Log.i(TAG,"Successfully published tweet!")
                        //send tweet back to timeline
                        val tweet = Tweet.fromJson(json.jsonObject)

                        val intent = Intent()
                        intent.putExtra("tweet", tweet)
                        setResult(RESULT_OK, intent)
                        finish()
                    }

                    override fun onFailure(
                        statusCode: Int,
                        headers: Headers?,
                        response: String?,
                        throwable: Throwable?
                    ) {
                        Log.e(TAG,"Failed to publish tweet!", throwable)
                    }



                })
            }

        }

    }
    companion object {
       val TAG = "ComposeActivity"
    }
}