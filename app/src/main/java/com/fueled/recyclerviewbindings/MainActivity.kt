package com.fueled.recyclerviewbindings

import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.fueled.recyclerviewbindings.databinding.BindingComponent

import com.fueled.recyclerviewbindings.adapter.RVAdapter
import com.fueled.recyclerviewbindings.databinding.ActivityMainBinding
import com.fueled.recyclerviewbindings.entity.User
import com.fueled.recyclerviewbindings.model.MainModel
import com.fueled.recyclerviewbindings.model.UserModel
import com.fueled.recyclerviewbindings.mvp.MainContract
import com.fueled.recyclerviewbindings.mvp.MainHandler
import com.fueled.recyclerviewbindings.mvp.MainPresenterImpl
import com.fueled.recyclerviewbindings.util.Mapper
import com.fueled.recyclerviewbindings.util.toast

class MainActivity : AppCompatActivity(), MainHandler, MainContract.View {

    private lateinit var binding: ActivityMainBinding
    private lateinit var model: MainModel

    private lateinit var presenter: MainContract.Presenter

    private lateinit var adapter: RVAdapter
    private val list = arrayListOf<UserModel?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main, BindingComponent())
        setBindings()
        setRecyclerView()

        presenter = MainPresenterImpl(this)
    }

    /**
     * set bindings for model and handler
     */
    private fun setBindings() {
        model = MainModel()
        model.visibleThreshold = 7
        binding.model = model
        binding.handler = this
    }

    /**
     * set RVAdapter to RecyclerView
     */
    private fun setRecyclerView() {
        adapter = RVAdapter(list) { toast(it?.name) }
        binding.rv.adapter = adapter
    }

    /**
     * called when pulled downwards
     */
    override fun onPulledToRefresh() {
        model.resetLoadingState = true
        presenter.initialize()
    }

    /**
     * called when RecyclerView scrolled to bottom
     */
    override fun onScrolledToBottom(page: Int) {
        if (model.loading) return
        presenter.getUsers(page)
    }

    /**
     * show progress loader at bottom of list
     */
    override fun showProgress() {
        binding.rv.post { adapter.add(null) }
        model.loading = true
    }

    /**
     * remove progress loader at bottom of list
     * if list is refreshing, clear the list
     */
    override fun hideProgress() {
        if (list.size > 0 && null == list[list.size - 1]) {
            adapter.remove(list.size - 1)
        }
        if (binding.srl.isRefreshing) {
            list.clear()
            binding.srl.isRefreshing = false
        }
        model.loading = false
    }

    /**
     * show items and add them to list
     */
    override fun showItems(items: List<User>) {
        val mappedItems = arrayListOf<UserModel>()
        items.map { mappedItems.add(Mapper.mapToUserModel(it)) }
        adapter.addAll(mappedItems)
    }

    /**
     * show error message
     */
    override fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    /**
     * terminate presenter
     */
    override fun onDestroy() {
        presenter.terminate()
        super.onDestroy()
    }
}
