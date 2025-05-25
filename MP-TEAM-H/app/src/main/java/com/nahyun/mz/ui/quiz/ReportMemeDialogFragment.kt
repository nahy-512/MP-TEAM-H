package com.nahyun.mz.ui.quiz

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.firebase.firestore.FirebaseFirestore
import com.nahyun.mz.R
import java.util.Date


/**
 * 사용자가 새로운 밈(콘텐츠)을 제보할 수 있는 대화상자 화면
 * DialogFragment를 상속받아 팝업 형태
 */
class ReportMemeDialogFragment : DialogFragment() {

    // UI 요소들을 저장할 변수들
    private lateinit var titleEditText: EditText        // 제목 입력 필드
    private lateinit var descriptionEditText: EditText  // 설명 입력 필드
    private lateinit var linkEditText: EditText         // 링크 입력 필드
    private lateinit var submitButton: LinearLayout     // 제출 버튼
    private lateinit var closeButton: ImageView         // 닫기 버튼

    private var db: FirebaseFirestore? = null

    // 로그 태그 (디버깅용)
    private val TAG = "ReportMemeDialog"

    /**
     * DialogFragment가 생성될 때 호출되는 함수
     * 대화상자의 기본 스타일을 설정
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 커스텀 다이얼로그 스타일 적용 (타이틀 바 제거)
        setStyle(STYLE_NO_TITLE, R.style.CustomDialogStyle)

        // 파이어베이스 파이어스토어 초기화
        try {
            db = FirebaseFirestore.getInstance()
        } catch (e: Exception) {
            // 초기화 실패 시 로그 출력
            Log.e(TAG, "Firestore 초기화 오류: ${e.message}")
        }
    }

    /**
     * 대화상자의 뷰를 생성하는 함수
     * 레이아웃을 연결하고 UI 요소들을 초기화
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 대화상자에 커스텀 배경 설정 (투명 배경)
        dialog?.window?.apply {
            requestFeature(Window.FEATURE_NO_TITLE)  // 타이틀 바 제거
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))  // 투명 배경
        }

        // 레이아웃 파일을 뷰로 변환 (인플레이트)
        val view = inflater.inflate(R.layout.dialog_report_meme, container, false)

        // UI 요소들 찾기 및 연결
        titleEditText = view.findViewById(R.id.et_title)
        descriptionEditText = view.findViewById(R.id.et_description)
        linkEditText = view.findViewById(R.id.et_link)
        submitButton = view.findViewById(R.id.btn_submit)
        closeButton = view.findViewById(R.id.btn_close_modal)

        // 닫기 버튼 클릭 이벤트 설정
        closeButton.setOnClickListener {
            dismiss()  // 대화상자 닫기
        }

        // 제출 버튼 클릭 이벤트 설정
        submitButton.setOnClickListener {
            submitReport()  // 제보 내용 파이어베이스에 전송
        }

        // 입력 필드 변경 감지하여 제출 버튼 상태 관리
        setupTextChangeListeners()

        return view
    }

    /**
     * 대화상자가 시작될 때 호출되는 함수
     * 대화상자의 크기를 설정
     */
    override fun onStart() {
        super.onStart()

        // 대화상자 크기를 화면의 90%로 설정
        dialog?.window?.apply {
            val width = (resources.displayMetrics.widthPixels * 0.9).toInt()  // 화면 너비의 90%
            setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
        }
    }

    /**
     * 입력 필드의 텍스트 변경을 감지하는 리스너를 설정하는 함수
     * 모든 필드가 채워져야 제출 버튼이 활성화
     */
    private fun setupTextChangeListeners() {
        // 텍스트 변경 감지 리스너 생성
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                // 텍스트가 변경될 때마다 제출 버튼 상태 업데이트
                updateSubmitButtonState()
            }
        }

        // 각 입력 필드에 텍스트 변경 리스너 적용
        titleEditText.addTextChangedListener(textWatcher)
        descriptionEditText.addTextChangedListener(textWatcher)
        linkEditText.addTextChangedListener(textWatcher)

        // 초기 버튼 상태 설정
        updateSubmitButtonState()
    }

    /**
     * 제출 버튼의 활성화/비활성화 상태를 업데이트하는 함수
     * 모든 필드가 채워져 있어야 버튼이 활성화
     */
    private fun updateSubmitButtonState() {
        // 각 필드가 비어있는지 확인
        val titleNotEmpty = titleEditText.text.isNotEmpty()
        val descriptionNotEmpty = descriptionEditText.text.isNotEmpty()
        val linkNotEmpty = linkEditText.text.isNotEmpty()

        // 모든 필드가 채워져 있을 때만 버튼 활성화
        submitButton.isEnabled = titleNotEmpty && descriptionNotEmpty && linkNotEmpty

        // 버튼 투명도 조정 (활성화 시 1.0, 비활성화 시 0.5)
        submitButton.alpha = if (submitButton.isEnabled) 1.0f else 0.5f
    }

    /**
     * 제보 내용을 파이어베이스에 저장하는 함수
     */
    private fun submitReport() {
        // 입력 데이터 가져오기 및 공백 제거
        val title = titleEditText.text.toString().trim()
        val description = descriptionEditText.text.toString().trim()
        val link = linkEditText.text.toString().trim()

        // 입력 유효성 검사 (추가 보안 체크)
        if (title.isEmpty() || description.isEmpty() || link.isEmpty()) {
            Toast.makeText(context, "모든 필드를 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        // 제출 버튼 비활성화 (중복 제출 방지)
        submitButton.isEnabled = false
        submitButton.alpha = 0.5f

        // 파이어스토어에 저장할 데이터 구조 생성
        val reportData = hashMapOf(
            "title" to title,           // 제목
            "description" to description, // 설명
            "link" to link,             // 링크
            "timestamp" to Date(),      // 제출 시간
            "status" to "pending"       // 상태 (검토 대기중)
        )

        // 파이어스토어의 'requests' 컬렉션에 데이터 저장
        db?.collection("requests")
            ?.add(reportData)
            ?.addOnSuccessListener { documentReference ->  // 저장 성공 시
                Log.d(TAG, "제보 저장 성공: ${documentReference.id}")
                Toast.makeText(context, "제보가 성공적으로 접수되었습니다.", Toast.LENGTH_SHORT).show()
                dismiss()  // 대화상자 닫기
            }
            ?.addOnFailureListener { e ->  // 저장 실패 시
                Log.e(TAG, "제보 저장 실패: ${e.message}")
                Toast.makeText(context, "제보 접수 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()

                // 오류 발생 시 버튼 다시 활성화
                submitButton.isEnabled = true
                submitButton.alpha = 1.0f
            }
    }

    /**
     * Companion Object - 정적 메서드들을 포함
     * 다른 클래스에서 이 DialogFragment의 인스턴스를 생성할 때 사용
     */
    companion object {
        /**
         * 새로운 ReportMemeDialogFragment 인스턴스를 생성
         * @return: 새로운 DialogFragment 인스턴스
         */
        fun newInstance(): ReportMemeDialogFragment {
            return ReportMemeDialogFragment()
        }
    }
}