package com.github.Luc16.BouncyBall.lwjgl3

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.github.Luc16.bouncyball.BouncyBall


fun main() {
    Lwjgl3Application(BouncyBall(), Lwjgl3ApplicationConfiguration().apply {
        setTitle("BouncyBall")
        setWindowedMode(1280, 720)
        setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png")
    })
}
