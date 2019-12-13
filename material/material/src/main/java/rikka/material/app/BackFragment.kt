package rikka.material.app

interface BackFragment {

    /**
     * @return should back
     */
    fun onBackPressed(): Boolean {
        return true
    }
}
