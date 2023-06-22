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
1. 해당 링크에 들어가 `.apk`파일을 설치한다.
   ( https://appcenter-filemanagement-distrib3ede6f06e.azureedge.net/4ed3eed7-18de-47db-b8d9-4cb1174bc81a/app-debug.apk?sv=2019-02-02&sr=c&sig=4GDguwEi4wpKuT9vxji26%2B1UeQsSrOuuqhpLy7JPq7U%3D&se=2023-06-23T10%3A33%3A52Z&sp=r )
> **아래 제공하는 설치 방법을 통해 심사위원단이 여러분의 제품/서비스를 실제 Microsoft 애저 클라우드에 배포하고 설치할 수 있어야 합니다. 만약 아래 설치 방법대로 따라해서 배포 및 설치가 되지 않을 경우 본선에 진출할 수 없습니다.**

### 사전 준비 사항
Azure speech service key

open ai key

github actions

key는 저희 팀 개인 키로 하드코딩 되어 있습니다.
> **여러분의 제품/서비스를 Microsoft 애저 클라우드에 배포하기 위해 사전에 필요한 준비 사항들을 적어주세요.**

## 시작하기
1. 이 레포지토리를 포크합니다.
2. 안드로이드 스튜디오에서 `File > New > Project From Version Control`을 클릭하여 포크한 레포지토리를 불러옵니다.
3. `local.properties`에 해당 api 키를 입력한다.
```
speech_key = "자신의 Azure speech service key 입력"
openAI_key = "자신의 open ai key 입력"
```
4. 앱을 실행합니다.
> **여러분의 제품/서비스를 Microsoft 애저 클라우드에 배포하기 위한 절차를 구체적으로 나열해 주세요.**
