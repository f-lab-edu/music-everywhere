# music-everywhere
취미 연주자들을 위한 플랫폼

> 취미로 악기를 다루다보면 다른 사람과 함께 연주하고 싶어집니다.<br>
> 이 서비스는 취미로 음악을 즐기는 것의 보편화를 위해 연주자들 간의 커넥션을 잇는 것을 목표로 합니다.

네이버 밴드와 같이 그룹 단위의 커뮤니티 서비스를 개발합니다.

## ✅개발 시 신경 쓴 것들
#### - **문서를 통한 커뮤니케이션** 실천하기
  - API 문서 작성하기 --> [문서 링크](https://github.com/f-lab-edu/music-everywhere/blob/main/docs/api-docs)
  - 발생했던 문제, 장애에 대해 `원인 -> 해결과정 -> 결론` 형식으로 정리하여 동일한 문제 발생을 막기
 
#### - 객체 지향 프로그래밍에 대해 고려하여 개발하기
  - 추상화 비용에 대해 고려하기

#### - 기술 적용 시 Trade-off를 고려하여 적용 기술 선택하기
#### - 가독성 좋은 코드(리뷰어가 읽기 좋은 코드)를 작성하기 위해 노력하기
#### - 좋은 테스트 코드를 작성하기 위해 노력하기
  - 효율적인 test fixture 재사용에 대해 고민하기


## ✅사용 기술 및 개발 환경
![image](https://github.com/f-lab-edu/music-everywhere/assets/70522355/e7b061fb-2c87-4a4c-9e99-32afec254f55)

## ✅Application UI
![image](https://github.com/f-lab-edu/music-everywhere/assets/70522355/defb703f-efb3-4721-9743-c51712afd68e)
[카카오 오븐](https://ovenapp.io/project/3S1Kpott40rHcrwzORNUdEzoKzHz3PEf#CRgqm) 에서 볼 수 있습니다.

## ✅Database ERD
![image](https://github.com/f-lab-edu/music-everywhere/assets/70522355/e72d86ff-1fc0-494a-bbfc-1e4781f9d65b)


## ✅프로젝트를 진행하며 고민한 TOPIC
#### 1. 트랜잭션 전파 전략과 UnexpectedRollbackException 발생
  - 문제 발생 및 해결 과정 --> [트랜잭션은 재사용할 수 없다구요!](https://velog.io/@hyeok-kong/%ED%8A%B8%EB%9E%9C%EC%9E%AD%EC%85%98%EC%9D%80-%EC%9E%AC%EC%82%AC%EC%9A%A9%ED%95%A0-%EC%88%98-%EC%97%86%EB%8B%A4%EA%B5%AC%EC%9A%94)
#### 2. 마이크로서비스에 API Gateway 패턴 도입하기
  - 도입 과정 --> [API Gateway를 만들어보자](https://velog.io/@hyeok-kong/API-Gateway%EB%A5%BC-%EB%A7%8C%EB%93%A4%EC%96%B4%EB%B3%B4%EC%9E%90)
#### 3. Facade 패턴을 도입해 순환 의존 문제 해결 / 복잡한 의존 관계 개선하기
  - 도입 과정 --> [Facade 패턴으로 의존 복잡성을 줄여보자!](https://velog.io/@hyeok-kong/Facade-%ED%8C%A8%ED%84%B4%EC%9C%BC%EB%A1%9C-%EB%B3%B5%EC%9E%A1%EB%8F%84%EB%A5%BC-%EC%A4%84%EC%97%AC%EB%B3%B4%EC%9E%90)
#### 4. 동시성 문제 발생과 해결 과정
  - 발생 이슈 --> [그룹 가입 시 동시성 이슈 발생](https://github.com/f-lab-edu/music-everywhere/issues/30)
  - 해결 과정 --> [동시성 문제와 synchronized](https://velog.io/@hyeok-kong/%EB%8F%99%EC%8B%9C%EC%84%B1-%EB%AC%B8%EC%A0%9C%EC%99%80-synchronized),
    [동시성 문제와 DB Lock](https://velog.io/@hyeok-kong/%EB%8F%99%EC%8B%9C%EC%84%B1-%EB%AC%B8%EC%A0%9C%EC%99%80-DB-Lock),
    [동시성 문제와 분산 락](https://velog.io/@hyeok-kong/%EB%8F%99%EC%8B%9C%EC%84%B1-%EB%AC%B8%EC%A0%9C%EC%99%80-%EB%B6%84%EC%82%B0-%EB%9D%BD)
#### 5. Consumer 도입으로 냄새나는 switch 구문 리팩토링하기
- 리팩토링 과정 --> [switch에서 냄새나는 것 같아..!](https://velog.io/@hyeok-kong/switch%EC%97%90%EC%84%9C-%EB%83%84%EC%83%88%EB%82%98%EB%8A%94-%EA%B2%83-%EA%B0%99%EC%95%84)
#### 6. Jenkins 적용 과정
