package com.github.Luc16.bouncyball.utils

import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector2
import kotlin.math.PI

fun Float.toRad() = PI *this/180

fun Camera.translate(x: Float = 0f, y: Float = 0f) = translate(x, y, 0f)
