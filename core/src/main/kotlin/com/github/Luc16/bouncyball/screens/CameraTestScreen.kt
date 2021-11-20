package com.github.Luc16.bouncyball.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Circle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.FillViewport
import com.badlogic.gdx.utils.viewport.FitViewport
import com.github.Luc16.bouncyball.BouncyBall
import com.github.Luc16.bouncyball.HEIGHT
import com.github.Luc16.bouncyball.WIDTH
import com.github.Luc16.bouncyball.components.Ball
import com.github.Luc16.bouncyball.components.PolygonRect
import com.github.Luc16.bouncyball.utils.dist2
import com.github.Luc16.bouncyball.utils.ortho
import com.github.Luc16.bouncyball.utils.toRad
import ktx.graphics.circle
import ktx.graphics.use
import kotlin.math.*

class CameraTestScreen(game: BouncyBall): CustomScreen(game) {

    private val ball = Ball(0f, 0f, 70f, angle = 225f)
    private val wall = PolygonRect(-80f, -120f, 90f, 80f, Color.BLUE)
    private val prevPos = Vector2(ball.x, ball.y)
    private var normal = Vector2()

    override fun render(delta: Float) {
        when {
            (Gdx.input.isKeyJustPressed(Input.Keys.R)) -> {
                ball.x = prevPos.x
                ball.y = prevPos.y
            }
            (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) -> {
                prevPos.set(ball.x, ball.y)
                val (collided, depth, nor) = wall.collideBall(ball)
                normal = nor
                if (collided){
                    val offset = -depth/ball.direction.dot(normal)
                    ball.move(ball.direction.x*offset, ball.direction.y*offset)
                }
            }
            (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) -> {
                prevPos.set(ball.x, ball.y)
                val (collided, depth, nor) = wall.collideBall(ball)
                normal = nor
                if (collided){
                    val offset = -depth/ball.direction.dot(normal)
                    val tg = normal.ortho()
                    ball.move(ball.direction.x*offset, ball.direction.y*offset)
                    val vertex = wall.findClosestPoint(ball)
                    if (abs(ball.pos.dot(tg)) > abs(vertex.dot(tg))) { // Condição precisa de ajustes
                        val (m, n) = ball.lineOfMovement()
                        val a = (1 + m*m)
                        val b = (vertex.x + m*vertex.y - n*m)//*2
                        val c = (vertex.x*vertex.x + (vertex.y - n)*(vertex.y - n) - ball.radius2)
                        val sqrtDelta = sqrt(b*b - a*c)
                        val x1 = (b + sqrtDelta)/a
                        val p1 = Vector2(x1, m*x1+n)
                        val x2 = (b - sqrtDelta)/a
                        val p2 = Vector2(x2, m*x2+n)

                        val pf = if (dist2(ball.pos, p1) < dist2(ball.pos, p2)) p1 else p2
                        ball.move(pf.x - ball.x, pf.y - ball.y)
                    }
                }
            }
            (Gdx.input.isKeyPressed(Input.Keys.W)) -> wall.rotate(1f)
            (Gdx.input.isKeyPressed(Input.Keys.S)) -> wall.rotate(-1f)

        }
        viewport.apply()
        val tg = normal.ortho()
        renderer.use(ShapeRenderer.ShapeType.Line, viewport.camera.combined){
            renderer.color = Color.RED
            renderer.circle(ball.x, ball.y, 5f)
            renderer.color = Color.WHITE
            renderer.circle(ball.x, ball.y, ball.radius)
            renderer.color = Color.YELLOW
            val m1 = ball.direction.y/ball.direction.x
            renderer.line(-WIDTH/2, m1*(-WIDTH/2),WIDTH/2, m1*(WIDTH/2))
            renderer.color = Color.LIGHT_GRAY
            val m2 = tg.y/tg.x
            renderer.line(-WIDTH/2, m2*(-WIDTH/2),WIDTH/2, m2*(WIDTH/2))
            renderer.color = Color.RED

            renderer.line(wall.x, wall.y, wall.x + normal.x*100f, wall.y + normal.y*100 )
            wall.draw(renderer)
        }

    }
    

}