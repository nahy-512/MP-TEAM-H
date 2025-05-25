package com.nahyun.mz.ui.quiz

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.nahyun.mz.R

/**
 * 전체 탐구 영역을 보여주는 액티비티
 * MZ 세대의 문화, 트렌드, 특징 등을 탐구하는 콘텐츠들을 모아놓은 화면
 */
class AllExploreActivity : AppCompatActivity() {

    /**
     * 액티비티가 생성될 때 실행되는 함수
     * 뒤로가기, 제보하기 버튼 설정 및 카드 클릭 이벤트 연결
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 화면에 보여줄 레이아웃 설정 (activity_all_culture.xml 파일 사용)
        setContentView(R.layout.activity_mz_all_culture)

        // '뒤로 가기' 버튼을 xml에서 찾아 변수에 저장
        val backButton = findViewById<ImageButton>(R.id.btn_back)

        // '뒤로 가기' 버튼을 클릭했을 때 현재 화면을 종료
        backButton.setOnClickListener {
            finish()
        }

        // '제보하기' 버튼을 xml에서 찾아 변수에 저장
        val reportButton = findViewById<Button>(R.id.btn_time_travel)

        // '제보하기' 버튼 클릭 시 제보 다이얼로그를 화면에 표시
        reportButton.setOnClickListener {
            ReportMemeDialogFragment.newInstance().show(supportFragmentManager, "report_meme_dialog")
        }

        // 탐구 영역 카드들을 클릭할 수 있도록 설정하는 함수 호출
        setupExploreCardClicks()
    }

    /**
     * 각 탐구 영역 카드를 클릭했을 때 해당 게시글로 이동하는 이벤트 설정
     * 총 8개의 탐구 영역 카드가 있고 각각 다른 게시글 ID로 이동
     */
    private fun setupExploreCardClicks() {
        // 카드 1 클릭 시 'explore1' 게시글 상세 화면으로 이동
        findViewById<CardView>(R.id.card_explore_1).setOnClickListener {
            navigateToPostDetail("explore", "explore1")
        }

        // 카드 2 클릭 시 'explore2' 게시글 상세 화면으로 이동
        findViewById<CardView>(R.id.card_explore_2).setOnClickListener {
            navigateToPostDetail("explore", "explore2")
        }

        // 카드 3 클릭 시 'explore3' 게시글 상세 화면으로 이동
        findViewById<CardView>(R.id.card_explore_3).setOnClickListener {
            navigateToPostDetail("explore", "explore3")
        }

        // 카드 4 클릭 시 'explore4' 게시글 상세 화면으로 이동
        findViewById<CardView>(R.id.card_explore_4).setOnClickListener {
            navigateToPostDetail("explore", "explore4")
        }

        // 카드 5 클릭 시 'explore5' 게시글 상세 화면으로 이동
        findViewById<CardView>(R.id.card_explore_5).setOnClickListener {
            navigateToPostDetail("explore", "explore5")
        }

        // 카드 6 클릭 시 'explore6' 게시글 상세 화면으로 이동
        findViewById<CardView>(R.id.card_explore_6).setOnClickListener {
            navigateToPostDetail("explore", "explore6")
        }

        // 카드 7 클릭 시 'explore7' 게시글 상세 화면으로 이동
        findViewById<CardView>(R.id.card_explore_7).setOnClickListener {
            navigateToPostDetail("explore", "explore7")
        }

        // 카드 8 클릭 시 'explore8' 게시글 상세 화면으로 이동
        findViewById<CardView>(R.id.card_explore_8).setOnClickListener {
            navigateToPostDetail("explore", "explore8")
        }
    }

    /**
     * 특정 게시글 상세 화면으로 이동하는 함수
     * @param category: 카테고리 (explore)
     * @param postId: 특정 게시글 ID (explore1, explore2, ...)
     */
    private fun navigateToPostDetail(category: String, postId: String) {
        // 다음 화면으로 이동하기 위한 Intent 생성
        val intent = Intent(this, PostDetailActivity::class.java)

        // 이동할 화면에 데이터(CATEGORY와 POST_ID)를 전달
        intent.putExtra("CATEGORY", category)
        intent.putExtra("POST_ID", postId)

        // 실제로 화면 이동 실행
        startActivity(intent)
    }
}