package com.kim.carrot

//import androidx.lifecycle.viewmodel.CreationExtras.Empty.map
import BookmarkAdapter
import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.kim.carrot.databinding.ActivitySubBinding
import net.daum.mf.map.api.CalloutBalloonAdapter
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Url





class SubActivity : AppCompatActivity() {
    lateinit var sharedPreferences: SharedPreferences
    lateinit var mapView: MapView
    private lateinit var bookmarkAdapter: BookmarkAdapter
    private lateinit var mapViewContainer : ViewGroup
    private var bookmarkList: List<SubActivity2.ResponseData> = ArrayList()
    fun setData(newList: List<SubActivity2.ResponseData>) {
        bookmarkList = newList
    }



    private var uLatitude : Double = 0.0
    private var uLongitude : Double = 0.0
    private  val eventListener = MarkerEventListener(this)

    data class TargetMapData(
        var sf_id: Int,
        var sf_name: String,
        var sf_tel: String?,
        var sf_latitude: Double, // 위도
        var sf_longitude: Double, // 경도
        var sf_addr: String
    )

    data class BookmarkData(
        val sf_id: String?
    )
    data class TargetBookmarkData(
        val id:Int?,
        val sf_id:Int?
    )

    data class ResponseData(
        var bookmark_id: Int,
        var sf_name: String,
        var sf_tel: String?,
        var sf_addr: String
    )

//    data class ResponseData(
//        val msg: String?
//    )






    ///////
    class CustomBalloonAdapter(inflater: LayoutInflater): CalloutBalloonAdapter {
        val mCalloutBalloon: View = inflater.inflate(R.layout.ballon_layout,null)
        val name: TextView = mCalloutBalloon.findViewById(R.id.ball_tv_name)
        val address: TextView = mCalloutBalloon.findViewById(R.id.ball_tv_address)

        override fun getCalloutBalloon(poiItem: MapPOIItem?): View {
            // 마커 클릭 시 나오는 말풍선
            var facilities = poiItem?.itemName?.split("|");
            name.text = facilities?.get(0)    // 해당 마커의 정보 이용 가능
            if(facilities?.size ==8) address.text = facilities?.get(1)
            else address.text = "current location"

            return mCalloutBalloon
        }

        override fun getPressedCalloutBalloon(poiItem: MapPOIItem?): View {
            // 말풍선 클릭 시
//            val ballonBinding:Button = mCalloutBalloon.findViewById(R.id.bookmark_button)
//                ballonBinding.text = "북마크제거"


            return mCalloutBalloon
        }


    }

    /////////
    fun initMapView() {
        mapView = MapView(this)
        mapView.setCalloutBalloonAdapter(CustomBalloonAdapter(layoutInflater))
        mapView.setPOIItemEventListener(eventListener)
        mapViewContainer.addView(mapView)

        // 줌 레벨 변경
        mapView.setZoomLevel(3, true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences("user",0)
        val subBinding = ActivitySubBinding.inflate(layoutInflater)
        setContentView(subBinding.root)

        //flag6
        bookmarkAdapter = BookmarkAdapter()

//        subBinding.targetbutton.setOnClickListener {
//            //////////////////
//        }
        ///////////////////////이게 즐쳐찾기 버튼 눌렀을 때의 이벤트를 처리할 수 있는거임....나중에 활용가능성
        ///////////////////////생기면 활용할 것








        // 위치 권한 허용
        requestLocationPermission()
        mapViewContainer = subBinding.mapViewContainer // 이 부분을 onCreate에서 이동

        // 맵뷰 표시
        initMapView()
        // 지도가 현재 위치로 표시
        setCurrentLocaion()

        addCustomMarker()



        fetchMissionData()
        fetchNonSelectedMissionData()






    }



    private fun setCurrentLocaion() {
        val uNowPosition = MapPoint.mapPointWithGeoCoord(uLatitude, uLongitude)
        mapView.setMapCenterPoint(uNowPosition, true)

        Log.d("Map", "${uLatitude}, ${uLongitude}")
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        val lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        try {
            val userNowLocation: Location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)!!
            uLatitude = userNowLocation.latitude
            uLongitude = userNowLocation.longitude
        } catch (e: NullPointerException) {
            Log.e("LOCATION_ERROR", e.toString())
            finish()
        }
    }

