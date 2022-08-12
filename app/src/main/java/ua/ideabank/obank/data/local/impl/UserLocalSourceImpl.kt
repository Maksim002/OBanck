package ua.ideabank.obank.data.local.impl

import ua.ideabank.obank.data.Constant
import ua.ideabank.obank.data.local.UserLocalSource
import ua.ideabank.obank.data.local.helper.SharedPrefsHelper

class UserLocalSourceImpl(
    sharedPrefsHelper: SharedPrefsHelper
) : UserLocalSource {
    override var token: String by sharedPrefsHelper.string(Constant.LocalData.TOKEN, "")

    override var phone: String by sharedPrefsHelper.string(Constant.LocalData.PHONE, "")
}