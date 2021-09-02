package com.github.Luc16.AStar

import com.badlogic.gdx.Application.LOG_DEBUG
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.viewport.FitViewport
import com.github.Luc16.AStar.screens.CustomScreen
import com.github.Luc16.AStar.screens.GameScreen
import ktx.app.KtxGame
import ktx.log.Logger
import ktx.log.logger

private val LOG: Logger = logger<AStar>()

const val UNIT_SCALE:Float = 1 / 16f
const val V_WIDTH = 16f
const val V_HEIGHT = 9f

class AStar : KtxGame<CustomScreen>() {
    val batch: Batch by lazy { SpriteBatch() }
    val drawer: ShapeRenderer by lazy { ShapeRenderer() }

    override fun create() {
        Gdx.app.logLevel = LOG_DEBUG
        LOG.debug { "Create game instance" }
        addScreen(GameScreen(this))
        setScreen<GameScreen>()
    }

    override fun dispose() {
        super.dispose()
        batch.dispose()
    }
}