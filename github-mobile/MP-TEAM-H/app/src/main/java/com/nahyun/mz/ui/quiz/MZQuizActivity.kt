package com.nahyun.mz.ui.quiz

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.nahyun.mz.R

/**
 * MZ 세대 관련 퀴즈를 진행하는 액티비티
 * 파이어베이스에서 퀴즈 데이터를 가져와 15초 제한 시간으로 진행
 */
class MZQuizActivity : AppCompatActivity() {

    private var db: FirebaseFirestore? = null

    // 퀴즈 관련 변수들
    private val quizList = mutableListOf<Quiz>()  // 현재 퀴즈들을 저장하는 리스트
    private var currentQuizIndex = 0              // 현재 퀴즈의 인덱스
    private var score = 0                         // 현재 점수
    private var selectedAnswerIndex = -1          // 사용자가 선택한 답의 인덱스 (-1은 선택 안 함)

    // 로그 태그 (디버깅용)
    private val TAG = "MZQuizActivity"

    // 타이머 관련 변수들
    private var timer: CountDownTimer? = null      // 카운트다운 타이머
    private lateinit var timerTextView: TextView   // 타이머를 표시할 텍스트뷰
    private val QUIZ_TIME_SECONDS = 15             // 퀴즈 제한 시간 (15초)

    // UI 요소들에 대한 참조 변수들
    private lateinit var questionNumTextView: TextView    // 질문 번호 표시
    private lateinit var questionTextView: TextView       // 질문 내용 표시
    private lateinit var questionImageView: ImageView     // 질문에 포함된 이미지
    private lateinit var progressIndicator: View          // 진행 상태 표시 바
    private lateinit var choiceButtons: List<LinearLayout>  // 선택지 버튼들
    private lateinit var choiceTextViews: List<TextView>   // 선택지 텍스트들
    private lateinit var nextButton: TextView             // 다음 버튼

    // 다음 문제로 자동 이동 시 사용할 핸들러
    private val handler = Handler(Looper.getMainLooper())

    /**
     * 액티비티가 생성될 때 호출되는 함수
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mz_quiz)

        try {
            // Firebase 초기화 시도
            FirebaseApp.initializeApp(this)
            db = FirebaseFirestore.getInstance()
        } catch (e: Exception) {
            Log.e(TAG, "Firebase 초기화 오류: ${e.message}")
            // Firebase 초기화 실패해도 계속 진행 (하드코딩된 퀴즈 사용)
        }

        try {
            // UI 요소들 초기화
            initViews()

            // 뒤로가기 버튼 설정
            findViewById<ImageButton>(R.id.btn_back)?.setOnClickListener {
                timer?.cancel()  // 타이머 취소
                handler.removeCallbacksAndMessages(null)  // 핸들러 콜백 제거
                finish()  // 현재 화면 종료
            }

            // 파이어스토어가 초기화되었으면 퀴즈 가져오기 시도
            if (db != null) {
                loadRandomQuizzes()
            } else {
                // Firebase 사용 불가능하면 바로 하드코딩된 퀴즈 사용
                loadHardcodedQuizzes()
            }
        } catch (e: Exception) {
            Log.e(TAG, "onCreate 오류: ${e.message}")
            Toast.makeText(this, "앱 초기화 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
            // 심각한 오류 발생 시 메인으로 돌아가기
            navigateToMain()
        }
    }

    /**
     * UI 요소들을 초기화하는 함수
     * 각 뷰를 찾아서 변수에 연결하고 이벤트 리스너를 설정
     */
    private fun initViews() {
        try {
            // 진행 상태 표시 바
            progressIndicator = findViewById(R.id.progress_indicator)

            // 질문 관련 뷰들
            questionNumTextView = findViewById(R.id.tv_question_num)
            questionTextView = findViewById(R.id.tv_question)
            questionImageView = findViewById(R.id.iv_question)

            // 타이머 텍스트뷰 생성 및 추가
            createTimerTextView()

            // 선택지 버튼들 (4개)
            choiceButtons = listOf(
                findViewById(R.id.btn_choice_1),
                findViewById(R.id.btn_choice_2),
                findViewById(R.id.btn_choice_3),
                findViewById(R.id.btn_choice_4)
            )

            // 선택지 텍스트뷰들 (4개)
            choiceTextViews = listOf(
                findViewById(R.id.tv_choice_1),
                findViewById(R.id.tv_choice_2),
                findViewById(R.id.tv_choice_3),
                findViewById(R.id.tv_choice_4)
            )

            // 선택지 클릭 이벤트 설정
            choiceButtons.forEachIndexed { index, button ->
                button.setOnClickListener {
                    // 이전에 선택한 색상 초기화
                    resetChoicesBackground()

                    // 현재 선택한 답변으로 설정
                    selectedAnswerIndex = index

                    // 선택한 답변 색상 변경 (연한 보라색)
                    setRoundedBackground(choiceButtons[index], "#F0EFFF")

                    // 다음 버튼 표시
                    nextButton.visibility = View.VISIBLE
                }
            }

            // 다음 버튼
            nextButton = findViewById(R.id.btn_next)
            nextButton.visibility = View.GONE  // 처음에는 숨김
            nextButton.setOnClickListener {
                // 타이머 취소
                timer?.cancel()
                // 핸들러 콜백 제거
                handler.removeCallbacksAndMessages(null)

                // 정답 처리 및 결과 표시
                processAnswer()
            }
        } catch (e: Exception) {
            Log.e(TAG, "initViews 오류: ${e.message}")
            throw e  // 필수 뷰 초기화 실패 시 상위로 예외 전파
        }
    }

