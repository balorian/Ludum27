package com.car.l.screens

import com.car.l.LudumGame
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.Gdx.graphics
import com.car.l.Assets.assets
import com.badlogic.gdx.scenes.scene2d.actions.Actions._
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Image

class MenuScreen(game: LudumGame) extends AbstractScreen(game) {
  val label = new Label("SOUL REAVER", assets.skin)
  label.setPosition(graphics.getWidth() / 2 - label.getWidth() / 2, 350)

  val labelMovement = new Label("Movement", assets.skin)
  labelMovement.setPosition(80, 300)

  val labelShooting = new Label("Shooting", assets.skin)
  labelShooting.setPosition(graphics.getWidth() - 80 - labelShooting.getWidth(), 300)

  val labelPotion = new Label("Potion", assets.skin)
  labelPotion.setPosition(graphics.getWidth() / 2 - labelPotion.getWidth() / 2, 180)

  val image = new Image(assets.uiAtlas.createSprite("controls"))
  image.setPosition(graphics.getWidth() / 2 - image.getWidth() / 2, 200)

  val button = new TextButton("START", assets.skin)
  button.setPosition(graphics.getWidth() / 2 - button.getWidth() / 2, 80)

  stage.addActor(label)
  stage.addActor(labelMovement)
  stage.addActor(labelShooting)
  stage.addActor(labelPotion)
  stage.addActor(image)
  stage.addActor(button)

  button.addListener(new ChangeListener {
    override def changed(event: ChangeEvent, actor: Actor) {
      game.transitionToScreen(game.testScreen)
      assets.playSound("button")
      assets.playSound("steps")
    }
  })

}