# spring-blog-project


# 프로젝트 소개

### Spring Security
### 적용된 게시판 페이지 만들기

#### - 회원 가입 및 로그인 기능 구현
#### - 로그인시 Jwt발행
#### - accessToken, refreshToken 구현
#### - 게시글 및 각 게시글의 댓글, 대댓글에 대한 CRUD 기능 구현
#### - 게시글 조회는 전체 게시글 조회 및 선택 게시글 조회 기능 구현
#### - 회원 가입 시 권한(Admin/User) 각각 부여
#### - Admin의 경우 모든 게시글 및 댓글 수정/삭제 권한 부여
#### - User의 경우 해당 User의 게시글 및 댓글만 수정/삭제 권한 부여
#### - 각 게시글 및 댓글에 대해 좋아요 기능 구현
#### - 좋아요는 한번 누르면 좋아요, 이미 눌렀으면 좋아요 취소 기능

## ERD

![erd.png](src%2Fmain%2Fresources%2Fstatic%2Fimages%2Ferd.png)

| URL                                           | Method | Request                                                                                                          | Response                                                                                                                                                                                                        |
|-----------------------------------------------|--------|------------------------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| api/posts                                     | GET    | -                                                                                                                | {"id": "1",<br/>"title": "title”<br/>"username": "username”<br/>"content": "content”<br/>"createdAt": "YYYY-MM-DD hh:mm”<br/>"modifiedAt": "YYYY-MM-DD hh:mm”<br/>"comment": "[....]”<br/> }<br/>               | 
| api/posts                                     | POST   | {<br/>"username" :"username"<br/>"content" :"content"<br/>}                                                      | {"id": "1",<br/>"title": "title”<br/>"username": "username”<br/>"content": "content”<br/>"createdAt": "YYYY-MM-DD hh:mm”<br/>}<br/>                                                                             | 
| api/posts/{id}                                | GET    | -                                                                                                                | {"id": "1",<br/>"title": "title”<br/>"username": "username”<br/>"content": "content”<br/>"createdAt": "YYYY-MM-DD hh:mm”<br/>"modifiedAt": "YYYY-MM-DD hh:mm”<br/>"comment": "[....]”<br/> }<br/>               | 
| api/posts/{id}                                | PUT    | {<br/>"title" :"title"<br/>"content" :"content"<br/>}                                                            | {"id": "1",<br/>"title": "title”<br/>"username": "username”<br/>"content": "content”<br/>"createdAt": "YYYY-MM-DD hh:mm”<br/>"modifiedAt": "YYYY-MM-DD hh:mm”<br/>"comment": "[....]”<br/> }<br/>               | 
| api/posts/admin/{id}                          | PUT    | {<br/>"title" :"title"<br/>"content" :"content"<br/>}                                                            | {"id": "1",<br/>"title": "title”<br/>"username": "username”<br/>"content": "content”<br/>"createdAt": "YYYY-MM-DD hh:mm”<br/>"modifiedAt": "YYYY-MM-DD hh:mm”<br/>"comment": "[....]”<br/> }<br/>               |
| api/posts/{id}                                | DELETE | -                                                                                                                | “msg” :”delet success”<br/>                                                                                                                                                                                     | 
| api/posts/admin/{id}                          | DELETE | -                                                                                                                | “msg” :”delet success”<br/>                                                                                                                                                                                     | 
| api/posts/{id}/like                           | POST   | -                                                                                                                | “msg”:”Like +1”<br/>                                                                                                                                                                                            | 
| api/posts/{id}/like                           | DELETE | -                                                                                                                | “msg”:”Like -1”<br/>                                                                                                                                                                                            | 
| api/user/sign                                 | POST   | {<br/>"username" :"username"<br/>"password" :"password"<br/>}                                                    | “msg”:”signup success”<br/>                                                                                                                                                                                     | 
| api/user/admin/sign                           | POST   | {<br/>"username" :"username"<br/>"password" :"password"<br/>"adminToken" :"asdfdsfsadfsdaf"<br/>}                | “msg”:”signup success”<br/>                                                                                                                                                                                     | 
| api/user/login                                | POST   | {<br/>"username" :"username"<br/>"password" :"password"<br/>}                                                    | “msg”:”login  success”<br/>                                                                                                                                                                                     | 
| api/user/reissue                              | POST   | {<br/>Headers “Refresh_Token”: “refresh token”<br/>}                                                             | “accessToken”:”asdfsdafasdfsadf”,<br/>”refreshToken”:”asdfsdfsdafasdf”<br/>                                                                                                                                     | 
| api/user/withdrawal                           | DELETE | {<br/>“password”: “password”<br/>}                                                                               | “msg”:”회원 탈퇴가 완료되었습니다.”<br/>                                                                                                                                                                                    | 
| api/comment/{posiId}                          | POST   | {<br/>“comment”: “comment”<br/>}                                                                                 | {"Postid": "1",<br/>"comment": "comment”<br/>"username": "username”<br/>"content": "content”<br/>"createdAt": "YYYY-MM-DD hh:mm”<br/>"modifiedAt": "YYYY-MM-DD hh:mm”<br/>"replyComments": "[....]”<br/> }<br/> | 
| api/comment/{id}                              | POST   | {<br/>“comment1”: “comment1”<br/>}                                                                               | {"Postid": "1",<br/>"comment": "comment1”<br/>"username": "username”<br/>"content": "content”<br/>"createdAt": "YYYY-MM-DD hh:mm”<br/>"modifiedAt": "YYYY-MM-DD hh:mm”<br/>"replyComments": "[....]”<br/> }<br/> | 
| api/comment/admin/{id}                        | POST   | {<br/>“comment2”: “comment2”<br/>}                                                                               | {"Postid": "1",<br/>"comment": "comment2”<br/>"username": "username”<br/>"content": "content”<br/>"createdAt": "YYYY-MM-DD hh:mm”<br/>"modifiedAt": "YYYY-MM-DD hh:mm”<br/>"replyComments": "[....]”<br/> }<br/> | 
| api/comment/{id}                              | DELETE | -                                                                                                                | “msg”:”해당 댓글이 삭제되었습니다.”<br/>                                                                                                                                                                                    | 
| api/comment/admin/{id}                        | DELETE | -                                                                                                                | “msg”:”해당 댓글이 삭제되었습니다.”<br/>                                                                                                                                                                                    | 
| api/comment/{id}/like                         | POST | -                                                                                                                | “msg”:”Like +1”<br/>                                                                                                                                                                                            | 
| api/comment/{id}/like                         | POST | -                                                                                                                | “msg”:”Like -1”<br/>                                                                                                                                                                                            | 
| api/reply-comments/{postId}/comments/{parentId} | POST   | {<br/>“id”: “1”<br/>“parenId”: “parenId”<br/>“reply-comment1”: “reply-comment2”<br/>“username”: “username”<br/>} | {“id”: “1”,<br/>”reply-comment”: “reply-comment1”<br/>“username”: “username”<br/>”createdAt”: YYYY-MM-DD hh:mm<br/>”modifiedAt”: YYYY-MM-DD hh:mm<br/>}<br/>                                                    | 
| api/reply-comments/{id}                       | PUT   | {<br/>”reply-comment”:”reply-comment2”<br/>”username”:”username”<br/>}                                           | {“id”: “1”,<br/>”reply-comment”: “reply-comment2”<br/>“username”: “username”<br/>”createdAt”: YYYY-MM-DD hh:mm<br/>”modifiedAt”: YYYY-MM-DD hh:mm<br/>}<br/>                                                    | 
| api/reply-comments/admin/<br/{id}             | PUT   | {<br/>“reply-comment”: “reply-comment3”<br/>“username”: “username”<br/>}                                         | {“id”: “1”,<br/>”reply-comment”: “reply-comment3”<br/>“username”: “username”<br/>”createdAt”: YYYY-MM-DD hh:mm<br/>”modifiedAt”: YYYY-MM-DD hh:mm<br/>}<br/>                                                    | 
| api/reply-comments/{id}                                  | DELETE | -                                                                                                                | “msg” :”해당 댓글이 삭제되었습니다”<br/>                                                                                                                                                                                     | 
| api/reply-comments/admin/{id}                                 | DELETE | -                                                                                                                | “msg” :”해당 댓글이 삭제되었습니다”<br/>                                                                                                                                                                                      | 