    /**
     * 사용자의 답변을 처리하는 함수
     * 정답/오답을 확인하고 다음 문제로 진행
     */
    private fun processAnswer() {
        try {
            val currentQuiz = quizList[currentQuizIndex]
            val correctIndex = currentQuiz.answer

            // 점수 계산
            if (selectedAnswerIndex == correctIndex) {
                // 정답인 경우 점수 추가 (문제당 20점)
                score += 20

                // 바로 다음 문제로 이동
                moveToNextQuiz()
            } else {
                // 오답인 경우 정답 표시 후 1초 뒤 다음 문제
                // 선택한 답은 이미 F0EFFF 색상으로 표시됨 (연한 보라색)

                // 정답 보기에 다른 색상 적용 (연한 분홍색)
                setRoundedBackground(choiceButtons[correctIndex], "#FBE8FF")

                // 1초 후 다음 문제로 이동
                handler.postDelayed({
                    moveToNextQuiz()
                }, 1000)
            }
        } catch (e: Exception) {
            Log.e(TAG, "processAnswer 오류: ${e.message}")
            moveToNextQuiz()  // 오류 발생 시 그냥 다음 문제로 이동
        }
    }

    /**
     * 타이머 텍스트뷰를 동적으로 생성하고 질문 번호 옆에 추가하는 함수
     * RelativeLayout을 사용해 질문 번호와 타이머를 같은 줄에 배치함
     */
    private fun createTimerTextView() {
        try {
            // 질문 번호 텍스트뷰와 그 부모 레이아웃 찾기
            val questionNumTextView = findViewById<TextView>(R.id.tv_question_num)
            val parentLayout = questionNumTextView.parent as? LinearLayout

            if (parentLayout != null) {
                // 원래 질문 번호의 위치 및 스타일 정보 저장
                val originalLayoutParams = questionNumTextView.layoutParams
                val originalTextSize = questionNumTextView.textSize

                // RelativeLayout으로 새로운 컨테이너 생성
                val relativeLayout = RelativeLayout(this)
                relativeLayout.layoutParams = originalLayoutParams

                // 질문 번호에 ID 부여 (RelativeLayout 내에서 참조하기 위해)
                questionNumTextView.id = View.generateViewId()

                // 질문 번호를 RelativeLayout의 왼쪽에 배치
                val questionParams = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
                )
                questionParams.addRule(RelativeLayout.ALIGN_PARENT_START)  // 왼쪽 정렬
                questionNumTextView.layoutParams = questionParams

                // 원래 부모에서 질문 번호 제거
                parentLayout.removeView(questionNumTextView)

                // RelativeLayout에 질문 번호 추가
                relativeLayout.addView(questionNumTextView)

                // 타이머 TextView 생성
                timerTextView = TextView(this).apply {
                    id = View.generateViewId()
                    val params = RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                    )
                    params.addRule(RelativeLayout.ALIGN_PARENT_END)  // 오른쪽 정렬
                    layoutParams = params
                    text = "$QUIZ_TIME_SECONDS:00"  // 초기 타이머 텍스트
                    setTextColor(android.graphics.Color.parseColor("#7D3BEE"))  // 보라색
                    textSize = originalTextSize / resources.displayMetrics.scaledDensity  // 동일한 크기
                }

                // RelativeLayout에 타이머 추가
                relativeLayout.addView(timerTextView)

                // 질문 번호가 있던 위치에 RelativeLayout 추가
                parentLayout.addView(relativeLayout, 0)

                Log.d(TAG, "타이머가 성공적으로 질문 번호 옆에 추가됨")
                return
            }

