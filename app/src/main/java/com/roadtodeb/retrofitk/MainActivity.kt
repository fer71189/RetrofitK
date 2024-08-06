package com.roadtodeb.retrofitk

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import com.roadtodeb.retrofitk.databinding.ActivityMainBinding
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val retrofitService = RetrofitInstance.getRetrofitInstance().create(AlbumService::class.java)

        val responseLiveData: LiveData<Response<Albums>> = liveData {
            val response = retrofitService.getAlbums()
            emit(response)
        }
        responseLiveData.observe(this , Observer {
            val albumList = it.body()?.listIterator()
            if (albumList != null)
                while (albumList.hasNext()){
                    val albumItem = albumList.next()
                    val albumTitle = "Album title: ${albumItem.title} \n"
                    binding.titleTextView.append(albumTitle)
                }
        })
    }
}