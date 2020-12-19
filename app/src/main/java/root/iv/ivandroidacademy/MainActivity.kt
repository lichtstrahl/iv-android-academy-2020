package root.iv.ivandroidacademy

import android.content.Intent
import android.os.Bundle
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<RelativeLayout>(R.id.mainBackground)
            .setOnClickListener {
                val intent = Intent(this, MovieDetailsActivity::class.java)
                startActivity(intent)
            }
    }
}