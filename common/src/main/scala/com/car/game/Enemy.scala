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

  def getEnemy(screen: LevelTestScreen): Enemy = {
    if (freeEnemies.isEmpty) new Enemy(Map("idle" -> new Animation(0.20f, assets.creatureAtlas.createSprites("skeleton"), Animation.LOOP)), screen)
    else {
      val ret = freeEnemies.pop()
      ret.setVisible(true)
      ret.speed += (random.toFloat - 0.5f) * 2
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
    set.remove(enemy)
    freeEnemies.push(enemy)
  }
}

class Enemy(animations: Map[String, Animation], var screen: LevelTestScreen) extends Entity(animations, 48, 7) {
  var speed = 2.5f
  var damage = 12
  var health = 7

  override def act(delta: Float) {
    if (health <= 0) {
      EnemyPool.returnEnemy(screen.enemySet, this)
    }

    val pc = screen.player.getCenter
    val c = getCenter
    val deltaV = new Vector2(pc._1 - c._1, pc._2 - c._2)
    deltaV.nor.scl(speed)
    scan(0.5f, new Vector2(deltaV.x, 0).nor, deltaV.len)
    scan(0.5f, new Vector2(0, deltaV.y).nor, deltaV.len)

    setRotation(deltaV.angle() + 270)

  }

  override def collides(): Boolean = {
    val spawnCol = !(screen.blockSet.forall(block => !(block.collidesWith(this)))) || !(screen.spawnSet.forall(spawn => !(spawn.collidesWith(this)))) ||
      !(screen.doorSet.forall(door => !(door.collidesWith(this)))) || !(screen.enemySet.forall(enemy => if (enemy.eq(this)) true else !(enemy.collidesWith(this))))

    (screen.level.get.collidesWith(boundingBox, Tile.WALL) || screen.level.get.collidesWith(boundingBox, Tile.WATER)) || spawnCol
  }
}