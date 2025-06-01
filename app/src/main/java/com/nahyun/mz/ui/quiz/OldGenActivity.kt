package com.nahyun.mz.ui.quiz

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.nahyun.mz.ui.MainActivity
import com.nahyun.mz.R

/**
 * 옛날 세대(Old Generation) 관련 콘텐츠를 보여주는 활동
 * 이 화면에서는 과거의 노래, 문화 등을 소개
 */
class OldGenActivity : AppCompatActivity() {

    /**
     * 노래 정보를 담는 데이터 클래스
     * @param title: 노래 제목
     * @param url: 유튜브 링크 주소
     * @param imageResId: 앨범 이미지 리소스 ID
     */
    data class SongItem(val title: String, val url: String, val imageResId: Int)

    // 모든 노래 목록을 저장하는 리스트
    private val allSongItems = listOf(
        SongItem("김현식-내사랑 내곁에", "https://youtu.be/gtOVozYocuw?si=Rgkm-3F7CbI-Wz4v", R.drawable.song_1),
        SongItem("엄정화-Poison", "https://www.youtube.com/watch?v=-jWdqK4Gqr8", R.drawable.song_2),
        SongItem("나훈아-강촌에 살고 싶네", "https://youtu.be/w5Fqg937hvI?si=QWbGpSX0dPaKYNA0", R.drawable.song_3),
        SongItem("들국화-그것만이 내 세상", "https://youtu.be/Aomt_cCNXO0?si=KAl1ovNy1qxwJkx4", R.drawable.song_4),
        SongItem("패티킴-가을을 남기고 간 사랑", "https://youtu.be/Bh0mflFrLFg?si=iy9nNjtU0zCnu75z", R.drawable.song_5),
        SongItem("조덕배-꿈에", "https://youtu.be/92nsZ2TDwIk?si=3M31FShj_9Efb0BD", R.drawable.song_6),
        SongItem("한동준-너를 사랑해", "https://youtu.be/X_QNm7U1CUQ?si=f-xxcm-GLrOHoqya", R.drawable.song_7),
        SongItem("최헌-가을비 우산속", "https://youtu.be/iQZ60_JAzRk?si=V3aI67kOFyxFnAVc", R.drawable.song_8),
        SongItem("혜은이-감수광", "https://youtu.be/oxKzv3ZKYRk?si=BWqzjx6zjsM62_Ky", R.drawable.song_9)
    )

    // 현재 화면에 표시된 랜덤 노래 아이템들을 저장하는 변수
    // lateinit은 나중에 초기화할 것임을 의미합니다
    private lateinit var displayedSongItems: List<SongItem>

    /**
     * 액티비티가 생성될 때 호출되는 함수
     * 화면 레이아웃을 설정하고 필요한 기능들을 초기화
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // activity_old.xml 레이아웃 파일을 화면에 표시
        setContentView(R.layout.activity_old)

        // 시간 여행 버튼 (메인 화면으로 돌아가는 버튼) 찾기
        val timeTravelButton = findViewById<LinearLayout>(R.id.btn_go_present)

        // 시간 여행 버튼 클릭 이벤트 설정
        timeTravelButton.setOnClickListener {
            // 메인 화면으로 돌아가는 인텐트 생성
            val intent = Intent(this, MainActivity::class.java)
            // 인텐트 플래그 설정: 기존 액티비티 스택 위에 새로 생성하지 않고 기존 것을 재사용
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            // 메인 화면으로 이동
            startActivity(intent)
            // 현재 화면 종료 (메모리에서 제거)
            finish()
        }

        // 레전드 세대 카드들의 클릭 이벤트 설정
        setupOldCardClicks()

        // 랜덤 노래 표시 및 클릭 이벤트 설정
        setupRandomSongs()
    }

    /**
     * 옛날 세대 관련 카드들의 클릭 이벤트를 설정하는 함수
     * 각 카드를 클릭하면 해당하는 상세 정보 화면으로 이동
     */
    private fun setupOldCardClicks() {
        // 첫 번째 옛날 세대 카드 (삐삐 용어 모음)
        findViewById<CardView>(R.id.card_old_1).setOnClickListener {
            // PostDetailActivity로 이동하는 인텐트 생성
            val intent = Intent(this, PostDetailActivity::class.java)
            // 카테고리를 "old"로 설정
            intent.putExtra("CATEGORY", "old")
            // 특정 게시물 ID 설정 (old1)
            intent.putExtra("POST_ID", "old1")
            // 상세 화면으로 이동
            startActivity(intent)
        }

        // 두 번째 옛날 세대 카드 (화장 관련)
        findViewById<CardView>(R.id.card_old_2).setOnClickListener {
            val intent = Intent(this, PostDetailActivity::class.java)
            intent.putExtra("CATEGORY", "old")
            intent.putExtra("POST_ID", "old2")
            startActivity(intent)
        }

        // 세 번째 옛날 세대 카드 (추억의 아이템)
        findViewById<CardView>(R.id.card_old_3).setOnClickListener {
            val intent = Intent(this, PostDetailActivity::class.java)
            intent.putExtra("CATEGORY", "old")
            intent.putExtra("POST_ID", "old3")
            startActivity(intent)
        }

        // 네 번째 옛날 세대 카드 (패션 관련)
        findViewById<CardView>(R.id.card_old_4).setOnClickListener {
            val intent = Intent(this, PostDetailActivity::class.java)
            intent.putExtra("CATEGORY", "old")
            intent.putExtra("POST_ID", "old4")
            startActivity(intent)
        }

        // 다섯 번째 옛날 세대 카드 (예능 모음)
        findViewById<CardView>(R.id.card_old_5).setOnClickListener {
            val intent = Intent(this, PostDetailActivity::class.java)
            intent.putExtra("CATEGORY", "old")
            intent.putExtra("POST_ID", "old5")
            startActivity(intent)
        }

        // 여섯 번째 옛날 세대 카드 (써니로 보는 80년대)
        findViewById<CardView>(R.id.card_old_6).setOnClickListener {
            val intent = Intent(this, PostDetailActivity::class.java)
            intent.putExtra("CATEGORY", "old")
            intent.putExtra("POST_ID", "old6")
            startActivity(intent)
        }

        // 일곱 번째 옛날 세대 카드 (하두리)
        findViewById<CardView>(R.id.card_old_7).setOnClickListener {
            val intent = Intent(this, PostDetailActivity::class.java)
            intent.putExtra("CATEGORY", "old")
            intent.putExtra("POST_ID", "old7")
            startActivity(intent)
        }

        // 여덟 번째 옛날 세대 카드 (네이트온 버튼)
        findViewById<CardView>(R.id.card_old_8).setOnClickListener {
            val intent = Intent(this, PostDetailActivity::class.java)
            intent.putExtra("CATEGORY", "old")
            intent.putExtra("POST_ID", "old8")
            startActivity(intent)
        }
    }

