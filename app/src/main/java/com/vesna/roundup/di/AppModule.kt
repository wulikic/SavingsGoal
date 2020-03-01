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
        token = "eyJhbGciOiJQUzI1NiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAAAH1TQZKbMBD8yhbn1RYCjBG33PKBPGA0GmyVQaIk4WQrlb9HIDDG2cqN7p5p9WjE70x7n7UZjJopGuyHD-B6bS4SzO0D7ZC9Z36SseJ8FqLIa8EkoGBVUXRMCCImZaHOpWyaGkQspl9j1vJTU_JTIUr-nmkIicjr-jQTgGgnE77bXpH7oVX0zvNTDqUsWFdCwyqAM5MCiOWAtagUF0J00TvYG5nUIZoSUFYNw64BVvEYRBT8zBpe1uUZsYrHxY441jdE8j51NfzEi5zHBpJxBo6xCxWx6tRUpWjqgks-D4x2pPlSUlJ2XaIyAwO1jkC9vQjhc3wRtCITdKfJHfle-3BgVqCUiyFbUjo8QFJCALwO9Kjc8U-nA73BFK7WaR9XxrRR-q7VBH0qltCDwTUaglMMrQnO9umgmVk1azrtBgjaGmY71k1G-YfkH6dvIB2Nkw922EakAfRqPIBREKhV1FOs2-BSNoC7UZjTjo46chQD-v9J6aykjT3ElWkT6OKWsM-N_4prKzm8wjbCQAFiGmgxwkVd8ZJ8hE-iTUpgHSKBvYjpAS7rTEnbPpmc-lu77YV2ardNeHdO-GHQW4wrfCpfCGbnXb6ya5ezne63UCnlgVqqHCHpMRyAP0rpyjzc4xo8u9g9x4Fbox-4xeeZYcGB8XGRX1ns4hdeu5hMQ3xB87uwTj25HdnN5shu_YHmf4ahv79So-pWapIeXbyEx7taUgAuxHKnz8Rckf35C2hqRlpABQAA.Boxf_eZEHv_VnuJ7sCTKggdqKtH-BxSmUHVph89OFN11mJhH1Btg42CaCVuuGyRFJQMa3v7FJJOHSzBU5oh8iRv25PumVoIlvsnwhGeplw_0-rGbr6Z364Wu8H3tDV45gO_LvFIvgR6G534EJvTpMOwheEQGvtShO02ZCW2VFYOjiYHuXrtppAF3HY3FNv3uYboNoSeCI68gtQhxYjjGbKNUHizbcVTGeKEQd8-Nbo4CDqshdCGU9l9GzCgJl3VGuGuzZ5OzBELxiZndrjJxEzDS1wbYrJrNVi1S7YlLr7AhTLosfVFhhSaaylfAUyx6h0dYo9qoeJd6pm-0IyHYwc-9Sx5CX409tRlgEAtliRq-aYXBBL_dtoNK1IJK1yVmS3sf1r8DMJk-qyKsjmDoMxvuTlUIjLyh0ypIqrAhFMFzmH8TICeF-ELEl8F_Y2zK1UP2m1kVOCi7LSFGkFcSmSm9vAsgz5E4c_96IurAqoH-8VHNMaA2xIE3fSgSXYWhsneB77TK0-ZDMPq5gX_Yfd3IH1FvttcODNyala8vvfl1rBoWt7m2uVCYkTgBqVegyXH1DPQzKmz53Egn1xGClW50UQA6x3zy4HHcEAmX2gnCzIKzc5hpCpwiA5pMJnkxEr-aXJsLFLHZw7E4PQiY1ewxEROGZMiGYVDrKlYC8LA",
