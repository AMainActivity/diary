package ru.ama.diary.presentation

import android.os.Bundle
import android.text.util.Linkify
import android.view.*
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.text.HtmlCompat
import ru.ama.diary.R
import ru.ama.diary.databinding.ActivityMainBinding
import ru.ama.diary.databinding.ItemMenuInfoBinding


class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            launchFirstScreen()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main_activity, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.menu_about -> {
                showPopupText(findViewById(R.id.menu_add_job), getString(R.string.ma_menu_about))
                true
            }

            R.id.menu_add_job -> {
                AddJobFragment.newInstance().show(
                    supportFragmentManager, AddJobFragment.NAME
                )
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showPopupText(anchor: View, txt: String) {
        val popupWindow = PopupWindow(application)
        popupWindow.animationStyle = R.style.dialog_animation
        val binding2 = ItemMenuInfoBinding.inflate(layoutInflater)
        popupWindow.setBackgroundDrawable(
            ResourcesCompat.getDrawable(
                getResources(), R.drawable.nulldr, null
            )
        )
        binding2.tvMenuHelp.linksClickable = true
        binding2.tvMenuHelp.autoLinkMask = Linkify.WEB_URLS
        binding2.tvMenuHelp.text = HtmlCompat.fromHtml(txt, HtmlCompat.FROM_HTML_MODE_LEGACY)
        popupWindow.isFocusable = true
        popupWindow.width = WindowManager.LayoutParams.WRAP_CONTENT
        popupWindow.height = WindowManager.LayoutParams.WRAP_CONTENT
        popupWindow.contentView = binding2.root
        popupWindow.showAsDropDown(anchor)

    }

    private fun launchFirstScreen() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, MainFragment.newInstance()).commit()
    }
}
