package com.car.game

import com.badlogic.gdx.graphics.g2d.Animation
import com.car.l.screens.LevelTestScreen
import com.badlogic.gdx.math.Vector2
import scala.collection.mutable.Stack
import scala.collection.mutable.HashSet
import com.car.l.Assets.assets
import scala.math.random

object EnemyPool {
  var freeEnemies: Stack[Enemy] = new Stack[Enemy]()

  def getEnemy(screen: LevelTestScreen, enemyType: Symbol): Enemy = {
    if (freeEnemies.isEmpty) {
      new Enemy(Map("skeleton" -> new Animation(0.20f, assets.creatureAtlas.createSprites("skeleton"), Animation.LOOP),
                    "ghost" -> new Animation(0.20f, assets.creatureAtlas.createSprites("ghost"), Animation.LOOP)), screen, enemyType)
    }
    else {
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
}

class Enemy(animations: Map[String, Animation], var screen: LevelTestScreen, var enemyType: Symbol) extends Entity(animations, 48, 7) {
  var speed: Float = 0
  var damage: Int = 0
  var health: Int = 0
  setType(enemyType)
  screen.enemySet.add(this)
  screen.stage.addActor(this)

  def setType(enemyType: Symbol){
	enemyType match{
	  case 'skeleton => swapAnimation("skeleton")
	  case 'ghost => swapAnimation("ghost")
	}
    speed = if(enemyType == 'skeleton) 2.5f else 1.5f
    damage = if(enemyType == 'skeleton) 4 else 9
    health = if(enemyType == 'skeleton) 7 else 14
  }
  
  override def act(delta: Float) {
    super.act(delta)
    if (health <= 0) {
      EnemyPool.returnEnemy(screen.enemySet, this)
      screen.createSoulShard(getX, getY)
      if(enemyType == 'skeleton)
        screen.player.score += 10
      else
        screen.player.score += 20
    }

    val pc = screen.player.getCenter
    val c = getCenter
    val deltaV = new Vector2(pc._1 - c._1, pc._2 - c._2)
    deltaV.nor.scl(speed)
    scan(0.5f, new Vector2(deltaV.x, 0).nor, deltaV.len)
    scan(0.5f, new Vector2(0, deltaV.y).nor, deltaV.len)

    setRotation((deltaV.angle() + 270).round/2 * 2)

  }

  override def collides(): Boolean = {
    val spawnCol = !(screen.blockSet.forall(block => !(block.collidesWith(this)))) || !(screen.spawnSet.forall(spawn => !(spawn.collidesWith(this)))) ||
      !(screen.doorSet.forall(door => !(door.collidesWith(this)))) || !(screen.enemySet.forall(enemy => if (enemy.eq(this)) true else !(enemy.collidesWith(this))))

    (screen.level.get.collidesWith(boundingBox, Tile.WALL) || screen.level.get.collidesWith(boundingBox, Tile.WATER)) || spawnCol
  }
}