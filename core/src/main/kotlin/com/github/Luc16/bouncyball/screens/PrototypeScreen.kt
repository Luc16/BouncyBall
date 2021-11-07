package com.github.Luc16.bouncyball.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.github.Luc16.bouncyball.BouncyBall
import com.github.Luc16.bouncyball.components.Ball
import com.github.Luc16.bouncyball.components.PlayerBall
import com.github.Luc16.bouncyball.components.PolygonRect
import com.github.Luc16.bouncyball.utils.dist2
import com.github.Luc16.bouncyball.utils.translate
import ktx.graphics.moveTo
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
    private val screenRect = Rectangle(0f, 0f, 1280f, 800f)
    private val camera = viewport.camera
    private val ball = PlayerBall(100f, 100f, 10f, camera)
    private var prevPos = Vector2().setZero()

    override fun show() {
        camera.moveTo(ball.pos)
    }

    override fun render(delta: Float) {
        handleInputs()

        ball.update(delta)
        walls.forEach { ball.collideWall(it) }
        bounceOfWalls()

        draw()
    }

    private fun handleInputs(){
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1) ||
        Gdx.input.isTouched(0) && Gdx.input.isTouched(1)) game.setScreen<BallScreen>()

        val touchPoint = Vector2(Gdx.input.x.toFloat(), Gdx.input.y.toFloat())
        viewport.unproject(touchPoint)
        walls.forEach { wall ->
            when {
                (Gdx.input.isTouched || Gdx.input.isButtonPressed(Input.Buttons.LEFT)) &&
                        dist2(touchPoint, wall.x, wall.y) <= wall.r*wall.r -> {
                    ball.speed = 0f
                    wall.rotate(1f)
                    prevPos.setZero()
                }
                Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W) -> wall.rotate(1f)
                Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S) -> wall.rotate(-1f)
            }
        }

        handleSwipe()
    }

    private fun handleSwipe(){
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

    private fun draw(){
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

}