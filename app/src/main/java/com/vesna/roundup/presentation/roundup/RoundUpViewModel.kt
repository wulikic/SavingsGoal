package com.vesna.roundup.presentation.roundup

import androidx.lifecycle.ViewModel
import com.vesna.roundup.domain.model.Period
import com.vesna.roundup.domain.usecase.AddToSavingsGoal
import com.vesna.roundup.domain.usecase.CalculateRoundUp
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import java.util.*

class RoundUpViewModel(
    private val calculateRoundUp: CalculateRoundUp,
    private val addToSavingsGoal: AddToSavingsGoal
) : ViewModel() {

    private val states = BehaviorSubject.createDefault(RoundUpState(roundUp = null, transferInProgress = false))
    private val events = PublishSubject.create<Event>()

    private var calculateRoundUpDisposable: Disposable? = null
    private val disposables = CompositeDisposable()

    fun onPeriodSelected(period: Period) {
        calculateRoundUpDisposable?.disposeIfNotDisposed()
        calculateRoundUpDisposable = calculateRoundUp.execute(period)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ states.onNext(currentState().copy(roundUp = it)) }, { e -> events.onNext(FindingRoundUpFailed) })
    }

    fun onAddToSavingGoalClicked() {
        states.value?.roundUp?.let {
            disposables.add(
                addToSavingsGoal.execute(it, UUID.randomUUID())
                    .doOnSubscribe { states.onNext(currentState().copy(transferInProgress = true)) }
                    .doAfterTerminate { states.onNext(currentState().copy(transferInProgress = false)) }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ events.onNext(MoneyTransferSucceeded) }, { e -> events.onNext(MoneyTransferFailed) })
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        calculateRoundUpDisposable?.disposeIfNotDisposed()
        disposables.clear()
    }

    fun states(): Observable<RoundUpState> = states
    fun events(): Observable<Event> = events

    private fun Disposable.disposeIfNotDisposed() {
        if (!isDisposed) dispose()
    }

    private fun currentState(): RoundUpState {
        return states.value!!
    }
}