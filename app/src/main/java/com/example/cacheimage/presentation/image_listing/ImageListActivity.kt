package com.example.cacheimage.presentation.image_listing

import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.cacheimage.R
import com.example.cacheimage.databinding.ActivityImageListBinding
import com.example.cacheimage.domain.model.Image
import com.example.cacheimage.presentation.image_listing.adapter.ImageListAdapter
import com.example.movieslistapp.util.GridItemDecorator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ImageListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityImageListBinding
    private lateinit var adapter: ImageListAdapter
    private val viewModel: ImageListViewModel by viewModels()
    private val images : ArrayList<Image> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.makeStretchedFullScreen()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_image_list)
        binding.lifecycleOwner = this
        initAdapter()
        observeImageList()
    }

    private fun initAdapter() {
        adapter = ImageListAdapter(images)
        binding.rvImageList.apply {
            this.adapter = this@ImageListActivity.adapter
            layoutManager = GridLayoutManager(this@ImageListActivity, 3)
            addItemDecoration(GridItemDecorator(3, 5, false))
        }
    }


    private fun observeImageList() = lifecycleScope.launch {
        viewModel.state.collectLatest {
            if(it.error!=null){
                Toast.makeText(this@ImageListActivity, "${it.error}", Toast.LENGTH_SHORT).show()
                viewModel.clearError()
            }

            images.clear()
            images.addAll(it.images)
            adapter.notifyDataSetChanged()
            showProgressBar(isShow = false)
        }
    }


    private fun Window.makeStretchedFullScreen() {
        this.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

    }

    private fun showProgressBar(isShow: Boolean){
        binding.clProgressbar.visibility = if(isShow) View.VISIBLE else View.GONE
    }
}