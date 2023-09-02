package com.example.domain.models

sealed class GenericErrors : Throwable() {
    object GenericError : GenericErrors()
}

sealed class RewardsErrors : GenericErrors() {
    object EmptyRewardList : RewardsErrors()
}