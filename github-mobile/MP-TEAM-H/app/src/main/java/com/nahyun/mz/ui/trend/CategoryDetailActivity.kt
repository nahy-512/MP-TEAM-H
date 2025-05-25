package com.nahyun.mz.ui.trend

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.nahyun.mz.databinding.ActivityCategoryDetailBinding

class CategoryDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCategoryDetailBinding
    private val db = Firebase.firestore
    private val itemList = mutableListOf<String>()
    private lateinit var adapter: SimpleListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val category = intent.getStringExtra("categoryName") ?: return
        val gender = intent.getStringExtra("gender") ?: "female"

        adapter = SimpleListAdapter(itemList)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        // 🔽 april_users에서 해당 성별 + 카테고리 필터링
        db.collection("april_users")
            .whereEqualTo("gender", gender.lowercase())
            .whereEqualTo("category", category)
            .get()
            .addOnSuccessListener { result ->
                val brandItemCountMap = mutableMapOf<String, Int>()

                for (doc in result) {
                    val brand = doc.getString("brand") ?: continue
                    val item = doc.getString("item") ?: continue
                    val key = "$brand - $item"
                    brandItemCountMap[key] = brandItemCountMap.getOrDefault(key, 0) + 1
                }

                val sorted = brandItemCountMap.entries
                    .sortedByDescending { it.value }
                    .take(5)

                itemList.clear()
                itemList.addAll(sorted.map { "${it.key} (${it.value}회)" })
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Log.e("CategoryDetail", "Firestore Error: ${it.message}")
            }
    }
}
