package com.kim.carrot

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kim.carrot.databinding.ActivityLoginBinding
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Url

class LoginActivity : AppCompatActivity() {

    private lateinit var loginbinding: ActivityLoginBinding
    private lateinit var mainintent: Intent

    data class NaverIdData(
        var refreshToken: String?,
        var u_name: String?,
        var u_email: String?,
        var u_provider: String?,
        var u_provider_id: String?
    )

    data class ResponseData(
        val msg: String?,
        val u_id:Int?
    )






    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainintent = Intent(this, MainActivity::class.java)
        loginbinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginbinding.root)

        val naverClientId = getString(R.string.social_login_info_naver_client_id)
        val naverClientSecret = getString(R.string.social_login_info_naver_client_secret)
        val naverClientName = getString(R.string.social_login_info_naver_client_name)
        NaverIdLoginSDK.initialize(this, naverClientId, naverClientSecret, naverClientName)

        setLayoutState(false)

        loginbinding.btnLogin.setOnClickListener {
            startNaverLogin()
        }

//        loginbinding.btnLogout.setOnClickListener {
//            startNaverDeleteToken()
//        }

        loginbinding.backmain.setOnClickListener {
            startActivity(mainintent)
        }

    }

    interface AuthApiService {
        //        @GET("http://3.35.218.61:8080/api/senior-facilities") // 현재의 GET 메서드와 함께 정의
//        fun getTargetData(): Call<List<TargetMapData>>
        @POST
        fun postnaverlogin(@Url url: String, @Body naverloginData: LoginActivity.NaverIdData): Call<LoginActivity.ResponseData>
    }

    val retrofit = Retrofit.Builder()
        .baseUrl("http://3.35.218.61:8080/api/") // 여기에 API의 기본 URL을 넣으세요.
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    val naverloginApiService = retrofit.create(AuthApiService::class.java)

    fun postNaverLogin(
        naverRefreshToken: String?,
        u_name: String?,
        u_email: String?,
        u_provider: String?,
        u_provider_id: String?
    ) {

        val naverlogindata = LoginActivity.NaverIdData(
            naverRefreshToken,
            u_name,
            u_email,
            u_provider,
            u_provider_id
            )

        //TODO : 링크 반영 할 것
        val call = naverloginApiService.postnaverlogin("http://3.35.218.61:8080/api/auth/local-login",naverlogindata)



    }






    /////////////////////////////////////////////////////////////////////////////////////////
    private fun startNaverLogin() {
        var naverAccessToken:String? =null
        var naverRefreshToken:String? =null
        val profileCallback = object : NidProfileCallback<NidProfileResponse> {
            override fun onSuccess(response: NidProfileResponse) {
                val u_name = response.profile?.name
                val u_email = response.profile?.email
                val u_provider = "naver"
                val u_provider_id =  response.profile?.id
                postNaverLogin(naverRefreshToken,u_name,u_email, u_provider, u_provider_id)
                setLayoutState(true)
                Toast.makeText(this@LoginActivity, "네이버 아이디 로그인 성공!", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(httpStatus: Int, message: String) {
                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                Toast.makeText(this@LoginActivity, "errorCode: ${errorCode}\n" +
                        "errorDescription: ${errorDescription}", Toast.LENGTH_SHORT).show()
            }
            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }
        }

        val oauthLoginCallback = object : OAuthLoginCallback {
            override fun onSuccess() {
                // 네이버 로그인 인증이 성공했을 때 수행할 코드 추가
                naverAccessToken= NaverIdLoginSDK.getAccessToken()
                naverRefreshToken = NaverIdLoginSDK.getRefreshToken() //이거를 태현이한테 retrofit으로
                /**
                 * access는 s.p에 저장
                 * refresh는 레트포핏으로 태현이에게 전송
                 */
                val sharedPreferences = getSharedPreferences("token", 0);
                val edit = sharedPreferences.edit()
                edit.putString("accessToken",naverAccessToken)
                edit.apply()


                //flag1
//                var naverRefreshToken = NaverIdLoginSDK.getRefreshToken()
//                Log.e("refresh", "REFRESH TOKEN : ${naverRefreshToken}")
//                var naverExpiresAt = NaverIdLoginSDK.getExpiresAt().toString()
//                var naverTokenType = NaverIdLoginSDK.getTokenType()
//                var naverState = NaverIdLoginSDK.getState().toString()

                //로그인 유저 정보 가져오기
                NidOAuthLogin().callProfileApi(profileCallback)
            }
            override fun onFailure(httpStatus: Int, message: String) {
                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                Toast.makeText(this@LoginActivity, "errorCode: ${errorCode}\n" +
                        "errorDescription: ${errorDescription}", Toast.LENGTH_SHORT).show()
            }
            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }
        }

        NaverIdLoginSDK.authenticate(this, oauthLoginCallback)
    }

    private fun startNaverLogout() {
        NaverIdLoginSDK.logout()
        setLayoutState(false)
        Toast.makeText(this, "네이버 아이디 로그아웃 성공!", Toast.LENGTH_SHORT).show()
    }

    private fun startNaverDeleteToken() {
        NidOAuthLogin().callDeleteTokenApi(this, object : OAuthLoginCallback {
            override fun onSuccess() {
                //서버에서 토큰 삭제에 성공한 상태입니다.
                setLayoutState(false)
                Toast.makeText(this@LoginActivity, "네이버 아이디 로그아웃 성공!", Toast.LENGTH_SHORT).show()
            }
            override fun onFailure(httpStatus: Int, message: String) {
                // 서버에서 토큰 삭제에 실패했어도 클라이언트에 있는 토큰은 삭제되어 로그아웃된 상태입니다.
                // 클라이언트에 토큰 정보가 없기 때문에 추가로 처리할 수 있는 작업은 없습니다.
                Log.d("naver", "errorCode: ${NaverIdLoginSDK.getLastErrorCode().code}")
                Log.d("naver", "errorDesc: ${NaverIdLoginSDK.getLastErrorDescription()}")
            }
            override fun onError(errorCode: Int, message: String) {
                // 서버에서 토큰 삭제에 실패했어도 클라이언트에 있는 토큰은 삭제되어 로그아웃된 상태입니다.
                // 클라이언트에 토큰 정보가 없기 때문에 추가로 처리할 수 있는 작업은 없습니다.
                onFailure(errorCode, message)
            }
        })
    }

    private fun setLayoutState(login: Boolean) {
        if(login){
            loginbinding.btnLogin.visibility = View.GONE
            loginbinding.backmain.visibility = View.VISIBLE


            startActivity(mainintent)
        }
        else{
            loginbinding.backmain.visibility = View.GONE
//            loginbinding.btnLogout.visibility = View.GONE
//            loginbinding.btnLogin.visibility = View.VISIBLE
//            loginbinding.startservice.visibility = View.GONE
        }
    }


}