package ss.wallpad.ui.search

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import ss.wallpad.R
import ss.wallpad.ui.MainActivity
import ss.wallpad.util.hideKeyboard

class SearchFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.search_fragment, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_menu, menu)
        val searchMenuItem = menu.findItem(R.id.action_search)
        val activity = requireActivity() as MainActivity
        val searchManager = activity.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = SearchView(activity.supportActionBar?.themedContext)
        searchView.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
        searchMenuItem.actionView = searchView
        searchView.isIconified = false
        searchView.setOnCloseListener {
            activity.hideKeyboard(view)
            activity.onBackPressed()
            true
        }
    }
}
