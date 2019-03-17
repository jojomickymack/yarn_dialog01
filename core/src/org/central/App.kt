package org.central

import com.badlogic.gdx.Screen
import org.central.screens.Game
import ktx.app.KtxGame


class App : KtxGame<Screen>() {

    override fun create() {
        AppObj
        val game = Game(this)

        addScreen(game)
        setScreen<Game>()
    }

    override fun dispose() {
        AppObj.dispose()
        println("all disposable memory freed")
        super.dispose()
    }
}
