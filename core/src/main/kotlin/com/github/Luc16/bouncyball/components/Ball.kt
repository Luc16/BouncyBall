package com.github.Luc16.bouncyball.components

import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import com.github.Luc16.bouncyball.utils.dist2
import com.github.Luc16.bouncyball.utils.translate

const val MAX_SPEED = 600f

class Ball(var x: Float, var y: Float, val radius: Float, private val camera: Camera? = null) {
    var speed = MAX_SPEED
    private val deceleration = 72f
    private val direction = Vector2(0f, 0f)
    val pos: Vector2 get() = Vector2(x, y)
    val radius2 get() = radius*radius

    fun move(valX: Float, valY: Float){
        x += valX
        y += valY
        camera?.translate(valX, valY)
    }

    fun move(vec: Vector2){
        x += vec.x
        y += vec.y
        camera?.translate(vec.x, vec.y)
    }

    fun update(delta: Float){
        move(direction.x*speed*delta, direction.y*speed*delta)
        speed -= deceleration*delta
        if (speed < 0) speed = 0f

    }

    fun collideWalls(walls: List<PolygonRect>){
        walls.forEach { wall ->
            val (collided, depth, normal) = wall.collideBall(this)
            if (collided){
                bounce(normal)
                move(-normal.x*depth, -normal.y*depth)
            }
        }
    }

    fun bounce(normal: Vector2){
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

    fun findClosestPoint(poly: PolygonRect): Vector2{
        var dist = Float.MAX_VALUE
        var point = Vector2()
        poly.vertices.forEach { vertex ->
            val d = dist2(vertex, x, y)
            if (d < dist) {
                dist = d
                point = vertex
            }
        }
        return point
    }

    fun draw(renderer: ShapeRenderer){
        renderer.circle(x, y, radius)
    }

    fun changeDirection(dir: Vector2){
        direction.set(dir).nor()
        speed = MAX_SPEED
    }

}