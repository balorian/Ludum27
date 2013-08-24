package com.car.l

import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.graphics.g2d.Sprite

class SplashScreen extends AbstractScreen {
  val stage: Stage = new Stage(Gdx.graphics.getWidth, Gdx.graphics.getHeight,
    false)

  def create() {
    val container = new Table
    val atlas = new TextureAtlas(Gdx.files.internal("assets/images/ui.pack"))

    stage.addActor(container)
    container.setFillParent(true)

    val image = new Image(atlas.createSprite("carmine_logo"))
    container.add(image).maxWidth(64).maxHeight(64).center
  }

  override def dispose(): Unit = {
    stage.dispose()
  }

  override def render(delta: Float): Unit = {
    stage.act(delta)
    stage.draw()
  }

  override def resize(width: Int, height: Int): Unit = {
    stage.setViewport(width, height, false)
  }
}