package com.car.l.screens

import com.car.l.LudumGame
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Pixmap.Format
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.Gdx.gl
import com.badlogic.gdx.graphics.GL10
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction
import com.badlogic.gdx.scenes.scene2d.actions.Actions._
import com.badlogic.gdx.scenes.scene2d.Action

class TransitionScreen(game: LudumGame) extends AbstractScreen(game) {
  val FADE_SPEED = 0.15f
  val FADE_DELAY = 0.05f

  var current: Option[AbstractScreen] = None
  var next: Option[AbstractScreen] = None
  var toDraw: Option[AbstractScreen] = None

  lazy val texture: Texture = {
    val pixmap = new Pixmap(640, 480, Format.RGBA8888);
    pixmap.setColor(0, 0, 0, 1f);
    pixmap.fill();
    new Texture(pixmap);
  }

  def reset() {
    toDraw = current
    stage.clear()

    val image = new Image(texture)

    image.addAction(sequence(fadeOut(0f), fadeIn(FADE_SPEED),
      delay(FADE_DELAY), run(new Runnable() {
        def run() {
          current.get.pause()
          next.get.resume()
          toDraw = next
        }
      }), fadeOut(FADE_SPEED), run(new Runnable() {
        def run() {
          game.transitionComplete()
        }
      })));

    image.setFillParent(true)
    stage.addActor(image)
  }

  override def render(delta: Float) {
    gl.glClearColor(0, 0, 0, 1);
    gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

    toDraw.get.render(delta)

    stage.act(delta)
    stage.draw()
  }
}
