package com.fueled.recyclerviewbindings

import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.fueled.recyclerviewbindings.databinding.BindingComponent

import com.fueled.recyclerviewbindings.adapter.RVAdapter
import com.fueled.recyclerviewbindings.databinding.ActivityMainBinding
import com.fueled.recyclerviewbindings.model.User
import com.fueled.recyclerviewbindings.mvp.MainContract
import com.fueled.recyclerviewbindings.mvp.MainPresenterImpl
import com.fueled.recyclerviewbindings.util.toast

import java.util.ArrayList

class MainActivity : AppCompatActivity(), MainContract.View {

    private lateinit var binding: ActivityMainBinding
    private lateinit var presenter: MainContract.Presenter

    private lateinit var adapter: RVAdapter
    private val list = ArrayList<User?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main, BindingComponent())
        setRecyclerView()

        presenter = MainPresenterImpl(this)
        binding.presenter = presenter
    }

    /**
     * set RVAdapter to RecyclerView
     */
    private fun setRecyclerView() {
        adapter = RVAdapter(list) { toast(it?.name) }
        binding.rv.adapter = adapter
    }

    /**
     * show progress loader at bottom of list
     */
    override fun showProgress(): Boolean {
        binding.rv.post { adapter.add(null) }
        return true
    }

    /**
     * remove progress loader at bottom of list
     * if list is refreshing, clear the list
     */
    override fun hideProgress(): Boolean {
        if (list.size > 0 && null == list[list.size - 1]) {
            adapter.remove(list.size - 1)
        }
        if (binding.srl.isRefreshing) {
            list.clear()
            binding.srl.isRefreshing = false
        }
        return false
    }

    /**
     * show items and add them to list
     */
    override fun showItems(items: List<User>) {
        adapter.addAll(items)
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
