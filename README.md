# BodyTok

운동일지 커뮤니티 서비스 ( BackEnd API )
- 개인의 운동일지를 작성하고, 커뮤니티에 공개하여 운동 경험을 나눌 수 있는 서비스

### 배포환경

Docker, Aws EC2

### 사용 기술
- Java 17
- Spring Boot 3.x
- Spring Security, Json Web Token
- Spring Data Jpa, Querydsl, MySql
- Lombok
- Swagger

---

### 기능 및 API
[Swagger API document]() <- 배포 후 링크 삽입해야함

#### Auth- jwt 기반 인증/인가
- 회원가입 / 로그인 / ~~로그아웃~~ (*redis로 구현 예정*)
#### User
- 개인 프로필 조회 (*Aws S3 통해 구현 예정*)
- 팔로우 등록 / 취소- 팔로워 / 팔로잉 리스트 조회
#### Personal Diary
- 다이어리 조회, 작성, 수정, 삭제 (*Aws S3 통해 구현 예정*)
- 해시태그 작성, 수정, 삭제
- 파라미터로 검색 가능 -> 제목, 본문, 해시태그, 해당 날짜( 캘린더 전용 )
#### Community Diary ( isPublic 필드로 공개여부 결정 )
- 모든 커뮤니티 다이어리 조회
- 댓글 등록, 삭제 ( 인증을 기반으로 소유자만 가능)
- 좋아요 등록, 취소

## ERD
[erd-cloud](https://www.erdcloud.com/d/GWegJBjXxGEt2erZm)
![운동 일지 커뮤니티 erd](https://github.com/gajchaewon/health-diary-back/assets/74637926/69ec6937-9ec6-41a6-b101-95f9f9d6966d)
