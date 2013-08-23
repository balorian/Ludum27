package com.car.l.tests

import com.car.l._
import junit.framework.Assert._
import _root_.android.test.AndroidTestCase

class AndroidTests extends AndroidTestCase {
  def testPackageIsCorrect() {
    assertEquals("com.car.l", getContext.getPackageName)
  }
}
