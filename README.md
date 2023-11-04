# Advavced-We-Share-Wish-Hair - Backend
💇‍♀️ AI 기반 헤어스타일 추천 어플리케이션 💇‍♂️

## 개선 목적
1. 아키텍처 : 기존의 레이어드 아키텍처는 유지하지만, 기존 Domain 계층의 잘못된 의존관계를 바로 잡는다.
2. CI/CD : `github action` 을 통해서 CI/CD 를 구성하고 관리와 배포에 대한 자동화 환경을 구성한다.
3. 테스트 코드 : `Test Double` 을 활용하여 불필요한 통합 테스트 환경에서 벗어나며 단위테스트에 집중하고 테스트 성능을 향상시킨다.
5. 프로덕트 코드 리펙토링 : 코드를 살피며 객체지향적인 코드로 리팩토링한다

## 🛠️ 기술 스택
* Java
* Spring Boot
* Spring Data JPA
* Query Dsl
* RestDocs
* JUnit5
* MySQL
* Github Action
* Docker

## 🕹️ API 목록
### 인증
* 로그인 API
* 로그아웃 API
* 이메일 중복체크 API
* 검증 메일 발송 API
* 메일 검증 API
* 토큰 재발급 API

### 사용자
* 회원가입 API
* 회원 탈퇴 API
* 회원 정보 수정 API
* 비밀번호 변경 API
* 비밀번호 갱신 API
* 사용자 얼굴형 업데이트 API

### 헤어스타일
* 메인 헤어스타일 추천 API
* 사용자 얼굴형 기반 헤어스타일 추천 API
* 찜한 헤어스타일 조회 API
* 전체 헤어스타일 조회 API
* 찜하기 API
* 찜 취소 API
* 찜 여부 조회 API

### 리뷰
* 리뷰 생성 API
* 리뷰 삭제 API
* 리뷰 수정 API
* 리뷰 단건 조회 API
* 전체 리뷰 조회 API
* 나의 리뷰 조회 API
* 이달의 추천 리뷰 조회 API
* 헤어스타일의 리뷰 조회 API
* 좋아요 실행 API
* 좋아요 취소 API
* 좋아요 여부 조회 API
