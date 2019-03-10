package org.central.screens

import org.central.actors.Damsel
import org.central.actors.Gravedigger
import com.badlogic.gdx.utils.Array


object GameObj {

    val gravedigger = Gravedigger()
    val damsel = Damsel()
    var selectCharacter = true

    val advanceText = "press a"

    var initialChoice = Array<String>(arrayOf("talk to gravedigger", "talk to damsel"))

    init {

    }
}