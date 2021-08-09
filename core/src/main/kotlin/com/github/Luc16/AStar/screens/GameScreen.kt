package com.github.Luc16.AStar.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.viewport.FitViewport
import com.github.Luc16.AStar.AStar
import ktx.graphics.use
import ktx.log.Logger
import ktx.log.logger

private val LOG: Logger = logger<GameScreen>()

class GameScreen(game: AStar): CustomScreen(game) {
    private val viewport = FitViewport(16f*32, 9f*32)

    override fun show() {
        LOG.debug { "First screen is now active" }
    }

    override fun render(delta: Float) {
        drawer.use(ShapeRenderer.ShapeType.Filled, viewport.camera.combined) { draw ->
            draw.color = Color.RED
            draw.circle(Gdx.input.x.toFloat(), (Gdx.graphics.height - Gdx.input.y).toFloat(), 80f)
            draw.color = Color.YELLOW
            draw.rect(1f, 1f, 50f, 80f)
        }
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
    }

    override fun dispose() {
        batch.dispose()
        drawer.dispose()
    }
}