package com.github.Luc16.bouncyball.components

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Polygon
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2

class PolygonRect(private val color: Color, vertices: FloatArray) {
    val body = Polygon(vertices)

    constructor(color: Color, vertices: List<Vector2>): this(color, FloatArray(2*vertices.size) { i ->
        if (i % 2 == 0) vertices[i/2].x
        else vertices[i/2].y
    })

    init {
        val wallCenter = Vector2(0f, 0f)
        body.transformedVertices.forEachIndexed { i, vertex ->
            if (i%2==0) wallCenter.x += vertex
            else wallCenter.y += vertex
        }

        wallCenter.x /= 4
        wallCenter.y /= 4
        body.setOrigin(wallCenter.x, wallCenter.y)
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
        renderer.color = color
        renderer.polygon(body.transformedVertices)
    }
}