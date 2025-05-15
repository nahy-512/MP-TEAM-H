package com.nahyun.mz.ui.quiz

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.nahyun.mz.R
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.nahyun.mz.MainActivity

/**
 * MZ 퀴즈 결과를 보여주는 액티비티
 * 점수 표시, 메시지 표시, 화면 캡처 기능 제공
 */
class MZQuizResultActivity : AppCompatActivity() {

    // 퀴즈 점수를 저장할 변수
    private var score = 0

    /**
     * 액티비티 생성 시 호출되는 함수
     * 점수 표시 및 버튼 이벤트 설정
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mz_quiz_result) // 레이아웃 설정

        // 이전 화면(MZQuizActivity)에서 전달받은 점수 데이터 가져오기
        score = intent.getIntExtra("SCORE", 0)

        // '뒤로가기' 버튼 클릭 시 메인 화면으로 이동
        findViewById<ImageButton>(R.id.btn_back).setOnClickListener {
            navigateToMain()
        }

        // 점수 숫자 텍스트뷰에 표시 (예: 70)
        val scoreTextView = findViewById<TextView>(R.id.tv_score)
        scoreTextView.text = score.toString()

        // "당신의 MZ력은 X점!" 텍스트 설정
        val scoreDescTextView = findViewById<TextView>(R.id.tv_score_desc)
        scoreDescTextView.text = "당신의 MZ력은 ${score}점!"

        // 점수에 따라 다른 결과 메시지 설정
        val messageTextView = findViewById<TextView>(R.id.tv_message)
        messageTextView.text = getScoreMessage(score)

        // '캡처하기' 버튼 클릭 시 화면을 이미지로 저장
        val captureButton = findViewById<Button>(R.id.btn_share)
        captureButton.setOnClickListener {
            captureScreen()
        }

        // '홈으로 돌아가기' 버튼 클릭 시 메인 화면으로 이동
        val homeButton = findViewById<Button>(R.id.btn_home)
        homeButton.setOnClickListener {
            navigateToMain()
        }
    }

    /**
     * 점수에 따라 다른 메시지를 반환하는 함수
     * @param score: 퀴즈 최종 점수
     * @return: 점수 구간에 맞는 메시지
     */
    private fun getScoreMessage(score: Int): String {
        return when {
            score >= 80 -> "오.. 좀 잘하시는데요?\n더 노력하시면 MZ력 만렙이 될 수 있어요!"
            score >= 60 -> "괜찮네요! MZ세대와 대화할 때\n어려움은 없을 것 같아요."
            score >= 40 -> "아쉽지만 절반은 성공!\n좀 더 MZ 용어를 알아보세요."
            score >= 20 -> "조금 더 노력해보세요.\nMZ 밈과 트렌드에 관심을 가져보는 건 어떨까요?"
            else -> "이런.. MZ 세계는 아직 먼 나라인가요?\n괜찮아요, 지금부터 알아가면 됩니다!"
        }
    }

    /**
     * 현재 화면을 캡처해서 갤러리에 저장하는 함수
     * 화면 전체를 비트맵으로 변환 후 JPEG 파일로 저장
     */
    private fun captureScreen() {
        // 상태바 제외한 전체 화면 뷰 가져오기
        val rootView = window.decorView.findViewById<FrameLayout>(android.R.id.content)
        rootView.isDrawingCacheEnabled = true  // 드로잉 캐시 활성화
        val bitmap = Bitmap.createBitmap(rootView.drawingCache)  // 비트맵으로 복사
        rootView.isDrawingCacheEnabled = false  // 캐시 비활성화

        try {
            // 저장할 파일명 만들기 (시간 기준)
            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val fileName = "MZ_Quiz_Result_$timestamp.jpg"

            // 저장 경로 지정 (앱 전용 폴더)
            val picturePath = getExternalFilesDir(null)?.absolutePath + "/Pictures/"
            val dir = File(picturePath)
            if (!dir.exists()) {
                dir.mkdirs()  // 폴더가 없으면 생성
            }

            // 파일 생성 및 비트맵 저장
            val file = File(dir, fileName)
            val fos = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)  // 고화질(100%)로 저장
            fos.flush()  // 버퍼 비우기
            fos.close()  // 파일 스트림 닫기

            // 갤러리 앱이 새 이미지를 인식하도록 미디어 스캔 요청
            val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            mediaScanIntent.data = android.net.Uri.fromFile(file)
            sendBroadcast(mediaScanIntent)

            // 저장 성공 메시지 출력
            Toast.makeText(this, "결과가 저장되었습니다: ${file.absolutePath}", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            e.printStackTrace()
            // 저장 실패 시 에러 메시지 출력
            Toast.makeText(this, "저장 실패: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * 메인 화면으로 이동하는 함수
     * 스택을 모두 정리하고 메인 화면을 새로 시작
     */
    private fun navigateToMain() {
        // MainActivity로 이동 (Fragment가 아닌 Activity로!)
        val intent = Intent(this, MainActivity::class.java)

        // 기존 액티비티 스택을 모두 정리하고 메인만 남김
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP

        // 선택사항: Quiz Fragment로 바로 이동하도록 설정
        intent.putExtra("navigate_to", "quiz")

        startActivity(intent)
        finish()  // 현재 액티비티 종료
    }
}