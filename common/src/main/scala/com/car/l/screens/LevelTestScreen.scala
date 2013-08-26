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
import com.car.game.Entity
import com.car.game.SoulShard
import scala.util.Random
import com.car.game.Meat
import com.car.game.Enemy
import scala.collection.mutable.HashSet
import com.car.game.BreakBlock

class LevelTestScreen(game: LudumGame) extends AbstractScreen(game: LudumGame) {
  val LOG_TAG = "LevelTestScreen"

  lazy val ui = new GameUI(player)

  var level: Option[Level] = None
  val player = new Player(Map("walk" -> new Animation(0.10f, assets.creatureAtlas.createSprites("walk_u"), Animation.LOOP),
    "idle" -> new Animation(1, assets.creatureAtlas.createSprite("walk_u", 2))), this)
  lazy val sprite = Assets.assets.tileAtlas.createSprite("bg")
  lazy val bgImage = new Image(sprite)
  val bg = Assets.assets.tileAtlas.createSprite("bg")

  val collectablesSet: HashSet[Entity] = HashSet.empty
  val doorSet: HashSet[Entity] = HashSet.empty
  val blockSet: HashSet[BreakBlock] = HashSet.empty
  val spawnSet: HashSet[SpawnPoint] = HashSet.empty

  def setLevel(key: String) {
    def clearLists() {
      spawnSet.clear
      collectablesSet.clear
      doorSet.clear
      blockSet.clear
    }

    clearLists

    level = Some(LevelLoader.load(key, this))
    player.newLevel(level.get)
    cameraControl

    stage.clear()
    stage.addActor(bgImage)
    stage.addActor(level.get)
    spawnSet foreach (stage.addActor(_))
    blockSet foreach (stage.addActor(_))
    collectablesSet foreach (stage.addActor(_))
    doorSet foreach (stage.addActor(_))
    stage.addActor(player)
  }

  override def inputProcessor = {
    val plex = new InputMultiplexer()
    plex.addProcessor(stage)
    plex.addProcessor(new PlayerProcessor(player))
    plex
  }

  def cameraControl() {
    stage.getCamera().position.set(player.getX(), player.getY, 0)
    bgImage.setPosition(player.getX() - sprite.getWidth() / 2, player.getY - sprite.getHeight() / 2)
    stage.getCamera().asInstanceOf[OrthographicCamera].zoom = 0.75f
    stage.getCamera().update()
  }

  override def render(delta: Float) {
    gl.glClearColor(0, 0, 0, 1)
    gl.glClear(GL10.GL_COLOR_BUFFER_BIT)

    if (justDraw) { stage.draw(); ui.render(delta); return }

    stage.act()
    cameraControl()
    stage.draw()

    ui.update
    ui.render(delta)
  }

  def spawnOnPoint(point: SpawnPoint) {
    val e = new Enemy(Map("idle" -> new Animation(0.20f, assets.creatureAtlas.createSprites("skeleton"), Animation.LOOP)), this)
    e.setPosition(point.getX(), point.getY())
    stage.addActor(e)
    Gdx.app.debug(LOG_TAG, "Spawn on " + point.getX() + ", " + point.getY())
  }

  def createSoulShard(x: Float, y: Float) = {
    val ss = new SoulShard(Map("idle" -> new Animation(0.20f, assets.creatureAtlas.createSprites("soul_shard"), Animation.LOOP)), this)
    ss.setPosition(x, y)
    collectablesSet.add(ss)
    stage.addActor(ss)
  }

  def createMeat(x: Float, y: Float) = {
    val meat = new Meat(Map("idle" -> new Animation(0.20f, assets.creatureAtlas.createSprites("meat"), Animation.LOOP)), this)
    meat.setPosition(x, y)
    collectablesSet.add(meat)
    stage.addActor(meat)
  }

  def collidesWithBlock(player: Player): Boolean = {
    var r = false
    doorSet foreach (block => if (block.collidesWith(player)) { r = true; block.collidedWith(player) })
    r
  }
}