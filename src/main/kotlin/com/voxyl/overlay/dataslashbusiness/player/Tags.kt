package com.voxyl.overlay.dataslashbusiness.player

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.TooltipArea
import androidx.compose.foundation.TooltipPlacement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.voxyl.overlay.ui.theme.VText
import com.voxyl.overlay.ui.theme.am

enum class Tags(val iconName: String, val icon: @Composable (Modifier) -> Unit, val desc: String) {
    You("account.png", { TagIcon(You, it, Color(66, 126, 255).am) }, "You"),
    Party("account-group.png", { TagIcon(Party, it, Color(128, 61, 184).am) }, "Your party members"),

    ManuallySearched("form-textbox.png", { TagIcon(ManuallySearched, it, Color(181, 179, 51).am) }, "Manually added"),
    FromChat("chat.png", { TagIcon(FromChat, it, Color(43, 176, 194).am) }, "Said your name in chat"),

    Error("account-question.png", { TagIcon(Error, it, Color(255, 85, 85).am) }, "Error, nicked maybe?"),

    VoxylDev("account-group.png", { TagIcon(VoxylDev, it, Color(13, 84, 26).am) }, "Developer for Voxyl Stats"),
    OverlayDev("account.png", { TagIcon(OverlayDev, it, Color(13, 84, 26).am) }, "Overlay Developer"),

    Ambmt("delete.png", { TagIcon(Ambmt, it, Color(115, 115, 115).am) }, "Ambmt"),

    LevelAndWWLB("account-star.png", { TagIcon(LevelAndWWLB, it, Color(201, 158, 16).am) }, "Top 100 in levels AND weighted wins"),
    LevelLB("account-star.png", { TagIcon(LevelLB, it, Color(140, 140, 140).am) }, "Top 100 in levels"),
    WWLB("account-star.png", { TagIcon(WWLB, it, Color(122, 95, 6).am) }, "Top 100 in weighted wins");

    companion object {
        @OptIn(ExperimentalFoundationApi::class)
        @Composable
        fun TagIcon(
            tag: Tags,
            modifier: Modifier = Modifier,
            color: Color
        ) {
            TooltipArea(
                tooltip = {
                    Surface(
                        color = Color(60, 60, 60, 50).am,
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        VText(
                            text = tag.desc,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                },
                delayMillis = 600,
                tooltipPlacement = TooltipPlacement.CursorPoint(
                    alignment = Alignment.BottomEnd,
                    offset = DpOffset(10.dp, 12.dp)
                )
            ) {
                Icon(
                    painter = painterResource("tags/${tag.iconName}"),
                    contentDescription = tag.name,
                    modifier = modifier,
                    tint = color
                )
            }
        }
    }
}