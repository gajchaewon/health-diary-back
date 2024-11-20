## BodyTok : 운동일지 커뮤니티 플랫폼 ( BackEnd API )
**BodyTok**은 운동 일지를 기록하고 공유하는데 특화된 커뮤니티 플랫폼입니다.
<br>사용자들은 자신의 운동 경험을 기록하고 공유함으로써 서로에게 영감을 주고 동기부여를 제공합니다.
<br>Java 17과 Spring Boot를 기반으로 구축되었으며, Spring Security 및 Json Web Token을 통해 보안을 강화하였습니다.

---
> ### 배포환경 ( 비용 문제로 서버는 닫혀 있습니다. )
> - Docker, EC2

### 사용 기술
- Java 17
- Spring Boot 3.x
- Spring Security, Json Web Token
- Spring Data Jpa, Querydsl, MySql
- Lombok
- Swagger

---

## 기능 및 API

### Auth ( jwt 기반 인증/인가 )
- `Spring Security` + `Jwt` + `Redis`
- 회원가입 / 로그인 / 로그아웃

### User
- 개인 프로필 `조회` / `수정`
  - Aws S3 를 사용한 `이미지 업로드` 구현 ( 프로필 ) 
- 팔로우 `등록` / `취소`
- 팔로워 / 팔로잉 리스트 `조회`

### Personal Exercise Diary
- 개인 운동 일지 `조회`, `작성`, `수정`, `삭제`
- 해시태그 `작성`, `수정`, `삭제`
  - 다이어리 작성 시 DB에 존재하는 해시태그 `조회` 가능
- 파라미터 `검색` -> 제목, 본문, 해시태그, 해당 날짜( 홈 캘린더 전용 )
- Aws S3 사용한 이미지 `업로드` / `삭제` 구현
  - S3 bucket 에 이미지를 업로드하거나 수정 시 key 를 반환
  - 다이어리 수정 시, 업로드 및 수정된 이미지의 s3 bucket key 를 통해 DB 업데이트

### Community Diary ( isPublic 필드로 공개여부 결정 )
- 모든 커뮤니티 다이어리 `조회`
- 댓글 `작성`,`수정`, `삭제`
  - `JwtAuthenticationFilter` 를 통해 로그인된 유저만 가능
  - *작성자만 수정 및 삭제 가능*
- 좋아요 `등록`, `취소`

## ERD
[erd-cloud](https://www.erdcloud.com/d/GWegJBjXxGEt2erZm)
- **TODO** : 테이블 명, 컬럼 데이터 타입 및 크기 수정
![운동 일지 커뮤니티 erd](https://github.com/gajchaewon/health-diary-back/assets/74637926/69ec6937-9ec6-41a6-b101-95f9f9d6966d)

---

## 주요 이슈 및 개선 사항

<details>
<summary><strong>제네릭 타입을 활용한 사진 업로드</strong></summary>

- 각 이미지 타입의 책임을 독립적으로 관리 : 유지보수성 향상
- 타입별 이미지 업로드를 통합 : 재사용성과 일관성이 보장.
- **개선 가능성**
  - 단일 업로드 메서드로 통합되어 있으나, 추가 이미지 타입이나 타입별 비즈니스 로직이 필요할 경우, 확장성이 제한됨.
  - 만약 특정 이미지 타입별로 업로드 시 사전·사후 처리가 필요하거나, 다른 저장소 혹은 설정을 활용해야 한다면, 업로드 로직에 대한 추상화를 통한 다형성 적용이 필요할 수 있음.

</details>

<details>
<summary><strong>다이어리 검색 및 해시태그 처리 개선</strong></summary>

- QueryDsl 활용한 동적 검색 조건 처리 : 코드의 확장성과 유지보수성 향상
- 선택적 테이블 JOIN : 불필요한 JOIN을 방지하고, 필요 시에만 사용하여 성능 최적화를 꾀함.
- **개선 가능성**
  - 여러 검색 조건 병합 : 여러 조건을 동시에 처리할 수 있는 구조로 개선하여 복잡한 쿼리도 적용하도록 개선 여지가 있음.
  - 쿼리 성능 최적화 : 인덱싱이나 쿼리 최적화를 적용. 특히, 텍스트 검색 방식에서 성능 이슈를 고려한 인덱스나 풀 텍스트 검색을 활용할 수 있습니다.

</details>

<details>
<summary><strong>Jwt 와 Redis를 활용한 인증 및 로그아웃 처리</strong></summary>

- 성능 최적화 : Redis 캐싱을 통해 유저 정보 조회 시 DB 부하를 줄이고 응답 시간 단축.
- 보안성 강화 : 로그아웃 시 Redis에 블랙리스트 토큰을 저장하여, 해당 토큰을 더 이상 사용할 수 없도록 처리, 세션 하이재킹 방지.

</details>
