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
import ktx.graphics.use

class CameraTestScreen(game: BouncyBall): CustomScreen(game) {

    private val ball = Circle(45f, 80f, 10f)

    override fun show() {
        viewport.camera.translate(ball.x, ball.y, 0f)
    }

    override fun render(delta: Float) {
        handleInput()
        val mouse = Vector2(Gdx.input.x.toFloat(), Gdx.input.y.toFloat())
        viewport.unproject(mouse)
        if (ball.contains(mouse)) println("$mouse In BALL")

        val numSquares = 20f
        viewport.apply()
        renderer.use(ShapeRenderer.ShapeType.Filled, viewport.camera.combined){
            val ratioX = WIDTH/numSquares
            for (i in 0..numSquares.toInt()){
                renderer.color = Color((i%3==0).toInt()*i/20f + 0.2f, (i%3==1).toInt()*i/20f  + 0.2f, (i%3==2).toInt()*i/20f  + 0.2f, 0f)
                renderer.rect(i*ratioX, 0f, (i + 1)*ratioX, HEIGHT)
            }
            renderer.color = Color.WHITE
            renderer.circle(ball.x, ball.y, ball.radius)
        }
        batch.use(viewport.camera.combined) {
            font.draw(batch, "ASDFASDFASDFASDFSADFASDFASFASFASFASFASFSAFASFI", 0f, HEIGHT/2)
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
