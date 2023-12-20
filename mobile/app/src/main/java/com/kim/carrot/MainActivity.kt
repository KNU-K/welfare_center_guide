package com.kim.carrot

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kim.carrot.databinding.ActivityLoginBinding
import com.kim.carrot.databinding.ActivityMainBinding
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var loginbinding: ActivityLoginBinding

    private fun startNaverDeleteToken() {
        NidOAuthLogin().callDeleteTokenApi(this, object : OAuthLoginCallback {
            override fun onSuccess() {
                //서버에서 토큰 삭제에 성공한 상태입니다.
                setLayoutState(false)
                Toast.makeText(this@MainActivity, "네이버 아이디 로그아웃 성공!", Toast.LENGTH_SHORT).show()
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

            val mainintent = Intent(this, MainActivity::class.java)
            startActivity(mainintent)
        }
        else{
//            loginbinding.btnLogout.visibility = View.GONE
//            loginbinding.btnLogin.visibility = View.VISIBLE
//            loginbinding.startservice.visibility = View.GONE
        }
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        loginbinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.button2.setOnClickListener {
            val intent = Intent(this, SubActivity2::class.java)
            startActivity(intent)
        }

        binding.button1.setOnClickListener {
            val intent = Intent(this, SubActivity::class.java)
            startActivity(intent)
        }



        binding.btnLogout.setOnClickListener {
            startNaverDeleteToken()
            val loginintent = Intent(this, LoginActivity::class.java)
            startActivity(loginintent)
        }
    }




}