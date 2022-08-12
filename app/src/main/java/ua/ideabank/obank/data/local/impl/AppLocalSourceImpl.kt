package ua.ideabank.obank.data.local.impl

import ua.ideabank.obank.data.local.AppLocalSource
import ua.ideabank.obank.data.local.helper.SharedPrefsHelper

class AppLocalSourceImpl(
    sharedPrefsHelper: SharedPrefsHelper
) : AppLocalSource {

    override var isFirstLaunch: Boolean by sharedPrefsHelper.boolean(
        "isFirstLaunch", true
    )

    override var isOnboardingShown: Boolean by sharedPrefsHelper.boolean(
        "isOnboardingShown", false
    )

    override var isTouchId: Boolean by sharedPrefsHelper.boolean(
    "isTouchId", false
    )

    override var language: String by sharedPrefsHelper.string(
        "language", "ua"
    )
}
