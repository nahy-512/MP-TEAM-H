package com.nahyun.mz.ui.trend

import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.nahyun.mz.databinding.ActivityCategoryDetailBinding
import com.nahyun.mz.R



class CategoryDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCategoryDetailBinding
    private val db = Firebase.firestore
    private val itemList = mutableListOf<ItemInfo>()
    private lateinit var adapter: SimpleListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        findViewById<ImageButton>(R.id.btn_back).setOnClickListener {
            finish()
        }

        val category = intent.getStringExtra("categoryName") ?: return
        val gender = intent.getStringExtra("gender") ?: "female"

        adapter = SimpleListAdapter(itemList)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        val marchMap = mutableMapOf<String, Int>()
        val aprilMap = mutableMapOf<String, Int>()

        val marchTask = db.collection("march_users")
            .whereEqualTo("gender", gender.lowercase())
            .whereEqualTo("category", category)
            .get()

        val aprilTask = db.collection("april_users")
            .whereEqualTo("gender", gender.lowercase())
            .whereEqualTo("category", category)
            .get()

        marchTask.continueWithTask { marchSnapshot ->
            for (doc in marchSnapshot.result) {
                val brand = doc.getString("brand") ?: continue
                val item = doc.getString("item") ?: continue
                val key = "$brand - $item"
                marchMap[key] = marchMap.getOrDefault(key, 0) + 1
            }
            aprilTask
        }.addOnSuccessListener { aprilSnapshot ->
            for (doc in aprilSnapshot) {
                val brand = doc.getString("brand") ?: continue
                val item = doc.getString("item") ?: continue
                val key = "$brand - $item"
                aprilMap[key] = aprilMap.getOrDefault(key, 0) + 1
            }

            val allKeys = (marchMap.keys + aprilMap.keys).toSet()
            val aprilTotal = aprilMap.values.sum()

            val resultList = allKeys.map { key ->
                val march = marchMap.getOrDefault(key, 0)
                val april = aprilMap.getOrDefault(key, 0)
                val diff = april - march

                val changeText = when {
                    diff > 0 -> "▲ ${diff}개"
                    diff < 0 -> "▼ ${-diff}개"
                    else -> ""
                }

                val color = when {
                    diff > 0 -> "#FF3B30"
                    diff < 0 -> "#007AFF"
                    else -> "#34C759"
                }

                Triple(key, changeText, color)
            }.sortedByDescending { (key, _, _) -> aprilMap.getOrDefault(key, 0) }
                .take(5)
                .mapIndexed { index, (key, change, color) ->
                    val march = marchMap.getOrDefault(key, 0)
                    val april = aprilMap.getOrDefault(key, 0)
                    val total = march + april

                    val totalText = "총 소비: ${total}회 (3월 ${march}회 → 4월 ${april}회)"
                    val ratioPercent = if (aprilTotal == 0) 0 else (april * 100) / aprilTotal
                    val ratioText = "카테고리 내 비중: ${ratioPercent}%"

                    val rank = "${index + 1}. $key"
                    val html = "<font color='$color'>$change</font>"

                    ItemInfo(
                        name = rank,
                        change = android.text.Html.fromHtml(html).toString(),
                        total = totalText,
                        ratio = ratioText,
                        query = key
                    )
                }

            itemList.clear()
            itemList.addAll(resultList)
            adapter.notifyDataSetChanged()
        }.addOnFailureListener {
            Log.e("CategoryDetail", "Firestore Error: ${it.message}")
        }
    }
}
