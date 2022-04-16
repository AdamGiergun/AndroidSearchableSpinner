package tech.hibk.searchablespinnerlibrary

import android.app.AlertDialog
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import tech.hibk.searchablespinnerlibrary.databinding.SearchableDialogBinding
import java.util.*

private const val TAG = "SearchableDialog"

class SearchableDialog(
    context: Context,
    val items: List<SearchableItem>,
    val titleString: String,
    val listener: (item: SearchableItem, position: Int) -> Unit,
    val cancelButtonText: String = context.getString(android.R.string.cancel),
    val cancelButtonColor: Int? = null,
    val onlyLightTheme: Boolean = false
) {
    private lateinit var alertDialog: AlertDialog
    var position: Int = 0
    var selected: SearchableItem? = null

    lateinit var searchListAdapter: SearchableListAdapter

    /***
     *
     * show the seachable dialog
     */
    fun show(activityContext: Context) {

        val context = if (onlyLightTheme) {
            activityContext.setTheme(R.style.LightTheme)
            ContextThemeWrapper(activityContext, R.style.LightTheme)
        } else {
            activityContext.setTheme(R.style.DayNightTheme)
            ContextThemeWrapper(activityContext, R.style.DayNightTheme)
        }

        AlertDialog.Builder(context).apply {
            SearchableDialogBinding.inflate(LayoutInflater.from(context)).run {

                searchListAdapter = SearchableListAdapter(context, items)

                rippleViewClose.apply {
                    if (cancelButtonText.isNotBlank()) {
                        text = cancelButtonText
                        cancelButtonColor?.let { setTextColor(it) }
                        setOnClickListener { alertDialog.dismiss() }
                    } else {
                        visibility = View.GONE
                    }
                }

                spinnerTitle.apply {
                    if (titleString.isNotBlank()) {
                        text = titleString
                    } else {
                        visibility = View.GONE
                    }
                }

                listView.apply {
                    adapter = searchListAdapter
                    onItemClickListener = AdapterView.OnItemClickListener { _, view, _, _ ->
                        val text = view.findViewById<TextView>(R.id.t1).text.toString()
                        for (j in items.indices) {
                            if (text.equals(items[j].title, ignoreCase = true)) {
                                position = j
                                selected = items[position]
                            }
                        }
                        try {
                            listener(selected!!, position)
                        } catch (e: Exception) {
                            Log.e(TAG, e.message ?: e.toString())
                        }

                        alertDialog.dismiss()
                    }
                }

                searchBox.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        charSequence: CharSequence,
                        i: Int,
                        i1: Int,
                        i2: Int
                    ) {
                    }

                    override fun onTextChanged(
                        charSequence: CharSequence,
                        i: Int,
                        i1: Int,
                        i2: Int
                    ) {
                    }

                    override fun afterTextChanged(editable: Editable) {
                        val filteredValues = items.filter { item ->
                            item.title.lowercaseAndTrim()
                                .contains(searchBox.text.toString().lowercaseAndTrim())
                        }
                        searchListAdapter = SearchableListAdapter(context, filteredValues)
                        listView.adapter = searchListAdapter
                    }
                })

                setView(root)
            }
        }.create().run {
            alertDialog = this
            setCancelable(true)
            show()
        }
    }

    private fun String.lowercaseAndTrim(): String =
        lowercase(Locale.getDefault()).trim { it <= ' ' }
}