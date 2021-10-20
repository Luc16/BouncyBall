package com.github.Luc16.bouncyball.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Polygon
import com.badlogic.gdx.math.Vector2
import com.github.Luc16.bouncyball.BouncyBall
import com.github.Luc16.bouncyball.HEIGHT
import com.github.Luc16.bouncyball.Utils.toRad
import com.github.Luc16.bouncyball.WIDTH
import com.github.Luc16.bouncyball.components.Ball
import com.github.Luc16.bouncyball.components.PolygonRect
import ktx.graphics.use
import kotlin.math.cos
import kotlin.math.sin

class PrototypeScreen(game: BouncyBall): CustomScreen(game) {

    private val walls = listOf(
        PolygonRect(Color.BLUE, floatArrayOf(140f, 500f, 140f, 600f, 1000f, 600f, 1000f, 500f)),
        PolygonRect(Color.VIOLET, floatArrayOf(600f, 200f, 600f, 260f, 1100f, 260f, 1100f, 200f))
    )
    private val ball = Ball(400f, 100f, 10f, 45f)

    override fun render(delta: Float) {
        when {
            Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) -> {
                val mouse = Vector2(Gdx.input.x.toFloat(), HEIGHT - Gdx.input.y.toFloat())
                ball.reset(mouse)
            }
            ball.rect.x + ball.rect.width >= WIDTH -> {
                ball.rect.x = WIDTH - ball.rect.width
                ball.bounce(Vector2(-1f, 0f))
            }
            ball.rect.x <= 0 -> {
                ball.rect.x = 0f
                ball.bounce(Vector2(1f, 0f))
            }
            ball.rect.y + ball.rect.height >= HEIGHT -> {
                ball.rect.y = HEIGHT - ball.rect.height
                ball.bounce(Vector2(0f, -1f))
            }
            ball.rect.y <= 0 -> {
                ball.rect.y = 0f
                ball.bounce(Vector2(0f, 1f))
            }
        }

        walls.forEach { wall ->
            when {
                Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W) -> wall.body.rotate(1f)
                Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S) -> wall.body.rotate(-1f)
            }
        }

        ball.collide(walls)
        renderer.use(ShapeRenderer.ShapeType.Line){
            walls.forEach { wall ->
                wall.draw(renderer)
                renderer.color = Color.WHITE
                renderer.circle(wall.body.originX, wall.body.originY, 10f)
            }
            renderer.color = Color.YELLOW
            ball.draw(renderer)
        }
    }


}