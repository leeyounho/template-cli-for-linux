# template-light-cli-for-linux

이 프로젝트는 리눅스 환경에서 특정 DB 쿼리를 수행하고 결과에 따라 후속 작업을 처리하는 간단한 Java 기반 Command Line Interface(CLI) 애플리케이션입니다. 빠른 실행 속도와 안정적인 데이터 처리에 중점을 두고 설계되었습니다.

## ✨ 주요 특징

- **빠른 실행 속도**: Spring Shell이나 Spring Boot 대신 Picocli와 최소화된 Spring Context를 사용하여 네이티브 명령어처럼 빠른 시작 및 실행 속도를 보장합니다.
- **체계적인 관리**: Spring Framework를 통해 데이터 접근, 트랜잭션 관리, 의존성 주입(DI)을 체계적으로 처리합니다.
- **직관적인 인터페이스**: Picocli를 기반으로 리눅스 명령어 스타일의 옵션과 도움말을 제공합니다.
- **독립적인 테스트**: H2 인메모리 데이터베이스와 JUnit5를 활용하여 외부 환경에 의존하지 않는 빠르고 안정적인 통합 테스트 환경을 구축합니다.

## 🚀 시작하기

### 1. 사전 요구사항

- Java 8 (OpenJDK) 설치
- Gradle 8.x 설치 (또는 프로젝트 내 `gradlew` 사용)

### 2. 설정

프로덕션 DB에 연결하기 위해 `src/main/resources/database.properties` 파일을 환경에 맞게 수정합니다.

```properties
# DataSource Properties (for HikariCP)
jdbc.driverClassName=oracle.jdbc.driver.OracleDriver
jdbc.url=jdbc:oracle:thin:@//<HOST>:<PORT>/<SERVICE_NAME>
jdbc.user=your_username
jdbc.password=your_password
```

### 3. 빌드

Gradle Wrapper를 사용하여 프로젝트를 빌드합니다. `application` 플러그인에 의해 실행 가능한 배포판이 생성됩니다.

```bash
./gradlew clean build
```

빌드가 성공하면 `build/distributions` 디렉토리에 압축 파일과 함께 압축이 해제된 실행 폴더가 생성됩니다.

### 4. 실행

생성된 스크립트를 사용하여 커맨드 라인에서 애플리케이션을 실행할 수 있습니다.

```bash
# 생성된 실행 폴더로 이동 (프로젝트 이름에 따라 경로 변경)
cd build/distributions/template-light-cli-for-linux-1.0-SNAPSHOT/bin

# 애플리케이션 실행 (예: -p 옵션으로 파라미터 전달)
./template-light-cli-for-linux -p "processed"
```

## ✅ 테스트

아래 명령어를 통해 H2 인메모리 DB를 사용하는 통합 테스트를 실행할 수 있습니다.

```bash
./gradlew test
```

## 📐 주요 설계 결정

이 프로젝트는 **빠른 실행 속도**라는 명확한 목표를 달성하기 위해 다음과 같은 설계적 결정을 내렸습니다.

1.  **CLI 프레임워크: Picocli**
    - Spring Shell은 실행 시마다 전체 Spring 컨텍스트를 로드하여 수 초의 지연이 발생할 수 있습니다.
    - 반면, Picocli는 자체 오버헤드가 거의 없어 즉각적인 실행이 가능하며, 필요한 시점에만 최소화된 Spring 컨텍스트를 수동으로 로드하여 사용합니다.

2.  **데이터 접근: `JdbcTemplate`**
    - Hibernate는 `SessionFactory` 초기화 과정이 무거워 CLI 애플리케이션의 시작 속도를 저하 시키는 주된 요인이 될 수 있습니다.
    - `JdbcTemplate`은 별도의 무거운 초기화 과정 없이 `DataSource`만 있으면 바로 사용할 수 있어 시작 속도가 매우 빠릅니다. 동시에 Spring의 `@Transactional`을 통한 선언적 트랜잭션 관리의 이점은 그대로 누릴 수 있습니다.

3.  **Spring 설정: 최소화된 Java 기반 설정**
    - `@ComponentScan`을 사용한 자동 빈 스캔은 불필요한 빈까지 로드하여 속도를 저하 시킬 수 있습니다.
    - `@Configuration` 클래스 내에 필요한 빈들만 `@Bean`으로 명시적으로 등록하여, 애플리케이션에 꼭 필요한 최소한의 컨텍스트만 구성하도록 최적화했습니다.