    //
    private fun requestLocationPermission() {
        val permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            // 권한 요청
            val requestPermissionLauncher = registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted ->
                if (isGranted) {
                    Log.d("carrot", "Location permission granted.")
                    // 권한이 허용된 경우, 현재 위치를 가져올 수 있음
                    getCurrentLocation()

                } else {
                    Log.d("carrot", "Location permission denied.")
                    // 권한이 거부된 경우, 사용자에게 메시지를 표시하거나 다른 조치를 취할 수 있음
                    //Toast.makeText(this, "위치 권한이 필요합니다.", Toast.LENGTH_SHORT).show()

                    finish()
                }
            }
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        else {
            // 이미 권한이 허용된 경우
            getCurrentLocation()

        }
    }


    //




    private fun addCustomMarker() {
        // 현재 위치에 마커 추가
        val marker = MapPOIItem()
        marker.apply {
            itemName = "현재 위치"   // 마커 이름
            mapPoint = MapPoint.mapPointWithGeoCoord(uLatitude, uLongitude)   // 좌표
            markerType= MapPOIItem.MarkerType.CustomImage
            customImageResourceId = R.drawable.curmaker

            isCustomImageAutoscale = true
            setCustomImageAnchor(0.5f, 1.0f)    // 마커 이미지 기준점
        }
        mapView.addPOIItem(marker)
    }




    private var selectedTargetMapData = ArrayList<TargetMapData>()
    private var nonSelectedTargetMapData = ArrayList<TargetMapData>()

    //받아온 데이터 마커 위에 표시
    private fun setMissionMark(dataList: ArrayList<TargetMapData>, color:String) {
        for (data in dataList) {

            2
            val marker = MapPOIItem()
            marker.apply {



                mapPoint = MapPoint.mapPointWithGeoCoord(data.sf_latitude, data.sf_longitude)
                when(color){
                    "yellow"->{
                        itemName = "${data.sf_name}|${data.sf_addr}|${data.sf_id}|${uLatitude}|${uLongitude}|${data.sf_latitude}|${data.sf_longitude}|1"
                        markerType = MapPOIItem.MarkerType.CustomImage
                        customImageResourceId = R.drawable.yellowflag
                        isCustomImageAutoscale = true
                    }
                    "blue"->{
                        itemName = "${data.sf_name}|${data.sf_addr}|${data.sf_id}|${uLatitude}|${uLongitude}|${data.sf_latitude}|${data.sf_longitude}|0"
                        markerType = MapPOIItem.MarkerType.CustomImage
                        customImageResourceId = R.drawable.blueflag
                        isCustomImageAutoscale = true}
                }
//                selectedMarkerType = MapPOIItem.MarkerType.RedPin
//                isCustomImageAutoscale = false
                setCustomImageAnchor(0.5f, 1.0f)



            }
            mapView.addPOIItem(marker)

        }



    }

    //flag2
    class MarkerEventListener(val context: Context): MapView.POIItemEventListener {
        var flag:Boolean = true

        interface BookmarkApiService {
            //        @GET("http://3.35.218.61:8080/api/senior-facilities") // 현재의 GET 메서드와 함께 정의
            //        fun getTargetData(): Call<List<TargetMapData>>
            @POST
            fun postBookmark(@Url url: String, @Body bookmarkData: BookmarkData): Call<ResponseData>
            @DELETE
            fun deleteBookmark(@Url url: String): Call<ResponseData>

        }
        val retrofit = Retrofit.Builder()
            .baseUrl("http://3.35.218.61:8080/api/") // 여기에 API의 기본 URL을 넣으세요.
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val bookmarkApiService = retrofit.create(BookmarkApiService::class.java)

        fun removeBookmark(bookmark_sfID: String?) {
            val sharedPreferences = context.getSharedPreferences("user", 0)
            val u_id: Int? = sharedPreferences.getInt("u_id", 0)
            val call = bookmarkApiService.deleteBookmark("http://3.35.218.61:8080/api/user/1/senior-facilities/${bookmark_sfID}")
            call.enqueue(object : Callback<ResponseData> {
                override fun onResponse(call: Call<ResponseData>, response: Response<ResponseData>) {
                    Log.d("kim", "DELETE 요청 응답 코드: ${response.code()}")
                    if (response.isSuccessful) {
                        // 성공적으로 요청이 완료된 경우 수행할 작업
                        var resValue = response.body()?.toString();
                        Log.d("응답 메세지", "응답 메시지: $resValue")
                        if (resValue.equals("succeed")) {
                            Log.e("kim", "북마크 제거 성공");
                        } else {
                            Log.e("kim", "북마크 제거 실패");
                        }
                    } else {
                        // 요청이 실패한 경우 수행할 작업
                        Log.e("kim", "DELETE 요청 실패")
                    }
                }

                override fun onFailure(call: Call<ResponseData>, t: Throwable) {
                    Log.e("test", "네트워크 오류 또는 예외 처리: ${t.message}")
                }
            })
        }
        fun createBookmark(bookmark_sfID: String?) {
            //Log.e("sfId_flag", bookmark_sfID.toString());
            val bookmarkData = BookmarkData(bookmark_sfID)

            val sharedPreferences =context.getSharedPreferences("user", 0);
            val u_id:Int? = sharedPreferences.getInt("u_id", 0)
            val call = bookmarkApiService.postBookmark("http://3.35.218.61:8080/api/user/${u_id}/bookmark",bookmarkData)

            call.enqueue(object : Callback<ResponseData> {
                override fun onResponse(call: Call<ResponseData>, response: Response<ResponseData>) {
                    if (response.isSuccessful) {
                        // 성공적으로 요청이 완료된 경우 수행할 작업
                        var resValue = response.body()?.toString();
                        Log.d("test",resValue.toString())
                        if(resValue.equals("bookmark good")){
                            Log.e("test","true");
                        }else{

                            Log.e("test","false");
                        }
                    } else {
                        // 요청이 실패한 경우 수행할 작업
                        Log.e("kim", "POST 요청 실패")
                    }
                }



                override fun onFailure(call: Call<ResponseData>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })
        }
        override fun onPOIItemSelected(mapView: MapView?, poiItem: MapPOIItem?) {
            // 마커 클릭 시
        }

        override fun onCalloutBalloonOfPOIItemTouched(mapView: MapView?, poiItem: MapPOIItem?) {
            // 말풍선 클릭 시 (Deprecated)
            // 이 함수도 작동하지만 그냥 아래 있는 함수에 작성하자
        }




        override fun onCalloutBalloonOfPOIItemTouched(mapView: MapView?, poiItem: MapPOIItem?, buttonType: MapPOIItem.CalloutBalloonButtonType?) {
            // 말풍선 클릭 시
            val parts = poiItem?.itemName?.split("|")




            flag = when{
                parts?.get(7) == "0" -> { //
                    true
                }
                else -> {
                    false
                }
            }


            when(flag) {
                true -> {
                    val builder = AlertDialog.Builder(context)

                    val itemList = arrayOf("북마크 추가", "길찾기", "취소")
                    builder.setTitle("${parts?.get(0)}")
                    builder.setItems(itemList) { dialog, which ->
                        when (which) {
                            0 -> {
                                //Toast.makeText(context, "토스트", Toast.LENGTH_SHORT).show()
                                //poiItem?.itemName = "${parts?.get(0)}|${parts?.get(1)}|${parts?.get(2)}|${parts?.get(3)}|${parts?.get(4)}|${parts?.get(5)}|${parts?.get(6)}|1"
//flag100

                                mapView?.removePOIItem(poiItem)

                                val newMarker = MapPOIItem()
                                newMarker.itemName = "${parts?.get(0)}|${parts?.get(1)}|${parts?.get(2)}|${parts?.get(3)}|${parts?.get(4)}|${parts?.get(5)}|${parts?.get(6)}|1"
                                val longitude:Double? = parts?.get(5)?.toDouble()
                                val latitude:Double? = parts?.get(6)?.toDouble()
                                if(longitude !=null && latitude != null)
                                    newMarker.mapPoint = MapPoint.mapPointWithGeoCoord(longitude, latitude)
                                newMarker.markerType = MapPOIItem.MarkerType.CustomImage

                                newMarker?.customImageResourceId = R.drawable.yellowflag

                                mapView?.addPOIItem(newMarker)
                                val bookmark_sfID = parts?.get(2)
                                Log.d("북마크 실험용(추가용)", "${bookmark_sfID}")
                                createBookmark(bookmark_sfID)





                            }
                            1 -> {
                                //val destinationLatitude = ylad

                                val uri = Uri.parse("kakaomap://route?sp=${parts?.get(3)},${parts?.get(4)}&ep=${parts?.get(5)},${parts?.get(6)}&by=FOOT")
                                val intent = Intent(Intent.ACTION_VIEW, uri)
                                context.startActivity(intent)
                            }
                            2 -> dialog.dismiss()   // 대화상자 닫기
                        }
                    }
                    builder.show()

                }
                false->{
                    val builder = AlertDialog.Builder(context)

                    val itemList = arrayOf("북마크 제거", "길찾기", "취소")
                    builder.setTitle("${parts?.get(0)}")
                    builder.setItems(itemList) { dialog, which ->
                        when (which) {
                            0 -> {
                                mapView?.removePOIItem(poiItem)
                                val newMarker = MapPOIItem()
                                newMarker.itemName = "${parts?.get(0)}|${parts?.get(1)}|${parts?.get(2)}|${parts?.get(3)}|${parts?.get(4)}|${parts?.get(5)}|${parts?.get(6)}|0"
                                val longitude:Double? = parts?.get(5)?.toDouble()
                                val latitude:Double? = parts?.get(6)?.toDouble()
                                if(longitude !=null && latitude != null)
                                    newMarker.mapPoint = MapPoint.mapPointWithGeoCoord(longitude, latitude)
                                newMarker.markerType = MapPOIItem.MarkerType.CustomImage
//                                poiItem?.markerType = MapPOIItem.MarkerType.CustomImage
//                                poiItem?.customImageResourceId = R.drawable.blueflag
//                                poiItem?.isCustomImageAutoscale = true
                                newMarker?.customImageResourceId = R.drawable.blueflag


                                mapView?.addPOIItem(newMarker)

                                val bookmark_sfID = parts?.get(2)
                                Log.d("태현", "${bookmark_sfID}")
                                removeBookmark(bookmark_sfID)











                            }
                            1 -> {
                                //val destinationLatitude = ylad

                                val uri = Uri.parse("kakaomap://route?sp=${parts?.get(3)},${parts?.get(4)}&ep=${parts?.get(5)},${parts?.get(6)}&by=FOOT")
                                val intent = Intent(Intent.ACTION_VIEW, uri)
                                context.startActivity(intent)
                            }
                            2 -> dialog.dismiss()   // 대화상자 닫기
                        }
                    }
                    builder.show()
                }

            }
        }


        override fun onDraggablePOIItemMoved(mapView: MapView?, poiItem: MapPOIItem?, mapPoint: MapPoint?) {
            // 마커의 속성 중 isDraggable = true 일 때 마커를 이동시켰을 경우
        }
    }




    //////
//    // Mission 데이터를 담을 모델 클래스
//    data class TargetData(val sf_id: Int, val sf_name: String, val sf_tel: String,
//                          val sf_latitude: Double, val sf_longitude: Double, val sf_addr: String)

    // Retrofit을 사용하여 JSON 데이터를 가져오기 위한 API 인터페이스
    interface TargetApiService {
        @GET // 여기에 실제 JSON URL을 넣으세요.
        fun getTargetData(@Url url: String, @Header("Authorization") token:String?): Call<List<TargetMapData>>
    }



    // Retrofit 인스턴스 생성
    val retrofit = Retrofit.Builder()
        .baseUrl("http://3.35.218.61:8080/api/senior-facilities/") // 여기에 API의 기본 URL을 넣으세요.
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // API 서비스 생성
    val targetApiService = retrofit.create(TargetApiService::class.java)
    //flag10
    // JSON 데이터를 가져오는 함수
    fun fetchMissionData() {
        val sharedPreferencesToken= getSharedPreferences("token", 0);
        val sharedPreferencesUser = getSharedPreferences("user", 0);
        val accessToken:String? = sharedPreferencesToken.getString("accessToken", "")
        val call = targetApiService.getTargetData("http://3.35.218.61:8080/api/senior-facilities/${sharedPreferencesUser.getInt("u_id",0)}",accessToken)
        call.enqueue(object : Callback<List<TargetMapData>> {
            override fun onResponse(call: Call<List<TargetMapData>>, response: Response<List<TargetMapData>>) {
                if (response.isSuccessful) {
                    val targetList = response.body() ?: emptyList()
                    // missionList를 MissionMapData 객체로 변환하여 missionMapData 리스트에 추가
                    //TargetMapData.clear() // 리스트 초기화
                    for (targetData in targetList) {
                        selectedTargetMapData.add(
                            TargetMapData(
                                targetData.sf_id,
                                targetData.sf_name,
                                targetData.sf_tel,
                                targetData.sf_latitude,
                                targetData.sf_longitude,
                                targetData.sf_addr
                            )
                        )
                    }
                    // 데이터를 성공적으로 가져왔으므로 지도에 표시하거나 다른 작업 수행
                    setMissionMark(selectedTargetMapData, "yellow")
                } else {
                    Log.e("kim", "error")
                }
            }


            override fun onFailure(call: Call<List<TargetMapData>>, t: Throwable) {
                // 네트워크 오류 또는 예외 처리
                return
            }
        })
    }

    fun fetchNonSelectedMissionData() {
        val sharedPreferencesToken= getSharedPreferences("token", 0);
        val sharedPreferencesUser = getSharedPreferences("user", 0);
        val accessToken:String? = sharedPreferencesToken.getString("accessToken", "")
        val call = targetApiService.getTargetData("http://3.35.218.61:8080/api/senior-facilities/non-selected/${sharedPreferencesUser.getInt("u_id",0)}",accessToken)
        call.enqueue(object : Callback<List<TargetMapData>> {
            override fun onResponse(call: Call<List<TargetMapData>>, response: Response<List<TargetMapData>>) {
                if (response.isSuccessful) {
                    val targetList = response.body() ?: emptyList()
                    // missionList를 MissionMapData 객체로 변환하여 missionMapData 리스트에 추가
                    //TargetMapData.clear() // 리스트 초기화
                    for (targetData in targetList) {
                        nonSelectedTargetMapData.add(
                            TargetMapData(
                                targetData.sf_id,
                                targetData.sf_name,
                                targetData.sf_tel,
                                targetData.sf_latitude,
                                targetData.sf_longitude,
                                targetData.sf_addr
                            )
                        )
                    }
                    // 데이터를 성공적으로 가져왔으므로 지도에 표시하거나 다른 작업 수행
                    setMissionMark(nonSelectedTargetMapData,"blue")
                } else {
                    Log.e("kim", "error")
                }
            }


            override fun onFailure(call: Call<List<TargetMapData>>, t: Throwable) {
                // 네트워크 오류 또는 예외 처리
                return
            }
        })
    }












}
