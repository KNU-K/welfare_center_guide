
import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.kim.carrot.SubActivity2
import com.kim.carrot.databinding.ItemRoomBinding
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.DELETE
import retrofit2.http.Url

class BookmarkAdapter : RecyclerView.Adapter<BookmarkAdapter.ViewHolder>() {

    private var bookmarkList: List<SubActivity2.ResponseData> = ArrayList()

    fun setData(newList: List<SubActivity2.ResponseData>) {
        bookmarkList = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRoomBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, this)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(bookmarkList[position])
    }

    override fun getItemCount(): Int {
        return bookmarkList.size
    }

    class ViewHolder(private val binding: ItemRoomBinding, private val adapter: BookmarkAdapter) :
        RecyclerView.ViewHolder(binding.root) {
        private val nameTextView: TextView = binding.sfName
        private val telTextView: TextView = binding.sfTel
        private val addrTextView: TextView = binding.sfAddr

        interface ApiService {
            @DELETE
            fun deleteBookmarkBybookmarkId(@Url url: String?): Call<ResponseBody>
        }

        // Retrofit 인스턴스 생성
        val retrofit = Retrofit.Builder()
            .baseUrl("http://3.35.218.61:8080/api/user/1/") // 기본 URL만 넣음
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // API 서비스 생성
        val targetApiService = retrofit.create(ApiService::class.java)

        fun deleteBookmarkBybookmarkId(bookmarkId: Int?, position: Int) {
            val url = "bookmark/$bookmarkId" // 동적으로 변경되는 URL 파라미터 생성
            Log.d("어뎁터용 북마크 확인", "${bookmarkId}")

            val call = targetApiService.deleteBookmarkBybookmarkId(url)

            call.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        val jsonResponse = response.body()?.string()

                        try {
                            val jsonObject = JSONObject(jsonResponse)
                            val msg = jsonObject.getString("msg")

                            Toast.makeText(itemView.context, msg, Toast.LENGTH_SHORT).show()

                            val updatedList = adapter.bookmarkList.toMutableList()
                            updatedList.removeAt(position)

                            (itemView.context as Activity).runOnUiThread {
                                adapter.setData(updatedList)
                            }

                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("err", "err")
                }
            })
        }

        fun bind(bookmark: SubActivity2.ResponseData) {
            nameTextView.text = bookmark.sf_name
            telTextView.text = bookmark.sf_tel ?: "No Tel"
            addrTextView.text = bookmark.sf_addr

            binding.deleteButton.setOnClickListener {
                deleteBookmarkBybookmarkId(bookmark.bookmark_id, adapterPosition)
            }
        }
    }
}
