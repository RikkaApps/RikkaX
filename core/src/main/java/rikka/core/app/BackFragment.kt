package rikka.core.app

interface BackFragment {

    /**
     * @return should back
     */
    fun onBackPressed(): Boolean {
        return true
    }
}
