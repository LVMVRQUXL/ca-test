package lamarque.loic.catest.ui

sealed interface UIState {
    data object Loading : UIState
    data object Error : UIState
    data class Success<T : Any>(val value: T) : UIState
}
