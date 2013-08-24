package com.car.ui

import com.badlogic.gdx.scenes.scene2d.ui.Widget
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.Gdx.gl
import com.car.l.Assets.assets
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Sprite
import scala.util.Random
import com.badlogic.gdx.math.MathUtils

class StateBar(val pX: Float, val pY: Float, val pWidth: Float, val pHeight: Float, val pColor: Color) extends Widget {
  lazy val top: NinePatch = assets.uiAtlas.createPatch("bar_top")
  lazy val liquid: NinePatch = assets.uiAtlas.createPatch("bar_liquid")
  lazy val surface: Sprite = assets.uiAtlas.createSprite("liquid_surface")

  setX(pX)
  setY(pY)

  var ratio = 1f
  var stateTime = 0f

  override def act(delta: Float) {
    stateTime += delta
  }

  override def draw(batch: SpriteBatch, parentAlpha: Float) {
    super.draw(batch, parentAlpha)

    ratio = MathUtils.clamp(1f - stateTime / 10f, 0, 1)

    batch.setColor(pColor)
    liquid.draw(batch, pX, pY, pWidth, MathUtils.clamp(pHeight * ratio, 32, pHeight))
    batch.setColor(Color.WHITE)

    batch.draw(surface, pX + (pWidth - surface.getWidth()) / 2, MathUtils.clamp(pY + pHeight * ratio - 16, 32, pHeight));

    top.draw(batch, pX, pY, pWidth, pHeight)
  }

  override def getPrefWidth() = pWidth
  override def getPrefHeight() = pHeight
}