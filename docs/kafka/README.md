## 카프카 토픽 네이밍 규칙
1. 소문자 사용: 토픽 이름은 모두 소문자로 구성된다.
2. 단어 구분: 두 단어 이상은 -(하이픈)으로 구분하여 작성한다.
3. 케밥 케이스: .(도트)를 사용하여 작성한다.


## 토픽 네이밍 템플릿
#### <환경>.<이벤트 발행 마이크로서비스명>.<이벤트 소비 마이크로서비스명>.<이벤트 타입>.<이벤트 세부 정보>
#### EX)
- dev.group.payment.created.increase-group-size
- prod.payment.group.refunded.decrease-group-size


### 템플릿 용어
#### 환경
- dev : 개발 환경
- prod : 프로덕션 환경

#### 마이크로서비스명
- `○○-service` 중 `○○` 만 기입
- ex) `gruop` / `payment` / `user` / ...

#### 이벤트 타입
- 리소스 상태 변화를 표시
- ex) `created` / `updated` / `deleted` / `refunded` / ...

#### 이벤트 세부 정보
- 발행/소비된 이벤트의 세부 정보를 기입
- 동작 유추가 가능할 수준으로 작성
- ex) `increase-group-size` / `decrease-group-size`
