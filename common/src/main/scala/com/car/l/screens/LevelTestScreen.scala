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
import com.car.game.EnemyPool
import com.car.game.Potion
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.actions.Actions._
import com.car.game.PowerPotion

class LevelTestScreen(game: LudumGame) extends AbstractScreen(game: LudumGame) {
  val LOG_TAG = "LevelTestScreen"

  lazy val ui = new GameUI(player)

  var level: Option[Level] = None
  val player = new Player(Map("walk" -> new Animation(0.05f, assets.creatureAtlas.createSprites("walk_u"), Animation.LOOP),
    "idle" -> new Animation(1, assets.creatureAtlas.createSprite("walk_u", 2)),
    "throw" -> new Animation(0.05f, assets.creatureAtlas.createSprites("throw"))), this)
  lazy val sprite = Assets.assets.tileAtlas.createSprite("bg")
  lazy val bgImage = new Image(sprite)
  val bg = Assets.assets.tileAtlas.createSprite("bg")

  val collectablesSet: HashSet[Entity] = HashSet.empty
  val doorSet: HashSet[Entity] = HashSet.empty
  val blockSet: HashSet[BreakBlock] = HashSet.empty
  val spawnSet: HashSet[SpawnPoint] = HashSet.empty
  val enemySet: HashSet[Enemy] = HashSet.empty

  var time = 0f

  def setLevel(key: String) {
    def clearLists() {
      spawnSet.clear
      collectablesSet.clear
      doorSet.clear
      blockSet.clear
      enemySet.clear
    }

    enemySet.foreach(enemy => EnemyPool.returnEnemy(enemySet, enemy))
    clearLists

    level = Some(LevelLoader.load(key, this))
    player.newLevel(level.get)
    ui.update
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
    time += delta

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
    val e = if (point.enemyType == 'skeleton) EnemyPool.getEnemy(this, 'skeleton) else if (point.enemyType == 'ghost) EnemyPool.getEnemy(this, 'ghost) else EnemyPool.getEnemy(this, 'zombie)
    e.setPosition(point.getX - 48, point.getY)
    if (e collides) {
      e.setPosition(point.getX(), point.getY() - 48)
      if (e collides) {
        e.setPosition(point.getX() + 48, point.getY())
        if (e collides) {
          e.setPosition(point.getX(), point.getY() + 48)
          if (e collides) {
            EnemyPool.returnEnemy(enemySet, e)
          }
        }
      }
    }
  }

  def createPotion(x: Float, y: Float) = {
    val pot = new Potion(Map("idle" -> new Animation(0.20f, assets.creatureAtlas.createSprites("potion"), Animation.LOOP_PINGPONG)), this)
    pot.setPosition(x, y)
    collectablesSet.add(pot)
    stage.addActor(pot)
  }

  def createPowerPotion(x: Float, y: Float) = {
    val pot = new PowerPotion(Map("idle" -> new Animation(0.20f, assets.creatureAtlas.createSprites("potion_red"), Animation.LOOP_PINGPONG)), this)
    pot.setPosition(x, y)
    collectablesSet.add(pot)
    stage.addActor(pot)
  }
  
  def createSoulShard(x: Float, y: Float) = {
    val ss = new SoulShard(Map("idle" -> new Animation(0.20f, assets.creatureAtlas.createSprites("soul"), Animation.LOOP_PINGPONG)), this)
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

  def displayMessage(text: String, x: Float, y: Float) {
    println("text to display " + text + " " + x + "," + y)
    
    val msg = new Label(text, assets.skin)
    msg.setPosition(x - msg.getWidth()/2, y)
    val msgTime = 6f
    msg.addAction(sequence(parallel(fadeOut(time), moveTo(x- msg.getWidth()/2, y + 100, time))))
    stage.addActor(msg)
  }
}