package com.tvapp.minimax

import android.os.Bundle
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment

class SearchFragment : Fragment() {

    private lateinit var searchEditText: androidx.leanback.widget.SearchEditText
    private lateinit var resultsList: ListView
    private var adapter: ArrayAdapter<String>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        searchEditText = view.findViewById(R.id.search_edit_text)
        resultsList = view.findViewById(R.id.results_list)

        setupSearch()
        return view
    }

    private fun setupSearch() {
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString()
                if (query.isNotEmpty()) {
                    performSearch(query)
                }
            }

            override fun afterTextChanged(s: android.text.Editable?) {}
        })

        adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1)
        resultsList.adapter = adapter
    }

    private fun performSearch(query: String) {
        // 模拟搜索结果
        val results = mutableListOf<String>()
        results.add("搜索结果1: $query 相关内容")
        results.add("搜索结果2: $query 热门视频")
        results.add("搜索结果3: $query 最新更新")
        results.add("搜索结果4: $query 推荐内容")

        adapter?.clear()
        results.forEach { adapter?.add(it) }
        adapter?.notifyDataSetChanged()
    }

    companion object {
        fun newInstance(): SearchFragment {
            return SearchFragment()
        }
    }
}
