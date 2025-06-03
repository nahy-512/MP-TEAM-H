package com.nahyun.mz.ui.quiz

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.recyclerview.widget.GridLayoutManager
import com.nahyun.mz.R
import com.nahyun.mz.databinding.ActivityMzAllCultureBinding
import com.nahyun.mz.domain.model.Meme
import com.nahyun.mz.domain.model.MemeType
import com.nahyun.mz.domain.model.exploreMemeList
import com.nahyun.mz.ui.base.BaseActivity

/**
 * 전체 탐구 영역을 보여주는 액티비티
 * MZ 세대의 문화, 트렌드, 특징 등을 탐구하는 콘텐츠들을 모아놓은 화면
 */
class AllExploreActivity : BaseActivity<ActivityMzAllCultureBinding>(R.layout.activity_mz_all_culture) {

    override fun setup() {
        setMemeAdapter()
    }

    /**
     * 액티비티가 생성될 때 실행되는 함수
     * 뒤로가기, 제보하기 버튼 설정 및 카드 클릭 이벤트 연결
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
    }

    private fun setMemeAdapter() {
        val memeAdapter = MemeAdapter(exploreMemeList)
        binding.rvMeme.apply {
            adapter = memeAdapter
            layoutManager = GridLayoutManager(this@AllExploreActivity, 2)
        }
        memeAdapter.setMemeClickListener(object : MemeAdapter.MyItemClickListener {
            override fun onItemClick(meme: Meme) { // 게시글 상세 화면으로 이동
                navigateToPostDetail(meme.id)
            }
        })
    }

    /**
     * 특정 게시글 상세 화면으로 이동하는 함수
     * @param category: 카테고리 (explore)
     * @param postId: 특정 게시글 ID (explore1, explore2, ...)
     */
    private fun navigateToPostDetail(postId: Int) {
        val memeTag = MemeType.EXPLORE.categoryName
        // 다음 화면으로 이동하기 위한 Intent 생성
        val intent = Intent(this, PostDetailActivity::class.java)

        // 이동할 화면에 데이터(CATEGORY와 POST_ID)를 전달
        intent.putExtra("CATEGORY", memeTag)
        intent.putExtra("POST_ID", "${memeTag}${postId}")

        // 실제로 화면 이동 실행
        startActivity(intent)
    }
}