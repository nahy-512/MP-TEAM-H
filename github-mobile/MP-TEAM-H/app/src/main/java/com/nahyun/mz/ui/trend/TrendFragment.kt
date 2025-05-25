package com.nahyun.mz.ui.trend

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.nahyun.mz.R
import com.nahyun.mz.databinding.FragmentTrendBinding
import com.nahyun.mz.ui.base.BaseFragment

class TrendFragment : BaseFragment<FragmentTrendBinding>(R.layout.fragment_trend) {

    private var selectedGender = "female"

    override fun setup() {
        // 기본 불러오기
        loadTopCategories(selectedGender)

        // 성별 선택 버튼 클릭 시
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

        setGenderButtonAlpha()
    }

    private fun setGenderButtonAlpha() {
        binding.btnFemale.alpha = if (selectedGender == "female") 1.0f else 0.3f
        binding.btnMale.alpha = if (selectedGender == "male") 1.0f else 0.3f
    }

    private fun loadTopCategories(gender: String) {
        val db = Firebase.firestore
        db.collection("april_users")
            .whereEqualTo("gender", gender)
            .get()
            .addOnSuccessListener { result ->
                val categoryCount = mutableMapOf<String, Int>()

                for (doc in result) {
                    val category = doc.getString("category") ?: continue
                    categoryCount[category] = categoryCount.getOrDefault(category, 0) + 1
                }

                val topCategories = categoryCount.entries
                    .sortedByDescending { it.value }
                    .take(5)

                val total = topCategories.sumOf { it.value }

                binding.categoryContainer.removeAllViews()

                for ((name, count) in topCategories) {
                    val percent = (count * 100) / total
                    val view = LayoutInflater.from(context).inflate(R.layout.item_category, null)
                    view.findViewById<TextView>(R.id.categoryName).text = name
                    view.findViewById<TextView>(R.id.categoryPercent).text = "$percent%"
                    view.findViewById<ProgressBar>(R.id.categoryBar).progress = percent
                    view.findViewById<ImageView>(R.id.categoryIcon).setImageResource(getIconResId(name))

                    // ✅ 카테고리 클릭 시 상세 화면으로 이동
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
