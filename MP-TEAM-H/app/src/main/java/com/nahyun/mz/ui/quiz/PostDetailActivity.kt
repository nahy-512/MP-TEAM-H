package com.nahyun.mz.ui.quiz

import android.graphics.Color
import android.os.Bundle
import android.text.util.Linkify
import android.text.method.LinkMovementMethod
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.nahyun.mz.R

/**
 * 게시글의 상세 내용을 보여주는 액티비티
 * 파이어베이스에서 게시글 정보를 가져와서 화면에 표시
 */
class PostDetailActivity : AppCompatActivity() {

    // 게시글 내용을 보여줄 컨테이너 (여러 블록들이 추가됨)
    private lateinit var contentContainer: LinearLayout

    // 파이어베이스 파이어스토어 인스턴스 (클라우드 데이터베이스)
    private val db = FirebaseFirestore.getInstance()

    /**
     * 액티비티가 생성될 때 호출되는 함수
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 레이아웃 파일 연결
        setContentView(R.layout.activity_post_detail)

        // 이전 화면에서 전달받은 게시글 ID와 카테고리 정보 가져오기
        // 만약 전달받지 못했으면 기본값 사용
        val postId = intent.getStringExtra("POST_ID") ?: "meme1"  // 기본값: meme1
        val category = intent.getStringExtra("CATEGORY") ?: "meme"  // 기본값: meme

        // 뷰 초기화
        contentContainer = findViewById(R.id.post_content_container)

        // 뒤로가기 버튼 찾기 및 클릭 이벤트 설정
        val backButton = findViewById<ImageButton>(R.id.btn_back)
        backButton.setOnClickListener {
            // 현재 화면 종료 (이전 화면으로 돌아가기)
            finish()
        }

        // 파이어베이스에서 게시글 데이터 가져오기
        loadPostData(category, postId)
    }

    /**
     * 파이어베이스에서 게시글 데이터를 가져오는 함수
     * @param category: 게시글 카테고리 (meme, explore, old 등)
     * @param postId: 구체적인 게시글 ID
     */
    private fun loadPostData(category: String, postId: String) {
        // 파이어베이스에서 카테고리 컬렉션의 특정 문서(게시글) 가져오기
        db.collection(category).document(postId)
            .get()
            .addOnSuccessListener { document ->  // 데이터 가져오기 성공 시
                // 문서가 존재하는지 확인
                if (document != null && document.exists()) {
                    // 게시글 기본 정보 가져오기
                    val title = document.getString("title") ?: ""  // 제목
                    val date = document.getString("date") ?: ""    // 등록일

                    // 화면에 제목과 등록일 표시
                    findViewById<TextView>(R.id.tv_post_title).text = title
                    findViewById<TextView>(R.id.tv_post_date).text = "$date 등록"

                    // 헤더 타이틀 설정 (카테고리에 따라 다르게)
                    val headerTitle = when(category) {
                        "meme" -> "밈 상세"
                        "explore" -> "탐구 영역 상세"
                        "old" -> "상세"
                        else -> "상세 보기"
                    }
                    findViewById<TextView>(R.id.tv_post_title_header).text = headerTitle

                    // 게시글 구조 정보 가져오기 (텍스트, 이미지 블록들의 배열)
                    val structure = document.get("structure") as? List<Map<String, Any>> ?: listOf()

                    // 각 블록을 순서대로 처리해서 화면에 추가
                    for (block in structure) {
                        when (block["type"] as? String) {
                            "text" -> {  // 텍스트 블록인 경우
                                val content = block["content"] as? String ?: ""
                                addTextBlock(contentContainer, content)
                            }
                            "image" -> {  // 이미지 블록인 경우
                                val ref = block["ref"] as? String ?: ""          // 이미지 파일명
                                val caption = block["caption"] as? String ?: ""  // 이미지 설명

                                // 로컬 이미지 리소스 ID 찾기
                                val resId = getImageResourceId(ref)
                                if (resId != 0) {  // 이미지가 존재하면
                                    addImageBlock(contentContainer, resId, caption)
                                }
                            }
                        }
                    }
                } else {
                    // 문서가 없는 경우 오류 메시지 표시
                    showErrorMessage("게시글을 찾을 수 없습니다")
                }
            }
            .addOnFailureListener { e ->  // 데이터 가져오기 실패 시
                // 오류 발생 시 오류 메시지 표시
                showErrorMessage("데이터를 불러오는 데 실패했습니다: ${e.message}")
            }
    }

