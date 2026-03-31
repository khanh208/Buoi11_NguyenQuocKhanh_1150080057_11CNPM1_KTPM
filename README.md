# Lab 11 - Java Selenium CI/CD Starter

Project nay la bo khoi dau de thuc hien Lab 11 bang Java voi Selenium, TestNG va GitHub Actions.

## Yeu cau

- Java 17+ (may local cua ban dang co Java 21 van dung tot)
- Maven 3.9+
- Google Chrome hoac Firefox

## Chay local

```bash
mvn -B clean test "-Dbrowser=chrome" "-DsuiteXmlFile=testng-smoke.xml"
```

Neu muon chay Firefox:

```bash
mvn -B clean test "-Dbrowser=firefox" "-DsuiteXmlFile=testng-smoke.xml"
```

## Automation

Moi lan push len `main` hoac `develop`, GitHub Actions se tu dong chay smoke test song song tren Chrome va Firefox.

## Tai lieu huong dan

Xem file `LAB11_JAVA_HUONG_DAN_CAP_NHAT.md`.
