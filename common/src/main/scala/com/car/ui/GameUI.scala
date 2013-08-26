package com.car.ui

import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.Gdx.graphics
import com.badlogic.gdx.graphics.Color
import com.car.l.Assets.assets
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.car.game.Player
import com.badlogic.gdx.scenes.scene2d.ui.Image

object GameUI {
  val LEVEL = "Level - "
  val SCORE = "Score - "
  val KEYS = "x"
}

class GameUI(val player: Player) {
  lazy val stage: Stage = new Stage(graphics.getWidth, graphics.getHeight,
    false)

  val font = assets.font

  val spiritBar = new StateBar(10, 10, 64, graphics.getHeight() - 20, new Color(1f, 0xFF / 0x13, 0xFF / 0x7F, 1))
  val healthBar = new StateBar(graphics.getWidth() - 74, 10, 64, graphics.getHeight() - 20, new Color(0.75f, 0, 0, 1))

  val scoreLabel = new Label(GameUI.SCORE + "0", assets.skin)
  scoreLabel.setPosition(100, graphics.getHeight() - 50)

  val levelLabel = new Label(GameUI.LEVEL + "1", assets.skin)
  levelLabel.setPosition(graphics.getWidth() - 100f - font.getBounds(levelLabel.getText()).width, graphics.getHeight() - 50)

  val keyImage = new Image(assets.uiAtlas.createSprite("key"))
  keyImage.setPosition(graphics.getWidth() / 2 - 16, 10)
  val keyLabel= new Label(GameUI.KEYS + "0", assets.skin)
  keyLabel.setPosition(graphics.getWidth() / 2, 10)

  stage.addActor(spiritBar)
  stage.addActor(healthBar)
  stage.addActor(scoreLabel)
  stage.addActor(levelLabel)
  stage.addActor(keyImage)
  stage.addActor(keyLabel)

  def update() {
    levelLabel.setText(GameUI.LEVEL + "1")
    scoreLabel.setText(GameUI.SCORE + player.score)
    keyLabel.setText(GameUI.KEYS + player.keys)

    spiritBar.ratio = player.currentSpirit / player.maxSpirit
    healthBar.ratio = player.currentHealth.toFloat / player.maxHealth.toFloat
  }

  def render(delta: Float) {
    stage.act(delta)
    stage.draw()

  }
}