            // 실패 시 오버레이 방법 사용
            createOverlayTimer()
        } catch (e: Exception) {
            Log.e(TAG, "타이머 추가 오류: ${e.message}")
            e.printStackTrace()

            // 실패 시 오버레이 방법으로 시도
            createOverlayTimer()
        }
    }

    /**
     * 오버레이 방식으로 타이머를 생성하는 함수 (마지막 대안)
     * 화면 위에 떠있는 형태로 타이머를 표시
     */
    private fun createOverlayTimer() {
        try {
            timerTextView = TextView(this)
            timerTextView.text = "$QUIZ_TIME_SECONDS:00"
            timerTextView.setTextColor(android.graphics.Color.parseColor("#7D3BEE"))
            timerTextView.textSize = 14f

            // 화면 오른쪽 상단에 배치할 레이아웃 파라미터
            val params = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            )
            params.gravity = android.view.Gravity.TOP or android.view.Gravity.END
            params.topMargin = (140 * resources.displayMetrics.density).toInt()  // 헤더 + 여백
            params.marginEnd = (40 * resources.displayMetrics.density).toInt()   // 우측 여백

            // 화면의 최상위 레이아웃에 추가
            val decorView = window.decorView as FrameLayout
            decorView.addView(timerTextView, params)

            Log.d(TAG, "타이머가 오버레이로 추가됨")
        } catch (e: Exception) {
            Log.e(TAG, "오버레이 타이머 생성 오류: ${e.message}")
        }
    }

    /**
     * 파이어베이스에서 랜덤 퀴즈들을 가져오는 함수
     * 데이터베이스에서 모든 퀴즈를 가져온 후 랜덤으로 5개를 선택
     */
    private fun loadRandomQuizzes() {
        try {
            db?.collection("quiz")
                ?.get()
                ?.addOnSuccessListener { documents ->  // 데이터 가져오기 성공 시
                    val allQuizzes = mutableListOf<Quiz>()

                    // 각 문서를 Quiz 객체로 변환
                    for (document in documents) {
                        try {
                            val question = document.getString("question") ?: ""

                            // choices 필드를 안전하게 파싱
                            val choicesRaw = document.get("choices")
                            val choices = when (choicesRaw) {
                                is ArrayList<*> -> choicesRaw.filterIsInstance<String>()
                                is List<*> -> choicesRaw.filterIsInstance<String>()
                                else -> listOf<String>()
                            }

                            val answer = document.getLong("answer")?.toInt() ?: 0
                            val image = document.getString("image") ?: ""

                            allQuizzes.add(Quiz(document.id, question, choices, answer, image))
                        } catch (e: Exception) {
                            Log.e(TAG, "퀴즈 파싱 오류: ${e.message}, 문서 ID: ${document.id}")
                            // 개별 퀴즈 파싱 실패 시 건너뛰기
                        }
                    }

                    // 랜덤으로 5개 선택
                    quizList.clear()
                    if (allQuizzes.size > 5) {
                        quizList.addAll(allQuizzes.shuffled().take(5))
                    } else {
                        quizList.addAll(allQuizzes)
                    }

                    // 첫 번째 퀴즈 표시
                    if (quizList.isNotEmpty()) {
                        showQuiz(currentQuizIndex)
                    } else {
                        Log.w(TAG, "Firestore에서 퀴즈를 불러올 수 없음, 하드코딩된 퀴즈로 대체")
                        loadHardcodedQuizzes()
                    }
                }
                ?.addOnFailureListener { e ->  // 데이터 가져오기 실패 시
                    Log.e(TAG, "Firestore 쿼리 실패: ${e.message}")
                    loadHardcodedQuizzes()
                }
        } catch (e: Exception) {
            Log.e(TAG, "loadRandomQuizzes 오류: ${e.message}")
            loadHardcodedQuizzes()
        }
    }

    /**
     * 하드코딩된 퀴즈들을 로드하는 함수
     * 파이어베이스 연결 실패 시 백업으로 사용
     */
    private fun loadHardcodedQuizzes() {
        try {
            quizList.clear()
            quizList.addAll(listOf(
                Quiz("quiz1", "ae, 나비스와 관련된 아이돌은?",
                    listOf("뉴진스", "에스파", "르세라핌", "아이브"), 1, ""),

                Quiz("quiz2", "'에스파의 멤버가 아닌 사람은?",
                    listOf("윈터", "닝닝", "사쿠라", "지젤"), 1, ""),

                Quiz("quiz3", "'맛있다의 가장 최신 표현은?",
                    listOf("JMT", "존맛", "미쳤다", "섹시푸드"), 3, ""),

                Quiz("quiz4", "'난 너를 보면~?",
                    listOf("나니가스키?", "티라미수케잌", "괜찮아 딩딩딩", "낭만이란 배를 타고"), 1, ""),

                Quiz("quiz5", "요즘 MZ가 많이 찍는 사진 방식은?",
                    listOf("셀카", "광각샷", "정면샷", "후면샷"), 3, "")
            ))

            Log.i(TAG, "하드코딩된 퀴즈 ${quizList.size}개 로드됨")
            // 첫 번째 퀴즈 표시
            showQuiz(currentQuizIndex)
        } catch (e: Exception) {
            Log.e(TAG, "loadHardcodedQuizzes 오류: ${e.message}")
            Toast.makeText(this, "퀴즈 로딩 실패: ${e.message}", Toast.LENGTH_SHORT).show()
            navigateToMain()
        }
    }

    /**
     * 특정 인덱스의 퀴즈를 화면에 표시하는 함수
     * @param index: 표시할 퀴즈의 인덱스
     */
    private fun showQuiz(index: Int) {
        try {
            if (index >= quizList.size) {
                // 모든 퀴즈를 다 풀었으면 결과 화면으로 이동
                showResult()
                return
            }

            // 이전 타이머 취소
            timer?.cancel()

            // 핸들러 콜백 제거 (이전 대기 중인 작업 취소)
            handler.removeCallbacksAndMessages(null)

            // 이전에 선택했던 답 초기화
            selectedAnswerIndex = -1
            resetChoicesBackground()

            // 현재 퀴즈 정보 가져오기
            val quiz = quizList[index]

            // 진행 상태 업데이트 (예: 3/5)
            updateProgress(index + 1, quizList.size)

            // 질문 번호 및 텍스트 설정
            questionNumTextView.text = "질문 ${index + 1}/${quizList.size}"
            questionTextView.text = quiz.question

            // 이미지 설정 - 안전하게 처리
            try {
                if (quiz.image.isNotEmpty()) {
                    val imageResId = getImageResourceId(quiz.image)
                    if (imageResId != 0) {
                        questionImageView.setImageResource(imageResId)
                        questionImageView.visibility = View.VISIBLE
                    } else {
                        questionImageView.visibility = View.GONE
                    }
                } else {
                    questionImageView.visibility = View.GONE
                }
            } catch (e: Exception) {
                Log.e(TAG, "이미지 로딩 오류: ${e.message}")
                questionImageView.visibility = View.GONE
            }

            // 선택지 텍스트 설정
            quiz.choices.forEachIndexed { i, choice ->
                if (i < choiceTextViews.size) {
                    choiceTextViews[i].text = choice
                }
            }

            // 다음 버튼 숨기기 (선택 후에 나타남)
            nextButton.visibility = View.GONE

            // 타이머 시작
            startTimer()
        } catch (e: Exception) {
            Log.e(TAG, "showQuiz 오류: ${e.message}")
            Toast.makeText(this, "퀴즈 표시 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
            navigateToMain()
        }
    }

    /**
     * 15초 카운트다운 타이머를 시작하는 함수
     * 5초 이하가 되면 빨간색으로 색상이 변경됨
     */
    private fun startTimer() {
        try {
            // 타이머 초기화 (15초)
            timerTextView.text = "$QUIZ_TIME_SECONDS:00"
            timerTextView.setTextColor(android.graphics.Color.parseColor("#7D3BEE"))

            // 타이머 시작 (15초 동안 1초마다 업데이트)
            timer = object : CountDownTimer(QUIZ_TIME_SECONDS * 1000L, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    try {
                        val secondsLeft = millisUntilFinished / 1000
                        timerTextView.text = "$secondsLeft:00"

                        // 5초 이하면 색상 변경 (경고 효과)
                        if (secondsLeft <= 5) {
                            timerTextView.setTextColor(android.graphics.Color.RED)
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "타이머 업데이트 오류: ${e.message}")
                    }
                }

                override fun onFinish() {
                    try {
                        timerTextView.text = "0:00"
                        timerTextView.setTextColor(android.graphics.Color.RED)

                        // 시간 초과 시 정답 표시 및 다음 문제로 이동
                        handleTimeOut()
                    } catch (e: Exception) {
                        Log.e(TAG, "타이머 종료 오류: ${e.message}")
                        moveToNextQuiz()  // 오류 시 그냥 다음으로
                    }
                }
            }.start()
        } catch (e: Exception) {
            Log.e(TAG, "타이머 시작 오류: ${e.message}")
        }
    }

    /**
     * 시간 초과 시 처리하는 함수
     * 정답을 표시하고 1초 후 다음 문제로 이동하게 함
     */
    private fun handleTimeOut() {
        try {
            // 현재 문제의 정답 인덱스
            val correctIndex = quizList[currentQuizIndex].answer

            // 정답 보기 색상 변경 (연한 분홍색으로 정답 표시)
            setRoundedBackground(choiceButtons[correctIndex], "#FBE8FF")

            // 1초 후 다음 문제로 이동
            handler.postDelayed({
                moveToNextQuiz()
            }, 1000)
        } catch (e: Exception) {
            Log.e(TAG, "시간 초과 처리 오류: ${e.message}")
            moveToNextQuiz()  // 오류 시 그냥 다음으로
        }
    }

    /**
     * 퀴즈 진행 상태를 업데이트하는 함수
     * 진행률 표시 바의 너비를 조정합니다
     * @param current: 현재 퀴즈 번호
     * @param total: 전체 퀴즈 개수
     */
    private fun updateProgress(current: Int, total: Int) {
        try {
            // 프로그레스 바 업데이트 (UI 스레드에서 실행)
            progressIndicator.post {
                try {
                    val parentWidth = (progressIndicator.parent as View).width
                    val progressWidth = (parentWidth * current) / total

                    val params = progressIndicator.layoutParams
                    params.width = progressWidth
                    progressIndicator.layoutParams = params
                } catch (e: Exception) {
                    Log.e(TAG, "프로그레스 바 업데이트 오류: ${e.message}")
                    // 프로그레스 바 업데이트 실패해도 계속 진행
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "updateProgress 오류: ${e.message}")
            // 프로그레스 업데이트 실패해도 계속 진행
        }
    }

    /**
     * 현재 퀴즈의 점수를 계산하는 함수 (현재는 사용되지 않음)
     * processAnswer()에서 직접 처리하므로 참고용으로 남겨둠
     */
    private fun calculateScore() {
        // 현재 퀴즈의 정답 확인
        val currentQuiz = quizList[currentQuizIndex]
        val correctIndex = currentQuiz.answer

        // 정답인 경우 점수 추가
        if (selectedAnswerIndex == correctIndex) {
            score += 20
        }
    }

    /**
     * 모든 선택지의 배경을 기본 상태로 초기화하는 함수
     * 새로운 질문을 보여줄 때나 다른 선택지를 클릭할 때 사용
     */
    private fun resetChoicesBackground() {
        // 모든 선택지 배경을 기본 흰색으로 초기화
        choiceButtons.forEach { button ->
            try {
                button.setBackgroundResource(R.drawable.quiz_rounded_button_white)
            } catch (e: Exception) {
                Log.e(TAG, "선택지 버튼 배경 초기화 오류: ${e.message}")
            }
        }
    }

    /**
     * 선택지에 라운드 코너 배경색을 설정하는 도우미 함수
     * @param view: 배경을 변경할 뷰
     * @param colorHex: 적용할 색상 (예: "#F0EFFF")
     */
    private fun setRoundedBackground(view: View, colorHex: String) {
        try {
            // 라운드 코너 배경 생성
            val shape = android.graphics.drawable.GradientDrawable()
            shape.shape = android.graphics.drawable.GradientDrawable.RECTANGLE
            shape.cornerRadius = 8 * resources.displayMetrics.density  // 8dp를 픽셀로 변환
            shape.setColor(android.graphics.Color.parseColor(colorHex))

            // 배경 설정
            view.background = shape
        } catch (e: Exception) {
            Log.e(TAG, "라운드 배경 설정 오류: ${e.message}")
            // 오류 발생 시 단순 색상 설정 시도
            try {
                view.setBackgroundColor(android.graphics.Color.parseColor(colorHex))
            } catch (e2: Exception) {
                Log.e(TAG, "단순 배경색 변경 오류: ${e2.message}")
            }
        }
    }

    /**
     * 다음 퀴즈로 이동하는 함수
     * 인덱스를 증가시키고 새로운 퀴즈를 표시합니다
     */
    private fun moveToNextQuiz() {
        currentQuizIndex++
        showQuiz(currentQuizIndex)
    }

    /**
     * 모든 퀴즈를 완료한 후 결과 화면으로 이동하는 함수
     */
    private fun showResult() {
        try {
            // 타이머 취소
            timer?.cancel()

            // 핸들러 콜백 제거
            handler.removeCallbacksAndMessages(null)

            // 결과 화면으로 이동
            val intent = Intent(this, MZQuizResultActivity::class.java)
            intent.putExtra("SCORE", score)  // 최종 점수 전달
            startActivity(intent)
            finish()  // 현재 화면 종료
        } catch (e: Exception) {
            Log.e(TAG, "결과 화면 이동 오류: ${e.message}")
            navigateToMain()
        }
    }

    /**
     * 이미지 파일명으로 리소스 ID를 찾는 함수
     * @param imageName: 찾고 싶은 이미지 파일명
     * @return: 리소스 ID (찾지 못하면 0)
     */
    private fun getImageResourceId(imageName: String): Int {
        return try {
            resources.getIdentifier(imageName, "drawable", packageName)
        } catch (e: Exception) {
            Log.e(TAG, "리소스 ID 찾기 오류: ${e.message}")
            0  // 오류 시 0 반환 (리소스 없음)
        }
    }

    /**
     * 메인 화면으로 이동하는 함수
     * 오류 발생 시나 뒤로가기 시 사용
     */
    private fun navigateToMain() {
        try {
            timer?.cancel()  // 타이머 취소
            handler.removeCallbacksAndMessages(null)  // 핸들러 콜백 제거

            val intent = Intent(this, QuizFragment::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        } catch (e: Exception) {
            Log.e(TAG, "메인으로 이동 오류: ${e.message}")
            finish()  // 그냥 현재 화면 종료
        }
    }

    /**
     * 액티비티가 종료될 때 호출되는 함수
     * 타이머와 핸들러를 정리
     */
    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()  // 타이머 취소
        handler.removeCallbacksAndMessages(null)  // 핸들러 콜백 제거
    }
}

/**
 * 퀴즈 정보를 담는 데이터 클래스
 * @param id: 퀴즈 고유 ID
 * @param question: 질문 내용
 * @param choices: 선택지 리스트 (4개)
 * @param answer: 정답 인덱스 (0-3)
 * @param image: 질문과 함께 표시할 이미지 파일명 (선택사항)
 */
data class Quiz(
    val id: String,
    val question: String,
    val choices: List<String>,
    val answer: Int,
    val image: String
)