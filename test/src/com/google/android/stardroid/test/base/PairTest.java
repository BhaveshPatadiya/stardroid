// Copyright 2008 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.android.stardroid.test.base;

import com.google.android.stardroid.base.Pair;

import junit.framework.TestCase;

/**
 * Unit tests for the Pair class.
 * 
 * @author Brent Bryan
 */
public class PairTest extends TestCase {

  public void testCreatePair() {
    Pair<String, Double> p = new Pair<String, Double>("ThreePointOne", 3.1);
    assertEquals("ThreePointOne", p.getFirst());
    assertEquals(3.1, p.getSecond().doubleValue());
  }
  
  public void testOf() {
    Pair<Integer, String> p = Pair.of(3, "Three");
    assertEquals(3, p.getFirst().intValue());
    assertEquals("Three", p.getSecond());
  } 

  public void testNull() {
    Pair<Integer, String> p = Pair.of(null, null);
    assertEquals(null, p.getFirst());
    assertEquals(null, p.getSecond());
  }
  
  public void testSetters() {
    Pair<Integer, Double> p = Pair.of(null, null);

    p.setFirst(2);
    assertEquals(2, p.getFirst().intValue());
    assertEquals(null, p.getSecond());
    
    p.setSecond(3.4);
    assertEquals(2, p.getFirst().intValue());
    assertEquals(3.4, p.getSecond().doubleValue());
  }
}
