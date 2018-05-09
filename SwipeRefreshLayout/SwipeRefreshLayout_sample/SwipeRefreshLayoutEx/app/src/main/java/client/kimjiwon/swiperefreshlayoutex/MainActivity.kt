package client.kimjiwon.swiperefreshlayoutex

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import client.kimjiwon.swiperefreshlayoutex.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding
    private var mRecyclerAdapter: RecyclerAdapter? = null
    private var userDatas: ArrayList<MainModel> = ArrayList()
    private var index: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this@MainActivity, R.layout.activity_main)
        mBinding.swipeRefreshLayout.setOnRefreshListener(onClickRefresh)


        var items: ArrayList<MainModel> = ArrayList()
        for (i: Int in 0..2) {
            items.add(MainModel("김지원", index++, R.drawable.abc_ic_star_black_48dp))
        }
        userDatas = items
        mRecyclerAdapter = RecyclerAdapter(items)
        mBinding.recyclerView.adapter = mRecyclerAdapter
        mBinding.recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
        mBinding.executePendingBindings()
    }

    private val onClickRefresh = SwipeRefreshLayout.OnRefreshListener {
        when (index % 4) {
            0 -> mBinding.swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_red_light)
            1 -> mBinding.swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright)
            2 -> mBinding.swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_green_light)
            3 -> mBinding.swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_red_light, android.R.color.holo_blue_bright, android.R.color.holo_green_light)
        }
        
        var items: ArrayList<MainModel> = ArrayList()
//        for (i: Int in 0..3) {
        items.add(MainModel("김지원", index++, R.mipmap.ic_launcher_round))
//        }
        mRecyclerAdapter?.addItems(items)
        Handler().postDelayed(Runnable { mBinding.swipeRefreshLayout.isRefreshing = false }, 2500)
    }

}
