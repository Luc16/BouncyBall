package com.github.Luc16.bouncyball

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.viewport.FillViewport
import com.badlogic.gdx.utils.viewport.FitViewport
import com.github.Luc16.bouncyball.screens.*
//import com.github.Luc16.bouncyball.screens.PrototypeScreen
import ktx.app.KtxGame

const val WIDTH = 450f
const val HEIGHT = 800f
//const val WIDTH = 1280f
//const val HEIGHT = 800f

class BouncyBall: KtxGame<CustomScreen>() {
    val renderer: ShapeRenderer by lazy { ShapeRenderer() }
    val font: BitmapFont by lazy { BitmapFont() }
    val batch: Batch by lazy { SpriteBatch() }
    val gameViewport = FitViewport(450f, 800f)

    override fun create() {
        font.data.scale(2f)
        addScreen(PrototypeScreen(this))
        addScreen(CameraTestScreen(this))
        addScreen(BallScreen(this))
        addScreen(TestMapScreen(this))
        addScreen(ParticleScreen(this))
        setScreen<CameraTestScreen>()
    }

    override fun dispose() {
        super.dispose()
        renderer.dispose()
        batch.dispose()
        font.dispose()
    }
}