package com.github.Luc16.BouncyBall.lwjgl3

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.github.Luc16.bouncyball.BouncyBall
import com.github.Luc16.bouncyball.HEIGHT
import com.github.Luc16.bouncyball.WIDTH


fun main() {
    Lwjgl3Application(BouncyBall(), Lwjgl3ApplicationConfiguration().apply {
        setTitle("BouncyBall")
        setWindowedMode(WIDTH.toInt(), HEIGHT.toInt())
        setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png")
    })
}
