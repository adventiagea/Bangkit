package com.dicoding.picodiploma.libas

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.dicoding.picodiploma.libas.databinding.ActivityPredictionBinding
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import java.lang.Exception
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class PredictionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPredictionBinding

    companion object{
        private const val NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "channel_01"
        private const val CHANNEL_NAME = "libas channel"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPredictionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dateText = binding.waktuPrediksi
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val date = LocalDate.now()
            val dateFormat = DateTimeFormatter.ofPattern("EEEE / dd-MMMM-yyyy")
            val dateTime = dateFormat.format(date)

            dateText.text = dateTime
        } else {
            fun Date.toString(format : String, locale: Locale = Locale.getDefault()): String {
                val dateFormat = SimpleDateFormat(format, locale)
                return dateFormat.format(this)
            }
            fun getCurrentDateTime(): Date {
                return Calendar.getInstance().time
            }

            val date = getCurrentDateTime()
            val dateInString = date.toString("dd-MMMM-yyyy")

            dateText.text = dateInString
        }

        getDetailData()

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun getDetailData(){
        val client = AsyncHttpClient()
        val url = "http://34.101.190.177/"

        client.get(url, object: AsyncHttpResponseHandler(){
            @SuppressLint("UseCompatLoadingForDrawables")
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                try {
                    val result = String(responseBody!!)
                    val jsonArray = JSONArray(result)
                    for (i in 0 until jsonArray.length()){
                        val jsonObject = jsonArray.getJSONObject(i)
                        val temp = jsonObject.getDouble("temp")
                        val humid = jsonObject.getDouble("humidity")
                        val rain = jsonObject.getDouble("rain")
                        val wind = jsonObject.getDouble("wind")
                        val predict = jsonObject.getDouble("predict")

                        val satuanWind = getText(R.string.satuan_wind)
                        val satuanTemp = getText(R.string.satuan_temp)
                        val satuanHumid = getText(R.string.satuan_humid)
                        val satuanRain = getText(R.string.satuan_rain)
                        val banjir = getText(R.string.merah)
                        val tidakBanjir = getText(R.string.ijo)
                        val backgroundBanjir = getDrawable(R.drawable.merah)
                        val backgroundTidakBanjir = getDrawable(R.drawable.ijo)

                        binding.curahHujanHasil.text = ("$rain $satuanRain")
                        binding.kecepatanAnginHasil.text = ("$wind $satuanWind")
                        binding.temperaturHasil.text = ("$temp $satuanTemp")
                        binding.kelembabanHasil.text = (humid.toString()+satuanHumid)

                        if (predict > 70) {
                            binding.prediksiHasil.text = banjir
                            binding.prediction.background = backgroundBanjir

                            val mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                            val mBuilder = NotificationCompat.Builder(this@PredictionActivity, CHANNEL_ID)
                                .setSmallIcon(R.drawable.ic_baseline_notifications_white_24)
                                .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_baseline_notifications_white_24))
                                .setContentTitle(resources.getString(R.string.prediksi))
                                .setContentText(resources.getString(R.string.siaga_banjir))
                                .setSubText(resources.getString(R.string.hasil))
                                .setAutoCancel(true)

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                                val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
                                channel.description = CHANNEL_NAME

                                mBuilder.setChannelId(CHANNEL_ID)
                                mNotificationManager.createNotificationChannel(channel)
                            }

                            val notification = mBuilder.build()
                            mNotificationManager.notify(NOTIFICATION_ID, notification)
                        }
                        else {
                            binding.prediksiHasil.text = tidakBanjir
                            binding.prediction.background = backgroundTidakBanjir
                        }
                    }

                } catch (e: Exception) {
                    Log.d("Exception Detail Data", e.message.toString())
                    Toast.makeText(this@PredictionActivity, "Exception Detail " + e.message.toString(), Toast.LENGTH_LONG).show()
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
                Toast.makeText(this@PredictionActivity, errMessage, Toast.LENGTH_LONG).show()
            }
        })
    }
}