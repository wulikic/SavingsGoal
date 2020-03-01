package com.vesna.roundup.data.network.model

interface TransactionsResponse {

    data class FeedItems(val feedItems: List<FeedItem>)

    data class FeedItem(val amount: CurrencyAndAmount, val direction: String)

    data class CurrencyAndAmount(val currency: String, val minorUnits: Int)
}