![Version](https://img.shields.io/badge/release-1.0.2-green.svg)
![API](https://img.shields.io/badge/JDK-21.0.8-blue.svg)

<h3 align="center">Discord: luckytsb</h3>

---

## ✨ Описание:

  CryptoFetchAPI — это простая и легковесная Java-библиотека для асинхронного получения актуальных курсов популярных криптовалют в евро (EUR) через CoinGeckoAPI.  
  Библиотека кэширует полученные данные и автоматически обновляет их с заданным интервалом, что снижает нагрузку и ускоряет работу приложений, использующих крипто-курс.  
  Идеально подходит **для интеграции в Minecraft-плагины или любые другие Java-проекты** на JDK 21+.

  ---
  
## 🚀 Установка:
<h3 align="center">Maven:</h3>
Добавьте в ваш pom.xml:

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.Hacker123ter</groupId>
        <artifactId>CryptoFetchAPI</artifactId>
        <version>v1.0.2</version>
    </dependency>
</dependencies>
```

<h3 align="center">Gradle:</h3>
Добавьте в build.gradle:

```gradle
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.Hacker123ter:CryptoFetchAPI:v1.0.2'
}
```

---

## 📦 Использование:

1. Получение курса по названию валюты:
```java
import org.dw363.criptofetch.CryptoFetchAPI;

double btcPrice = CryptoFetchAPI.get("bitcoin");
System.out.println("Bitcoin price: " + btcPrice + " €");
```
2. Получение курс с форматированием:
```java
import org.dw363.criptofetch.CryptoFetchAPI;

String ethPriceStr = CryptoFetchAPI.str("ethereum");
System.out.println("Ethereum price: " + ethPriceStr);
```
3. Использование enum CryptoCurrency:
```java
import org.dw363.criptofetch.CryptoFetchAPI;
import org.dw363.criptofetch.currency.CryptoCurrency;

double dogePrice = CryptoFetchAPI.get(CryptoCurrency.DOGECOIN);
System.out.println("Dogecoin price: " + dogePrice + " €");
```
4. Установка интервала автообновления курса (в секундах):
```java
import org.dw363.criptofetch.manager.CryptoCacheManager;

// установить интервал обновления в 120 секунд
CryptoCacheManager.setUpdateInterval(120);
```

---

## 🔧 Как работает:

- При первом запросе курса криптовалюты библиотека выполняет асинхронный HTTP-запрос к API CoinGecko.
- Полученное значение сохраняется в локальном кэше.
- Автоматически запускается фоновый таймер, который обновляет курсы в кэше с заданным интервалом (по умолчанию 60 секунд).
  P.S. Это сделано для предотвращения частых запросов к внешним серверам — если ставить интервал слишком маленьким, можно превысить лимиты API или получить временную блокировку. Обычно достаточно интервала 30–60 секунд.
- Все последующие запросы возвращают последнее сохранённое значение из кэша без задержек.
- Если при обновлении происходит ошибка, она логируется в консоль, но не прерывает работу.

---

## 📋 API:

- `CryptoFetchAPI.get(String currencyName)` — получить цену криптовалюты по её имени (например, `"bitcoin"`).
- `CryptoFetchAPI.str(String currencyName)` — получить цену с форматированием (например, `"12345.67 €"`).
- `CryptoCacheManager.setUpdateInterval(long seconds)` — установить интервал обновления данных.
- `CryptoCurrency` — enum поддерживаемых валют.

### Поддерживаемые криптовалюты:

- **BITCOIN** (`bitcoin`)
- **ETHEREUM** (`ethereum`)
- **LITECOIN** (`litecoin`)
- **DOGECOIN** (`dogecoin`)
- **FLOKI_INU** (`floki`)
- **SHIBA_INU** (`shiba-inu`)
- **BABY_DOGE_COIN** (`baby-doge-coin`)
- **PEPECOIN** (`pepecoin`)

---

## ⚙️ Требования:

- Java 21 или выше (используется java.net.http.HttpClient из JDK 11+).
- Интернет-соединение для обращения к CoinGeckoAPI.

---

## Благодарности:

Библиотека использует открытый API [CoinGecko](https://www.coingecko.com/en/api) для получения актуальных курсов криптовалют.

---
  
