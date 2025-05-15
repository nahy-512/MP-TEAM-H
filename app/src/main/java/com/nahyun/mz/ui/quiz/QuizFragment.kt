package com.nahyun.mz.ui.quiz

import android.content.Intent
import android.net.Uri
import android.widget.TextView
import com.nahyun.mz.R
import com.nahyun.mz.databinding.FragmentQuizBinding
import com.nahyun.mz.ui.base.BaseFragment

class QuizFragment : BaseFragment<FragmentQuizBinding>(R.layout.fragment_quiz) {

    /**
     * 뉴스 정보를 담는 데이터 클래스
     * @param title: 뉴스 제목
     * @param url: 뉴스 링크 주소
     */
    data class NewsItem(val title: String, val url: String)

    // 전체 뉴스 목록 (미리 정의된 리스트)
    private val allNewsItems = listOf(
        NewsItem("\"안 귀여워서 안 써요\"... 귀여움에 빠진 카드사", "https://www.fetv.co.kr/mobile/article.html?no=163814"),
        NewsItem("제테크도 놀이처럼! MZ세대의 이색 투자법 4", "https://woman.donga.com/money/article/all/12/5543965/1"),
        NewsItem("MZ세대 취업 희망 조건은 월급보다 '워라밸'", "https://biz.sbs.co.kr/article/20000200047?division=NAVER"),
        NewsItem("호캉스는 끝났다! MZ가 주목하는 새 낭만 여행지", "https://kizmom.hankyung.com/news/view.html?aid=202412134762o"),
        NewsItem("알뜰살뜰 MZ, 무지출챌린지 유행?!", "https://www.sedaily.com/NewsView/2DBNUCNJXV"),
        NewsItem("진짜 때려치고 만다.. 공무원에 등돌린 MZ들", "https://www.edaily.co.kr/News/Read?newsId=02296006642141368&mediaCodeNo=257&OutLnkChk=Y")
    )

    // 현재 화면에 보여질 뉴스 3개 (랜덤 선택)
    private lateinit var displayedNewsItems: List<NewsItem>

    override fun setup() {
        // 뷰 초기화, ClickListener 정의 등 작업 진행

        // "MZ 퀴즈 시작하기" 버튼 클릭 시 퀴즈 소개 화면으로 이동
        binding.btnStartQuiz.setOnClickListener {
            val intent = Intent(requireContext(), MZQuizIntroActivity::class.java)
            startActivity(intent)
        }

        // "시간 여행" 버튼 클릭 시 과거 세대 화면으로 이동
        binding.btnGoPast.setOnClickListener {
            val intent = Intent(requireContext(), OldGenActivity::class.java)
            startActivity(intent)
        }

        // "밈 더보기" 텍스트 클릭 시 전체 밈 화면으로 이동
        binding.tvMoreMeme.setOnClickListener {
            val intent = Intent(requireContext(), AllMemesActivity::class.java)
            startActivity(intent)
        }

        // "탐구 영역 더보기" 텍스트 클릭 시 전체 탐구 영역 화면으로 이동
        binding.tvMoreExplore.setOnClickListener {
            val intent = Intent(requireContext(), AllExploreActivity::class.java)
            startActivity(intent)
        }

        // 밈 카드 클릭 이벤트 설정
        setupMemeCardClicks()

        // 탐구 영역 카드 클릭 이벤트 설정
        setupExploreCardClicks()

        // 랜덤 뉴스 설정 및 클릭 이벤트 연결
        setupRandomNews()
    }

