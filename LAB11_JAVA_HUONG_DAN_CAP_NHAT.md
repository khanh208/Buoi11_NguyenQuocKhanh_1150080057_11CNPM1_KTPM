# LAB 11 JAVA - Huong dan cap nhat tu Lab 1 den Lab 7

Tai lieu nay duoc tong hop tu 2 file `.docx` trong thu muc va duoc cap nhat lai theo cach lam moi hon.

## Nhung diem da cap nhat so voi tai lieu goc

- Dung `actions/checkout@v5` va `actions/setup-java@v5`.
- Dung Selenium Java `4.41.0`.
- Mau cu dung WebDriverManager. Ban moi co the dung Selenium Manager tich hop san trong Selenium 4, gon hon va it phu thuoc hon.
- Selenium Grid 4 co the nhan endpoint truc tiep `http://localhost:4444`, khong bat buoc noi them `/wd/hub`.
- Project dung Java 17 lam muc tieu build de hop voi GitHub Actions, nhung local co the chay bang JDK 21.

## Cau truc project hien tai

- `.github/workflows/selenium-ci.yml`: workflow tu dong chay khi push.
- `pom.xml`: Maven + Selenium + TestNG.
- `src/main/java`: code dung chung.
- `src/test/java`: test.
- `testng-smoke.xml`: suite cho Lab 1.
- `testng-grid.xml`: suite chuan bi cho Lab 4.
- `docker-compose.yml`: chuan bi cho Selenium Grid.

## Bai 1 - GitHub Actions CI/CD co ban

### Muc tieu

Moi lan ban push code tu VSCode len GitHub, GitHub Actions tu dong chay smoke test ma khong can bam tay.

### File da duoc tao cho Bai 1

- `.github/workflows/selenium-ci.yml`
- `README.md`
- `.gitignore`
- `pom.xml`
- `src/main/java/vn/ktpm/lab11/driver/DriverFactory.java`
- `src/test/java/vn/ktpm/lab11/tests/SmokeLoginPageTest.java`
- `testng-smoke.xml`

### Luong hoat dong automation

1. Ban sua code trong VSCode.
2. Ban commit va push len nhanh `main` hoac `develop`.
3. GitHub nhan su kien `push`.
4. Workflow `selenium-ci.yml` duoc kick off.
5. GitHub Actions checkout source, cai Java 17, restore Maven cache.
6. Maven chay TestNG suite `testng-smoke.xml`.
7. Ket qua test va screenshot fail duoc upload thanh artifact.

### Cach thuc hien tren VSCode

1. Mo folder project trong VSCode.
2. Khoi tao git neu chua co:

```bash
git init
git branch -M main
git add .
git commit -m "Init Lab 11 Java starter"
```

3. Tao repository tren GitHub, sau do lien ket remote:

```bash
git remote add origin https://github.com/<tai-khoan>/<ten-repo>.git
git push -u origin main
```

4. Vao tab `Actions` tren GitHub de xem pipeline.

### Cach chay local truoc khi push

```bash
mvn -B clean test "-Dbrowser=chrome" "-DsuiteXmlFile=testng-smoke.xml"
```

Neu muon test headless ngay tren may local:

```bash
mvn -B clean test "-Dbrowser=chrome" "-Dheadless=true" "-DsuiteXmlFile=testng-smoke.xml"
```

### Giai thich code quan trong

`DriverFactory.java`

- Tu dong bat headless khi co bien moi truong `CI`.
- Ho tro ca Chrome va Firefox.
- Giu san luong `RemoteWebDriver` de sang Lab 4 chi can them `-Dgrid.url=http://localhost:4444`.

`selenium-ci.yml`

- Trigger khi `push`, `pull_request`, hoac `workflow_dispatch`.
- Dung Maven cache de lan chay sau nhanh hon.
- Luon upload artifact ke ca khi test fail.

### Bai 1 can nop gi

- Anh chup pipeline mau xanh khi test pass.
- Tao 1 assertion sai co y, push len de lay anh mau do.
- Download artifact va chup man hinh thu muc ket qua.

