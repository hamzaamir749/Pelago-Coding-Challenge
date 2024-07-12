package com.pelagohealth.codingchallenge.presentation.components

import android.webkit.URLUtil
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.pelagohealth.codingchallenge.domain.model.Fact
import kotlinx.coroutines.launch

@Composable
fun SwipeableCard(fact: Fact, position: Int, onSwipe: (position: Int) -> Unit) {
    val coroutineScope = rememberCoroutineScope()
    val offsetX = remember { Animatable(0f) }
    var cardWidth by remember { mutableIntStateOf(0) }


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .onGloballyPositioned { coordinates ->
                cardWidth = coordinates.size.width
            }
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onHorizontalDrag = { change, dragAmount ->
                        coroutineScope.launch {
                            offsetX.snapTo(offsetX.value + dragAmount)
                        }
                        if (change.positionChange() != Offset.Zero) change.consume()
                    },
                    onDragEnd = {
                        coroutineScope.launch {
                            val swipeThreshold = cardWidth / 2
                            if (offsetX.value > swipeThreshold) {
                                onSwipe(position)
                            } else if (offsetX.value < -swipeThreshold) {
                                onSwipe(position)
                            }
                            offsetX.animateTo(0f, animationSpec = tween(durationMillis = 300))
                        }
                    }
                )
            }
            .offset { IntOffset(offsetX.value.toInt(), 0) }
    ) {
        Text(text = fact.text, modifier = Modifier.padding(8.dp))

        if (URLUtil.isValidUrl(fact.url)) {
            val localUriHandler = LocalUriHandler.current
            Text(text = fact.url, color = Color.Blue, modifier = Modifier
                .padding(8.dp)
                .clickable {
                    localUriHandler.openUri(fact.url)
                })
        } else {
            Text(text = fact.url, modifier = Modifier.padding(8.dp))
        }

    }
}