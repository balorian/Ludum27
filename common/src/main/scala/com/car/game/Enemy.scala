package com.car.game

import com.badlogic.gdx.graphics.g2d.Animation
import com.car.l.screens.LevelTestScreen
import com.badlogic.gdx.math.Vector2
import scala.collection.mutable.Stack
import scala.collection.mutable.HashSet
import com.car.l.Assets.assets
import scala.math.random
import scala.util.Random
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.graphics.g2d.SpriteBatch

object EnemyPool {
  var freeEnemies: Stack[Enemy] = new Stack[Enemy]()
  val random = new Random

  def getEnemy(screen: LevelTestScreen, enemyType: Symbol): Enemy = {
    if (freeEnemies.isEmpty) {
      new Enemy(Map("skeleton" -> new Animation(0.20f, assets.creatureAtlas.createSprites("skeleton"), Animation.LOOP),
        "ghost" -> new Animation(0.20f, assets.creatureAtlas.createSprites("ghost"), Animation.LOOP),
        "zombie" -> new Animation(0.20f, assets.creatureAtlas.createSprites("zombie"))), screen, enemyType)
    } else {
      val ret = freeEnemies.pop()
      ret.setVisible(true)
      ret.enemyType = enemyType
      ret.setType(enemyType)
      screen.stage.addActor(ret)
      screen.enemySet.add(ret)
      ret
    }
  }

  def returnEnemy(set: HashSet[Enemy], enemy: Enemy) {
    enemy.health = 7
    enemy.speed = 2.5f
    enemy.setVisible(false)
    enemy.remove
    enemy.setPosition(0, 0)
    set.remove(enemy)
    freeEnemies.push(enemy)
  }

  def drop(screen: LevelTestScreen, x: Float, y: Float) {
    if (random.nextFloat <= 0.03) { screen.createPotion(x, y) }
    else screen.createSoulShard(x, y)
  }
}

class Enemy(animations: Map[String, Animation], var screen: LevelTestScreen, var enemyType: Symbol) extends Entity(animations, 48, 7) {
  var speed: Float = 0
  var damage: Int = 0
  var health: Int = 0
  var shotTimer: Float = 10f
  setType(enemyType)
  screen.enemySet.add(this)
  screen.stage.addActor(this)

  def setType(enemyType: Symbol) {
    enemyType match {
      case 'skeleton => swapAnimation("skeleton")
      case 'zombie => swapAnimation("zombie")
      case 'ghost => swapAnimation("ghost")
    }
    speed = if (enemyType == 'skeleton) 2.5f else if (enemyType == 'zombie) 1.5f else 2f
    damage = if (enemyType == 'skeleton) 9 else if (enemyType == 'zombie) 6 else 14
    health = if (enemyType == 'skeleton) 7 else if (enemyType == 'zombie) 14 else 9 
    shotTimer = 10f - random.toFloat * 5
  }

  override def act(delta: Float) {
    super.act(delta)
    if (health <= 0) {
      EnemyPool.drop(screen, getX(), getY())
      EnemyPool.returnEnemy(screen.enemySet, this)
      if (enemyType == 'skeleton)
        screen.player.score += 10
      else if(enemyType == 'zombie)
        screen.player.score += 20
        else
          screen.player.score += 40
    }

    val pc = screen.player.getCenter
    val c = getCenter
    val deltaV = new Vector2(pc._1 - c._1, pc._2 - c._2)
    deltaV.nor.scl(speed)
    scan(0.5f, new Vector2(deltaV.x, 0).nor, deltaV.len)
    scan(0.5f, new Vector2(0, deltaV.y).nor, deltaV.len)

    setRotation(deltaV.angle() + 270)
    
    if(enemyType == 'ghost){
      shotTimer -= delta
      def spawnShot() {
      val shot = GhostShotPool.getShot(screen)
      val shotPos = new Vector2(getX + 48 / 2f, getY + 48 / 2f).add(deltaV.nor.scl(27f))
      shot.setPosition(shotPos.x, shotPos.y)
      shot.deltaV = deltaV.nor.scl(speed * 2)
      }
      
      if(shotTimer < 0){
        spawnShot()
        shotTimer = 10f - random.toFloat * 5
      }
    }
  }

  override def collides(): Boolean = {
    val spawnCol = !(screen.blockSet.forall(block => !(block.collidesWith(this)))) || !(screen.spawnSet.forall(spawn => !(spawn.collidesWith(this)))) ||
      !(screen.doorSet.forall(door => !(door.collidesWith(this)))) || !(screen.enemySet.forall(enemy => if (enemy.eq(this)) true else !(enemy.collidesWith(this))))

    (screen.level.get.collidesWith(boundingBox, Tile.WALL) || screen.level.get.collidesWith(boundingBox, Tile.WATER)) || spawnCol
  }
}

object GhostShotPool{
  var freeShots: Stack[GhostShot] = new Stack[GhostShot]()
  
  def getShot(screen: LevelTestScreen): GhostShot = {
    if(freeShots.isEmpty) new GhostShot(screen, assets.creatureAtlas.findRegion("ghost_ball", 1))
    else {
      val ret = freeShots.pop()
      ret.setVisible(true)
      screen.stage.addActor(ret)
      ret
    }
  }
  
  def returnShot(shot: GhostShot){
    shot.deltaV = new Vector2(0, 0)
    shot.spinCounter = 0
    shot.setVisible(false)
    shot.remove
    freeShots.push(shot)
  }
}

class GhostShot(screen: LevelTestScreen, image: TextureRegion) extends Actor{
  val damage = 5
  var deltaV = new Vector2(0, 0)
  var spinCounter = 0
  screen.stage.addActor(this)
  
  override def act(delta: Float){
    setPosition(getX + deltaV.x, getY + deltaV.y)
    
    if(screen.player.contains(getX, getY)) {screen.player.modHealth(damage); GhostShotPool.returnShot(this)}
    screen.spawnSet.foreach(spoint => if (spoint.contains(getX, getY)) {GhostShotPool.returnShot(this)})
    screen.blockSet.foreach(block => if (block.contains(getX, getY)) {GhostShotPool.returnShot(this)})
    screen.doorSet.foreach(door => if (door.contains(getX, getY)) {GhostShotPool.returnShot(this)})
    
    if(screen.level.get.collidesWith(getX+image.getRegionWidth/2, getY+image.getRegionHeight/2, Tile.WALL)){
      GhostShotPool.returnShot(this)
    }
    spinCounter += 1
    setRotation(spinCounter * 45)
  }
  
  override def draw(batch: SpriteBatch, parentAlpha: Float){
    val center = image.getRegionWidth/2
    batch.draw(image.getTexture, getX-center, getY-center, image.getRegionWidth/2, image.getRegionHeight/2, image.getRegionWidth, image.getRegionHeight, 1, 1,
    		 	getRotation, image.getRegionX, image.getRegionY, image.getRegionWidth, image.getRegionHeight, false, false)
  }
  
}