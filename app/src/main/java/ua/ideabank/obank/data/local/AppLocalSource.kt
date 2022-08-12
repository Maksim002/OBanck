package ua.ideabank.obank.data.local

interface AppLocalSource {
    var isFirstLaunch: Boolean
    var isOnboardingShown: Boolean
    var isTouchId: Boolean
    var language: String
}