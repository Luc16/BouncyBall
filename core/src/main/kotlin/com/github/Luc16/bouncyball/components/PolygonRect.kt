package com.github.Luc16.bouncyball.components

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Circle
import com.badlogic.gdx.math.Polygon
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2

class PolygonRect(x: Float, y: Float, width: Float, height: Float, private val color: Color, rotation: Float = 0f) {
    val body = Polygon(floatArrayOf(
        x, y,
        x, y + height,
        x + width, y + height,
        x + width, y
    ))
    val center: Circle
    var live = true

    init {
        val wallCenter = Vector2(0f, 0f)
        body.transformedVertices.forEachIndexed { i, vertex ->
            if (i%2==0) wallCenter.x += vertex
            else wallCenter.y += vertex
        }

        wallCenter.x /= 4
        wallCenter.y /= 4
        body.setOrigin(wallCenter.x, wallCenter.y)
        center = Circle(wallCenter.x, wallCenter.y, 40f)

        body.rotate(rotation)
    }

    fun side(pos: Vector2, rect: Rectangle): Boolean{
        val points = List(body.transformedVertices.size/2) { i ->
            Vector2(body.transformedVertices[2*i], body.transformedVertices[2*i + 1])
        }
        val m1 = (points[0].y - points[3].y)/(points[0].x - points[3].x)
        val y1 = m1*pos.x + (points[0].y - m1*points[0].x)
        val m2 = (points[1].y - points[2].y)/(points[1].x - points[2].x)
        val y2 = m2*pos.x + (points[1].y - m2*points[1].x)

        return (pos.y + rect.width in y1..y2) ||
                (pos.y + rect.width in y2..y1) ||
                (pos.y in y1..y2) ||
                (pos.y in y2..y1)
    }

    fun draw(renderer: ShapeRenderer){
        if (!live) return
        renderer.color = color
        renderer.polygon(body.transformedVertices)
        renderer.color = Color.WHITE
        renderer.circle(center.x, center.y, center.radius)
    }
}