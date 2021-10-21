package com.github.Luc16.bouncyball.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.github.Luc16.bouncyball.BouncyBall
import com.github.Luc16.bouncyball.components.Ball
import com.github.Luc16.bouncyball.components.PolygonRect
import com.github.Luc16.bouncyball.utils.translate
import ktx.graphics.use

class PrototypeScreen(game: BouncyBall): CustomScreen(game) {

    private val walls = listOf(
        PolygonRect(140f, 500f, 860f, 100f, Color.BLUE),
        PolygonRect(600f, 200f, 500f, 60f, Color.VIOLET)
    )
    private val ball = Ball(100f, 100f, 10f, 45f)
    private val screenRect = Rectangle(0f, 0f, 1280f, 720f)
    private val camera = viewport.camera

    override fun show() {
        camera.translate(ball.rect.x, ball.rect.y)
    }

    override fun render(delta: Float) {
        handleInputs()

        walls.forEach { wall ->
            when {
                Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W) -> wall.body.rotate(1f)
                Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S) -> wall.body.rotate(-1f)
            }
        }

        viewport.apply()
        ball.collide(walls, camera)

        renderer.use(ShapeRenderer.ShapeType.Line, camera){
            renderer.color = Color.WHITE
            renderer.rect(0f, 0f, screenRect.width, screenRect.height)

            walls.forEach { wall ->
                wall.draw(renderer)
                renderer.color = Color.WHITE
                renderer.circle(wall.body.originX, wall.body.originY, 10f)
            }
            renderer.color = Color.YELLOW
            ball.draw(renderer)
        }
    }

    private fun handleInputs(){
        when {
            Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) -> {
                val mouse = Vector3(Gdx.input.x.toFloat(),  Gdx.input.y.toFloat(), 0f)
                camera.unproject(mouse)
                ball.changeDirection(mouse)
            }
            ball.rect.x + ball.rect.width >= screenRect.width -> {
                ball.rect.x = screenRect.width - ball.rect.width
                ball.bounce(Vector2(-1f, 0f))
            }
            ball.rect.x <= 0 -> {
                ball.rect.x = 0f
                ball.bounce(Vector2(1f, 0f))
            }
            ball.rect.y + ball.rect.height >= screenRect.height -> {
                ball.rect.y = screenRect.height - ball.rect.height
                ball.bounce(Vector2(0f, -1f))
            }
            ball.rect.y <= 0 -> {
                ball.rect.y = 0f
                ball.bounce(Vector2(0f, 1f))
            }
        }
    }

}