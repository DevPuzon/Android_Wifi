package com.tofaha.Android_wifi.ui.main


import android.app.Activity
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.*
import android.speech.RecognizerIntent
import android.support.v7.app.AppCompatActivity
import android.support.annotation.RequiresApi
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.tofaha.Android_wifi.Pref
import com.tofaha.Android_wifi.R
import com.tofaha.Android_wifi.app.MyData
import com.tofaha.Android_wifi.app.TofahaApplication
import com.tofaha.Android_wifi.connection.CLoseConnection
import com.tofaha.Android_wifi.connection.OpenConnection
import com.tofaha.Android_wifi.connection.SendMessages
import com.tofaha.Android_wifi.ui.FloatingMenuFragment
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.getStackTraceString
import org.jetbrains.anko.uiThread
import java.io.*
import java.util.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() , MainView {

    var input: BufferedReader? = null
    @Inject
    lateinit var pref: Pref
    private val REQUEST_SPEECH_INPUT = 100
    private var relay1: String = ""
    private var relay1f: String = ""
    private var relay1n: String = ""
    private var relay2: String = ""
    private var relay2f: String = ""
    private var relay2n: String = ""
    private var relay3: String = ""
    private var relay3f: String = ""
    private var relay3n: String = ""
    private var relay4: String = ""
    private var relay4f: String = ""
    private var relay4n: String = ""
    private var relay5: String = ""
    private var relay5f: String = ""
    private var relay5n: String = ""
    private var relay6: String = ""
    private var relay6f: String = ""
    private var relay6n: String = ""
    @BindView(R.id.server_response)
    lateinit var serverResponse : TextView
//    init {
//        this.relay1 = "led 1"
//        this.relay1n = "led 1 on"
//        this.relay1f = "led 1 off"
//        this.relay2 = "led 2"
//        this.relay2n = "led 2 on"
//        this.relay2f = "led 2 off"
//        this.relay3 = "led 3"
//        this.relay3n = "led 3 on"
//        this.relay3f = "led 3 off"
//        this.relay4 = "led 4"
//        this.relay4n = "led 4 on"
//        this.relay4f = "led 4 off"
//        this.relay5 = "led 5"
//        this.relay5n = "led 5 on"
//        this.relay5f = "led 5 off"
//        this.relay6 = "led 6"
//        this.relay6n = "led 6 on"
//        this.relay6f = "led 6 off"
//    }

    private fun getRelay1() {
        this.relay1 = txtRel1.text.toString()
        txtRel1.text.clear()
        txtRel1.hint = this.relay1
        this.relay1n = this.relay1 + " on"
        this.relay1f = this.relay1 + " off"
    }

    private fun getRelay2() {
        this.relay2 = txtRel2.text.toString()
        txtRel2.text.clear()
        txtRel2.hint = this.relay2
        this.relay2n = this.relay2 + " on"
        this.relay2f = this.relay2 + " off"
    }

    private fun getRelay3() {
        this.relay3 = txtRel3.text.toString()
        txtRel3.text.clear()
        txtRel3.hint = this.relay3
        this.relay3n = this.relay3 + " on"
        this.relay3f = this.relay3 + " off"
    }

    private fun getRelay4() {
        relay4 = txtRel4.text.toString()
        txtRel4.text.clear()
        txtRel4.hint = this.relay4
        this.relay4n = this.relay4 + " on"
        this.relay4f = this.relay4 + " off"
    }

    private fun getRelay5() {
        this.relay5 = txtRel5.text.toString()
        txtRel5.text.clear()
        txtRel5.hint = this.relay5
        this.relay5n = this.relay5 + " on"
        this.relay5f = this.relay5 + " off"
    }

    private fun getRelay6() {
        this.relay6 = txtRel6.text.toString()
        txtRel6.text.clear()
        txtRel6.hint = this.relay6
        this.relay6n = this.relay6 + " on"
        this.relay6f = this.relay6 + " off"
    }

    private fun getSpeech() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say Something")
        try {
            startActivityForResult(intent, REQUEST_SPEECH_INPUT);
        } catch (ex: Exception) {
            Toast.makeText(this,ex.message,Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            REQUEST_SPEECH_INPUT ->{
                if(resultCode == Activity.RESULT_OK && null != data){
                    val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    speechMessage(result[0])
                }
            }
        }
    }

    private fun resetRelay() {
        this.relay1 = "led 1"
        this.relay1n = "led 1 on"
        this.relay1f = "led 1 off"
        this.relay2 = "led 2"
        this.relay2n = "led 2 on"
        this.relay2f = "led 2 off"
        this.relay3 = "led 3"
        this.relay3n = "led 3 on"
        this.relay3f = "led 3 off"
        this.relay4 = "led 4"
        this.relay4n = "led 4 on"
        this.relay4f = "led 4 off"
        this.relay5 = "led 5"
        this.relay5n = "led 5 on"
        this.relay5f = "led 5 off"
        this.relay6 = "led 6"
        this.relay6n = "led 6 on"
        this.relay6f = "led 6 off"
    }

    private fun speechMessage(s: String) {
        openConnection()
        Thread.sleep(3000)
        when(s){
            relay1n -> sendMessage("01")
            relay1f -> sendMessage("00")
            relay2n -> sendMessage("t1")
            relay2f -> sendMessage("t0")
            relay3n -> sendMessage("th1")
            relay3f -> sendMessage("th0")
            relay4n -> sendMessage("f1")
            relay4f -> sendMessage("f0")
            relay5n -> sendMessage("fi1")
            relay5f -> sendMessage("fi0")
            relay6n -> sendMessage("s1")
            relay6f -> sendMessage("s0")
            "shutdown" -> sendMessage("sn")
            "turn on" -> sendMessage("tn")
            "turn off" -> sendMessage("tf")
            "all on" -> sendMessage("an")
            "all off" -> sendMessage("af")
        }
    }

//    fun notif() {
//        Toast.makeText(this,"",Toast.LENGTH_SHORT).show()
//    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        try{
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)

            (this.application as TofahaApplication).getAppComponent()!!.inject(this)

            ButterKnife.bind(this)

            MyData.mainActivity = this

            supportActionBar!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.blue_menu)))
            window.statusBarColor = resources.getColor(R.color.blue_menu)

            this.fab.bringToFront()
            this.fab.parent.requestLayout()
        }catch (ex: Exception){
            ex(ex)
        }
        btnSpeech.setOnClickListener {
            getSpeech()
        }
        btnReset.setOnClickListener {
            resetRelay()
        }
        btnSave.setOnClickListener {
            if (txtRel1.text.isNotEmpty()){
                getRelay1()
            }
            if (txtRel2.text.isNotEmpty()){
                getRelay2()
            }
            if (txtRel3.text.isNotEmpty()){
                getRelay3()
            }
            if (txtRel4.text.isNotEmpty()){
                getRelay4()
            }
            if (txtRel5.text.isNotEmpty()){
                getRelay5()
            }
            if (txtRel6.text.isNotEmpty()){
                getRelay6()
            }
        }
        refresh_connection.setOnClickListener {
            openConnection()
        }
    }
    fun ex(ex: Exception){
        Toast.makeText(this,ex.message,Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.fab)
    operator fun next() {
        var dialogFrag = FloatingMenuFragment.newInstance()
        dialogFrag.setParentFab(fab)
        dialogFrag.show(getSupportFragmentManager(), dialogFrag.getTag())
    }
    override fun onResume() {
        super.onResume()
        openConnection()
    }

    override fun onPause() {
        super.onPause()
        closeConnection()
    }

    override fun openConnection() {
        var openConnection = OpenConnection(pref.ipAddress!!, pref.portNumber)
        openConnection!!.execute()
    }

    override fun closeConnection() {
        var closeConnection = CLoseConnection()
        closeConnection!!.execute()
    }

    override fun receiveMessage() {
        doAsync {

            input = BufferedReader(InputStreamReader(MyData.socket.getInputStream()))
            var msgText = "waiting ...."
            MyData.THREAD_RUNNING = true

            while (true) {

                //println(input?.readLine())
                Thread.sleep(300)
                msgText = input?.readLine().toString()

                uiThread {
                    serverResponse.text = msgText
                }
            }
        }}

    override fun sendMessage(s: String) {
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show()
        var sendMessages = SendMessages(s)
        sendMessages!!.execute()
    }


//    fun setRelay1(relay1: String) {
//        Intrinsics.checkParameterIsNotNull(relay1, "<set-?>")
//        this.relay1 = relay1
//    }
//
//    fun setRelay1f(relay1f: String) {
//        Intrinsics.checkParameterIsNotNull(relay1f, "<set-?>")
//        this.relay1f = relay1f
//    }
//
//    fun setRelay1n(relay1n: String) {
//        Intrinsics.checkParameterIsNotNull(relay1n, "<set-?>")
//        this.relay1n = relay1n
//    }
//
//    fun setRelay2(relay2: String) {
//        Intrinsics.checkParameterIsNotNull(relay2, "<set-?>")
//        this.relay2 = relay2
//    }
//
//    fun setRelay2f(relay2f: String) {
//        Intrinsics.checkParameterIsNotNull(relay2f, "<set-?>")
//        this.relay2f = relay2f
//    }
//
//    fun setRelay2n(relay2n: String) {
//        Intrinsics.checkParameterIsNotNull(relay2n, "<set-?>")
//        this.relay2n = relay2n
//    }
//
//    fun setRelay3(relay3: String) {
//        Intrinsics.checkParameterIsNotNull(relay3, "<set-?>")
//        this.relay3 = relay3
//    }
//
//    fun setRelay3f(relay3f: String) {
//        Intrinsics.checkParameterIsNotNull(relay3f, "<set-?>")
//        this.relay3f = relay3f
//    }
//
//    fun setRelay3n(relay3n: String) {
//        Intrinsics.checkParameterIsNotNull(relay3n, "<set-?>")
//        this.relay3n = relay3n
//    }
//
//    fun setRelay4(relay4: String) {
//        Intrinsics.checkParameterIsNotNull(relay4, "<set-?>")
//        this.relay4 = relay4
//    }
//
//    fun setRelay4f(relay4f: String) {
//        Intrinsics.checkParameterIsNotNull(relay4f, "<set-?>")
//        this.relay4f = relay4f
//    }
//
//    fun setRelay4n(relay4n: String) {
//        Intrinsics.checkParameterIsNotNull(relay4n, "<set-?>")
//        this.relay4n = relay4n
//    }
//
//    fun setRelay5(relay5: String) {
//        Intrinsics.checkParameterIsNotNull(relay5, "<set-?>")
//        this.relay5 = relay5
//    }
//
//    fun setRelay5f(relay5f: String) {
//        Intrinsics.checkParameterIsNotNull(relay5f, "<set-?>")
//        this.relay5f = relay5f
//    }
//
//    fun setRelay5n(relay5n: String) {
//        Intrinsics.checkParameterIsNotNull(relay5n, "<set-?>")
//        this.relay5n = relay5n
//    }
//
//    fun setRelay6(relay6: String) {
//        Intrinsics.checkParameterIsNotNull(relay6, "<set-?>")
//        this.relay6 = relay6
//    }
//
//    fun setRelay6f(relay6f: String) {
//        Intrinsics.checkParameterIsNotNull(relay6f, "<set-?>")
//        this.relay6f = relay6f
//    }
//
//    fun setRelay6n(relay6n: String) {
//        Intrinsics.checkParameterIsNotNull(relay6n, "<set-?>")
//        this.relay6n = relay6n
//    }
//
//    fun setServerResponse(serverResponse: TextView) {
//        Intrinsics.checkParameterIsNotNull(serverResponse, "<set-?>")
//        this.serverResponse = serverResponse
//    }
}
