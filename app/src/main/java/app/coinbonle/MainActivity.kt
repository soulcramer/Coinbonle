package app.coinbonle

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import app.coinbonle.ui.main.AlbumsFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, AlbumsFragment.newInstance())
                .commitNow()
        }
    }
}