package com.car.l.screens

import com.badlogic.gdx.Game
import com.car.l.LudumGame
import com.car.game.Player
import com.badlogic.gdx.graphics.g2d.Animation
import com.car.l.Assets.assets
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.InputProcessor
import com.car.game.PlayerProcessor
import com.car.game.Level
import com.car.ui.GameUI
import com.car.l.LevelLoader
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Gdx.gl
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.car.l.Assets
import com.badlogic.gdx.graphics.GL10
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.car.game.SpawnPoint

import scala.collection.mutable.ListBuffer

class LevelTestScreen(game: LudumGame) extends AbstractScreen(game: LudumGame) {
  val LOG_TAG = "LevelTestScreen"

  lazy val ui = new GameUI
  var level: Option[Level] = None
  val player = new Player(Map("walk" -> new Animation(0.10f, assets.creatureAtlas.createSprites("walk_u"), Animation.LOOP),
    "idle" -> new Animation(1, assets.creatureAtlas.createSprite("walk_u", 2))), this)
  lazy val sprite = Assets.assets.tileAtlas.createSprite("bg")
  lazy val bgImage = new Image(sprite)
  val bg = Assets.assets.tileAtlas.createSprite("bg")

  val spawnList: ListBuffer[SpawnPoint] = new ListBuffer

  def setLevel(key: String) {
    def clearLists() {
      spawnList.dropRight(0)
    }

    level = Some(LevelLoader.load(key, this))

    stage.clear()
    stage.addActor(bgImage)
    stage.addActor(level.get)
    spawnList foreach (stage.addActor(_))
    stage.addActor(player)
  }

  override def into() {
    Gdx.app.debug(LOG_TAG, "GOING TO LEVEL 1")
    setLevel("level1")
  }

  override def inputProcessor = {
    val plex = new InputMultiplexer()
    plex.addProcessor(stage)
    plex.addProcessor(new PlayerProcessor(player))
    plex
  }

  override def render(delta: Float) {
    gl.glClearColor(0, 0, 0, 1)
    gl.glClear(GL10.GL_COLOR_BUFFER_BIT)

    //    spawnList foreach (sp => sp.act(delta))
    stage.act()
    cameraControl()
    stage.draw()
    ui.render(delta)

    def cameraControl() {
      stage.getCamera().position.set(player.getX(), player.getY, 0)
      bgImage.setPosition(player.getX() - sprite.getWidth() / 2, player.getY - sprite.getHeight() / 2)
      stage.getCamera().asInstanceOf[OrthographicCamera].zoom = 0.75f
      stage.getCamera().update()
    }
  }

  def spawnOnPoint(point: SpawnPoint) {
    Gdx.app.debug(LOG_TAG, "Spawn on " + point.getX() + ", " + point.getY())
  }
}