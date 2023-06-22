# `모공이공` - `Senior Care`

해커그라운드 해커톤에 참여하는 `모공이공` 팀의 `Senior Care`입니다.

## 참고 문서

> 아래 두 링크는 해커톤에서 앱을 개발하면서 참고할 만한 문서들입니다. 이 문서들에서 언급한 서비스 이외에도 더 많은 서비스들이 PaaS, SaaS, 서버리스 형태로 제공되니 참고하세요.

- [순한맛](./REFERENCES_BASIC.md)
- [매운맛](./REFERENCES_ADVANCED.md)

## 제품/서비스 소개

<!-- 아래 링크는 지우지 마세요 -->
[제품/서비스 소개 보기](TOPIC.md)
<!-- 위 링크는 지우지 마세요 -->

## 오픈 소스 라이센스

<!-- 아래 링크는 지우지 마세요 -->
[오픈소스 라이센스 보기](./LICENSE)
<!-- 위 링크는 지우지 마세요 -->

## 설치 방법
1. 아래의 QR코드를 찍어 이동한다.
   
![앱 설치 QR](https://github.com/hackersground-kr/mobile20/assets/76491242/99eabb49-1927-47fb-bf93-ac7a07d47f07)

2. `DOWNLOAD`버튼을 눌러 `.apk`파일을 설치한다.

![image](https://github.com/hackersground-kr/mobile20/assets/76491242/485f6eaf-95ee-4f3a-a629-3043b38c83ec)


### 사전 준비 사항
* Azure speech service key
   1. Microsoft Azure Portal에서 리소스 그룹을 선택합니다.
   2. 해당 리소스 그룹에서 `만들기`를 눌러 `음성`을 만듭니다.
   3. 지역은 `Korea Central`, 이름은 `speech-service-mob20`, 가격 책정 계층은 `Standard S0`으로 설정합니다.
   4. `검토 + 만들기`를 누르고, 유효성 검사 통과 시 `만들기`를 눌러 배포합니다.
   5. `리소스로 이동`을 누르고, 아래에서 키 값을 확인합니다.

* OpenAI API key
   1. OpenAI 홈페이지( https://openai.com/blog/openai-api )에 접속하고, 로그인합니다.
   2. 우측 상단의 프로필을 클릭하여 `View API keys`를 선택합니다.
   3. `Create new secret key`을 선택해서 키를 발급 받습니다.

## 시작하기
1. 이 레포지토리를 포크합니다.
2. 안드로이드 스튜디오에서 `File > New > Project From Version Control`을 클릭하여 포크한 레포지토리를 불러옵니다.
3. `local.properties`에 해당 api 키를 추가하고, 상단의 try again 버튼을 누릅니다.
   ```
   speech_key = "자신의 Azure speech service key 입력"
   openAI_key = "자신의 open ai key 입력"
   ```
4. `Build > Build Bundle(s) / APK(s) > Build APK(s)`를 통해 `.apk`파일을 생성합니다.
5. Visual Studio App Center에 올립니다.
