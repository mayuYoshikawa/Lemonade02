/*
 * Copyright (C) 2021 The Android Open Source Project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.lemonade

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private val LEMONADE_STATE = "LEMONADE_STATE"
    private val LEMON_SIZE = "LEMON_SIZE"
    private val SQUEEZE_COUNT = "SQUEEZE_COUNT"

    // レモンを選ぶ状態
    private val SELECT = "select"

    // レモンを搾る状態
    private val SQUEEZE = "squeeze"

    // レモネードを飲む状態
    private val DRINK = "drink"

    // グラスが空の状態
    private val RESTART = "restart"

    // 選択されている状態
    private var lemonadeState = "select"

    // ランダムな数字を格納する為の変数
    private var lemonSize = -1

    // レモンを搾った回数
    private var squeezeCount = -1

    // レモネードを何回作ったか
    private var lemonadeCount = 0

    private var lemonTree = LemonTree()
    private var lemonImage: ImageView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // === DO NOT ALTER THE CODE IN THE FOLLOWING IF STATEMENT ===
        if (savedInstanceState != null) {
            lemonadeState = savedInstanceState.getString(LEMONADE_STATE, "select")
            lemonSize = savedInstanceState.getInt(LEMON_SIZE, -1)
            squeezeCount = savedInstanceState.getInt(SQUEEZE_COUNT, -1)
        }
        // === END IF STATEMENT ===
        lemonImage = findViewById(R.id.image_lemon_state)
        setViewElements()

        // 画像がクリックされたら、メソッドを呼び出す
        lemonImage!!.setOnClickListener {
            clickLemonImage()
        }
        // 画像が長押しされたらメソッドを呼び出す
        lemonImage!!.setOnLongClickListener {
            showSnackbar()
            false
        }

        // ボタンのオブジェクト参照のため、変数に格納
        var countButton = findViewById<Button>(R.id.CountButton)
        var skipButton = findViewById<Button>(R.id.SkipButton)

        //カウントボタンがクリックされたらテキストを表示させる
        countButton.setOnClickListener {
            val countText: TextView = findViewById(R.id.countText)
            countText.text = "${lemonadeCount}回飲んだよ"
        }
        //skipボタンがクリックされたらDRINKの画面に遷移する
        skipButton.setOnClickListener {
            lemonSize = 1
            lemonadeState = SQUEEZE
            clickLemonImage()
        }
    }

    /**
     * === DO NOT ALTER THIS METHOD ===
     * This method saves the state of the app if it is put in the background.
     */
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(LEMONADE_STATE, lemonadeState)
        outState.putInt(LEMON_SIZE, lemonSize)
        outState.putInt(SQUEEZE_COUNT, squeezeCount)
        super.onSaveInstanceState(outState)
    }

    private fun clickLemonImage() {

        when (lemonadeState) {

            //ランダムな数字をlemonSizeに格納、レモン搾った回数をリセットし、
            //lemonadeStateを更新する
            SELECT -> {
                lemonSize = lemonTree.pick()
                squeezeCount = 0
                lemonadeState = SQUEEZE
            }
            //レモンを絞った回数(squeezeCount)を増やし、ランダムな数字(lemonSize)を減らす
            SQUEEZE -> {
                squeezeCount++
                lemonSize--
                //lemonSizeが0の場合のみlemonadeStateを更新
                if (lemonSize == 0) {
                    lemonadeState = DRINK
                }
            }
            //lemonSizeをリセット、何回飲んだかのlemonadeCountを増やし、lemonadeStateを更新
            DRINK -> {
                lemonSize = -1
                lemonadeCount++
                lemonadeState = RESTART
            }
            //lemonadeStateを更新
            RESTART -> {
                lemonadeState = SELECT
            }
        }
        //メソッド呼び出す
        setViewElements()
    }

    private fun setViewElements() {
        //テキストと画像のオブジェクト参照のため変数に格納
        val textAction: TextView = findViewById(R.id.text_action)
        val lemonImage: ImageView = findViewById(R.id.image_lemon_state)

        //lemonadeStateに対応した文字と画像を表示する
        when (lemonadeState) {
            SELECT -> {
                textAction.setText(R.string.lemon_select)
                lemonImage.setImageResource(R.drawable.lemon_tree)
            }
            SQUEEZE -> {
                textAction.setText(R.string.lemon_squeeze)
                lemonImage.setImageResource(R.drawable.lemon_squeeze)
            }
            DRINK -> {
                textAction.setText(R.string.lemon_drink)
                lemonImage.setImageResource(R.drawable.lemon_drink)
            }
            RESTART -> {
                textAction.setText(R.string.lemon_empty_glass)
                lemonImage.setImageResource(R.drawable.lemon_restart)
            }
        }
    }

    /**
     * === DO NOT ALTER THIS METHOD ===
     * Long clicking the lemon image will show how many times the lemon has been squeezed.
     */
    private fun showSnackbar(): Boolean {
        if (lemonadeState != SQUEEZE) {
            return false
        }
        val squeezeText = getString(R.string.squeeze_count, squeezeCount)
        Snackbar.make(
            findViewById(R.id.constraint_Layout),
            squeezeText,
            Snackbar.LENGTH_SHORT
        ).show()
        return true
    }
}

//ランダムな数字生成
class LemonTree {
    fun pick(): Int {
        return (2..4).random()
    }
}