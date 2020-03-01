package com.vesna.roundup.di

import android.content.Context
import com.google.gson.Gson
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
                             getSavingsGoal: GetOrCreateSavingsGoal) =
        MainViewModelFactory(getWeeks, calculateRoundUp, getSavingsGoal)

    @Provides
    fun getAllWeeksSinceAccountCreation(getAccount: GetAccount, getWeeks: GetWeeks) =
        GetAllWeeksSinceAccountCreation(
            getAccount = getAccount,
            getWeeks = getWeeks
        )

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
    fun api(retrofitApi: RetrofitApi, apiDateFormatter: DateTimeFormatter) = Api(
        retrofitApi = retrofitApi,
        token = "eyJhbGciOiJQUzI1NiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAAAH1Ty46cMBD8lRXn7RVvMLfc8gP5gLbdzFgDNrLNJKso_x6DYRgmq9yoqu5ytdv8TpRzSZfgpEDSaD6cRzsofeGobx_CjMl74mYeKpqGsTytGXAUDMo874ExIuA8l03B27ZGForp15R0WdUWWVo3Zf6eKPSRSPO0WAgUwszafzeDJPtDyeCdplWKBc-hL7CFErEBzpAgRVGzUmaMsT54e3MjHTtYVbQyz3oQaVNDmVYFtFmIlMqSNzIt2rZqQkcY65sQ5FzsarMqy9MMoSQeZsgEAROSoKzasmBtnWc8WwYWZqLlUmJSuK5RQeNInSWUby-C_5xeBCVJe9Ursmd-UM6fmA1IaUPIjqTyDxAV71FcR3pUHvinVZ7ecPZXY5ULKwOlpborOeMQizkOqMUWTaCVIIz21gzxoIXZNKN7ZUf0ymgwPfSzlu4hucfpO4hHi9l5M-4j0ohqMx5RS_TUSRoo1O1wLRvR3sgvaSdLPVkKAd3_pHhW1KYBw8qU9nSxa9jnxn_FrZWsuOI-wkgeQxrsRICruuE1-YSfRLsUwTZEBEcRqBEv20xR2z-Bz8Ot2_dCB3XYRnw4R_wwGIwIK3wqXwkwyy5f2a3Lml4Ne6iY8kStVZYEqcmfgDtL8coc3sMaHFzMkePEbdFP3OrzzIC3qF1Y5FcWh_iF1yFGUx9e0PIujJVPbmd2tzmze7-n5Z8B4e6v1CT7jZq5EzZcwuNdrSlQrMR6p8_EUpH8-Qvf3f9OQAUAAA.fmsBoAiCbz7V7j_Y4MIuhk3JNrtapyew7Hgz91yGfHQwtfCDa0JKxrowFEXJnQ3V3-oLzTNC8TMju757A4_sS2s7F-p7POOaG_bse8F3wn1GtO87FmTHJLphlG5V0MRC9QhIMp7EPeamm7ic-r99L3E3u3ReGLn81izIWXDaAPd4cKX8VMp_xqB5O75pPvb8oCh7kf4yZXlDLLPunQ9SXsOL8HGV978n39AAnC96nmbdbIjnREmAGNxq-dmHGBPM3bRbkSbunNQqm_51kInvmEJSwvlefU5_mDxCgzP19gM1gjQC87wODRNNiO-1BOsEszohvSQhkk4q7HM4MvyXpDvrFTGLBjvZf_7CP7mpnM9ygdNTKZsmxpzq69XlSKDIUiSpIWT8WTqk--djSt7hU14a98sX8-0Ca3disShReKtDk-OU2O5jAhAEticGWJ_JP9oJ5ni6sxhxtmI5ogM02oxIX_59Jf0mMV5K3C5XUI770a2S26S6J1DkfHg1GRWN4C21GTIjZfpQIl458rbS_5i3UxdS7KpHNtL_rQzrohqabwGJMG_ZMR_h5lyq_BoqSJnziZEgLnPgn3zce2hvTVAmBxUxNWGvIbtjPtdwn_OS5FJb1Vyud64d3Vz7IwFCWBNYSV7H2vgbeYV7_bJxWdWtPDYfCO5sYn6Ob3CMwqA",
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
    fun retrofit(gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api-sandbox.starlingbank.com")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun gson() = Gson()
}