## Bai 2 - Matrix Strategy chay song song da browser

### Muc tieu

Cho Chrome va Firefox chay song song tren GitHub Actions de giam thoi gian CI.

### Cach sua workflow

Da cap nhat workflow sang matrix:

```yaml
jobs:
  run-tests:
    runs-on: ubuntu-latest
    strategy:
      fail-fast: false
      matrix:
        browser: [chrome, firefox]
    steps:
      - name: Checkout source
        uses: actions/checkout@v5
      - name: Set up Java 17
        uses: actions/setup-java@v5
        with:
          distribution: temurin
          java-version: '17'
          cache: maven
      - name: Run smoke suite
        run: mvn -B clean test -Dbrowser=${{ matrix.browser }} -DsuiteXmlFile=testng-smoke.xml
      - name: Upload test evidence
        if: always()
        uses: actions/upload-artifact@v4
        with:
          name: test-results-${{ matrix.browser }}
          path: |
            target/surefire-reports/
            target/screenshots/
```

### Luu y ky thuat

- `fail-fast: false` de Firefox van chay du Chrome fail.
- `DriverFactory` cua project da ho tro Firefox headless.
- Artifact da tach rieng thanh `test-results-chrome` va `test-results-firefox`.

### Cach chup man hinh bai 2

1. Push code len GitHub.
2. Mo tab `Actions`.
3. Chon workflow run moi nhat.
4. Chup man hinh khi thay 2 job `Smoke tests (chrome)` va `Smoke tests (firefox)` chay cung luc.

### Cach so sanh thoi gian tuan tu vs song song

`Chay tuan tu`

- Tam thoi sua workflow bo `strategy.matrix`.
- Chay lan 1 voi `-Dbrowser=chrome`.
- Chay lan 2 voi `-Dbrowser=firefox`.
- Cong tong thoi gian 2 lan lai.

`Chay song song`

- Dung workflow matrix hien tai.
- Lay tong thoi gian cua 1 workflow run co 2 job song song.

`Mau bang ghi ket qua`

| Cau hinh | Thoi gian | Ghi chu |
|---|---:|---|
| Tuan tu | ... giay | Chrome xong roi Firefox |
| Song song matrix | ... giay | Chrome va Firefox cung luc |
| Tiet kiem | ... giay | Nhanh hon ...% |

## Bai 3 - GitHub Secrets bao mat credential

### Muc tieu

Khong hardcode username va password trong source code.

### Tao secret tren GitHub

- `SAUCEDEMO_USERNAME`
- `SAUCEDEMO_PASSWORD`

### Them vao workflow

```yaml
      - name: Run login test
        run: mvn -B clean test -Dbrowser=chrome -DsuiteXmlFile=testng-smoke.xml
        env:
          APP_USERNAME: ${{ secrets.SAUCEDEMO_USERNAME }}
          APP_PASSWORD: ${{ secrets.SAUCEDEMO_PASSWORD }}
```

### Mau Java cap nhat

```java
public final class SecretConfig {
    private SecretConfig() {
    }

    public static String getUsername() {
        return readValue("APP_USERNAME", "standard_user");
    }

    public static String getPassword() {
        return readValue("APP_PASSWORD", "");
    }

    private static String readValue(String envName, String fallback) {
        String value = System.getenv(envName);
        return value == null || value.isBlank() ? fallback : value;
    }
}
```

### Kiem tra nhanh

```bash
rg "secret_sauce|standard_user" src
```

Ket qua mong doi: khong co password that trong source.

## Bai 4 - Selenium Grid voi Docker

### Muc tieu

Chay test song song tren Grid local de thay su khac biet giua local va distributed execution.

### File da chuan bi

- `docker-compose.yml`
- `testng-grid.xml`

### Khoi dong Grid

```bash
docker compose up -d
docker ps
```

Mo `http://localhost:4444` de xem Grid UI.

### Chay test tren Grid

```bash
mvn -B clean test "-Dgrid.url=http://localhost:4444" "-DsuiteXmlFile=testng-grid.xml"
```

### Diem cap nhat

