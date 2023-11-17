# Advavced-We-Share-Wish-Hair - Backend
💇‍♀️ AI 기반 헤어스타일 추천 어플리케이션 💇‍♂️

## 개선 목적
### 아키텍처 
* 기존의 레이어드 아키텍처는 유지하지만, 기존 Domain 계층의 잘못된 의존관계를 바로 잡는다.
* 관련 블로깅 : <a href="https://velog.io/@namhm23/JPA-Repository-%EC%82%AC%EC%9A%A9-%EC%8B%9C-%EA%B3%84%EC%B8%B5%EA%B0%84-%EC%9D%98%EC%A1%B4%EA%B4%80%EA%B3%84-%EB%AC%B8%EC%A0%9C%EC%A0%90">블로그 링크</a>
### CI/CD 
* `github action` 을 통해서 `CI/CD` 를 구성하고 관리와 배포에 대한 자동화 환경을 구성한다.
### 테스트 코드
* `Test Double` 을 활용하여 불필요한 통합 테스트 환경에서 벗어나며 단위테스트에 집중하고 테스트 성능을 향상시킨다.
* 단위 테스트에 집중하며 기존의 억지로 엣지 케이스를 만들며 테스트한 부자연스러운 테스트 케이스를 제거한다.
* Bcrypt 암호화 구현체를 테스트 환경에서 사용하지 않으며 테스트 속도를 향상시킨다.<br><br>

**기존 테스트 결과**
<br><br>
<img width="300" alt="image" src="https://github.com/EunChanNam/We-Share-Wish-Hair/assets/75837025/b54c92b7-b932-4cf2-979d-1513a0692560">

**개선한 테스트 결과**
<br><br>
<img width="300" alt="image" src="https://github.com/EunChanNam/advanced-we-share-wish-hair/assets/75837025/a20e020b-eecb-4fe6-9212-7a29ce224e8e">


### 좋아요 관련 동시성 관리 및 성능 개선 
* 기존에 동시성을 관리하는 방법으로 `join` 을 통해 쿼리로 해결하는 방법에서 `Redis` 를 통한 방법으로 교체
* 관련 블로깅 <a href="https://velog.io/@namhm23/%EC%A2%8B%EC%95%84%EC%9A%94-%EB%8F%99%EC%8B%9C%EC%84%B1-%EC%9D%B4%EC%8A%88%EC%99%80-%EC%BF%BC%EB%A6%AC-%EC%84%B1%EB%8A%A5-%EB%AC%B8%EC%A0%9C-%ED%95%B4%EA%B2%B0">블로그 링크</a>

## 🛠️ 기술 스택
### 프로젝트
<img width="623" alt="image" src="https://github.com/EunChanNam/advanced-we-share-wish-hair/assets/75837025/393f370a-c604-4106-aeba-3ba05ea4e389">

### 인프라
<img width="532" alt="image" src="https://github.com/EunChanNam/advanced-we-share-wish-hair/assets/75837025/011b04df-8d22-4a24-a1b2-0d56f23fb3de">

---

## 프로젝트 소개
<img width="900" alt="image" src="https://github.com/EunChanNam/We-Share-Wish-Hair/assets/75837025/91db7148-d5c1-48bf-b5d2-ec29ac691591">
<img width="900" alt="image" src="https://github.com/EunChanNam/We-Share-Wish-Hair/assets/75837025/70582e7b-3f39-4cbf-94e3-88b3d6cd3cca">
<img width="900" alt="image" src="https://github.com/EunChanNam/We-Share-Wish-Hair/assets/75837025/04243c46-dac3-4419-9d49-ea2dc9ca1b05">
<br>
<img width="900" alt="image" src="https://github.com/EunChanNam/We-Share-Wish-Hair/assets/75837025/5fe4837c-fbdb-46fc-b88e-2cf7381018dc">
<img width="900" alt="image" src="https://github.com/EunChanNam/We-Share-Wish-Hair/assets/75837025/fea1efc0-00da-4b38-8d34-844b2d82a242">

---

## 테스트
![image](https://github.com/EunChanNam/advanced-we-share-wish-hair/assets/75837025/8179e215-776a-48d6-a9af-ca01bd8faab7)

![image](https://github.com/EunChanNam/advanced-we-share-wish-hair/assets/75837025/e275990a-2bb7-4fb4-8f0a-83ed4d46bfaa)

---

## 🕹️ API 목록
**Swagger API 명세 링크**<br>
http://3.21.14.25:8080/swagger-ui/index.html

<table>
  <tr>
    <td><img src="https://github.com/EunChanNam/advanced-we-share-wish-hair/assets/75837025/03db748d-0a72-4194-83ae-2e670e70016c" alt="image1" width="100%"></td>
    <td><img src="https://github.com/EunChanNam/advanced-we-share-wish-hair/assets/75837025/3942f618-0a62-49c9-897f-fa4f62975c8d" alt="image2" width="100%"></td>
  </tr>
  <tr>
    <td><img src="https://github.com/EunChanNam/advanced-we-share-wish-hair/assets/75837025/fe85a23c-9720-4278-adfc-ff2809efeea4" alt="image3" width="100%"></td>
    <td><img src="https://github.com/EunChanNam/advanced-we-share-wish-hair/assets/75837025/822894e2-7042-4ef3-98ed-a901a5d6a987" alt="image4" width="100%"></td>
  </tr>
</table>