    /**
     * 밈 카드 클릭 시 게시글 상세 페이지로 이동하는 이벤트 설정
     * 3개의 메인 밈 카드에 각각 다른 밈 게시글 ID 연결
     */
    private fun setupMemeCardClicks() {
        // 첫 번째 밈 카드 클릭 시
        binding.cardMeme1.setOnClickListener {
            val intent = Intent(requireContext(), PostDetailActivity::class.java)
            // 이동할 화면에 카테고리와 게시글 ID 정보 전달
            intent.putExtra("CATEGORY", "meme")
            intent.putExtra("POST_ID", "meme1")
            startActivity(intent)
        }

        // 두 번째 밈 카드 클릭 시
        binding.cardMeme2.setOnClickListener {
            val intent = Intent(requireContext(), PostDetailActivity::class.java)
            intent.putExtra("CATEGORY", "meme")
            intent.putExtra("POST_ID", "meme2")
            startActivity(intent)
        }

        // 세 번째 밈 카드 클릭 시
        binding.cardMeme3.setOnClickListener {
            val intent = Intent(requireContext(), PostDetailActivity::class.java)
            intent.putExtra("CATEGORY", "meme")
            intent.putExtra("POST_ID", "meme3")
            startActivity(intent)
        }
    }

    /**
     * 탐구 영역 카드 클릭 시 게시글 상세 페이지로 이동하는 이벤트 설정
     * 3개의 메인 탐구 영역 카드에 각각 다른 탐구 게시글 ID 연결
     */
    private fun setupExploreCardClicks() {
        binding.cardExplore1.setOnClickListener {
            val intent = Intent(requireContext(), PostDetailActivity::class.java)
            // 이동할 화면에 카테고리와 게시글 ID 정보 전달
            intent.putExtra("CATEGORY", "explore")
            intent.putExtra("POST_ID", "explore1")
            startActivity(intent)
        }

        binding.cardExplore2.setOnClickListener {
            val intent = Intent(requireContext(), PostDetailActivity::class.java)
            intent.putExtra("CATEGORY", "explore")
            intent.putExtra("POST_ID", "explore2")
            startActivity(intent)
        }

        binding.cardExplore3.setOnClickListener {
            val intent = Intent(requireContext(), PostDetailActivity::class.java)
            intent.putExtra("CATEGORY", "explore")
            intent.putExtra("POST_ID", "explore3")
            startActivity(intent)
        }
    }

    /**
     * 뉴스 3개를 랜덤으로 선택해 화면에 보여주는 함수
     * 전체 뉴스 목록에서 섞어서 3개 선택 후 제목 표시 및 클릭 이벤트 연결
     */
    private fun setupRandomNews() {
        // 뉴스 목록을 섞어서 3개 선택
        // shuffled(): 리스트를 랜덤으로 섞음
        // take(3): 처음 3개 요소만 선택
        displayedNewsItems = allNewsItems.shuffled().take(3)

        // 선택된 뉴스 제목 표시
        // binding을 사용해서 직접 접근하거나, 아래처럼 findViewById 사용
        val newsTitle1 = binding.newsItem1.getChildAt(1) as TextView
        val newsTitle2 = binding.newsItem2.getChildAt(1) as TextView
        val newsTitle3 = binding.newsItem3.getChildAt(1) as TextView

        newsTitle1.text = displayedNewsItems[0].title
        newsTitle2.text = displayedNewsItems[1].title
        newsTitle3.text = displayedNewsItems[2].title

        // 각 뉴스 클릭 시 해당 URL로 이동
        binding.newsItem1.setOnClickListener {
            openNewsUrl(displayedNewsItems[0].url)
        }

        binding.newsItem2.setOnClickListener {
            openNewsUrl(displayedNewsItems[1].url)
        }

        binding.newsItem3.setOnClickListener {
            openNewsUrl(displayedNewsItems[2].url)
        }
    }

    /**
     * 주어진 URL을 브라우저로 여는 함수
     * @param url: 열려는 뉴스 기사 URL
     */
    private fun openNewsUrl(url: String) {
        try {
            // Intent.ACTION_VIEW: 브라우저에서 URL 열기
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        } catch (e: Exception) {
            // 에러가 날 경우 콘솔에 로그 출력
            e.printStackTrace()
        }
    }
}