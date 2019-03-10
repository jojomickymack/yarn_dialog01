package org.central.dialog

import com.badlogic.gdx.scenes.scene2d.Action
import com.badlogic.gdx.scenes.scene2d.ui.Label
import org.central.AppObj


class TypewriterAct(textToDisplay: String) : Action() {
    private var text = textToDisplay
    private var elapsedTime = 0f
    val rate= 30f
    var completed = false

    init {

    }

    override fun act(delta: Float): Boolean {
        elapsedTime += delta
        val label = target as Label

        if (!this.completed) {
            var chars = (elapsedTime * rate).toInt()
            if (chars > text.length) chars = text.length
            val partialText = text.substring(0, chars)
            label.setText(partialText)
            this.completed = chars >= text.length
            AppObj.dialogSnd.play()
        } else label.setText(text)
        return false
    }
}