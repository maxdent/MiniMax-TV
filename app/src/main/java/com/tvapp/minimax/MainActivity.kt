package com.tvapp.minimax

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.FragmentActivity
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.*
import androidx.lifecycle.lifecycleScope

class MainActivity : FragmentActivity() {

    private lateinit var browseFragment: BrowseSupportFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        browseFragment = BrowseSupportFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, browseFragment)
            .commit()

        setupBrowseFragment()
    }

    private fun setupBrowseFragment() {
        browseFragment.headersState = BrowseSupportFragment.HEADERS_ENABLED
        browseFragment.isTitle_transparent = true
        browseFragment.backgroundDrawable = getDrawable(R.drawable.app_background)

        browseFragment.onItemViewClickedListener = ItemViewClickedListener()
        browseFragment.onItemViewSelectedListener = ItemViewSelectedListener()

        // 加载内容
        lifecycleScope.launch {
            loadContent()
        }
    }

    private suspend fun loadContent() {
        val rowsAdapter = ArrayObjectAdapter(ListRowPresenter())
        val cardPresenter = CardPresenter()

        // 主要内容行
        val mainAdapter = ArrayObjectAdapter(cardPresenter)
        mainAdapter.add(
            Movie("首页", "访问网站首页", R.drawable.ic_home, "https://www.5m9m1o8e7e9.shop/")
        )
        mainAdapter.add(
            Movie("热门视频", "热门视频内容", R.drawable.ic_video_library, "https://www.5m9m1o8e7e9.shop/hot")
        )
        mainAdapter.add(
            Movie("最新更新", "最新更新内容", R.drawable.ic_update, "https://www.5m9m1o8e7e9.shop/latest")
        )

        rowsAdapter.add(ListRow(HeaderItem("主页"), mainAdapter))

        // 分类行
        val categoriesAdapter = ArrayObjectAdapter(cardPresenter)
        categoriesAdapter.add(
            Movie("分类1", "第一类内容", R.drawable.ic_category, "https://www.5m9m1o8e7e9.shop/category/1")
        )
        categoriesAdapter.add(
            Movie("分类2", "第二类内容", R.drawable.ic_category, "https://www.5m9m1o8e7e9.shop/category/2")
        )
        categoriesAdapter.add(
            Movie("分类3", "第三类内容", R.drawable.ic_category, "https://www.5m9m1o8e7e9.shop/category/3")
        )

        rowsAdapter.add(ListRow(HeaderItem("分类"), categoriesAdapter))

        browseFragment.adapter = rowsAdapter
    }

    private inner class ItemViewClickedListener : OnItemViewClickedListener {
        override fun onItemClicked(
            itemViewHolder: Presenter.ViewHolder?,
            item: Any?,
            rowViewHolder: RowPresenter.ViewHolder?,
            row: Row?
        ) {
            if (item is Movie) {
                val intent = Intent(this@MainActivity, WebViewActivity::class.java)
                intent.putExtra("URL", item.url)
                intent.putExtra("TITLE", item.title)
                startActivity(intent)
            }
        }
    }

    private inner class ItemViewSelectedListener : OnItemViewSelectedListener {
        override fun onItemSelected(
            itemViewHolder: Presenter.ViewHolder?,
            item: Any?,
            rowViewHolder: RowPresenter.ViewHolder?,
            row: Row?
        ) {
            // 可选：处理项目选中事件
        }
    }
}