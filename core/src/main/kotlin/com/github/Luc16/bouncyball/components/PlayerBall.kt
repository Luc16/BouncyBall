package com.github.Luc16.bouncyball.components

import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.Color
import com.github.Luc16.bouncyball.utils.translate

class PlayerBall(x: Float, y: Float, radius: Float,
                 private val camera: Camera,
                 color: Color = Color.YELLOW):
    Ball(x, y, radius, color) {

    init {
        direction.setZero()
    }

    override fun move(valX: Float, valY: Float) {
        super.move(valX, valY)
        camera.translate(valX, valY)
    }
}