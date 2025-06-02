package com.nahyun.mz.ui.login

import android.content.Intent
import com.nahyun.mz.ui.MainActivity
import com.nahyun.mz.R
import com.nahyun.mz.databinding.ActivityLoginBinding
import com.nahyun.mz.ui.base.BaseActivity

class LoginActivity : BaseActivity<ActivityLoginBinding>(R.layout.activity_login) {
    override fun setup() {
        binding.isShowPassword = false // 초기화

        initClickListeners()
    }

    fun initClickListeners() {
        // 비밀번호 보기/숨기기
        binding.loginPwdShowBtn.setOnClickListener {
            binding.isShowPassword = !(binding.isShowPassword!!)
        }

        // 로그인
        binding.loginBtn.setOnClickListener {
            //TODO: 로그인 API 호출
            startActivity(Intent(this, MainActivity::class.java)) // 메인 화면으로 이동
            finish()
        }
    }
}