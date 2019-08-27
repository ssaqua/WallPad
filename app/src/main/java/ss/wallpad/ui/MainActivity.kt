package ss.wallpad.ui

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import dagger.android.AndroidInjection
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.main_activity.*
import ss.wallpad.Injectable
import ss.wallpad.NavGraphDirections
import ss.wallpad.R
import ss.wallpad.util.hideKeyboard
import javax.inject.Inject

class MainActivity : AppCompatActivity(), HasSupportFragmentInjector {
    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    private val navController by lazy { findNavController(R.id.nav_host_fragment) }
    private val appBarConfiguration = AppBarConfiguration.Builder(
        R.id.collectionFragment,
        R.id.savedFragment
    ).build()

    override fun onCreate(savedInstanceState: Bundle?) {
        setupInjection()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        setSupportActionBar(toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)
        bottom_navigation.setupWithNavController(navController)
        bottom_navigation.setOnNavigationItemReselectedListener { menuItem ->
            if (menuItem.itemId != navController.currentDestination?.id) {
                navController.navigate(
                    menuItem.itemId,
                    null,
                    NavOptions.Builder()
                        .setPopUpTo(menuItem.itemId, false)
                        .setLaunchSingleTop(true)
                        .build()
                )
            }
        }
    }

    private fun setupInjection() {
        AndroidInjection.inject(this)
        supportFragmentManager.registerFragmentLifecycleCallbacks(object : FragmentManager.FragmentLifecycleCallbacks() {
            override fun onFragmentCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
                if (f is Injectable) AndroidSupportInjection.inject(f)
            }
        }, true)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if (intent.action == Intent.ACTION_SEARCH) {
            intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                navController.navigate(NavGraphDirections.openGallery(query))
            }
        }
    }

    override fun supportFragmentInjector() = fragmentInjector

    override fun onSupportNavigateUp(): Boolean {
        hideKeyboard(window.decorView)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
