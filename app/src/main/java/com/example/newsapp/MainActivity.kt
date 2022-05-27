package com.example.newsapp

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.list_news.*
import org.json.JSONArray
import org.json.JSONObject


 class MainActivity : AppCompatActivity(), NewsItemClicked, AdapterView.OnItemSelectedListener{

    private lateinit var mAdapter: NewsAdapter
    private var category:String = "general"
    private var country:String = "in"


    val map:HashMap<String , String> = HashMap()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        recyclerview.layoutManager = LinearLayoutManager(this)
        fetchData()
        mAdapter = NewsAdapter(this)
        recyclerview.adapter = mAdapter

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerview.addItemDecoration(
            DividerItemDecoration(
                baseContext,
                layoutManager.orientation
            )
        )



        setCategorySpinner()
        setCountrySpinner()
    }

    private fun fetchData() {

           var  url:String = "https://newsapi.org/v2/top-headlines?country=" + country + "&category=" + category +  "&apiKey=0a227df11dcf48788c91625801fbf55e"


            val jsonObjectRequest = object: JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                Response.Listener {
                    val newsJsonArray = it.getJSONArray("articles")
                    val newsArray = ArrayList<News>()
                    for(i in 0 until newsJsonArray.length()) {
                        val newsJsonObject = newsJsonArray.getJSONObject(i)
                        val news = News(
                            newsJsonObject.getString("title"),
                            newsJsonObject.getString("author"),
                            newsJsonObject.getString("url"),
                            newsJsonObject.getString("urlToImage")
                        )
                        newsArray.add(news)
                    }

                    mAdapter.updateNews(newsArray)
                },
                Response.ErrorListener {

                }

            ) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["User-Agent"] = "Mozilla/5.0"
                    return headers
                }
            }

            MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
        }


    override fun onClick(item: News) {
        val url = item.url
        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(url));
    }

    private fun setCategorySpinner() {
        val spinner: Spinner = findViewById(R.id.categoryspinner)

        val adapter:ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            this,
            R.array.category,
            android.R.layout.simple_spinner_item)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = this

    }

    private fun setCountrySpinner() {
        val spinner: Spinner = findViewById(R.id.countryspinner)

        val adapter:ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            this,
             R.array.county,
            android.R.layout.simple_spinner_item)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = this
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        val selection: String = p0?.getItemAtPosition(p2) as String
        if (!TextUtils.isEmpty(selection)) {
            if (selection.equals(getString(R.string.General))) {
                category = getString(R.string.General)
            } else if (selection.equals(getString(R.string.Entertainment))) {
                category = getString(R.string.Entertainment)

            } else if (selection.equals(getString(R.string.Sports))) {
                category = getString(R.string.Sports)

            } else if (selection.equals(getString(R.string.Business))) {
                category = getString(R.string.Business)

            } else if (selection.equals(getString(R.string.Science))) {
                category = getString(R.string.Science)

            } else if (selection.equals(getString(R.string.Health))) {
                category = getString(R.string.Health)

            } else if (selection.equals(getString(R.string.Technology))) {
                category = getString(R.string.Technology)
            }
            else {
                country = map[selection].toString()
            }


        }
        fetchData()
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }



}




