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

    private val ball = Ball(0f, 0f, 70f)
    private val wall = PolygonRect(-80f, -120f, 90f, 80f, Color.BLUE)
    private val dir = Vector2(sqrt(2f)/2, sqrt(2f)/2)
    private val prevPos = Vector2(ball.x, ball.y)
    private var normal = Vector2()
    private var o = 0f

    override fun render(delta: Float) {
        when {
            (Gdx.input.isKeyJustPressed(Input.Keys.R)) -> {
                ball.x = prevPos.x
                ball.y = prevPos.y
            }
            (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) -> {
                prevPos.set(ball.x, ball.y)
                val (collided, depth, n) = wall.collideBall(ball)
                normal = n
                if (collided){
                    val offset = -depth/dir.dot(normal)
                    ball.move(dir.x*offset, dir.y*offset)
                }
            }
            (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) -> {
                prevPos.set(ball.x, ball.y)
                val (collided, depth, n) = wall.collideBall(ball)
                normal = n
                if (collided){
                    val offset = -depth/dir.dot(normal)
                    o = offset/50
                }
            }
            (Gdx.input.isKeyPressed(Input.Keys.O)) -> ball.move(dir.x*o, dir.y*o)
            (Gdx.input.isKeyPressed(Input.Keys.P)) -> println(sqrt(dist2(prevPos, ball.pos)))
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
            renderer.line(-225f, -225f, 225f, 225f)
            renderer.color = Color.LIGHT_GRAY
            val m = tg.y/tg.x
            renderer.line(-WIDTH/2, m*(-WIDTH/2),WIDTH/2, m*(WIDTH/2))
            renderer.color = Color.RED

            renderer.line(wall.x, wall.y, wall.x + normal.x*100f, wall.y + normal.y*100 )
            wall.draw(renderer)
        }
    }
    

}