## 팀원

| 이름      | 사람  |
|---------|-----|
| post    | 윤여준 | 
| comment | 문지영 | 
| user    | 이상환 | 
| Likes    | 장성준 | 
| security    | 장성준 | 
| Replycomment    | 장성준 | 
| RefreshToken    | 장성준 | 

# 6가지 질문

**1. Spring Security를 적용했을 때 어떤 점이 도움이 되셨나요?**
보안과 관련해서 체계적으로 관리해주기 때문에 url마다 보안 관련 로직을 일일이 작성하지 않아도 된다, 컨트롤러 앞에 Filter를 기반으로 동작하여 의존성이 적다

**2. Spring Security를 사용하지 않는다면 어떻게 인증/인가를 효율적으로 처리할 수 있을까요?**
웹의 비연결성, Stateless 특징 때문에 페이지가 바뀌어도 로그인을 유지해주기 위해서 쿠키 -세션 방식이나 Token발급 방법을 사용할 수 있다,

컨트롤러에서 인증/인가를 마치고 서비스를 호출한다

**3. IoC / DI에 대해 간략하게 설명해 주세요.**

IoC : 제어의 역전이라는 의미로 사용자가 객체를 생성하는 것이 아니고 프레임워크가 알아서 생성된 객체를 주입시켜 사용하는 것
DI : IoC를 구현하는 디자인 패턴, 클래스 타입에 고정되지 않고 인터페이스 타입으로 주입 받아 구현할 수 있다, 의존성이 떨어지고 재사용성이 높아진다.