    /**
     * 랜덤으로 선택된 노래들을 화면에 표시하고 클릭 이벤트를 설정하는 함수
     * 총 9개의 노래 중에서 랜덤으로 3개를 선택해서 보여줌
     */
    private fun setupRandomSongs() {
        try {
            // 각 노래 카드뷰 찾기 (3개)
            val songCard1 = findViewById<CardView>(R.id.song_card_1)
            val songCard2 = findViewById<CardView>(R.id.song_card_2)
            val songCard3 = findViewById<CardView>(R.id.song_card_3)

            // 노래 제목을 표시할 텍스트뷰 찾기 (3개)
            val songTitle1 = findViewById<TextView>(R.id.song_title_1)
            val songTitle2 = findViewById<TextView>(R.id.song_title_2)
            val songTitle3 = findViewById<TextView>(R.id.song_title_3)

            // 노래 이미지를 표시할 이미지뷰 찾기 (3개)
            val songImage1 = findViewById<ImageView>(R.id.song_image_1)
            val songImage2 = findViewById<ImageView>(R.id.song_image_2)
            val songImage3 = findViewById<ImageView>(R.id.song_image_3)

            // 전체 노래 목록을 셞플(섞고) 하고 처음 3개만 선택
            // shuffled()는 리스트를 랜덤으로 섞어주는 함수
            // take(3)은 처음 3개 요소만 가져오는 함수
            displayedSongItems = allSongItems.shuffled().take(3)

            // 선택된 노래 정보를 화면에 표시 - 첫 번째 노래
            songTitle1.text = displayedSongItems[0].title  // 노래 제목 설정
            songImage1.setImageResource(displayedSongItems[0].imageResId)  // 앨범 이미지 설정

            // 선택된 노래 정보를 화면에 표시 - 두 번째 노래
            songTitle2.text = displayedSongItems[1].title
            songImage2.setImageResource(displayedSongItems[1].imageResId)

            // 선택된 노래 정보를 화면에 표시 - 세 번째 노래
            songTitle3.text = displayedSongItems[2].title
            songImage3.setImageResource(displayedSongItems[2].imageResId)

            // 노래 카드 클릭 이벤트 설정
            // 첫 번째 카드 클릭 시 해당 노래의 유튜브 URL을 열기
            songCard1.setOnClickListener {
                openYoutubeUrl(displayedSongItems[0].url)
            }

            // 두 번째 카드 클릭 시 해당 노래의 유튜브 URL을 열기
            songCard2.setOnClickListener {
                openYoutubeUrl(displayedSongItems[1].url)
            }

            // 세 번째 카드 클릭 시 해당 노래의 유튜브 URL을 열기
            songCard3.setOnClickListener {
                openYoutubeUrl(displayedSongItems[2].url)
            }
        } catch (e: Exception) {
            // 오류 발생 시 예외를 콘솔에 출력
            e.printStackTrace()
            // 실제 앱에서는 여기에 토스트 메시지를 표시할 수 있습니다
            // Toast.makeText(this, "노래 로딩 중 오류가 발생했습니다", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * 주어진 유튜브 URL을 열어주는 함수
     * 유튜브 앱이 있으면 앱으로, 없으면 웹 브라우저로 열어줌
     * @param url: 열고 싶은 유튜브 URL
     */
    private fun openYoutubeUrl(url: String) {
        try {
            // 유튜브 앱이 설치되어 있으면 앱에서 열기 시도
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        } catch (e: Exception) {
            // 유튜브 앱이 설치되지 않았거나 URL을 열 수 없는 경우 웹 브라우저로 시도
            try {
                // 크롬 브라우저로 열기 시도
                val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                webIntent.setPackage("com.android.chrome")  // 크롬 브라우저 지정
                startActivity(webIntent)
            } catch (e2: Exception) {
                // 크롬도 없으면 기본 브라우저로 시도
                try {
                    val defaultIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    defaultIntent.setPackage(null)  // 패키지 지정 없이 (기본 브라우저 사용)
                    startActivity(defaultIntent)
                } catch (e3: Exception) {
                    // 모든 시도가 실패한 경우 오류 출력
                    e3.printStackTrace()
                    // 실제 앱에서는 여기에 토스트 메시지를 표시할 수 있습니다
                    // Toast.makeText(this, "링크를 열 수 없습니다", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}