package com.nahyun.mz.domain.model

import com.nahyun.mz.R
import com.nahyun.mz.domain.model.MemeType.*

enum class MemeType(val categoryName: String) {
    MEME("meme"),
    EXPLORE("explore"),
    OLD("old")
}

data class Meme(
    val id: Int,
    val type: MemeType,
    val title: String,
    val imageRes: Int
)

// 최신 MZ 인기 밈
val recentMemeList = listOf<Meme>(
    Meme(
        id = 1,
        type = MEME,
        title = "'퉁퉁퉁..뭐?' 그게 뭔데?",
        imageRes = R.drawable.meme_1_1
    ),
    Meme(
        id = 2,
        type = MEME,
        title = "'나니가스키?' 모르면 들어와",
        imageRes = R.drawable.meme_2
    ),
    Meme(
        id = 3,
        type = MEME,
        title = "관식이병이 유행이래요",
        imageRes = R.drawable.meme_3
    ),
    Meme(
        id = 4,
        type = MEME,
        title = "요즘 대세는 듀..가나디",
        imageRes = R.drawable.meme_4
    ),
    Meme(
        id = 5,
        type = MEME,
        title = "아직도 JMT 하는 사람 없지?",
        imageRes = R.drawable.meme_5
    ),
    Meme(
        id = 6,
        type = MEME,
        title = "올리브영 복숭아 당도최고?",
        imageRes = R.drawable.meme_6_1
    ),
    Meme(
        id = 7,
        type = MEME,
        title = "불닥볶근면을낉여오거라.",
        imageRes = R.drawable.meme_7
    ),
    Meme(
        id = 8,
        type = MEME,
        title = "이 아저씨 모르면 누르세요",
        imageRes = R.drawable.meme_8
    ),
)

// MZ 탐구 영역
val exploreMemeList = listOf<Meme>(
    Meme(
        id = 1,
        type = EXPLORE,
        title = "요즘 초통령은 뽀로로가 \n아니라고?",
        imageRes = R.drawable.cul_1_1
    ),
    Meme(
        id = 2,
        type = EXPLORE,
        title = "해외 MZ에게도 유명한 MZ 성지",
        imageRes = R.drawable.cul_2_1
    ),
    Meme(
        id = 3,
        type = EXPLORE,
        title = "MZ는 요즘 옛날 감성에 \nㅃrㅈiㄷr..✩",
        imageRes = R.drawable.cul_3
    ),
    Meme(
        id = 4,
        type = EXPLORE,
        title = "'예절샷’이 뭔지 아시나요?",
        imageRes = R.drawable.cul_4
    ),
    Meme(
        id = 5,
        type = EXPLORE,
        title = "어떤 아이돌이 제일 인기일까?",
        imageRes = R.drawable.cul_5
    ),
    Meme(
        id = 6,
        type = EXPLORE,
        title = "MZ가 주목하는 다이소 화장품?!",
        imageRes = R.drawable.cul_6
    ),
    Meme(
        id = 7,
        type = EXPLORE,
        title = "요즘 MZ는 '텀블러 꾸미기'에 \n진심이라고?",
        imageRes = R.drawable.cul_7
    ),
    Meme(
        id = 8,
        type = EXPLORE,
        title = "엄마, 공부는 '갬성'이에요.",
        imageRes = R.drawable.cul_8
    ),
)

// OLD
val oldMemeList = listOf<Meme>(
    Meme(
        id = 1,
        type = OLD,
        title = "엄마아빠를 설레게 하던 \n삐삐 암호 모음.zip",
        imageRes = R.drawable.old_1
    ),
    Meme(
        id = 2,
        type = OLD,
        title = "파란 섀도우로 완성한 Y2K",
        imageRes = R.drawable.old_2_3
    ),
    Meme(
        id = 3,
        type = OLD,
        title = "이건 기억력 테스트입니다.",
        imageRes = R.drawable.old_3_6
    ),
    Meme(
        id = 4,
        type = OLD,
        title = "패션은 돌고 돈다. \n지금 봐도 세련된 X세대 ",
        imageRes = R.drawable.old_4_0
    ),
    Meme(
        id = 5,
        type = OLD,
        title = "촌스러워서 더 재밌는 \n그 시절 예능들.zip",
        imageRes = R.drawable.old_5_5
    ),
    Meme(
        id = 6,
        type = OLD,
        title = "영화 '써니'로 다시 보는 80년대",
        imageRes = R.drawable.old_6_1
    ),
    Meme(
        id = 7,
        type = OLD,
        title = "그리운 '하두리' 감성..✩",
        imageRes = R.drawable.old_7
    ),
    Meme(
        id = 8,
        type = OLD,
        title = "누르면 '절대' 안 되는\n 폰 버튼이 있었다고?",
        imageRes = R.drawable.old_8
    ),
)