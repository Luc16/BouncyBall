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

    private val angle = 270f
    private val ball = Ball(-84f, 300f, 10f, angle = angle, speed = 600f)
    private val wall = PolygonRect(-80f, -120f, 90f, 80f, Color.BLUE)
    private val prevPos = Vector2(ball.x, ball.y)
    private var normal = Vector2()

    override fun render(delta: Float) {
        ball.update(delta)
        ball.collideWallDirected(wall)
        when {
            (Gdx.input.isKeyPressed(Input.Keys.NUM_1)) -> game.setScreen<PrototypeScreen>()
            (Gdx.input.isKeyJustPressed(Input.Keys.R)) -> {
                ball.changeDirection(Vector2(cos(angle.toRad()), sin(angle.toRad())))
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
                ball.collideWallDirected(wall)
            }
            (Gdx.input.isKeyPressed(Input.Keys.W)) -> wall.rotate(1f)
            (Gdx.input.isKeyPressed(Input.Keys.S)) -> wall.rotate(-1f)

        }
        viewport.apply()
        renderer.use(ShapeRenderer.ShapeType.Line, viewport.camera.combined){
            renderer.color = Color.RED
            renderer.circle(ball.x, ball.y, 5f)
            renderer.color = Color.WHITE
            renderer.circle(ball.x, ball.y, ball.radius)
            renderer.color = Color.YELLOW
            val (m, n) = ball.lineOfMovement()
            renderer.line(-WIDTH/2, m*(-WIDTH/2) + n,WIDTH/2, m*(WIDTH/2) + n)
            renderer.color = Color.RED

            renderer.line(wall.x, wall.y, wall.x + normal.x*100f, wall.y + normal.y*100 )
            wall.draw(renderer)
        }

    }
    

}