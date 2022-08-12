package ua.ideabank.obank.domain.enums

enum class ContentState {
    CONTENT, LOADING, EMPTY, ERROR;

    val isContent: Boolean get() = this == CONTENT

    val isLoading: Boolean get() = this == LOADING

    val isEmpty: Boolean get() = this == EMPTY

    val isError: Boolean get() = this == ERROR
}