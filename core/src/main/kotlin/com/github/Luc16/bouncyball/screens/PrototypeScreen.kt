package com.github.Luc16.bouncyball.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.github.Luc16.bouncyball.BouncyBall
import com.github.Luc16.bouncyball.components.Ball
import com.github.Luc16.bouncyball.components.PolygonRect
import com.github.Luc16.bouncyball.utils.translate
import ktx.graphics.use

const val CLICK_MARGIN = 100f

class PrototypeScreen(game: BouncyBall): CustomScreen(game) {

    private val walls = listOf(
        PolygonRect(50f, 600f, 200f, 50f, Color.BLUE),
        PolygonRect(300f, 600f, 200f, 50f, Color.BLUE),
        PolygonRect(550f, 600f, 200f, 50f, Color.BLUE),
        PolygonRect(800f, 600f, 200f, 50f, Color.BLUE),
        PolygonRect(50f, 200f, 200f, 50f, Color.BLUE),
        PolygonRect(300f, 200f, 200f, 50f, Color.BLUE),
        PolygonRect(1050f, 600f, 200f, 50f, Color.BLUE),
        PolygonRect(600f, 200f, 500f, 60f, Color.VIOLET)
    )
    private val ball = Ball(100f, 100f, 10f, 45f)
    private val screenRect = Rectangle(0f, 0f, 1280f, 800f)
    private val camera = viewport.camera
    private var clickTime = 0
    private var prevPos = Vector2().setZero()

    override fun show() {
        camera.translate(ball.rect.x, ball.rect.y)
    }

    override fun render(delta: Float) {
        handleInputs()

        ball.collide(walls, camera)
        bounceOfWalls()

        viewport.apply()
        renderer.use(ShapeRenderer.ShapeType.Line, camera){
            renderer.color = Color.LIGHT_GRAY
            renderer.rect(0f, 0f, screenRect.width, screenRect.height)
            walls.forEach { wall ->
                wall.draw(renderer)
            }
            renderer.color = Color.YELLOW
            ball.draw(renderer)

        }
    }

    private fun handleInputs(){
        clickTime++
        val touchPoint = Vector2(Gdx.input.x.toFloat(), Gdx.input.y.toFloat())
        viewport.unproject(touchPoint)
        walls.forEach { wall ->
            if (!wall.live) return@forEach
            when {
                (Gdx.input.isTouched || Gdx.input.isButtonPressed(Input.Buttons.LEFT)) && wall.center.contains(touchPoint) -> {
                    ball.speed = 0f
                    wall.body.rotate(1f)
                    clickTime = 0
                    prevPos.setZero()
                }
                Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W) -> wall.body.rotate(1f)
                Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S) -> wall.body.rotate(-1f)
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) walls.forEach { it.live = true }

        when {
            Gdx.input.justTouched() || Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) -> {
                prevPos.set(Gdx.input.x.toFloat(), Gdx.input.y.toFloat())
            }
            (!Gdx.input.isTouched || !Gdx.input.isButtonPressed(Input.Buttons.LEFT)) && !prevPos.isZero  -> {
                val dir = Vector2(Gdx.input.x.toFloat() - prevPos.x, -(Gdx.input.y.toFloat() - prevPos.y))
                if (!dir.isZero(CLICK_MARGIN)) ball.changeDirection(dir)
                prevPos.setZero()
            }
            Gdx.input.isKeyJustPressed(Input.Keys.S) -> ball.speed = 0f
            !Gdx.input.isTouched -> prevPos.setZero()
        }
    }

    private fun bounceOfWalls(){
        when {
            ball.rect.x + ball.rect.width >= screenRect.width -> {
                camera.translate(x = screenRect.width - ball.rect.width - ball.rect.x)
                ball.rect.x = screenRect.width - ball.rect.width
                ball.bounce(Vector2(-1f, 0f))
            }
            ball.rect.x <= 0 -> {
                camera.translate(x = -ball.rect.x)
                ball.rect.x = 0f
                ball.bounce(Vector2(1f, 0f))
            }
            ball.rect.y + ball.rect.height >= screenRect.height -> {
                camera.translate(y = screenRect.height - ball.rect.height - ball.rect.y)
                ball.rect.y = screenRect.height - ball.rect.height
                ball.bounce(Vector2(0f, -1f))
            }
            ball.rect.y <= 0 -> {
                camera.translate(y = -ball.rect.y)
                ball.rect.y = 0f
                ball.bounce(Vector2(0f, 1f))
            }
        }
    }

}