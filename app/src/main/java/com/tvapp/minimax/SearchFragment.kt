package com.tvapp.minimax

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.leanback.widget.SearchEditText
import androidx.leanback.widget.SearchResultProvider
import androidx.leanback.widget.SpeechOrbView

class SearchFragment : Fragment(), SearchResultProvider {

    private lateinit var searchEditText: SearchEditText
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
        searchEditText.setOnSearchBoxListener(object : SearchEditText.SearchBoxListener {
            override fun onSearchQuerySubmit(query: String) {
                performSearch(query)
            }

            override fun onSearchQueryChange(query: String) {
                if (query.isNotEmpty()) {
                    performSearch(query)
                }
            }
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

    override fun getResultsAdapter(): androidx.recyclerview.widget.RecyclerView.Adapter<*>? {
        return adapter as? androidx.recyclerview.widget.RecyclerView.Adapter<*>
    }

    override fun onQueryTextChange(newQuery: String): Boolean {
        performSearch(newQuery)
        return true
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        performSearch(query)
        return true
    }

    companion object {
        fun newInstance(): SearchFragment {
            return SearchFragment()
        }
    }
}