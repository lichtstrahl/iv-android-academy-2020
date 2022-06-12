package root.iv.ivandroidacademy.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import root.iv.ivandroidacademy.R
import root.iv.ivandroidacademy.fragment.MoviesListFragment

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