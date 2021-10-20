package com.github.Luc16.bouncyball.components

import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Polygon
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.github.Luc16.bouncyball.Utils.toRad
import kotlin.math.cos
import kotlin.math.sin

class Ball(private val initialX: Float, private val initialY: Float, radius: Float, angle: Float) {
    val rect = Rectangle(initialX + radius, initialY + radius, 2*radius, 2*radius)
    private val speed = 20f
    private val direction = Vector2(cos(angle.toRad()).toFloat(), sin(angle.toRad()).toFloat())


    fun collide(walls: List<PolygonRect>){
        rect.run {
            val prevPos = Vector2(x, y)
            x += direction.x*speed
            y += direction.y*speed

            walls.forEach { wall ->
                if (overlaps(wall.body)){
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
        renderer.circle(rect.x + rect.width/2, rect.y + rect.height/2, rect.width/2)
    }

    fun reset(dir: Vector2){
        rect.x = initialX
        rect.y = initialY
        direction.set(dir.x - initialX, dir.y - initialY).nor()
    }

}