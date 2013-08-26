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
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Pixmap.Format
import com.badlogic.gdx.graphics.Texture

class StateBar(val pX: Float, val pY: Float, val pWidth: Float, val pHeight: Float, val pColor: Color) extends Widget {
  val lineTex = {
    val pixmap = new Pixmap(1, 1, Format.RGBA8888)
    pixmap.setColor(0, 0, 0, 1f)
    pixmap.fill()
    new Texture(pixmap)
  }

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

    batch.setColor(pColor)
    liquid.draw(batch, pX, pY, pWidth, MathUtils.clamp(pHeight * ratio, 32, pHeight))
    batch.setColor(Color.WHITE)

    batch.draw(surface, pX + (pWidth - surface.getWidth()) / 2, MathUtils.clamp(pY + pHeight * ratio - 16, 32, pHeight));

    top.draw(batch, pX, pY, pWidth, pHeight)

    for (i <- 1 to 9) {
      batch.draw(lineTex, getX + 8, getY + i * 45, 8 + (1 - i%2) * 8, 3)
    }
  }

  override def getPrefWidth() = pWidth
  override def getPrefHeight() = pHeight
}