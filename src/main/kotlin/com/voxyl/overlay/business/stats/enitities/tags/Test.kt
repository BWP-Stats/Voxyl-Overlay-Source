package com.voxyl.overlay.business.stats.enitities.tags

import androidx.compose.ui.Modifier

enum class Test : Tag {
    TEST {
        override val icon: (Modifier) -> Unit
            get() = TODO("Not yet implemented")
        override val desc: String
            get() = TODO("Not yet implemented")
    };
}
