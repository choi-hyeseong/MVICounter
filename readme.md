# MVICounter
MVI 패턴을 이용한 카운터 애플리케이션
![Image](https://github.com/user-attachments/assets/43908aa0-dba0-412f-a002-75063077a2e0)

* State = 카운터 상태
* Intent = 사용자의 Input (Click, Reset)
* SideEffect = 알림 (5의 배수 토스트, 100의 배수 스낵바)

### 기타
* State는 단순하게 지금처럼 값만 넣어도 되고, sealed class로 선언해서 여러개의 추가 상태로 나누어도 됨. (LoadingState, SuccessState)
* Flow는 collect { }에 핸들링 로직을 넣어도 되고, onEach를 한다음 collect를 수행해도 됨. cold-observable이라
* init시에 collect 수행할경우 viewModelScope에서 처리하기. 아직 변수가 init되지 않음

### Flow
* StateFlow - 뷰 상태 관리. 1개이상 = 상태유지
* SharedFlow - vm에게 데이터 전달. 0개이상 = 데이터 전달
* Channel - Hot-Stream, 이벤트 전달에 적합함
