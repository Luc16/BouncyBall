package com.github.Luc16.AStar.lwjgl3

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.github.Luc16.AStar.AStar
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration


fun main() {
    Lwjgl3Application(AStar(), Lwjgl3ApplicationConfiguration().apply {
        setTitle("AStar")
        setWindowedMode(640, 480)
        setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png")
    })
}
