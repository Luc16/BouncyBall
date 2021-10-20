package com.github.Luc16.bouncyball

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.github.Luc16.bouncyball.screens.CustomScreen
import com.github.Luc16.bouncyball.screens.PrototypeScreen
import ktx.app.KtxGame

const val WIDTH = 1280
const val HEIGHT = 720

class BouncyBall: KtxGame<CustomScreen>() {
    val renderer: ShapeRenderer by lazy { ShapeRenderer() }
    val font: BitmapFont by lazy { BitmapFont() }
    val batch: Batch by lazy { SpriteBatch() }

    override fun create() {
        font.data.scale(2f)
        addScreen(PrototypeScreen(this))
        setScreen<PrototypeScreen>()
    }

    override fun dispose() {
        super.dispose()
        renderer.dispose()
        batch.dispose()
        font.dispose()
    }
}