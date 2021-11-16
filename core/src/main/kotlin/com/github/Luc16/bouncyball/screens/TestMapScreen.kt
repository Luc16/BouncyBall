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
import com.github.Luc16.bouncyball.utils.randomColor
import com.github.Luc16.bouncyball.utils.translate
import ktx.graphics.moveTo
import ktx.graphics.use
import kotlin.math.min
import kotlin.random.Random

class TestMapScreen(game: BouncyBall): CustomScreen(game) {
    private val screenRect = Rectangle(0f, 0f, 800f, 400f)
    private val wall = PolygonRect(400f, 200f, 200f, 50f, randomColor(0.3f), 180*Random.nextFloat())

    private val camera = viewport.camera
    private val ball = PlayerBall(100f, 100f, 10f, camera)
    private var prevPos = Vector2().setZero()
    private val miniMapRatio = 0.2f

    override fun show() {
        camera.moveTo(ball.pos)
    }

    override fun render(delta: Float) {
        handleInputs()

        ball.update(delta)
        ball.bounceOfWalls(screenRect)
        ball.collideWall(wall)

//        val dir = Vector2(wall.vertices[1].y - wall.vertices[2].y, wall.vertices[2].x - wall.vertices[1].x).nor()
//        val dir = Vector2(ball.x - wall.x, ball.y - wall.y).nor()
//        if (wall.x > screenRect.width || wall.x < 0 || wall.y > screenRect.height || wall.y < 0) speed *= -1
//        if (wall.x > screenRect.width) wall.move(Vector2(screenRect.width - wall.x, 0f))
//        if (wall.x < 0) wall.move(Vector2(-wall.x, 0f))
//        if (wall.y > screenRect.width) wall.move(Vector2(0f, screenRect.height - wall.y))
//        if (wall.y < 0) wall.move(Vector2(0f, -wall.y))
//        wall.move(dir.scl(speed))

        draw()
    }

    private fun handleInputs(){
        when {
//            Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W) -> wall.move(dir.scl(4f))
//            Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S) -> wall.move(dir.scl(-4f))
            Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A) -> wall.rotate(1f)
            Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D) -> wall.rotate(-1f)
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

    private fun showMinimap(renderer: ShapeRenderer){
        val startPoint = Vector2(
            viewport.camera.position.x - viewport.worldWidth/2 + 5f,
            viewport.camera.position.y + viewport.worldHeight/2 - screenRect.height*miniMapRatio - 5f
        )
        renderer.use(ShapeRenderer.ShapeType.Filled, camera){
            renderer.color = Color.LIGHT_GRAY
            renderer.rect(startPoint.x, startPoint.y, screenRect.width*miniMapRatio, screenRect.height*miniMapRatio)
            renderer.color = Color.BLACK
            renderer.rect(startPoint.x, startPoint.y+1, screenRect.width*miniMapRatio-1, screenRect.height*miniMapRatio-1)
            renderer.color = Color.YELLOW
            renderer.circle(startPoint.x + ball.x*miniMapRatio, startPoint.y + ball.y*miniMapRatio, ball.radius*miniMapRatio)
            renderer.color = wall.color
            wall.forEachPair { v1, v2 ->
                renderer.line(
                    startPoint.x + v1.x*miniMapRatio,
                    startPoint.y + v1.y*miniMapRatio,
                    startPoint.x + v2.x*miniMapRatio,
                    startPoint.y + v2.y*miniMapRatio,
                )
            }
        }
    }

    private fun draw(){

        viewport.apply()
        renderer.use(ShapeRenderer.ShapeType.Line, camera){
            renderer.color = Color.LIGHT_GRAY
            renderer.rect(0f, 0f, screenRect.width, screenRect.height)
            wall.draw(renderer)
            renderer.color = Color.YELLOW
            ball.draw(renderer)
        }
        showMinimap(renderer)

    }

}