//        token = "eyJhbGciOiJQUzI1NiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAAAH1Ty5KcMAz8lS3Oqy3er1tu-YF8gLDEjGvApmwzyVYq_x6DYRgmW7nR3VK7ZZnfkbQ2aiOcJBCP-sM6NINUlw7V7UPoMXqP7Nz5iqpIqrIWDVRxmkKeUANdV9VQiiouy6TGqiBfzL-mqE2KOkviKs7q90iiC0ScxuVCoBB6Vu67HojND0mLd55ShhVB05QC8jhPoaY6Ae7ruvNGXSbQezt9YxU6miIjEkUCsaAM8pwZuphKoLrhqujzMqkK3-HH-iYEWxu6MOsy7LiCoqwKyMuYocEihkyQSBssY6R-GVjoiZfykBSua1RQOHJrGOntRXCf04sgiZWTvWRz5gdp3YnZAJHxIVsm6R4gKM6huI78qDzwTyMdv-HsrtpI61cGUpG8S5pxCMUdDqjEFk2gIRBaOaOHcNDCbJpWvTQjOqkV6B76WZF9SPZx-g7C0WK2To_7iDyi3IxHVISOW-KBfd0O17IRzY3dknYy3LNhH9D-TwpnBW0aULAf0_HFrGGfG_8Vt1Y24or7CCM79GmwFR6u6obX5BN-Mu9SANsQARxFIEe8bDMFbf-Ebh5u7b4XPqjDNuDDOeCHwaCFX-FT-UqAXnb5ym5dRvdy2EOFlCdqrTIsWE7uBOxZCldm8e7XYOGijxwnbot-4lafZwacQWX9Ir-yOMQvvA4xmDr_gpZ3oQ09uZ3Z3ebM7v2Ol38GhL2_UhP1GzV3Vhh_CY93taZAsRLrnT4TS0X05y-ouomqQAUAAA.vpFVcrMceugP08xsnn3AxhhBX2iV09HNUBeIBzkCxaFlI0jcTQV0ViMYVeBcBT4MjLShkKboivJxDtC2GhmfHzhvUisJIO3jYYGGnp_GWTH-ZMnKHouADqx4QU6oRFZoO-vqKNMULA-KIO0_-BSPl93rPpJ9oIqLqTnRc2OSExem7o1pWjzfLwmBLDjPn7xGuLNt0zSn0k0vgzfwucWaoDGC0UrTEi4MB_y4uhIo0HPNHxZtpu1FBEhqW2-aMfZYUtAUvHkrrgE_R0LsyWf56_kAcS5zuF35ACnwxRCjYJ1hK5zHmvd5qt-P1u96nQUaC6bg-YqRnknIMCxKYtFPnUdlhRwWECZCddzO8WgVahbTVnbDRAdWWUgzAT_GjpAax7F3JsLupoXidzgCmoIqfhjLAuwoJHCKHiKERWugDYBZzdXLwk2vzLYT2y6tG2CPeNTn2Qv5Taw2pW3O2v7opyw6UhplELDnzNOWRZetVErVF2XJMGvdmWBoHV82i2K9UTiyzvfAgmt1gs76sYwiANiZvYu6CGWUFU0SZoCO24oiOo5cEWktQBrPHg2g4GeQHvrXI6INO7sN-HRJ7BzU6BoVVYgceH9xQxWp-haOt9SAgn4Xv_f7E4c0K2csk4LJsF-5ozwH-H9WcuwqBUPn02eRcss6OpYMCZ67AH506C8",
//        token = "eyJhbGciOiJQUzI1NiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAAAH1Ty46cMBD8lRXn7RVvMLfc8gP5gLbdzFgDNrLNJKso_x6DYRgmq9yoqu5ytdv8TpRzSZfgpEDSaD6cRzsofeGobx_CjMl74mYeKpqGsTytGXAUDMo874ExIuA8l03B27ZGForp15R0WdUWWVo3Zf6eKPSRSPO0WAgUwszafzeDJPtDyeCdplWKBc-hL7CFErEBzpAgRVGzUmaMsT54e3MjHTtYVbQyz3oQaVNDmVYFtFmIlMqSNzIt2rZqQkcY65sQ5FzsarMqy9MMoSQeZsgEAROSoKzasmBtnWc8WwYWZqLlUmJSuK5RQeNInSWUby-C_5xeBCVJe9Ursmd-UM6fmA1IaUPIjqTyDxAV71FcR3pUHvinVZ7ecPZXY5ULKwOlpborOeMQizkOqMUWTaCVIIz21gzxoIXZNKN7ZUf0ymgwPfSzlu4hucfpO4hHi9l5M-4j0ohqMx5RS_TUSRoo1O1wLRvR3sgvaSdLPVkKAd3_pHhW1KYBw8qU9nSxa9jnxn_FrZWsuOI-wkgeQxrsRICruuE1-YSfRLsUwTZEBEcRqBEv20xR2z-Bz8Ot2_dCB3XYRnw4R_wwGIwIK3wqXwkwyy5f2a3Lml4Ne6iY8kStVZYEqcmfgDtL8coc3sMaHFzMkePEbdFP3OrzzIC3qF1Y5FcWh_iF1yFGUx9e0PIujJVPbmd2tzmze7-n5Z8B4e6v1CT7jZq5EzZcwuNdrSlQrMR6p8_EUpH8-Qvf3f9OQAUAAA.fmsBoAiCbz7V7j_Y4MIuhk3JNrtapyew7Hgz91yGfHQwtfCDa0JKxrowFEXJnQ3V3-oLzTNC8TMju757A4_sS2s7F-p7POOaG_bse8F3wn1GtO87FmTHJLphlG5V0MRC9QhIMp7EPeamm7ic-r99L3E3u3ReGLn81izIWXDaAPd4cKX8VMp_xqB5O75pPvb8oCh7kf4yZXlDLLPunQ9SXsOL8HGV978n39AAnC96nmbdbIjnREmAGNxq-dmHGBPM3bRbkSbunNQqm_51kInvmEJSwvlefU5_mDxCgzP19gM1gjQC87wODRNNiO-1BOsEszohvSQhkk4q7HM4MvyXpDvrFTGLBjvZf_7CP7mpnM9ygdNTKZsmxpzq69XlSKDIUiSpIWT8WTqk--djSt7hU14a98sX8-0Ca3disShReKtDk-OU2O5jAhAEticGWJ_JP9oJ5ni6sxhxtmI5ogM02oxIX_59Jf0mMV5K3C5XUI770a2S26S6J1DkfHg1GRWN4C21GTIjZfpQIl458rbS_5i3UxdS7KpHNtL_rQzrohqabwGJMG_ZMR_h5lyq_BoqSJnziZEgLnPgn3zce2hvTVAmBxUxNWGvIbtjPtdwn_OS5FJb1Vyud64d3Vz7IwFCWBNYSV7H2vgbeYV7_bJxWdWtPDYfCO5sYn6Ob3CMwqA",
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