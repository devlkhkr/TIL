### &#128204; 사용 이유
홈플러스 임대 매장 관리 시스템 재구축 프로젝트 내 애자일 방법론의 채용으로 TDD 모델 설계 및 백엔드 Class 단위 테스트.
### &#128204; 사용 라이브러리
JUnit + AssertJ
### &#128204; 테스트와 프로덕션 코드 분리
프로덕션 소프트웨어를 배포할 때 테스트를 함께 포함할 수 있지만 보통 그렇게 하지 않는 이유는, 로딩하는 JAR 파일이 커지고 코드 베이스의 공격 표면(Attack surface)도 늘어나기 때문이다.
이를 위한 테스트 패키지 아키텍처 테스트를 별도 디렉터리로 분리하지만 프로덕션 코드와 같은 패키지에 넣는다. 이클립스와 메이븐 같은 도구는 이 모델을 권장한다.  
src 디렉토리와 별개로 클래스 패스에 test 디렉토리를 두고, 실제 테스트할 클래스의 패키지명을 동일하게 만들어 테스트 클래스를 작성한다.  
이렇게 디렉토리와 패키지를 구성하면 각 테스트는 검증하고자 하는 대상 클래스와 동일한 패키지를 갖는다.  
즉, 테스트 클래스는 패키지 수준의 접근 권한을 가지며, WAS를 실행하지 않고 테스트코드로만 검증이 가능하다.
### &#128204; AssertJ 사용으로 JUnit의 부족한 Assertions 메서드를 보완
JUnit과 AssertJ를 함께 사용하면 개발 속도와 코드의 양, 가독성 등을 보완할 수 있다.  
이를 순서대로 소개하자면,

#### 1) 가독성  
아래의 두가지 코드를 비교해보자.
  
A.	assertEquals(a,b);  
B.	assertThat(a).isEqualTo(b) 
  
A가 JUnit에서 제공하는 메서드이고, B가 AssertJ에서 제공하는 메서드이다.  
A의 코드는 어느 것이 실제 값이고, 어느 것이 예상 값인지 쉽게 유추할 수 없다.  
B의 코드는 메서드의 직관적인 네이밍으로 왼쪽에서 오른쪽으로 자연스럽게 읽힌다.  
이와 같이 AssertJ는 가독성이 좋지않은 JUnit의 메서드를 읽기 좋게 풀어내었다.  
  
####	2) 자세한 실패 메시지로 실패 원인 파악 용이  
A.	assertTrue(name.contains(“o”));
위 테스트가 실패한다면 다음과 같은 메시지를 출력한다.  
```java
expected: <true> but was: <false>
Expected :true
Actual   :false
```
이 실패 메시지를 통해서는 예상값이 true이지만 실제 값은 false라는 사실만 알 수 있고,  
자세하게 테스트의 어떤 부분에서 실패했는지 알 수 없다. (“o”를 포함하지 않아서 실패)  
B.	이를 AssertJ를 활용해 바꿔보자.  
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
A.	JUnit은 간단한 메서드들밖에 제공해주지 않아서, 비교를 위해 추가로 많은 JAVA 코드작업들이 필요하다.
```java
// JUnit
assertTrue(winners.containsAll(List.of("성훈", "지우")) && winners.size() == 2);
assertArrayEquals(winners.toArray(), new String[]{"성훈", "지우"});
assertTrue(winners.containsAll(List.of("성훈", "지우")));
```

