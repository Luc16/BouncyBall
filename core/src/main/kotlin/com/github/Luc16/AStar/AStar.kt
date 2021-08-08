package com.github.Luc16.AStar

import com.badlogic.gdx.Game
import com.github.Luc16.AStar.FirstScreen

/** [com.badlogic.gdx.ApplicationListener] implementation shared by all platforms.  */
class AStar : Game() {
    override fun create() {
        setScreen(FirstScreen())
    }
}