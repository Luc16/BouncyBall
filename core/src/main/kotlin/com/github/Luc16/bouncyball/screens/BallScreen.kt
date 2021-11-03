package com.github.Luc16.bouncyball.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.github.Luc16.bouncyball.BouncyBall
import com.github.Luc16.bouncyball.components.Ball
import com.github.Luc16.bouncyball.utils.translate
import ktx.graphics.use

class BallScreen(game: BouncyBall): CustomScreen(game) {

    private val screenRect = Rectangle(0f, 0f, 1280f, 800f)
    private val camera = viewport.camera
    private val ball = Ball(100f, 100f, 10f)
    private var prevPos = Vector2().setZero()

    override fun render(delta: Float) {
        handleInputs()

        ball.update(delta)
        bounceOfWalls()

        viewport.apply()
        renderer.use(ShapeRenderer.ShapeType.Line, camera){
            renderer.color = Color.LIGHT_GRAY
            renderer.rect(0f, 0f, screenRect.width, screenRect.height)
            renderer.color = Color.YELLOW
            ball.draw(renderer)

        }
    }

    private fun handleInputs(){
        val touchPoint = Vector2(Gdx.input.x.toFloat(), Gdx.input.y.toFloat())
        viewport.unproject(touchPoint)

        when {
            Gdx.input.justTouched() || Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) -> {
                prevPos.set(Gdx.input.x.toFloat(), Gdx.input.y.toFloat())
            }
            (!Gdx.input.isTouched || !Gdx.input.isButtonPressed(Input.Buttons.LEFT)) && !prevPos.isZero  -> {
                val dir = Vector2(Gdx.input.x.toFloat() - prevPos.x, -(Gdx.input.y.toFloat() - prevPos.y))
                if (!dir.isZero(CLICK_MARGIN)) ball.changeDirection(dir)
                prevPos.setZero()
            }

            !Gdx.input.isTouched -> prevPos.setZero()
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) camera.translate(y = 10f)
        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) camera.translate(x = -10f)
        if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) camera.translate(y = -10f)
        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) camera.translate(x = 10f)
    }

    private fun bounceOfWalls(){
        when {
            ball.x + ball.radius >= screenRect.width -> {
                ball.move(screenRect.width - (ball.radius + ball.x), 0f)
                ball.bounce(Vector2(-1f, 0f))
            }
            ball.x - ball.radius <= 0 -> {
                ball.move(-ball.x + ball.radius, 0f)
                ball.bounce(Vector2(1f, 0f))
            }
            ball.y + ball.radius >= screenRect.height -> {
                ball.move(0f, screenRect.height - ball.radius - ball.y)
                ball.bounce(Vector2(0f, -1f))
            }
            ball.y - ball.radius <= 0 -> {
                ball.move(0f, -ball.y + ball.radius)
                ball.bounce(Vector2(0f, 1f))
            }
        }
    }

}