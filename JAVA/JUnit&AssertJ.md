### &#128204; 사용 이유
홈플러스 임대 매장 관리 시스템 재구축 프로젝트 내 애자일 방법론의 채용으로 TDD 모델 설계 및 백엔드 Class 단위 테스트.
### &#128204; 사용 라이브러리
JUNIT + ASSERTJ
### &#128204; 테스트와 프로덕션 코드 분리
프로덕션 소프트웨어를 배포할 때 테스트를 함께 포함할 수 있지만 보통 그렇게 하지 않는 이유는, 로딩하는 JAR 파일이 커지고 코드 베이스의 공격 표면(Attack surface)도 늘어나기 때문이다.
이를 위한 테스트 패키지 아키텍처 테스트를 별도 디렉터리로 분리하지만 프로덕션 코드와 같은 패키지에 넣는다. 이클립스와 메이븐 같은 도구는 이 모델을 권장한다.  
src 디렉토리와 별개로 클래스 패스에 test 디렉토리를 두고, 실제 테스트할 클래스의 패키지명을 동일하게 만들어 테스트 클래스를 작성한다.  
이렇게 디렉토리와 패키지를 구성하면 각 테스트는 검증하고자 하는 대상 클래스와 동일한 패키지를 갖는다.  
즉, 테스트 클래스는 패키지 수준의 접근 권한을 가진다.
### &#128204; ASSERTJ 사용으로 JUNIT의 부족한 Assertions 메서드를 보완
Junit과 AssertJ를 함께 사용하면 개발 속도와 코드의 양, 가독성 등을 보완할 수 있다.  
이를 순서대로 소개하자면,

#### 1) 가독성  
아래의 두가지 코드를 비교해보자.
  
A.	assertEquals(a,b);  
B.	assertThat(a).isEqualTo(b) 
  
A가 JUNIT에서 제공하는 메서드이고, B가 ASSERTJ에서 제공하는 메서드이다.  
A의 코드는 어느 것이 실제 값이고, 어느 것이 예상 값인지 쉽게 유추할 수 없다.  
B의 코드는 메서드의 직관적인 네이밍으로 왼쪽에서 오른쪽으로 자연스럽게 읽힌다.  
이와 같이 ASSERTJ는 가독성이 좋지않은 JUNIT의 메서드를 읽기 좋게 풀어내었다.  
  
####	2) 자세한 실패 메시지로 실패 원인 파악 용이  
A.	assertTrue(name.contains(“o”));
위 테스트가 실패한다면 다음과 같은 메시지를 출력한다.  
```java
expected: <true> but was: <false>
Expected :true
Actual   :false
```
이 실패 메시지를 통해서는 예상값이 true이지만 실제 값은 false라는 사실만 알 수 있고,  
무슨 테스트를 실패했는지 (“o”를 포함하지 않아서 실패) 했다는 사실은 알 수 없다.  
B.	이를 ASSERTJ를 활용해 바꿔보자.  
assertThat(a).contains(“o”);
위 테스트를 돌렸을 때 실패 메시지는 다음과 같다.  
```java
java.lang.AssertionError:
Expecting actual:
  "ash"
to contain:
  "o"
```
실패 메시지만 보고도 어느 것을 테스트하려 했는데 실패한건지, 실패의 원인이 무엇인지 파악할 수 있다.  
#### 3)	간결하고 다양한 검증 메서드 제공으로 인한 편리함
A.	JUNIT은 간단한 메서드들밖에 제공해주지 않아서, 비교를 위해 추가로 많은 JAVA 코드작업들이 필요하다.
```java
// JUnit
assertTrue(winners.containsAll(List.of("애쉬", "스플릿")) && winners.size() == 2);
assertArrayEquals(winners.toArray(), new String[]{"애쉬", "스플릿"});
assertTrue(winners.containsAll(List.of("애쉬", "스플릿")));
```

B.	ASSERTJ는 다양한 메서드를 제공해줌으로써, 추가 구현을 덜어준다. 또한 통일성도 있고, 문장처럼 읽혀서 가독성도 좋다.\
```java
assertThat(winners).containsExactlyInAnyOrder("애쉬", "스플릿");
assertThat(winners).containsExactly("애쉬", "스플릿");
assertThat(winners).contains("애쉬", "스플릿");
```
#### 4)	메서드 체이닝으로 다중 테스트를 한 번에 
A.	문자열의 검증
```java
assertThat("[TITLE] Hello, My Name is ash")
.isNotEmpty()
.contains("[TITLE]")
.containsOnlyOnce("ash")
.doesNotStartWith("[ERROR]");
```
B.	숫자의 검증
```java
assertThat(score)
.isPositive()
.isGreaterThan(60)
.isLessThanOrEqualTo(75);
```
위와 같이, 메서드 체이닝을 통해 여러가지 테스트를 한 번에 할 수 있다.
위 테스트들을 JUnit이 기본 제공하는 메서드를 사용하여 테스트한다면 해당 코드는 가독성이 떨어지고 코드의 길이도 길어지게 될 것이다.
### &#128204; 간단한 테스팅 예제
```java
Players players = new Players("애쉬", "스플릿", "아코", "히이로", "비버", "마코");
List<String> winners = players.findWinners();
assertThat(winners).containsExactlyInAnyOrder("애쉬", "비버");
```
findWinners 메서드는 랜덤한 두명의 우승자를 구하는 함수라고 가정한다.  
그렇다면 위 테스트는 랜덤한 우승자가 “애쉬“, “비버”와 모두 일치하면 성공, 한명이라도 다르다면 실패를 반환 할 것이다.  
예를들어 우승자 예측을 한명만 성공한 경우가 있다면 아래와 같은 에러를 마주 할 것이다.
```java
java.lang.AssertionError:
Expecting actual:
["애쉬", "스플릿"]
to contain exactly in any order:
["애쉬", "비버"]
elements not found:
["비버"]
and elements not expected:
["스플릿"]
```
위의 메시지를 해석해보면 아래 네가지 사실을 알 수 있다. 
1.	실제 우승자는 애쉬와 스플릿이다.
2.	우리가 예상한 우승자는 애쉬와 비버이다.
3.	비버는 우승자에 포함되지 못했다.
4.	스플릿은 우승자가 아닐 줄 알았는데 우승자에 포함됐다
이렇게 AssertJ를 사용하면 자세한 실패 메시지를 통해, 실패 원인을 보다 정확하게 파악할 수 있다.
