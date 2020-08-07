package com.trylaarsdam.testapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.trylaarsdam.testapp.mqtt.MqttClientHelper
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*


const val EXTRA_MESSAGE = "com.trylaarsdam.TestApp.MESSAGE"

class MainActivity : AppCompatActivity() {

    private val mqttClient by lazy {
        MqttClientHelper(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setMqttCallBack()
    }

    private fun setMqttCallBack(){
        mqttClient.setCallback(object : MqttCallbackExtended {
            override fun connectComplete(reconnect: Boolean, s: String) {
                var msg = "connection to broker established:\n'$SOLACE_MQTT_HOST'"
                Log.w("Debug", msg)
                val textView = findViewById<TextView>(R.id.textView3).apply {
                    text = msg
                }
            }

            override fun messageArrived(topic: String, message: MqttMessage) {
                Log.w("Debug", "Message received from host '$SOLACE_MQTT_HOST': $message")
                val str: String = "\n$message"
                val textView = findViewById<TextView>(R.id.textView3).apply {
                    text = str
                }
            }

            override fun connectionLost(cause: Throwable?) {
                var msg = "connection to broker lost:\n'$SOLACE_MQTT_HOST'"
                Log.w("Debug", msg)
                val textView = findViewById<TextView>(R.id.textView3).apply {
                    text = msg
                }
            }

            override fun deliveryComplete(token: IMqttDeliveryToken) {
                Log.w("Debug", "Message published to host '$SOLACE_MQTT_HOST'")
            }
        })
    }

    /** Called when the user taps the Send button */
    fun sendMessage(view: View) {
        // Do something in response to button
        val editText = findViewById<EditText>(R.id.editText)
        val message = editText.text.toString()
        //val intent = Intent(this, DisplayMessageActivity::class.java).apply {
        //    putExtra(EXTRA_MESSAGE, message)
        //}
        mqttClient.publish("cpu-android/testing" ,message)
        //startActivity(intent)
    }

    fun subToTopic(view: View) {
        // Do something in response to button
        val editText2 = findViewById<EditText>(R.id.editText2)
        val message = editText2.text.toString()
        mqttClient.subscribe(message)
    }
}