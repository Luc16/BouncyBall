package com.github.Luc16.BouncyBall.android

import com.badlogic.gdx.backends.android.AndroidApplication
import android.os.Bundle
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import com.github.Luc16.bouncyball.BouncyBall

/** Launches the Android application.  */
class AndroidLauncher : AndroidApplication() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialize(BouncyBall(), AndroidApplicationConfiguration())
    }
}