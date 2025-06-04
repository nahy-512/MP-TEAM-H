package com.nahyun.mz.ui.quiz

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.recyclerview.widget.GridLayoutManager
import com.nahyun.mz.R
import com.nahyun.mz.databinding.ActivityMzAllMemeBinding
import com.nahyun.mz.domain.model.Meme
import com.nahyun.mz.domain.model.MemeType
import com.nahyun.mz.domain.model.recentMemeList
import com.nahyun.mz.ui.base.BaseActivity

/**
 * 전체 밈(Meme)들을 보여주는 액티비티
 * MZ 세대에게 인기있는 밈, 유행어, 인터넷 문화 등을 모아놓은 화면
 */
class AllMemesActivity : BaseActivity<ActivityMzAllMemeBinding>(R.layout.activity_mz_all_meme) {

    override fun setup() {
        setMemeAdapter()
    }

    /**
     * 액티비티가 시작될 때 호출되는 함수
     * 레이아웃 설정 및 버튼 이벤트 연결
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
    }

    private fun setMemeAdapter() {
        val memeAdapter = MemeAdapter(recentMemeList)
        binding.rvMeme.apply {
            adapter = memeAdapter
            layoutManager = GridLayoutManager(this@AllMemesActivity, 2)
        }
        memeAdapter.setMemeClickListener(object : MemeAdapter.MyItemClickListener {
            override fun onItemClick(meme: Meme) { // 게시글 상세 화면으로 이동
                navigateToPostDetail(meme.id)
            }
        })
    }

    /**
     * 게시글 상세 화면으로 이동하는 공통 함수
     * @param category: 카테고리 (meme)
     * @param postId: 특정 게시글 ID (meme1, meme2, ...)
     */
    private fun navigateToPostDetail(postId: Int) {
        val memeTag = MemeType.MEME.categoryName
        // PostDetailActivity로 이동할 Intent 생성
        val intent = Intent(this, PostDetailActivity::class.java)

        // 이동할 화면에 카테고리와 게시글 ID 정보 전달
        intent.putExtra("CATEGORY", memeTag)
        intent.putExtra("POST_ID", "${memeTag}${postId}")

        // 화면 전환 실행
        startActivity(intent)
    }
}