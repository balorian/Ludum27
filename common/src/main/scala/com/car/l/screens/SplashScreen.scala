package com.car.l.screens

import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.car.l.Assets
import com.car.l.Assets.assets.manager
import com.car.l.LudumGame

class SplashScreen(game: LudumGame) extends AbstractScreen(game) {
  var done = false
  var drawOnly = false

  def create() {
    Assets.assets.loadAll

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
      Gdx.app.debug("Splash", "Loading done!");
    } else if (!done) {
      // display loading information
      val progress = manager.getProgress();
      Gdx.app.debug("Splash", "Loaded " + progress * 100 + "%");
    }

    if (done && !drawOnly) {
      drawOnly = true
      this.game.transitionToScreen(this.game.testScreen)
    }

    super.render(delta)
  }
}