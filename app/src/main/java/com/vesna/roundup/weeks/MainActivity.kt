package com.vesna.roundup.weeks

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.vesna.roundup.App
import com.vesna.roundup.R
import io.reactivex.disposables.CompositeDisposable
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: MainViewModelFactory
    private lateinit var viewModel: MainViewModel
    private val disposables = CompositeDisposable()

    private lateinit var spinner: Spinner
    private lateinit var adapter: ArrayAdapter<UiPeriod>

    private val dateFormatter: DateTimeFormatter = DateTimeFormat.mediumDate()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (applicationContext as App).appComponent.inject(this)

        setContentView(R.layout.activity_main)

        spinner = findViewById(R.id.spinner)
        adapter = ArrayAdapter(this, R.layout.spinner_item)
        spinner.adapter = adapter

        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
    }

    override fun onStart() {
        super.onStart()
        disposables.add(
            viewModel.states()
                .subscribe { updateUi(it) }
        )

        disposables.add(
            viewModel.events()
                .subscribe { handleEvent(it) }
        )
    }

    private fun updateUi(state: State) {
        adapter.clear()
        adapter.addAll(state.weeks.map { UiPeriod(it, dateFormatter) })
        adapter.notifyDataSetChanged()
    }

    private fun handleEvent(event: Event) {

    }

    override fun onStop() {
        super.onStop()
        disposables.clear()
    }
}
