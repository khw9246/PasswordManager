# 🔐 Password Manager & Generator (비밀번호 매니저 및 생성기)

안전한 비밀번호 등록, 실시간 비밀번호 강도 측정, 그리고 강력한 무작위 비밀번호 자동 생성을 지원하는 안드로이드 애플리케이션입니다.

---

## 🚀 주요 기능 (Key Features)

1. **실시간 비밀번호 강도 측정 (Real-time Password Strength Meter)**
   - 비밀번호 입력 시 실시간으로 강도를 분석하여 시각적인 피드백을 제공합니다.
   - 강도 측정 기준:
     - 길이 (8자 이상)
     - 영문자(a-z, A-Z) 포함 여부
     - 숫자(0-9) 포함 여부
     - 특수문자(!@#$%^&*) 포함 여부
   - 측정된 강도는 **5단계(미입력, 위험, 약함, 보통, 안전)**의 텍스트와 가로형 프로그레스 바(ProgressBar)의 색상 및 게이지로 실시간 시각화됩니다.

2. **안전한 비밀번호 자동 생성 (Secure Password Generator)**
   - `SecureRandom`을 사용하여 보안상 강력하고 무작위적인 **12자리 비밀번호**를 자동으로 생성합니다.
   - 대소문자, 숫자, 특수 기호가 고루 섞여 있어 보안 수준을 최대로 높일 수 있습니다.

3. **비밀번호 보기/숨기기 토글 (Password Visibility Toggle)**
   - 눈 모양 아이콘 버튼(`ImageButton`)을 터치하여 작성 중이거나 자동 생성된 비밀번호의 가시성을 자유롭게 전환할 수 있습니다.

4. **유효성 검사 및 저장 (Validation & Mock Saving)**
   - 사이트 이름, 아이디/이메일, 비밀번호의 모든 필수 입력 필드가 올바르게 입력되었는지 검증합니다.
   - 저장이 완료되면 안내 토스트(Toast) 메시지와 함께 현재 화면이 종료됩니다.

---

## 🛠️ 개발 환경 및 사양 (Technical Specification)

- **OS/Platform**: Android (Min SDK: 24 / Target SDK: 35 / Compile SDK: 35)
- **개발 언어**: Java (Java 8 호환 컴파일 옵션)
- **빌드 도구**: Gradle (Kotlin DSL, `build.gradle.kts`)
- **주요 종속성 (Dependencies)**:
  - `androidx.appcompat:appcompat:1.6.1`
  - `com.google.android.material:material:1.11.0`
  - `androidx.constraintlayout:constraintlayout:2.1.4`
  - `androidx.core:core-ktx:1.13.1`

---

## 📂 프로젝트 구조 (Project Directory Structure)

```text
Password/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/cookandroid/password/
│   │       │   └── MainActivity.java       # 앱의 핵심 비즈니스 로직 및 이벤트 핸들링
│   │       └── res/
│   │           ├── layout/
│   │           │   └── activity_main.xml   # 스크롤 가능한 새 비밀번호 등록 화면 레이아웃
│   │           └── values/                 # 스타일, 색상, 문자열 등 리소스 정의
│   └── build.gradle.kts                     # 모듈 수준 Gradle 설정 파일
└── build.gradle.kts                         # 프로젝트 수준 Gradle 설정 파일
```

---

## 💻 코드 상세 정보 (Implementation Details)

- **[MainActivity.java](file:///c:/CookAndroid/Project/Password/app/src/main/java/com/cookandroid/password/MainActivity.java)**
  - `updatePasswordStrength(String password)`: 정규표현식을 활용하여 비밀번호의 조건 부합 여부를 검사하고 점수(0~4점)별로 ProgressBar 상태와 안내 텍스트 색상을 설정합니다.
  - `generateSecurePassword(int length)`: 암호학적으로 안전한 난수 생성기인 `SecureRandom`을 기반으로 문자 조합을 추출하여 비밀번호를 완성합니다.
  - `saveData()`: 필수값 누락 여부를 확인하는 간단한 검증 후 성공 토스트를 출력합니다.
- **[activity_main.xml](file:///c:/CookAndroid/Project/Password/app/src/main/res/layout/activity_main.xml)**
  - 작은 화면의 디바이스나 키보드가 올라올 때 레이아웃이 가려지지 않도록 `ScrollView` 내부에 `LinearLayout`을 감싸 구성되었습니다.
  - 비밀번호 입력 창 옆에 토글 버튼이 나란히 위치할 수 있도록 가로형 중첩 레이아웃 구조를 활용하였습니다.