    /**
     * 오류 메시지를 화면에 표시하는 함수
     * @param message: 표시할 오류 메시지
     */
    private fun showErrorMessage(message: String) {
        // 헤더 부분에 오류 정보 표시
        findViewById<TextView>(R.id.tv_post_title_header).text = "오류"
        findViewById<TextView>(R.id.tv_post_title).text = "게시글을 불러올 수 없습니다"
        findViewById<TextView>(R.id.tv_post_date).text = ""

        // 오류 메시지 텍스트뷰 생성
        val textView = TextView(this)

        // 레이아웃 파라미터 설정 (크기와 여백)
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,  // 가로: 부모 크기에 맞춤
            LinearLayout.LayoutParams.WRAP_CONTENT   // 세로: 내용에 맞춤
        )
        params.setMargins(0, dpToPx(16), 0, 0)  // 위쪽에 16dp 여백 추가
        textView.layoutParams = params

        // 텍스트 속성 설정
        textView.text = message
        textView.textSize = 16f  // 폰트 크기
        textView.setTextColor(resources.getColor(android.R.color.holo_red_dark, theme))  // 빨간색
        textView.gravity = android.view.Gravity.CENTER  // 중앙 정렬

        // 컨테이너에 추가
        contentContainer.addView(textView)
    }

    /**
     * 이미지 파일명으로 안드로이드 리소스 ID를 찾는 함수
     * @param imageName: 찾고 싶은 이미지 파일명 (확장자 제외)
     * @return: 리소스 ID (찾지 못하면 0)
     */
    private fun getImageResourceId(imageName: String): Int {
        // getIdentifier 함수를 사용해 리소스 ID 찾기
        val resId = resources.getIdentifier(imageName, "drawable", packageName)
        return if (resId != 0) resId else 0  // 이미지가 없는 경우 0 반환
    }

    /**
     * 텍스트 블록을 생성해서 컨테이너에 추가하는 함수
     * @param container: 텍스트를 추가할 레이아웃
     * @param text: 표시할 텍스트 내용
     */
    private fun addTextBlock(container: LinearLayout, text: String) {
        // 새로운 텍스트뷰 생성
        val textView = TextView(this)

        // 레이아웃 파라미터 설정
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(0, dpToPx(8), 0, dpToPx(8))  // 위아래 8dp 여백
        textView.layoutParams = params

        // 텍스트 속성 설정
        textView.text = text
        textView.textSize = 15f
        textView.setTextColor(Color.parseColor("#666666"))  // 회색
        textView.setLineSpacing(dpToPx(4).toFloat(), 1f)  // 줄 간격 설정

        // 링크 클릭 가능하게 만들기
        Linkify.addLinks(textView, Linkify.WEB_URLS)  // URL을 자동으로 링크로 변환
        textView.movementMethod = LinkMovementMethod.getInstance()  // 링크 클릭 활성화

        // 컨테이너에 추가
        container.addView(textView)
    }

    /**
     * 이미지 블록을 생성해서 컨테이너에 추가하는 함수
     * @param container: 이미지를 추가할 레이아웃
     * @param imageResId: 이미지 리소스 ID
     * @param caption: 이미지 설명 (선택사항)
     */
    private fun addImageBlock(container: LinearLayout, imageResId: Int, caption: String) {
        if (imageResId == 0) return  // 이미지가 없으면 추가하지 않음

        // 이미지 컨테이너 레이아웃 생성
        val imageContainer = LinearLayout(this)
        val containerParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        containerParams.setMargins(0, dpToPx(12), 0, dpToPx(12))  // 위아래 12dp 여백
        imageContainer.layoutParams = containerParams
        imageContainer.orientation = LinearLayout.VERTICAL  // 세로 방향 배치

        // 이미지 뷰 생성
        val imageView = ImageView(this)
        imageView.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        imageView.adjustViewBounds = true  // 이미지 비율에 맞춰 크기 조정
        imageView.scaleType = ImageView.ScaleType.FIT_CENTER  // 중앙 정렬로 맞춤
        imageView.setImageResource(imageResId)  // 이미지 설정

        // 컨테이너에 이미지 추가
        imageContainer.addView(imageView)

        // 캡션이 있는 경우 추가
        if (caption.isNotEmpty()) {
            val captionView = TextView(this)
            val captionParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            captionParams.setMargins(0, dpToPx(4), 0, 0)  // 위쪽에 4dp 여백
            captionView.layoutParams = captionParams
            captionView.text = caption
            captionView.textSize = 13f
            captionView.gravity = android.view.Gravity.CENTER  // 중앙 정렬
            captionView.setTextColor(Color.parseColor("#666666"))

            // 컨테이너에 캡션 추가
            imageContainer.addView(captionView)
        }

        // 메인 컨테이너에 이미지 컨테이너 추가
        container.addView(imageContainer)
    }

    /**
     * dp(density-independent pixels)를 px(pixels)로 변환하는 함수
     * @param dp: 변환할 dp 값
     * @return: 변환된 px 값
     */
    private fun dpToPx(dp: Int): Int {
        val scale = resources.displayMetrics.density  // 현재 디바이스의 화면 밀도 가져오기
        return (dp * scale + 0.5f).toInt()  // dp에 밀도를 곱하고 반올림
    }
}