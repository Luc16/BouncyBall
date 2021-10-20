package com.github.Luc16.bouncyball.screens


import com.github.Luc16.bouncyball.BouncyBall
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import ktx.app.KtxScreen

abstract class CustomScreen(
    val game: BouncyBall,
    val batch: Batch = game.batch,
    val renderer: ShapeRenderer = game.renderer,
    val font: BitmapFont = game.font,
) : KtxScreen