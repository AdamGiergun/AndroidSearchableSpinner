package tech.hibk.searchablespinner

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import tech.hibk.searchablespinner.databinding.ActivitySampleBinding
import tech.hibk.searchablespinnerlibrary.SearchableDialog
import tech.hibk.searchablespinnerlibrary.SearchableItem

class SampleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ActivitySampleBinding.inflate(layoutInflater).run {
            setContentView(root)

            val items = List(100) { i ->
                SearchableItem(i.toLong(), "Test-$i")
            }

            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    spinner.selectedItem?.title?.let { title ->
                        Toast.makeText(this@SampleActivity, title, Toast.LENGTH_SHORT).show()
                    }
                }
            }

            spinner2.nothingSelectedText = "nothing selected"
            spinner2.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {}

                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        spinner2.selectedItem?.title?.let { title ->
                            Toast.makeText(this@SampleActivity, title, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            spinner2.items = items

            spinner2.setSelection(2)

            showDialog.setOnClickListener {
                SearchableDialog(this@SampleActivity,
                    items,
                    "Day Night Dialog",
                    { item, _ ->
                        Toast.makeText(this@SampleActivity, item.title, Toast.LENGTH_SHORT).show()
                    }).show()
            }

            showLightDialog.setOnClickListener {
                SearchableDialog(
                    this@SampleActivity,
                    items,
                    "Light Dialog",
                    { item, _ ->
                        Toast.makeText(this@SampleActivity, item.title, Toast.LENGTH_SHORT).show()
                    },
                    cancelButtonColor = ContextCompat.getColor(
                        this@SampleActivity,
                        R.color.colorPrimary
                    ),
                    onlyLightTheme = true
                ).show()
            }


            darkMode.setOnClickListener {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }

            lightMode.setOnClickListener {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }

            moreInfo.setOnClickListener {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://github.com/AdamGiergun/AndroidSearchableSpinner")
                    )
                )
            }
        }
    }
}