B.	AssertJ는 다양한 메서드를 제공해줌으로써, 추가 구현을 덜어준다. 또한 통일성도 있고, 문장처럼 읽혀서 가독성도 좋다.  
```java
// AssertJ
assertThat(winners).containsExactlyInAnyOrder("성훈", "지우");
assertThat(winners).containsExactly("성훈", "지우");
assertThat(winners).contains("성훈", "지우");
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
위와 같이, 메서드 체이닝을 통해 여러가지 테스트를 한 번에 진행 할 수 있다.
위 테스트들을 JUnit이 기본 제공하는 메서드를 사용하여 테스트한다면 해당 코드는 가독성이 떨어지고 코드의 길이도 길어지게 될 것이다.
### &#128204; 간단한 테스팅 예제
```java
Players players = new Players("강형", "성훈", "형래", "지훈", "석훈", "지우", "석훈");
List<String> winners = players.findWinners();
assertThat(winners).containsExactlyInAnyOrder("성훈", "지우");
```
findWinners 메서드는 랜덤한 두명의 우승자를 구하는 함수라고 가정한다.  
그렇다면 위 테스트는 랜덤한 우승자가 “성훈“, “지우” 와 모두 일치하면 성공, 한명이라도 다르다면 실패를 반환 할 것이다.  
예를들어 우승자 예측을 한명만 성공한 경우가 있다면 아래와 같은 에러를 마주 할 것이다.
```java
java.lang.AssertionError:
Expecting actual:
["지우", "형래"]
to contain exactly in any order:
["성훈", "지우"]
elements not found:
["성훈"]
and elements not expected:
["형래"]
```
위의 메시지를 해석해보면 아래 네가지 사실을 알 수 있다. 
1.	실제 우승자는 지우와 형래이다.
2.	우리가 예상한 우승자는 성훈과 지우이다.
3.	성훈이는 우승자에 포함되지 못했다.
4.	형래는 우승자가 아닐 줄 알았는데 우승자에 포함됐다.
     
이렇게 AssertJ를 사용하면 자세한 실패 메시지를 통해, 실패 원인을 보다 정확하게 파악할 수 있다.

### &#128204; 테스트환경 구성방법 (IntelliJ 기준)
1. 테스트 폴더를 생성한다.  
상위 폴더에서 New > Directory를 클릭하여 src 하위에 main과 같은 레벨로 test 폴더를 생성한다. (Spring Initializr 사용시 자동 생성)  
2. main을 소스폴더로, test 폴더를 테스트 폴더로 지정한다.  
최상위 프로젝트 폴더 우클릭 > 모듈 설정 열기 > 모듈 > test > 소스  
<다음으로 표시> 우측 버튼들 중 main/java는 소스로, test/java는 테스트로 선택한다.  
테스트 폴더가 지정되면 소스 폴더는 파란색, 테스트 폴더는 초록색으로 변경된 것을 확인할 수 있다.  
3. main과 test의 패키지 구조를 동일하게 구성하고, test에서는 테스트 할 클래스명 뒤에 Test suffix를 붙인다. (예:CmsnService, CmsnServiceTest)  
  
<img width="312" src="https://github.com/devlkhkr/TIL/assets/84236655/75f15a4f-ba66-4a8b-b1aa-d7fc09f9040d">
  
4. 테스트 코드를 작성한다.
```java
// main/.../cmsn/service/CmsnService.java
@Service
public class CmsnService {
    public int calcCmsn(int a, int b) {
        CmsnEntity cmsnEntity = new CmsnEntity();
        return a + b;
    }
}
```
```java
// test/.../cmsn/service/CmsnServiceTest.java
@SpringBootTest
public class CmsnServiceTest {
    private CmsnService cmsnService = new CmsnService();
    @Test
    public void calcCmsn() {
        int calcTestResult = cmsnService.calcCmsn(2,3);

        assertThat(calcTestResult)
                .isPositive()
                .isGreaterThanOrEqualTo(3)
                .isLessThan(10);
    }

}
```
5. 단위테스트별 프로그램을 구성한다.
  5-1. 실행/디버그 구성 선택 클릭 > 구성 편집으로 진입한다.  
  5-2. +버튼을 눌러 Junit을 추가한다.  
  5-3. 테스트 단위 이름과, 클래스를 설정한다.
  
<img width="312" alt="스크린샷 2024-02-29 오전 10 47 48" src="https://github.com/devlkhkr/TIL/assets/84236655/1d26b386-d2e0-4bbc-a64f-64b1d270c73d">
  
6. 테스트 진행
   실행 구성 목록에서 테스팅 할 단위테스트를 선택 후 RUN 버튼을 눌러 테스트를 시작한다.  
   위의 코드는 정상적으로 테스트를 성공한다.  
     
   <img width="528" alt="스크린샷 2024-02-29 오전 10 55 04" src="https://github.com/devlkhkr/TIL/assets/84236655/00b14be7-d262-4c5b-be35-b317d66d0245">
  
8. 테스트 실패 케이스  
   의도적인 테스트 실패 케이스를 작성을 위해 isLessThen의 파라미터를 5로 변경해보았다.
   ```java
   .isLessThen(5);
   ```
   5보다 작은 수가 들어와야 하는데, 5가 들어왔으므로 테스트에 실패한다.
     
   <img width="541" alt="스크린샷 2024-02-29 오전 10 56 40" src="https://github.com/devlkhkr/TIL/assets/84236655/8d1cf094-cea8-4eaf-bbc6-07459b02c18b">
  
     
### &#128204; 마무리

