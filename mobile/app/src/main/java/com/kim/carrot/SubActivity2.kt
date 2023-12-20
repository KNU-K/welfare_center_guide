package com.kim.carrot
import BookmarkAdapter
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.kim.carrot.databinding.ActivitySub2Binding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url

class SubActivity2 : AppCompatActivity() {

    data class ResponseData(
        var bookmark_id: Int,
        var sf_name: String,
        var sf_tel: String?,
        var sf_addr: String
    )

    private lateinit var binding: ActivitySub2Binding
    private lateinit var bookmarkAdapter: BookmarkAdapter
    private lateinit var subActivity: SubActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySub2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        bookmarkAdapter = BookmarkAdapter()
        binding.recyclerView.adapter = bookmarkAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        val bookmark_uID: Int = 9 // TODO: 나중에 공유되는 형태로 u_Id 가져오기
        getBookmark(bookmark_uID)
    }

    interface BookmarkApiService {
        @GET
        fun getBookmark(@Url url: String?): Call<ArrayList<ResponseData>> //////////////////////////////////여기 물음표 추가함
    }

    val retrofit = Retrofit.Builder()
        .baseUrl("http://3.35.218.61:8080/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val bookmarkApiService = retrofit.create(BookmarkApiService::class.java)

    fun getBookmark(bookmark_uID: Int?) {
        val sharedPreferences = getSharedPreferences("user", 0)
        val u_id: Int? = sharedPreferences.getInt("u_id", 0)
        Log.d("u_id", "${u_id}")
        val call = bookmarkApiService.getBookmark("http://3.35.218.61:8080/api/user/$u_id/bookmark")

        call.enqueue(object : Callback<ArrayList<ResponseData>> {
            override fun onResponse(
                call: Call<ArrayList<ResponseData>>,
                response: Response<ArrayList<ResponseData>>
            ) {
                if (response.isSuccessful) {
                    val responseData = response.body()
                    if (responseData != null) {
                        bookmarkAdapter.setData(responseData)


                    } else {
                        // 응답이 없거나 데이터가 비어 있는 경우에 대한 처리
                        // Log.e("kim", "응답 데이터가 비어있습니다.")
                    }
                } else {
                    // 요청이 실패한 경우 수행할 작업
                    // Log.e("kim", "POST 요청 실패")
                }
            }

            override fun onFailure(call: Call<ArrayList<ResponseData>>, t: Throwable) {
                // 네트워크 오류 또는 예외 처리
                // Log.e("kim", "네트워크 오류: ${t.message}")
            }
        })
    }
}
