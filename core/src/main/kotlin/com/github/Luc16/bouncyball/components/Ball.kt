package com.github.Luc16.bouncyball.components

import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Polygon
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.github.Luc16.bouncyball.utils.toRad
import com.github.Luc16.bouncyball.utils.translate
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class Ball(x: Float, y: Float, radius: Float, angle: Float) {
    val rect = Rectangle(x - radius, y - radius, 2*radius, 2*radius)
    var speed = 10f
    private val deceleration = 0.02f
    private val direction = Vector2(cos(angle.toRad()).toFloat(), sin(angle.toRad()).toFloat())

    fun collide(walls: List<PolygonRect>, camera: Camera){
        rect.run {
            val prevPos = Vector2(x, y)
            x += direction.x*speed
            y += direction.y*speed
            speed -= deceleration
            if (speed < 0) speed = 0f

            camera.translate(direction.x*speed, direction.y*speed)
            camera.update()

            walls.forEach { wall ->
                if (!wall.live) return@forEach
                if (overlaps(wall.body)){
//                    wall.live = false
                    val angle = wall.body.rotation - (if (wall.side(prevPos, this)) 0 else 90)
                    val normal = Vector2(cos(angle.toRad()).toFloat(), sin(angle.toRad()).toFloat())

                    bounce(normal)
                }
            }
        }
    }

    private fun overlaps(polygon: Polygon): Boolean{
        rect.run {
            val vertices = listOf(Vector2(x, y), Vector2(x + width, y), Vector2(x, y + height), Vector2(x + width, y + height))
            vertices.forEach { vertex ->
                if (polygon.contains(vertex)) {
                    return true
                }
            }
            return false
        }
    }

    fun bounce(normal: Vector2){
        val dot = direction.dot(normal)
        direction.x -= 2*normal.x*dot
        direction.y -= 2*normal.y*dot
        direction.nor()
    }

    fun draw(renderer: ShapeRenderer){
        renderer.circle(rect.x + rect.width/2, rect.y + rect.height/2, rect.width/2f)
    }

    fun changeDirection(dir: Vector2){
        direction.set(dir).nor()
        speed = 10f
    }

}