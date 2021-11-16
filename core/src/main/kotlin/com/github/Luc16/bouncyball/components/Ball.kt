package com.github.Luc16.bouncyball.components

import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import com.github.Luc16.bouncyball.utils.dist2
import com.github.Luc16.bouncyball.utils.toRad
import com.github.Luc16.bouncyball.utils.translate
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Rectangle
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

const val MAX_SPEED = 600f
const val DECELERATION = 72f

open class Ball(var x: Float,
                var y: Float,
                var radius: Float,
                private var color: Color = Color.YELLOW,
                angle: Float = 0f,
                private val deceleration: Float = DECELERATION,
                var speed: Float = MAX_SPEED
) {
    val direction = Vector2(cos(angle.toRad()), sin(angle.toRad()))
    val pos: Vector2 get() = Vector2(x, y)
    val radius2 get() = radius*radius

    open fun move(valX: Float, valY: Float){
        x += valX
        y += valY
    }

    private fun move(vec: Vector2){
        x += vec.x
        y += vec.y
    }

    open fun update(delta: Float){
        move(direction.x*speed*delta, direction.y*speed*delta)
        speed -= deceleration*delta
        if (speed < 0) speed = 0f
    }

    fun collideBall(other: Ball){
        if (dist2(pos, other.pos) <= (radius + other.radius)*(radius + other.radius)){
            val offset = radius + other.radius - sqrt(dist2(pos, other.pos))
            val normal = Vector2(x - other.x, y - other.y).nor()

            move(normal.x * offset/2, normal.y * offset/2)
            bounce(normal)

            other.move(-normal.x * offset/2, -normal.y * offset/2)
            other.bounce(normal)
        }
    }

    fun collideWall(wall: PolygonRect){
        val (collided, depth, normal) = wall.collideBall(this)
        if (collided){
            bounce(normal)
            move(-normal.x*depth, -normal.y*depth)
        }
    }

    private fun bounce(normal: Vector2){
        val dot = direction.dot(normal)
        direction.x -= 2*normal.x*dot
        direction.y -= 2*normal.y*dot
        direction.nor()
    }

    fun projectCircle(axis: Vector2): Pair<Float, Float>{
        val direction = Vector2(axis.x*radius, axis.y*radius)
        val p1 = Vector2(x + direction.x, y + direction.y)
        val p2 = Vector2(x - direction.x, y - direction.y)

        var min = p1.dot(axis)
        var max = p2.dot(axis)

        if (max < min) min = max.also { max = min }

        return Pair(min, max)
    }

    fun changeDirection(dir: Vector2){
        direction.set(dir).nor()
        speed = MAX_SPEED
    }

    fun bounceOfWalls(screenRect: Rectangle){
        when {
            x + radius >= screenRect.width -> {
                move(screenRect.width - (radius + x), 0f)
                bounce(Vector2(-1f, 0f))
            }
            x - radius <= 0 -> {
                move(-x + radius, 0f)
                bounce(Vector2(1f, 0f))
            }
            y + radius >= screenRect.height -> {
                move(0f, screenRect.height - radius - y)
                bounce(Vector2(0f, -1f))
            }
            y - radius <= 0 -> {
                move(0f, -y + radius)
                bounce(Vector2(0f, 1f))
            }
        }
    }

    fun draw(renderer: ShapeRenderer){
        renderer.color = color
        renderer.circle(x, y, radius)
    }

}