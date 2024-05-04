## BodyTok : 운동일지 커뮤니티 플랫폼 ( BackEnd API )
**BodyTok**은 운동 일지를 기록하고 공유하는데 특화된 커뮤니티 플랫폼입니다.
<br>사용자들은 자신의 운동 경험을 기록하고 공유함으로써 서로에게 영감을 주고 동기부여를 제공합니다.
<br>Java 17과 Spring Boot를 기반으로 구축되었으며, Spring Security 및 Json Web Token을 통해 보안을 강화하였습니다.

---
> ### 배포환경 ( 배포 중 )
> - `Docker` + `github action` 을 통한 `CI/CD`
> - Aws EC2

### 사용 기술
- Java 17
- Spring Boot 3.x
- Spring Security, Json Web Token
- Spring Data Jpa, Querydsl, MySql
- Lombok
- Swagger

---

## 기능 및 API
~~[Swagger API document]()~~ ( 배포 중 )

### Auth ( jwt 기반 인증/인가 )
- `Spring Security` + `Jwt` + `Redis`
- 회원가입 / 로그인 / 로그아웃
- ~~OAuth~~ ( 도입 예정 )

### User
- 개인 프로필 `조회` / `수정`
  - ~~Aws S3 사용한 이미지 `업로드` / `수정` / `삭제`~~ ( 구현 중 )
- 팔로우 `등록` / `취소`
- 팔로워 / 팔로잉 리스트 `조회`

### Personal Exercise Diary
- 개인 운동 일지 `조회`, `작성`, `수정`, `삭제`
- 해시태그 `작성`, `수정`, `삭제`
  - 다이어리 작성 시 DB에 존재하는 해시태그 `조회` 가능
- 파라미터 `검색` -> 제목, 본문, 해시태그, 해당 날짜( 홈 캘린더 전용 )
- **Aws S3 사용한 이미지 `업로드` / `삭제` 구현**
  - S3 bucket 에 이미지를 업로드하거나 수정 시 key 를 반환
  - 다이어리 수정 시, 업로드 및 수정된 이미지의 s3 bucket key 를 통해 DB 업데이트

### Community Diary ( isPublic 필드로 공개여부 결정 )
- 모든 커뮤니티 다이어리 `조회`
- 댓글 `작성`,`수정`, `삭제`
  - `JwtAuthenticationFilter` 를 통해 로그인된 유저만 가능
  - *작성자만 수정 및 삭제 가능*
- 좋아요 `등록`, `취소`

### ~~아키텍쳐~~ ( 작성 중 )

## ERD
[erd-cloud](https://www.erdcloud.com/d/GWegJBjXxGEt2erZm)
- **TODO** : 테이블 명, 컬럼 데이터 타입 및 크기 수정
![운동 일지 커뮤니티 erd](https://github.com/gajchaewon/health-diary-back/assets/74637926/69ec6937-9ec6-41a6-b101-95f9f9d6966d)

