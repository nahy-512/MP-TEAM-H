package com.nahyun.mz.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nahyun.mz.data.local.RoomConstant

enum class WordType {
    RECENT, // 최신 신조어/유행어 (2023-2024)
    OLD // 옛날말/고어
}

@Entity(tableName = RoomConstant.Table.WORD)
data class Word(
    val word: String,
    val meaning: String,
    val type: WordType,
    val isLike: Boolean = false,
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
) {
    companion object {
        val initWordList = listOf(
            // === 최신 신조어/유행어 (2023-2024) ===
            Word("갬성", "감성을 뜻하는 신조어. 감성을 독특하게 표현한 말이다.", WordType.RECENT),
            Word("군싹", "군침이 싹 도는 모습을 줄인 신조어.", WordType.RECENT),
            Word("머선129", "'무슨 일이야'를 특이하게 표현한 신조어.", WordType.RECENT),
            Word("별다줄", "별걸 다 줄인다의 줄임말. 기존 용어를 과도하게 축약하는 현상을 빗댄 신조어.", WordType.RECENT),
            Word("어쩔티비", "상대방의 말에 반응할 때 '어쩌라고'라는 의미로 사용되는 신조어.", WordType.RECENT),
            Word("캠밥", "캠핑 밥의 줄임말로, 캠핑장에서 먹는 식사를 의미하는 신조어.", WordType.RECENT),
            Word("갓생", "갓(God) + 인생. 완벽하고 멋진 하루를 보내는 것을 뜻하는 신조어.", WordType.RECENT),
            Word("킹받네", "킹(King) + 받다. 매우 화나다는 뜻의 신조어.", WordType.RECENT),
            Word("JMT", "존맛탱의 줄임말. '정말 맛있다'는 의미의 신조어.", WordType.RECENT),
            Word("TMI", "Too Much Information의 줄임말. 불필요한 정보라는 뜻.", WordType.RECENT),
            Word("아점", "아침 + 점심. 아침과 점심을 겸한 식사를 뜻하는 신조어.", WordType.RECENT),
            Word("점저", "점심 + 저녁. 점심과 저녁을 겸한 식사를 뜻하는 신조어.", WordType.RECENT),
            Word("뇸뇸", "냠냠의 변형. 맛있게 먹는 모습을 표현하는 신조어.", WordType.RECENT),
            Word("웅니", "'언니' 의 귀여운 버전. Kpop 여자 가수 장원영이 사용하며 유행했다", WordType.RECENT),
            Word("실화냐", "'실화입니까?'의 줄임말. 믿기 어려운 상황에 사용.", WordType.RECENT),
            Word("억텐", "억지로 텐션을 올리는 것. 억지 + 텐션의 합성어.", WordType.RECENT),
            Word("무야호", "신나고 즐거운 기분을 표현하는 감탄사. 무한도전 짤에서 시작됐다.", WordType.RECENT),
            Word("존못", "'존나 못생겼다'의 줄임말. 매우 못생겼다는 뜻.", WordType.RECENT),
            Word("존잘", "'존나 잘생겼다'의 줄임말. 매우 잘생겼다는 뜻.", WordType.RECENT),
            Word("존예", "'존나 예쁘다'의 줄임말. 매우 예쁘다는 뜻.", WordType.RECENT),
            Word("헬창", "헬스장에서 운동을 열심히 하는 사람을 뜻하는 신조어.주로 헬스에 집착하는 사람들을 얕잡아 부를 때 사용한다.", WordType.RECENT),
            Word("극혐", "'극도로 혐오스럽다'의 줄임말.", WordType.RECENT),
            Word("캘박", "'캘린더 박제'의 의미. 일정을 확정할 때 쓰인다.", WordType.RECENT),
            Word("좋댓구알", "'좋아요, 댓글, 구독, 알림설정'의 줄임말. 유튜브 관련 신조어.", WordType.RECENT),
            Word("셀카", "셀프 카메라의 줄임말. 자신을 촬영하는 것.", WordType.RECENT),
            Word("브이로그", "Video + Blog. 영상으로 만든 개인 일기.", WordType.RECENT),
            Word("먹스타그램", "먹방 + 인스타그램. 음식 사진을 올리는 것.", WordType.RECENT),
            Word("손민수", "누군가를 따라한다는 의미로 사용된다. 웹툰 치즈인더트랩의 등장인물 손민수에서 유래.", WordType.RECENT),
            Word("프로불참러", "프로 + 불참러. 모임에 자주 불참하는 사람.", WordType.RECENT),
            Word("프로자만러", "프로 + 자만러. 자만심이 강한 사람.", WordType.RECENT),
            Word("취린이", "취업 + 어린이. 취업 초보자를 뜻하는 신조어.", WordType.RECENT),
            Word("금린이", "금융 + 어린이. 금융 초보자를 뜻하는 신조어.", WordType.RECENT),
            Word("헬린이", "헬스 + 어린이. 운동 초보자를 뜻하는 신조어.", WordType.RECENT),
            Word("요린이", "요리 + 어린이. 요리 초보자를 뜻하는 신조어.", WordType.RECENT),
            Word("운린이", "운전 + 어린이. 운전 초보자를 뜻하는 신조어.", WordType.RECENT),
            Word("갓벽", "갓(God) + 완벽. 완벽함을 강조하는 신조어.", WordType.RECENT),
            Word("레게노", "레전드 + 설명 불가. 설명할 수 없을 정도로 대단한 것.", WordType.RECENT),
            Word("넘사벽", "넘을 수 없는 사차원의 벽. 절대 따라할 수 없는 실력.", WordType.RECENT),
            Word("워라밸", "Work + Life + Balance. 일과 삶의 균형.", WordType.RECENT),
            Word("소확행", "소소하지만 확실한 행복. 작은 행복을 뜻함.", WordType.RECENT),
            Word("YOLO", "You Only Live Once. 인생은 한 번뿐이라는 뜻.", WordType.RECENT),
            Word("인싸", "인사이더. 사교적이고 인기 많은 사람.", WordType.RECENT),
            Word("아싸", "아웃사이더. 혼자 있는 것을 좋아하는 사람.", WordType.RECENT),
            Word("핵인싸", "핵 + 인싸. 매우 사교적인 사람.", WordType.RECENT),
            Word("핵아싸", "핵 + 아싸. 매우 내향적인 사람.", WordType.RECENT),
            Word("만반잘부", "만나서 반가워, 잘 부탁해의 줄임말.", WordType.RECENT),
            Word("팩트폭격", "사실로 공격하는 것. 정확한 지적.", WordType.RECENT),
            Word("2000원 비싸졌다.", "반박하고싶은 비판을 들어 상처받았다는 의미. 팩트폭격 -> 순살됐다 -> 2000원 비싸졌다.", WordType.RECENT),
            Word("호불호", "좋아함과 싫어함. 취향이 갈리는 것.", WordType.RECENT),
            Word("극혐주의", "극도로 혐오스러우니 주의하라는 뜻.", WordType.RECENT),
            Word("스압주의", "스크롤 압박 주의. 긴 글에 대한 경고.", WordType.RECENT),
            Word("댕댕이", "강아지를 귀엽게 부르는 신조어.", WordType.RECENT),
            Word("냥이", "고양이를 귀엽게 부르는 신조어.", WordType.RECENT),
            Word("멍멍이", "강아지를 귀엽게 부르는 신조어.", WordType.RECENT),
            Word("꿀잼", "꿀 + 재미. 매우 재미있다는 뜻.", WordType.RECENT),
            Word("노잼", "노 + 재미. 재미없다는 뜻.", WordType.RECENT),
            Word("핵꿀잼", "핵 + 꿀 + 재미. 엄청 재미있다는 뜻.", WordType.RECENT),
            Word("핵노잼", "핵 + 노 + 재미. 엄청 재미없다는 뜻.", WordType.RECENT),
            Word("느좋", "'느낌 좋다'의 줄임말. '느좋남', '느좋녀'처럼 사용됨.", WordType.RECENT),
            Word("싹싹김치", "긍정적인 감정을 표현하는 인터넷 신조어. 주로 기분이 좋거나 일이 잘 풀릴 때 사용", WordType.RECENT),
            Word("호랑해", "세븐틴 멤버 호시 + 사랑해의 의미", WordType.RECENT),

            // === 옛날말/고어 ===
            Word("가막소", "옛말로 감옥을 의미하는 단어.", WordType.OLD),
            Word("갈치", "옛말로 가르침, 가르치다를 의미하는 단어.", WordType.OLD),
            Word("꿰다", "옛말로 바느질하다를 의미하는 단어.", WordType.OLD),
            Word("나랏말싸미", "우리나라 말이라는 뜻의 옛말. 훈민정음 서문에 등장.", WordType.OLD),
            Word("어엿비", "불쌍히, 가엾게 여기다는 뜻의 옛말.", WordType.OLD),
            Word("아으", "옛말로 '아'를 뜻하는 감탄사.", WordType.OLD),
            Word("나리", "옛말로 높은 분을 부르는 존칭어.", WordType.OLD),
            Word("소인", "옛말로 자신을 낮춰 부르는 말.", WordType.OLD),
            Word("아뢰오", "옛말로 '말씀드립니다'의 뜻.", WordType.OLD),
            Word("하오체", "옛말 높임 문체 중 하나. '~하오' 형태.", WordType.OLD),
            Word("수라간", "궁중의 임금님 식사를 준비하는 곳.", WordType.OLD),
            Word("내관", "궁중의 환관을 뜻하는 옛말.", WordType.OLD),
            Word("마마", "왕이나 왕비를 높여 부르는 옛말.", WordType.OLD),
            Word("전하", "왕을 높여 부르는 존칭어.", WordType.OLD),
            Word("중전", "왕비를 뜻하는 옛말.", WordType.OLD),
            Word("세자", "왕의 후계자인 아들을 뜻하는 옛말.", WordType.OLD),
            Word("공주", "왕의 딸을 뜻하는 옛말.", WordType.OLD),
        )
    }
}