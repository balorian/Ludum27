package com.car.l

import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.graphics.g2d.Sprite

import com.car.l.Assets.instance.manager

class SplashScreen extends AbstractScreen {
  var done = false

  def create() {
    Assets.instance.loadAll

    val container = new Table
    val atlas = new TextureAtlas(Gdx.files.classpath("images/ui.pack"))
    stage.addActor(container)
    container.setFillParent(true)

    val image = new Image(atlas.createSprite("carmine_logo"))
    container.add(image).maxWidth(64).maxHeight(64).center
  }

  override def dispose(): Unit = {
    stage.dispose()
  }

  override def render(delta: Float): Unit = {
    manager.update

    if (manager.update() && !done) {
      done = true;
    } else if (!done) {
      // display loading information
      val progress = manager.getProgress();
      Gdx.app.debug("Splash", "Loaded " + progress * 100 + "%");
    }

    if (done) {
      //      game.transitionTo(game.adventureScreen);
    }

    stage.act(delta)
    stage.draw()
  }

  override def resize(width: Int, height: Int): Unit = {
    stage.setViewport(width, height, false)
  }
}