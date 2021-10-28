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
import com.github.Luc16.bouncyball.components.PolygonRect
import com.github.Luc16.bouncyball.utils.toRad
import ktx.graphics.use
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class CameraTestScreen(game: BouncyBall): CustomScreen(game) {

    private val ball = Circle(0f, 0f, 70f)
    private val wall = PolygonRect(-225f, -150f, 450f, 80f, Color.BLUE)
    private val dir = Vector2(sqrt(2f)/2, sqrt(2f)/2)
    private var offset = 0f

    override fun show() {
        wall.body.rotate(-60f)
//        viewport.camera.translate(ball.x, ball.y, 0f)
    }

    override fun render(delta: Float) {
        val a = (wall.body.transformedVertices[5] - wall.body.transformedVertices[3])/(wall.body.transformedVertices[4] - wall.body.transformedVertices[2])
        val b = -1f
        val c = wall.body.transformedVertices[5] - a*wall.body.transformedVertices[4]

        val dist = ball.radius - abs(a*ball.x + b*ball.y + c)/sqrt(a*a + b*b)
        val angle = wall.body.rotation -  90
        val normal = Vector2(cos(angle.toRad()).toFloat(), sin(angle.toRad()).toFloat())

        when {
            (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) -> {
                offset = dist/dir.dot(normal)
                ball.x -= dir.x*offset
                ball.y -= dir.y*offset
            }
            (Gdx.input.isKeyJustPressed(Input.Keys.R)) -> {
                ball.x += dir.x*offset
                ball.y += dir.y*offset
            }
            (Gdx.input.isKeyPressed(Input.Keys.W)) -> wall.body.rotate(1f)
            (Gdx.input.isKeyPressed(Input.Keys.S)) -> wall.body.rotate(-1f)


        }


        viewport.apply()
        renderer.use(ShapeRenderer.ShapeType.Line, viewport.camera.combined){
            renderer.color = Color.RED
            renderer.circle(ball.x, ball.y, 5f)
            renderer.color = Color.WHITE
            renderer.circle(ball.x, ball.y, ball.radius)
            renderer.color = Color.YELLOW
            renderer.line(-225f, -225f, 225f, 225f)

            renderer.line(wall.body.originX, wall.body.originY, wall.body.originX + normal.x*100f, wall.body.originY + normal.y*100 )
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

private fun Boolean.toInt(): Int = if (this) 1 else 0
