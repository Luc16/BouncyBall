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
import com.github.Luc16.bouncyball.utils.toRad
import ktx.graphics.circle
import ktx.graphics.use
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class CameraTestScreen(game: BouncyBall): CustomScreen(game) {

    private val ball = Ball(0f, 0f, 70f)
    private val wall = PolygonRect(-80f, -120f, 90f, 80f, Color.BLUE)
    private val dir = Vector2(sqrt(2f)/2, sqrt(2f)/2)
    private val prevPos = Vector2(ball.x, ball.y)
    private var normal = Vector2()

    override fun show() {
//        wall.body.rotate(-60f)
//        viewport.camera.translate(ball.x, ball.y, 0f)
    }

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
            renderer.line(-225f, -225f, 225f, 225f)
            renderer.color = Color.RED
//            renderer.circle(p, 10f)

            renderer.line(wall.x, wall.y, wall.x + normal.x*100f, wall.y + normal.y*100 )
            wall.draw(renderer)
        }
    }

    private fun handleInput(){
        val speed = 20f
        ball.run {
            if(keyPressed(Input.Keys.W)) {
               viewport.camera.translate(0f, speed, 0f)
                y += speed
            }
            if(keyPressed(Input.Keys.A)) {
               viewport.camera.translate(-speed, 0f, 0f)
                x -= speed
            }
            if(keyPressed(Input.Keys.S)) {
               viewport.camera.translate(0f, -speed, 0f)
                y -= speed
            }
            if(keyPressed(Input.Keys.D)) {
               viewport.camera.translate(speed, 0f, 0f)
                x += speed
            }
            if (x + radius > WIDTH){
               viewport.camera.translate(WIDTH - x - radius, 0f, 0f)
                x = WIDTH - radius
            } else if (x - radius < 0) {
               viewport.camera.translate(-(x - radius), 0f, 0f)
                x = radius
            }

            if (y + radius > HEIGHT){
               viewport.camera.translate(0f, HEIGHT - y - radius, 0f)
                y = HEIGHT - radius
            } else if (y - radius < 0) {
               viewport.camera.translate(0f, -(y -radius), 0f)
                y = radius
            }
        }
    }

    private fun keyPressed(key: Int): Boolean = Gdx.input.isKeyPressed(key)


}