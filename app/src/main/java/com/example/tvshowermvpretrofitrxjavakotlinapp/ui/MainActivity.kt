package com.example.tvshowermvpretrofitrxjavakotlinapp.ui

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.example.tvshowermvpretrofitrxjavakotlinapp.R
import com.example.tvshowermvpretrofitrxjavakotlinapp.R.string
import com.example.tvshowermvpretrofitrxjavakotlinapp.interfaces.ViewInterface
import com.example.tvshowermvpretrofitrxjavakotlinapp.model.DataModel
import com.example.tvshowermvpretrofitrxjavakotlinapp.presenter.DataPresenter
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), ViewInterface {

    private lateinit var dataPresenter:DataPresenter
    private lateinit var sharedPref: SharedPreferences
    private val MyPreference = "myPref"
    private var user: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPref = getSharedPreferences(MyPreference, Context.MODE_PRIVATE)

        dataPresenter = DataPresenter(this)

        edName.requestFocus()

        btSearch.setOnClickListener {
            it.hideKeyboard()
            user = edName.text.toString()

            if (user.isNotEmpty() && user.isNotBlank()){

                if (sharedPref.contains(user)) {
                    useCachedData(user)
                }else {
                    dataPresenter.start()
                }
            }else {
                removeViews()
                tvError.visibility = View.VISIBLE
                tvNoUserError.visibility = View.GONE
            }
        }

    }

    override fun init() {
        dataPresenter.loadDataFromRepo(user)
    }

    override fun showError(message: String) {

        removeViews()
        tvError.visibility = View.GONE

        if (message.contains("HTTP", false)){
            tvNetworkError.visibility = View.GONE
            tvNoUserError.visibility = View.VISIBLE
            tvNoUserError.text = message
        }else{
            tvNoUserError.text = message
            tvNoUserError.visibility = View.VISIBLE
            tvNetworkError.visibility = View.VISIBLE
        }
    }

    override fun loadDataModel(data: DataModel) {
        displayData(data)
        write(user, data)
    }

    override fun onDestroy() {
        dataPresenter.onDestroy()
        super.onDestroy()
    }

    //Display data
    private fun displayData(data: DataModel?){

        removeErrorMsgs()

        tvDays.text = getString(string.num_of_days)

        tvName.text = data?.name

        val date: String? = data?.let { calculateNumOfDays(data.premiered) }
        tvNumber.text = date

        Picasso.get()
                .load(data?.image?.medium)
                .resize(210,295)
                .error(R.drawable.ic_launcher_background)
                .into(imgTvView)

        drawViews()
    }

    //Remove error messages
    private fun removeErrorMsgs(){
        tvError.visibility = View.GONE
        tvNetworkError.visibility = View.GONE
        tvNoUserError.visibility = View.GONE
    }

    private fun removeViews(){
        tvName.visibility = View.GONE
        tvNumber.visibility = View.GONE
        tvDays.visibility = View.GONE
        imgTvView.visibility = View.GONE
    }

    //Draw views
    private fun drawViews(){
        tvName.visibility = View.VISIBLE
        tvNumber.visibility = View.VISIBLE
        tvDays.visibility = View.VISIBLE
        imgTvView.visibility = View.VISIBLE
    }

    //Hide the keyboard
    private fun View.hideKeyboard() {
        val inputManager =
                context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }

    //Calculate number of days
    private fun calculateNumOfDays(date: String?) : String{
        val pattern = "yyyy-MM-dd"
        val sdf: DateFormat = SimpleDateFormat(pattern, Locale.UK)

        val z: ZoneId = ZoneId.of("Europe/London")
        val today: LocalDate = LocalDate.now(z)
        val currentDateValue: Date? = sdf.parse("$today")

        val startDateValue: Date? = date?.let { sdf.parse(date)} ?: currentDateValue

        val diff: Long = currentDateValue!!.time - (startDateValue!!.time)
        val numDays = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)

        return numDays.toString()
    }

    private fun write(user: String?, data: DataModel){
        val editor: SharedPreferences.Editor = sharedPref.edit()
        val gson = Gson()
        val sData: String = gson.toJson(data)
        editor.putString(user, sData)
        editor.apply()
    }

    private fun read(user: String?): DataModel{
        var json: String? = null
        if (sharedPref.contains(user))
            json = sharedPref.getString(user, "")

        return Gson().fromJson(json, DataModel::class.java)
    }

    //Use cached version of data
    private fun useCachedData(user: String?){
        val data: DataModel = read(user)

        displayData(data)
    }
}