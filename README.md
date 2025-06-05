2025-1 모바일 프로그래밍 <Team H - 요즘 애들은>

### ⭐️ 주요 기능
<table>
  <tr>
    <td align=center>
      구분
    </td>
    <td align=center>
      화면
    </td>
    <td align=center>
      기능
    </td>
  </tr>
  <tr>
    <td align=center>
      번역기
    </td>
    <td align=center>
        <img width="800" align=center alt="image" src="https://github.com/user-attachments/assets/003c60b9-9bd2-434a-a486-2d8220d81486" />
    </td>
    <td>
      <p>
        - 단어 검색 </br>
        - 단어 즐겨찾기 저장/삭제</br>
        - 즐겨찾기 한 단어 목록 보기 </br>
        - 신조어/옛말 사전 (단어 모아보기)
      </p>
    </td>
  </tr>
  <tr>
    <td align=center>
      밈 & 퀴즈
    </td>
    <td>
      <img width="800" alt="image" src="https://github.com/user-attachments/assets/61988a0a-fbeb-4aa4-b5e1-40e9ecb147e4" />
    </td>
    <td>
      <p>
        - 밈 관련 퀴즈 풀기(MZ력 테스트)</br>
        - 최신/예전 밈 모아보기 & 상세보기</br>
        - 밈 관련 기사 보러가기</br>
        - 그 시절 노래 들으러 가기(유튜브)
      </p>
    </td>
  </tr>
  <tr>
    <td align=center>
      토론방
    </td>
    <td>
      <img width="800" alt="image" src="https://github.com/user-attachments/assets/8b5c20b4-9fd0-4820-8d6f-64adf1f91178" />
    </td>
    <td>
      <p>
        - 게시글 목록 불러오기</br>
        - 게시글의 신조어/얫말 단어 번역하기</br>
        - 게시글에 반응 남기기(좋아요/댓글)</br>
        - 댓글 좋아요</br>
        - 게시글 생성
      </p>
    </td>
  </tr>
  <tr>
    <td align=center>
      소비</br>트랜드
    </td>
    <td align=center>
      <img width="550" alt="image" src="https://github.com/user-attachments/assets/152c7a16-6fd8-4c79-84b2-2ae27cdbfe00" />
    </td>
    <td>
      <p>
        - 밈 관련 퀴즈 풀기 (MZ력 테스트)</br>
        - 최신/예전 밈 모아보기 & 상세보기</br>
        - 밈 관련 기사 보러가기</br>
        - 그 시절 노래 들으러 가기(유튜브)</br>
      </p>
    </td>
  </tr>
</table>

### 📸 Screenshot

<table>
    <tr>
      <td>

https://github.com/user-attachments/assets/7d44551e-d3e2-4cd2-a034-79aefa9c9f1c

</td>
    <td>

https://github.com/user-attachments/assets/284d83e5-b07b-4d15-8b32-4ef386af77c0

</td>
    <td>

https://github.com/user-attachments/assets/3d1c7747-21b0-45fc-918e-183afb6283eb

</td>
<td>

https://github.com/user-attachments/assets/9cf38e4e-98e0-41ec-97b8-37fc2a783f9d

</td>
  </tr>
<tr>
    <td align="center"><b>번역기</b></td>
    <td align="center"><b>밈/퀴즈</b></td>
    <td align="center"><b>토론방</b></td>
    <td align="center"><b>소비 트렌드</b></td>
  </tr>
</table>

## 💥 Data Storage Flow
<RoomDB 단어 저장 흐름>
|아키텍처 다이어그램|프로젝트 적용 내용|
|:--:|:--:|
|<img src="https://github.com/user-attachments/assets/3aaa8732-8bfa-420d-ac7a-761fc82a7562" />| <img src="https://github.com/user-attachments/assets/e1aaefb3-a101-43c1-93ff-436d0607acad">

<즐겨찾기 한 단어 확인>
|Database Inspector|App|
|:--:|:--:|
|<img src="https://github.com/user-attachments/assets/151811f9-3e14-45c3-a153-651e3e9cd84e" />| <img width=375 src="https://github.com/user-attachments/assets/3bf157be-9907-44b9-9bac-d2587e261641">

## 📂 Package
```
📦 mz
├── 📜 MZApplication.kt
├── 📂 data
│   ├── 📂 local
│   └── 📂remote
├── 📂 domain
│   ├── 📂 model
│   └── 📂 repository
├── 📂 ui
│   ├── 📜 MainActivity.kt
│   ├── 📂 base
│   ├── 📂 discussion
│   ├── 📂 login
│   ├── 📂 quiz
│   ├── 📂 translator
│   └── 📂 trend
└── 📂 utils
    ├── 📜 BindingAdapter.kt
    └── 📜 TimeConverter.kt
```

## 👥 Contributors

|권민지|김나현|유연이|장선우|
|:----:|:----:|:----:|:----:|
|<img src="https://avatars.githubusercontent.com/u/113248329?s=96&v=4" width="150" />|<img src="https://avatars.githubusercontent.com/u/101113025?v=4" width="150" />|<img src="https://avatars.githubusercontent.com/u/105120219?s=96&v=4" width="150" />|<img src="https://avatars.githubusercontent.com/u/164745248?s=96&v=4" width="150" />|
| 번역기 | 토론방, 사전 | 밈 & 퀴즈 | 소비 트렌드 |

