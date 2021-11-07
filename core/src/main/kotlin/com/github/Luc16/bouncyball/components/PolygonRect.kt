package com.github.Luc16.bouncyball.components

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import com.github.Luc16.bouncyball.utils.dist2
import com.github.Luc16.bouncyball.utils.sign
import com.github.Luc16.bouncyball.utils.toRad
import kotlin.math.*

const val DIAGONAL_OFFSET = 3f

class PolygonRect( x: Float, y: Float, width: Float, height: Float, private val color: Color) {
    private val vertices = listOf(
        Vector2(x, y + height - DIAGONAL_OFFSET),
        Vector2(x + DIAGONAL_OFFSET, y + height),

        Vector2(x + width, y + height + DIAGONAL_OFFSET),
        Vector2(x + width + DIAGONAL_OFFSET, y + height),

        Vector2(x + width + DIAGONAL_OFFSET, y),
        Vector2(x + width, y - DIAGONAL_OFFSET),

        Vector2(x + DIAGONAL_OFFSET, y),
        Vector2(x, y + DIAGONAL_OFFSET)
    )
    val x = x + width/2
    val y = y + height/2
    val r = 40f

    fun rotate(deg: Float){
        val rad = deg.toRad()
        val cos = cos(rad)
        val sin = sin(rad)
        vertices.forEach { v ->
            v.x -= x
            v.y -= y
            val oldX: Float = v.x
            v.x = (cos * v.x - sin * v.y) + x
            v.y = (sin * oldX + cos * v.y) + y
        }
    }

    private fun forEachPair(action: (Vector2, Vector2) -> Unit){
        for (i in vertices.indices){
            action(vertices[i], vertices[(i+1)%vertices.size])
        }
    }

    fun findClosestPoint(ball: Ball): Vector2{
        var dist = Float.MAX_VALUE
        var point = Vector2()
        vertices.forEach { vertex ->
            val d = dist2(vertex, ball.x, ball.y)
            if (d < dist) {
                dist = d
                point = vertex
            }
        }
        return point
    }

    fun collideBall(ball: Ball): Triple<Boolean, Float, Vector2> {
        var depth = Float.MAX_VALUE
        val normal = Vector2()

        var colliding = true
        forEachPair { v1, v2 ->
            if (!colliding) return@forEachPair

            val axis = Vector2(v1.y - v2.y, v2.x - v1.x)
            axis.nor()

            val (minPR, maxPR) = projectVertices(axis)
            val (minB, maxB) = ball.projectCircle(axis)

            if (minB >= maxPR || minPR >= maxB) {
                colliding = false
                return@forEachPair
            }

            val axisDepth = min(maxB - minPR, maxPR - minB)

            if (axisDepth < depth){
                normal.set(axis)
                depth = axisDepth
            }

        }
        if (!colliding) return Triple(false, 0f, Vector2())

        val closestPoint = findClosestPoint(ball)

        val axis = Vector2(ball.y - closestPoint.y, closestPoint.x - ball.x)
        axis.nor()

        val (minPR, maxPR) = projectVertices(axis)
        val (minB, maxB) = ball.projectCircle(axis)

        if (minB >= maxPR || minPR >= maxB) {
            return Triple(false, 0f, Vector2())
        }

        val axisDepth = min(maxB - minPR, maxPR - minB)
        if (axisDepth < depth){
            normal.set(axis)
            depth = axisDepth
        }

        val direction = Vector2(x - ball.x, y - ball.y)

        if (direction.dot(normal) < 0f) normal.scl(-1f)

        return Triple(true, depth, normal)
    }

    private fun projectVertices(axis: Vector2): Pair<Float, Float> {
        var min = Float.MAX_VALUE
        var max = Float.MIN_VALUE
        vertices.forEach { vertex ->
            val projection = vertex.dot(axis)
            min = min(projection, min)
            max = max(projection, max)
        }
        return Pair(min, max)
    }



    fun draw(renderer: ShapeRenderer){
        renderer.color = color
        forEachPair { v1, v2 ->
            renderer.line(v1, v2)
        }
        renderer.color = Color.WHITE
        renderer.circle(x, y, 40f)
    }
}