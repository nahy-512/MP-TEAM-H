package com.nahyun.mz.ui.quiz

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.nahyun.mz.R

/**
 * MZ 퀴즈 시작 전 소개 화면을 담당하는 액티비티
 * 퀴즈 규칙 설명 및 시작 버튼을 제공
 */
class MZQuizIntroActivity : AppCompatActivity() {

    /**
     * 액티비티 생성 시 호출되는 함수
     * 뒤로가기 버튼과 퀴즈 시작 버튼 이벤트 설정
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 레이아웃 파일 연결 (activity_mz_quiz_intro.xml)
        setContentView(R.layout.activity_mz_quiz_intro)

        // 뒤로가기 버튼 찾기 및 클릭 이벤트 설정
        findViewById<ImageButton>(R.id.btn_back).setOnClickListener {
            finish()  // 현재 화면 종료 (이전 화면으로 돌아감)
        }

        // 테스트 시작하기 버튼 찾기
        val startButton = findViewById<Button>(R.id.btn_start_test)

        // 시작 버튼 클릭 시 실제 퀴즈 화면으로 이동
        startButton.setOnClickListener {
            val intent = Intent(this, MZQuizActivity::class.java)
            startActivity(intent)  // 퀴즈 화면으로 이동
            finish()  // 소개 화면은 닫기 (뒤로가기 시 바로 메인으로 돌아가도록)
        }
    }
}