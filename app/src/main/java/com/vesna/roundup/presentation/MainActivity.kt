package com.vesna.roundup.presentation

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.vesna.roundup.App
import com.vesna.roundup.R
import com.vesna.roundup.domain.model.Period
import com.vesna.roundup.presentation.roundup.RoundUpState
import com.vesna.roundup.presentation.roundup.RoundUpViewModel
import com.vesna.roundup.presentation.weekselection.LoadingWeeksError
import com.vesna.roundup.presentation.weekselection.UiPeriod
import com.vesna.roundup.presentation.weekselection.WeekSelectionViewModel
import io.reactivex.disposables.CompositeDisposable
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: MainViewModelFactory
    private lateinit var weekSelectionVM: WeekSelectionViewModel
    private lateinit var roundUpVM: RoundUpViewModel

    private val disposables = CompositeDisposable()

    private lateinit var spinner: Spinner
    private lateinit var adapter: ArrayAdapter<UiPeriod>
    private lateinit var roundUpView: TextView

    private val dateFormatter: DateTimeFormatter = DateTimeFormat.mediumDate()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (applicationContext as App).appComponent.inject(this)

        setContentView(R.layout.activity_main)

        spinner = findViewById(R.id.spinner)
        roundUpView = findViewById(R.id.roundup)

        adapter = ArrayAdapter(this, R.layout.spinner_item)
        spinner.adapter = adapter

        val vmProvider = ViewModelProvider(this, viewModelFactory)
        weekSelectionVM = vmProvider[WeekSelectionViewModel::class.java]
        roundUpVM = vmProvider[RoundUpViewModel::class.java]
    }

    override fun onStart() {
        super.onStart()
        disposables.add(
            weekSelectionVM.states()
                .subscribe { updateWeeks(it) }
        )

        disposables.add(
            weekSelectionVM.events()
                .subscribe { handleEvent(it) }
        )

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                adapter.getItem(position)?.period?.let { roundUpVM.onPeriodSelected(it) }
            }
        }

        disposables.add(roundUpVM.states()
            .subscribe { updateRoundUp(it) })
    }

    private fun updateWeeks(list: List<Period>) {
        adapter.clear()
        adapter.addAll(list.map {
            UiPeriod(
                it,
                dateFormatter
            )
        })
        adapter.notifyDataSetChanged()
    }

    private fun handleEvent(event: LoadingWeeksError) {
    }

    private fun updateRoundUp(roundUpState: RoundUpState) {
        roundUpView.text = String.format("£ %.00f", roundUpState.roundUp)
    }

    override fun onStop() {
        super.onStop()
        disposables.clear()
    }
}