**4. AOP에 대해 설명해 주세요.**
관점 지향 프로그래밍이라고 불립니다. 즉 각각 핵심, 부가기능의 관점에 맞춰서 모듈화한다
핵심 기능과 부가기능을 함께 묶어서 이용하는 것이 아닌 부가기능을 모듈화시켜 다양한 클래스가 재활용하여 공통으로 사용할 수 있다

**5. RefreshToken 적용에 대한 장/단점을 작성해 주세요! 적용해 보지 않으셨다면 JWT를 사용하여 인증/인가를 구현했을 때의 장/단점에 대해 작성해 주세요!**

RefreshToken을 사용하면 AccessToken의 유효 기간이 짧아 탈취 당하더라도 금방 사용할 수 없어지고 정상적인 클라이언트는 RefreshToken을 이용해서 다시 재발급 받을 수 있다

RefreshToken을 탈취 당하면 1회 한정 공격자가 AccessToken을 발급 받을 수 있다

JWT는 장점으로는 동시 접속자가 많을 때 서버의 부하를 낮춰주고 별도 저장소가 필요 없어 서버 자원을 절약하고 인증 시 다른 곳을 거치지 않아 효율적이다
단점 구현하는데 복잡도가 증가하고 JWT의 내용이 클수록 네트워크 비용이 증가, 저장할 정보가 제한적

**6. 즉시 로딩 / 지연 로딩에 대해 설명해 주세요!**
즉시 로딩이란 데이터 조회 시 연관 데이터를 모두 한 번에 불러온다. 때문에 지연 로딩보다 초기의 로딩 시간이 필요, 연관이 많을 경우 로딩 시간이 오래 걸릴 수 있다
지연 로딩은 로딩 시점에 프록시 객체로 가져오고 실제 사용할 때 연관된 객체의 데이터를 불러온다. 가급적이면 지연 로딩만 사용























