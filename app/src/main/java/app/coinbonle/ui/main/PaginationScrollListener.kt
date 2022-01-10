package app.coinbonle.ui.main

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class PaginationScrollListener(
    private val layoutManager: LinearLayoutManager
) : RecyclerView.OnScrollListener() {

    var isLoading: Boolean = false

    protected abstract fun loadMoreItems()

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
        val hasScrolledEnough = visibleItemCount + firstVisibleItemPosition >= totalItemCount
        if (!isLoading && hasScrolledEnough && firstVisibleItemPosition >= 0) {
            loadMoreItems()
        }
    }
}
