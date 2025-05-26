package com.nahyun.mz.ui.trend

<<<<<<< HEAD
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
=======
>>>>>>> origin/develop
import com.nahyun.mz.R
import com.nahyun.mz.databinding.FragmentTrendBinding
import com.nahyun.mz.ui.base.BaseFragment

class TrendFragment : BaseFragment<FragmentTrendBinding>(R.layout.fragment_trend) {

<<<<<<< HEAD
    private var selectedGender = "female"

    override fun setup() {
        loadTopCategories(selectedGender)
        setGenderButtonAlpha()

        binding.btnFemale.setOnClickListener {
            selectedGender = "female"
            setGenderButtonAlpha()
            loadTopCategories("female")
        }

        binding.btnMale.setOnClickListener {
            selectedGender = "male"
            setGenderButtonAlpha()
            loadTopCategories("male")
        }
    }

    private fun setGenderButtonAlpha() {
        binding.btnFemale.alpha = if (selectedGender == "female") 1.0f else 0.3f
        binding.btnMale.alpha = if (selectedGender == "male") 1.0f else 0.3f
    }

    private fun loadTopCategories(gender: String) {
        val db = Firebase.firestore
        val marchMap = mutableMapOf<String, Int>()
        val aprilMap = mutableMapOf<String, Int>()

        db.collection("march_users").whereEqualTo("gender", gender).get()
            .continueWithTask { marchSnapshot ->
                for (doc in marchSnapshot.result) {
                    val category = doc.getString("category") ?: continue
                    marchMap[category] = marchMap.getOrDefault(category, 0) + 1
                }
                db.collection("april_users").whereEqualTo("gender", gender).get()
            }
            .addOnSuccessListener { aprilSnapshot ->
                for (doc in aprilSnapshot) {
                    val category = doc.getString("category") ?: continue
                    aprilMap[category] = aprilMap.getOrDefault(category, 0) + 1
                }

                val topCategories = aprilMap.entries
                    .sortedByDescending { it.value }
                    .take(5)

                val total = topCategories.sumOf { it.value }

                binding.categoryContainer.removeAllViews()

                for ((name, aprilCount) in topCategories) {
                    val percent = (aprilCount * 100) / total
                    val marchCount = marchMap.getOrDefault(name, 0)

                    val rate = if (marchCount == 0) 100 else ((aprilCount - (marchCount-10) * 50) / marchCount)

                    val rateText = if (rate >= 100) {
                        "" // ✅ 100% 이상은 표시 안 함
                    } else {
                        when {
                            rate > 0 -> "▲ $rate%"
                            rate < 0 -> "▼ ${-rate}%"
                            else -> ""
                        }
                    }
                    val rateColor = when {
                        rate > 0 -> Color.parseColor("#FF3B30") // 빨강
                        rate < 0 -> Color.parseColor("#007AFF") // 파랑
                        else -> Color.parseColor("#34C759")     // 초록
                    }

                    val view = LayoutInflater.from(context).inflate(R.layout.item_category, null)

                    val textContainer = view.findViewById<LinearLayout>(R.id.textContainer)
                    val nameView = view.findViewById<TextView>(R.id.categoryName)
                    val percentView = view.findViewById<TextView>(R.id.categoryPercent)
                    val bar = view.findViewById<ProgressBar>(R.id.categoryBar)
                    val icon = view.findViewById<ImageView>(R.id.categoryIcon)

                    nameView.text = name
                    percentView.text = "$percent%"
                    bar.progress = percent
                    icon.setImageResource(getIconResId(name))

                    // 증감률 텍스트 추가
                    val diffView = TextView(requireContext())
                    diffView.text = rateText
                    diffView.textSize = 12f
                    diffView.setTextColor(rateColor)
                    textContainer.addView(diffView)

                    // 클릭 이벤트
                    view.setOnClickListener {
                        val intent = Intent(requireContext(), CategoryDetailActivity::class.java)
                        intent.putExtra("categoryName", name)
                        intent.putExtra("gender", gender)
                        startActivity(intent)
                    }

                    binding.categoryContainer.addView(view)
                }
            }
            .addOnFailureListener {
                Log.e("TrendFragment", "Firebase Error: ${it.message}")
            }
    }

    private fun getIconResId(name: String): Int {
        return when (name) {
            "뷰티" -> R.drawable.ic_beauty
            "패션" -> R.drawable.ic_fashion
            "디지털" -> R.drawable.ic_digital
            "건강" -> R.drawable.ic_health
            "취미/문화" -> R.drawable.ic_entertainment
            else -> R.drawable.ic_placeholder
        }
    }
}
=======
    override fun setup() {
        // 뷰 초기화, ClickListener 정의 등 작업 진행
    }
}
>>>>>>> origin/develop
