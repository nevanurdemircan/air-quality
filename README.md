# Air Quality Monitoring System

Bu proje, OpenWeatherMap API’den hava kalitesi verilerini çekip Kafka’ya gönderen, PostgreSQL veritabanında saklayan ve REST API üzerinden erişim sağlayan bir hava kalitesi izleme sistemidir.

## Projenin Amacı ve Kapsamı

- OpenWeatherMap API’den hava kalitesi verilerini almak
- Kafka ile veri akışı sağlamak
- PostgreSQL üzerinde verileri saklamak
- Anomali tespiti ve sorgulama endpointleri sunmak
- REST API ile hava kalitesi verilerini kullanıcılara sunmak

##  Sistem Mimarisi ve Komponentler

- **Spring Boot Uygulaması:** API servislerini ve iş mantığını barındırır.
- **PostgreSQL:** Hava kalitesi verilerini saklamak için kullanılır.
- **Kafka:** Veri akışını sağlamak ve ölçeklenebilirliği artırmak için kullanılır.
- **OpenWeatherMap API:** Hava kalitesi verilerini almak için dış API kaynağı.
- **Docker Compose :** PostgreSQL ve Kafka’yı yerel ortamda ayağa kaldırmak için kullanıldı.

## Teknoloji Seçimleri ve Gerekçeleri

- **Spring Boot:** Hızlı geliştirme ve geniş ekosistem.
- **PostgreSQL:** Güvenilir, açık kaynaklı ilişkisel veritabanı.
- **Kafka:** Gerçek zamanlı veri akışı ve mesajlaşma altyapısı.
- **OpenWeatherMap API:** Güvenilir hava kalitesi verisi sağlayıcısı.
- **Lombok:** Kod tekrarını azaltmak için.

## Kurulum Adımları

1. **PostgreSQL Kurulumu**
    - PostgreSQL yükleyin → [https://www.postgresql.org/download/](https://www.postgresql.org/download/)
    - Yeni veritabanı oluşturun:
      ```sql
      CREATE DATABASE air_quality;
      ```
    - Kullanıcı adı ve şifreyi `application.properties` dosyasına uygun şekilde ayarlayın.

2. **Kafka Kurulumu**
    - Kafka yükleyin → [https://kafka.apache.org/quickstart](https://kafka.apache.org/quickstart)
    - Kafka’yı başlatın:
      ```bash
      bin/zookeeper-server-start.sh config/zookeeper.properties
      bin/kafka-server-start.sh config/server.properties
      ```

3. **Proje Bağımlılıkları**
    - IntelliJ veya başka bir IDE ile projeyi açın.
    - Maven bağımlılıklarını yükleyin:
      ```bash
      mvn clean install
      ```

4. **Uygulamayı Çalıştırma**
    - IntelliJ’den `main` sınıfını başlatın.
    - Ya da terminalden:
      ```bash
      mvn spring-boot:run
      ```

### API Endpoint’leri

| Endpoint                           | Açıklama                                    |
|-------------------------------------|--------------------------------------------|
| POST `/api/air-quality/fetch`      | OpenWeatherMap’ten veri çekip Kafka’ya gönderir. |
| GET `/api/air-quality/getAll`      | Tüm hava kalitesi verilerini getirir.       |
| GET `/api/air-quality/anomalies-by-date?start={date}&end={date}` | Tarih aralığındaki anomalileri getirir. |
| GET `/api/air-quality/location?latitude={lat}&longitude={lon}`  | Belirli konumdaki verileri getirir.     |
| GET `/api/air-quality/pollution-density`  | En yoğun kirlenme olan bölgeleri getirir. |
| POST `/api/air-quality/manual-input` | Manuel hava kalitesi verisi ekler.        |
| GET `/api/air-quality/last-anomaly` | Son anomali verisini getirir.             |

### Örnek Tarih Formatı
```
2025-04-29T15:30:00
```

- OpenWeatherMap API anahtarı:
  ```properties
  openweathermap.api.key=1d16d0705a71d96f18248d60c78d318a
  ```
- Kafka konfigürasyonu:
  ```properties
  spring.kafka.bootstrap-servers=kafka:9092
  ```
- Veritabanı konfigürasyonu:
  ```properties
  spring.datasource.url=jdbc:postgresql://air-quality-db:5432/air_quality
  spring.datasource.username=postgres
  spring.datasource.password=31.10.01Nd
  ```

## Sorun Giderme (Troubleshooting) Rehberi

| Sorun                                      | Çözüm                                                          |
|-------------------------------------------|---------------------------------------------------------------|
| PostgreSQL bağlantı hatası                 | DB’nin çalıştığından ve `url/username/password` bilgilerinin doğru olduğundan emin olun. |
| Kafka bağlantı hatası                      | Kafka servislerinin çalışır durumda olduğundan emin olun.    |
| OpenWeatherMap API erişim hatası           | API key’in geçerli olduğunu ve API limitini aşmadığınızı kontrol edin. |
| `application.properties` değişikliklerinin uygulanmaması | Uygulamayı yeniden başlatmayı unutmayın.                     |
| Maven bağımlılık hataları                  | `mvn clean install` çalıştırın.                             |

---

