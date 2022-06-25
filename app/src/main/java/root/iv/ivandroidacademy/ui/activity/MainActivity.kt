package root.iv.ivandroidacademy.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import root.iv.ivandroidacademy.R
import root.iv.ivandroidacademy.ui.fragment.MoviesListFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.mainFrame, MoviesListFragment())
                .commit()
        }
    }
}