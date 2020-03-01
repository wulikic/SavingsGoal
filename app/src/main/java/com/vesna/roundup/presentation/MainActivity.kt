package com.vesna.roundup.presentation

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.vesna.roundup.App
import com.vesna.roundup.R
import com.vesna.roundup.domain.model.Period
import com.vesna.roundup.presentation.roundup.*
import com.vesna.roundup.presentation.weekselection.LoadingWeeksError
import com.vesna.roundup.presentation.weekselection.UiPeriod
import com.vesna.roundup.presentation.weekselection.WeekSelectionViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: MainViewModelFactory
    private lateinit var weekSelectionVM: WeekSelectionViewModel
    private lateinit var roundUpVM: RoundUpViewModel
    private lateinit var submitBtn: Button

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
        submitBtn = findViewById(R.id.submit)

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
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { updateWeeks(it) }
        )

        disposables.add(
            weekSelectionVM.events()
                .observeOn(AndroidSchedulers.mainThread())
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
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { updateRoundUp(it) })

        disposables.add(roundUpVM.events()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { handleEvent(it) })

        submitBtn.setOnClickListener { roundUpVM.onAddToSavingGoalClicked() }
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

    private fun handleEvent(event: Event) {
        val message = when (event) {
            is MoneyTransferSucceeded -> "Money successfully transferred"
            is MoneyTransferFailed -> "Transferring money failed"
            is FindingRoundUpFailed -> "Finding round up failed"
        }
        AlertDialog.Builder(this)
            .setNeutralButton("Ok", null)
            .setMessage(message).create().show()
    }

    private fun updateRoundUp(roundUpState: RoundUpState) {
        roundUpState.roundUp?.let {
            roundUpView.text = String.format("£ %s", it / 100.0)
        } ?: kotlin.run {
            roundUpView.text = null
        }

        submitBtn.isEnabled = !roundUpState.transferInProgress
    }

    override fun onStop() {
        super.onStop()
        disposables.clear()
    }
}
