package com.vesna.roundup.presentation.roundup

import androidx.lifecycle.ViewModel
import com.vesna.roundup.domain.model.Period
import com.vesna.roundup.domain.usecase.CalculateRoundUp
import com.vesna.roundup.domain.usecase.GetOrCreateSavingsGoal
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

class RoundUpViewModel(
    private val calculateRoundUp: CalculateRoundUp,
    private val getSavingsGoal: GetOrCreateSavingsGoal
) : ViewModel() {

    private val states = BehaviorSubject.createDefault(RoundUpState(roundUp = null))
//    private val events = PublishSubject.create<WeekSelectionEvent>()

    private var calculateRoundUpDisposable: Disposable? = null
    private val disposables = CompositeDisposable()

    fun onPeriodSelected(period: Period) {
        calculateRoundUpDisposable?.disposeIfNotDisposed()
        calculateRoundUpDisposable = calculateRoundUp.execute(period)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ states.onNext(RoundUpState(roundUp = it)) }, { e ->  })
    }

    fun onAddToSavingGoalClicked() {
//        disposables.add(
//            // todo
//        )
    }

    override fun onCleared() {
        super.onCleared()
        calculateRoundUpDisposable?.disposeIfNotDisposed()
        disposables.clear()
    }

    fun states(): Observable<RoundUpState> = states
//    fun events(): Observable<WeekSelectionEvent> = events

    fun Disposable.disposeIfNotDisposed() {
        if (!isDisposed) dispose()
    }
}