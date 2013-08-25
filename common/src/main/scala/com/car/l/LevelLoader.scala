package com.car.l

import com.car.l.Assets.assets
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.Gdx
import scala.collection.convert.Wrappers._
import com.car.game.Level
import com.car.game.Tile

object LevelLoader {
  val LOG_TAG = "LevelLoader"
  
  val MAPS_DIR = "maps/"
  val MAP_SUFFIX = ".png"

  val KEY = 0xFFD800FF
  val DOOR = 0x7D7D00FF
  val SPAWN_1 = 0x4CFF00FF
  val SPAWN_2 = 0x287D00FF
  val BREAKABLE_WALL = 0xD67FFFFF

  def load(key: String): Level = {
    def spawnKey(i: Int, j: Int) = Gdx.app.debug(LOG_TAG, "Key at " + i + "/" + j)
    def spawnDoor(i: Int, j: Int) = Gdx.app.debug(LOG_TAG, "Door at " + i + "/" + j)
    def spawnPoint1(i: Int, j: Int) = Gdx.app.debug(LOG_TAG, "Point1 at " + i + "/" + j)
    def spawnPoint2(i: Int, j: Int) = Gdx.app.debug(LOG_TAG, "Point2 at " + i + "/" + j)
    def spawnBreakable(i: Int, j: Int) = Gdx.app.debug(LOG_TAG, "Breakable at " + i + "/" + j)

    val levelData = new Pixmap(Gdx.files.classpath(MAPS_DIR + key + MAP_SUFFIX));
    val width = levelData.getWidth()
    val height = levelData.getHeight()
    val tilemap = new Array[Int](width * height)

    for (j <- 0 until height; i <- 0 until width) {
      val pixel = levelData.getPixel(i, j)

      def flip(j: Int) = height - (j+1)
      val index = flip(j) * width + i
      
      pixel match {
        case KEY =>
          tilemap(index) = Tile.GROUND; spawnKey(i, j)
        case DOOR =>
          tilemap(index) = Tile.GROUND; spawnDoor(i, j)
        case SPAWN_1 =>
          tilemap(index) = Tile.GROUND; spawnPoint1(i, j)
        case SPAWN_2 =>
          tilemap(index) = Tile.GROUND; spawnPoint2(i, j)
        case BREAKABLE_WALL =>
          tilemap(index) = Tile.GROUND; spawnBreakable(i, j)
        case _ => tilemap(index) = pixel
      }
    }

    new Level(width, height, tilemap)
  }
}

class LevelLoader {

}