- Docker image trong tai lieu goc la `4.18.1`, da duoc cap nhat len `4.39.0`.
- Grid 4 hien nay co the dung endpoint goc `http://localhost:4444`.

## Bai 5 - Allure Report nang cao

### Muc tieu

Gan annotation va tao report de doc ket qua test de hon.

### Cap nhat pom.xml khi sang Bai 5

```xml
<properties>
    <allure.version>2.33.0</allure.version>
</properties>

<dependency>
    <groupId>io.qameta.allure</groupId>
    <artifactId>allure-testng</artifactId>
    <version>${allure.version}</version>
</dependency>
```

### Annotation mau

```java
@Test
@Feature("Dang nhap")
@Story("UC-001")
@Description("Dang nhap voi tai khoan hop le")
@Severity(SeverityLevel.CRITICAL)
public void testLoginSuccess() {
    Allure.step("Mo trang dang nhap", () -> driver().get(baseUrl()));
}
```

## Bai 6 - Pipeline day du + publish report

### Muc tieu

Sau khi test chay xong, artifact va report duoc giu lai, co the day len GitHub Pages.

### Huong mo rong workflow

- Job 1: chay test.
- Job 2: generate Allure report.
- Job 3: deploy `allure-report/` len GitHub Pages.

### Goi y thu tu lam

1. Tao `allure-results`.
2. Generate HTML report sau khi test.
3. Dung action deploy Pages sau khi branch `gh-pages` hoac Pages artifact san sang.

## Bai 7 - Test Strategy va Test Plan

### Muc tieu

Viet duoc 2 tai lieu:

- `docs/Test-Strategy.md`
- `docs/Test-Plan-Sprint-01.md`

### Khung Test Strategy nen co

1. Muc tieu chat luong.
2. Pham vi kiem thu.
3. Loai test: smoke, regression, cross-browser, API, security.
4. Moi truong test.
5. Cong cu dung trong du an.
6. Tieu chi vao va ra.
7. Rui ro va cach giam thieu.

### Khung Test Plan nen co

1. Sprint dang test.
2. User story duoc cover.
3. Test case uu tien cao.
4. Lich chay test.
5. Nhan su va phan cong.
6. Tieu chi pass/fail.

## Tong hop tieu chi cham diem Lab 11

### Bai 1

- Co file `.github/workflows/selenium-ci.yml`.
- Pipeline pass va co anh log mau xanh.
- Co lan push gay fail co y va co anh log mau do.
- Download duoc artifact va xem duoc ket qua.

### Bai 2

- Workflow co `strategy.matrix`.
- Chay song song `chrome` va `firefox`.
- Co anh tab Actions hien 2 job.
- Co so sanh thoi gian truoc va sau matrix.

### Bai 3

- Da tao `SAUCEDEMO_USERNAME` va `SAUCEDEMO_PASSWORD`.
- Workflow truyen secret qua `env`.
- Java doc tu `System.getenv()` truoc.
- Khong con credential that trong source code.

### Bai 4

- `docker-compose.yml` co 1 hub, 2 chrome node, 1 firefox node.
- Co anh Grid UI da dang ky node.
- Co chay test qua `RemoteWebDriver`.
- Co bang so sanh thoi gian 1 thread, 2 thread, 4 thread.

### Bai 5

- Da them Allure dependency.
- Test co annotation `@Feature`, `@Story`, `@Severity`.
- Co screenshot khi fail.

### Bai 6

- Pipeline day du chay test, tao report, luu artifact.
- Report co the xem lai tren GitHub Pages hoac artifact.

### Bai 7

- Co Test Strategy document.
- Co Test Plan document cho it nhat 1 sprint.
- Noi dung ro pham vi, muc tieu, rui ro, tieu chi vao ra.

## Lenh nen dung trong buoi nop bai

```bash
git add .
git commit -m "Complete Lab 11 - Bai 1 starter"
git push origin main
```

Neu ban muon, buoc tiep theo minh co the lam tiep luon Bai 2 cho ban bang cach cap nhat workflow sang matrix strategy ngay trong project nay.
