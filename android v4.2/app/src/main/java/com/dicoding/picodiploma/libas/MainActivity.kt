package com.dicoding.picodiploma.libas

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.dicoding.picodiploma.libas.databinding.ActivityMainBinding
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val listVariable = ArrayList<Variables>()
    private val variables = MutableLiveData<ArrayList<Variables>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val loginButton : ImageButton = findViewById(R.id.button_login)
        loginButton.setOnClickListener{
            getDetailData(this)
            bind(variable = Variables())
        }
    }

    fun getDetailData(context: Context){
        val client = AsyncHttpClient()
        val url = "https://api.openweathermap.org/data/2.5/onecall?lat=-6.2146&lon=106.8451&exclude=minutely,daily&appid=00f4b30ea4e91d7ae367ad8a755601b7"

        client.get(url, object: AsyncHttpResponseHandler(){
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                try {
                    val result = String(responseBody!!)
                    val jsonObject = JSONObject(result)
                    val temp = jsonObject.getInt("temp")
                    val humid = jsonObject.getInt("humidity")
                    val rain = jsonObject.getInt("clouds")
                    val wind = jsonObject.getInt("wind_speed")
                    listVariable.add(
                        Variables(
                            temp,
                            humid,
                            rain,
                            wind
                        )
                    )
                    variables.postValue(listVariable)
                } catch (e: Exception) {
                    Log.d("Exception Detail User", e.message.toString())
                    Toast.makeText(context, "Exception Detail" + e.message.toString(), Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                val errMessage = when(statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error?.message.toString()}"
                }
                Log.d("onFailure Detail Data", error?.message.toString())
                Toast.makeText(context, errMessage, Toast.LENGTH_LONG).show()
            }
        })
    }

    fun bind(variable: Variables){
        val data = Variables(
            variable.temp,
            variable.humid,
            variable.rain,
            variable.wind)
        val intent = Intent(this, PredictionActivity::class.java)
        intent.putExtra(PredictionActivity.DETAIL_DATA, data)
        startActivity(intent)
    }
}