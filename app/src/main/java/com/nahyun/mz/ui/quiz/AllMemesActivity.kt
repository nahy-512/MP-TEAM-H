package com.nahyun.mz.ui.quiz

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.nahyun.mz.R

/**
 * 전체 밈(Meme)들을 보여주는 액티비티
 * MZ 세대에게 인기있는 밈, 유행어, 인터넷 문화 등을 모아놓은 화면
 */
class AllMemesActivity : AppCompatActivity() {

    /**
     * 액티비티가 시작될 때 호출되는 함수
     * 레이아웃 설정 및 버튼 이벤트 연결
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 해당 액티비티에서 사용할 레이아웃 설정 (activity_all_meme.xml 파일)
        setContentView(R.layout.activity_mz_all_meme)

        // '뒤로가기' 버튼 찾기 (xml의 btn_back)
        val backButton = findViewById<ImageButton>(R.id.btn_back)

        // 뒤로가기 버튼 클릭 시 현재 액티비티 종료
        backButton.setOnClickListener {
            finish()
        }

        // '제보하기' 버튼 찾기 (xml의 btn_time_travel)
        val reportButton = findViewById<Button>(R.id.btn_time_travel)

        // 제보 버튼 클릭 시, 제보 다이얼로그 창 띄우기
        reportButton.setOnClickListener {
            ReportMemeDialogFragment.newInstance().show(supportFragmentManager, "report_meme_dialog")
        }

        // 모든 밈 카드의 클릭 이벤트 설정 함수 호출
        setupMemeCardClicks()
    }

    /**
     * 각 밈 카드 클릭 시 해당 게시글로 이동하는 이벤트 설정 함수
     * 총 8개의 밈 카드가 있고 각각 다른 게시글 ID로 이동
     */
    private fun setupMemeCardClicks() {
        // 첫 번째 밈 카드 클릭 시 (퉁퉁퉁..뭐?)
        findViewById<CardView>(R.id.card_meme_1).setOnClickListener {
            navigateToPostDetail("meme", "meme1")
        }

        // 두 번째 밈 카드 클릭 시 (나니가스키?)
        findViewById<CardView>(R.id.card_meme_2).setOnClickListener {
            navigateToPostDetail("meme", "meme2")
        }

        // 세 번째 밈 카드 클릭 시 (관식이병)
        findViewById<CardView>(R.id.card_meme_3).setOnClickListener {
            navigateToPostDetail("meme", "meme3")
        }

        // 네 번째 밈 카드 클릭 시 (요즘 대세는 듀..가나디)
        findViewById<CardView>(R.id.card_meme_4).setOnClickListener {
            navigateToPostDetail("meme", "meme4")
        }

        // 다섯 번째 밈 카드 클릭 시 (아직도 JMT 하는 사람 없지?)
        findViewById<CardView>(R.id.card_meme_5).setOnClickListener {
            navigateToPostDetail("meme", "meme5")
        }

        // 여섯 번째 밈 카드 클릭 시 (올리브영 복숭아 당도최고?)
        findViewById<CardView>(R.id.card_meme_6).setOnClickListener {
            navigateToPostDetail("meme", "meme6")
        }

        // 일곱 번째 밈 카드 클릭 시 (불닭볶근면을낅여오거라)
        findViewById<CardView>(R.id.card_meme_7).setOnClickListener {
            navigateToPostDetail("meme", "meme7")
        }

        // 여덟 번째 밈 카드 클릭 시 (이 아저씨 모르면 누르세요)
        findViewById<CardView>(R.id.card_meme_8).setOnClickListener {
            navigateToPostDetail("meme", "meme8")
        }
    }

    /**
     * 게시글 상세 화면으로 이동하는 공통 함수
     * @param category: 카테고리 (meme)
     * @param postId: 특정 게시글 ID (meme1, meme2, ...)
     */
    private fun navigateToPostDetail(category: String, postId: String) {
        // PostDetailActivity로 이동할 Intent 생성
        val intent = Intent(this, PostDetailActivity::class.java)

        // 이동할 화면에 카테고리와 게시글 ID 정보 전달
        intent.putExtra("CATEGORY", category)
        intent.putExtra("POST_ID", postId)

        // 화면 전환 실행
        startActivity(intent)
    }
}