package com.vesna.roundup.di

import android.content.Context
import com.google.gson.Gson
import com.vesna.roundup.BuildConfig
import com.vesna.roundup.data.AccountRepoImpl
import com.vesna.roundup.data.SavingsGoalRepoImpl
import com.vesna.roundup.data.localstorage.AccountLocalStorage
import com.vesna.roundup.data.localstorage.SavingsGoalStorage
import com.vesna.roundup.data.network.Api
import com.vesna.roundup.data.network.RetrofitApi
import com.vesna.roundup.domain.repo.AccountRepo
import com.vesna.roundup.domain.repo.SavingsGoalRepo
import com.vesna.roundup.domain.usecase.*
import com.vesna.roundup.presentation.MainViewModelFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.joda.time.format.DateTimeFormatter
import org.joda.time.format.ISODateTimeFormat
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class AppModule(private val context: Context) {

    @Provides
    fun mainViewModelFactory(getWeeks: GetAllWeeksSinceAccountCreation,
                             calculateRoundUp: CalculateRoundUp,
                             addToSavingsGoal: AddToSavingsGoal) =
        MainViewModelFactory(getWeeks, calculateRoundUp, addToSavingsGoal)

    @Provides
    fun getAllWeeksSinceAccountCreation(getAccount: GetAccount, getWeeks: GetWeeks) =
        GetAllWeeksSinceAccountCreation(
            getAccount = getAccount,
            getWeeks = getWeeks
        )

    @Provides
    fun addToSavingsGoal(transferMoneyToSavingsGoal: TransferMoneyToSavingsGoal,
                         getAccount: GetAccount,
                         getOrCreateSavingsGoal: GetOrCreateSavingsGoal) =
        AddToSavingsGoal(
            transferMoneyToSavingsGoal = transferMoneyToSavingsGoal,
            getAccount = getAccount,
            getOrCreateSavingsGoal = getOrCreateSavingsGoal)

    @Provides
    fun transferMoneyToSavingsGoal(api: Api) = TransferMoneyToSavingsGoal(api)

    @Provides
    fun calculateRoundUp(api: Api, getAccount: GetAccount) = CalculateRoundUp(api, getAccount)

    @Provides
    fun getOrCreateSavingsGoal(repo: SavingsGoalRepo) = GetOrCreateSavingsGoal(repo)

    @Provides
    fun getAccount(repo: AccountRepo) = GetAccount(repo)

    @Provides
    fun getWeeks() = GetWeeks()

    @Provides
    fun accountRepo(api: Api, localStorage: AccountLocalStorage): AccountRepo =
        AccountRepoImpl(api = api, localStorage = localStorage)

    @Provides
    fun savingsGoalRepo(api: Api, localStorage: SavingsGoalStorage): SavingsGoalRepo =
        SavingsGoalRepoImpl(api = api, localStorage = localStorage)

    @Provides
    fun savingGoalStorage() =
        SavingsGoalStorage(context.getSharedPreferences("savingGoal_pref", Context.MODE_PRIVATE))

    @Provides
    fun api(retrofitApi: RetrofitApi, accountLocalStorage: AccountLocalStorage, apiDateFormatter: DateTimeFormatter) = Api(
        retrofitApi = retrofitApi,
        token = BuildConfig.USER_TOKEN,
        accountLocalStorage = accountLocalStorage,
        apiDateFormatter = apiDateFormatter
    )

    @Provides
    @Singleton
    fun apiDateFormatter(): DateTimeFormatter = ISODateTimeFormat.dateTime()  // 2017-05-08T12:34:21.000Z

    @Provides
    fun accountLocalStorage() =
        AccountLocalStorage(context.getSharedPreferences("account_prefs", Context.MODE_PRIVATE))

    @Provides
    fun retrofitApi(retrofit: Retrofit): RetrofitApi =
        retrofit.create(RetrofitApi::class.java)

    @Provides
    @Singleton
    fun retrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api-sandbox.starlingbank.com")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun okHttpClient(): OkHttpClient =
        OkHttpClient().newBuilder()
            .addInterceptor(
                HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }).build()

    @Provides
    @Singleton
    fun gson() = Gson()
}