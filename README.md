#  Air Quality Backend Project

##  Projenin Amacı ve Kapsamı

Bu proje, hava kalitesi sensörlerinden gelen verilerin toplanması, işlenmesi ve saklanmasını sağlayan bir backend servisidir. Kafka entegrasyonu ile gelen yüksek hacimli veriler hem gerçek zamanlı hem de geçmişe dönük analiz için kullanılabilir hâle getirilir.

### Kapsam:
- Kafka ile hava kalitesi verilerinin alınması
- Anomali tespiti ve raporlama
- Lokasyon bazlı hava kalitesi sorgulama
- En kirli bölgelerin listelenmesi
- Manuel veri girişi imkânı
- API üzerinden veri erişimi ve yönetimi

## Sistem Mimarisi ve Komponentlerin Açıklaması

### Komponentler:
- **Kafka Producer** → Hava kalitesi ölçüm cihazlarından veri gönderir.
- **Kafka Broker** → Mesajları tutar ve tüketicilere dağıtır.
- **Backend Service** → Kafka’dan mesaj alır, işler, veritabanına kaydeder.
- **Database** → Ölçüm ve anomali verilerini saklar.
- **REST API** → Son kullanıcı ve diğer sistemler için erişim sağlar.

## ⚙️ Teknoloji Seçimleri ve Gerekçeleri

- **Spring Boot** → Hızlı geliştirme, güçlü Kafka ve REST desteği
- **Apache Kafka** → Gerçek zamanlı mesaj kuyruğu
- **PostgreSQL** → Güvenilir ve ölçeklenebilir veri depolama
- **Lombok** → Daha temiz ve az boilerplate kod
- **Docker** → Kolay kurulum ve dağıtım

## 🚀 Kurulum Adımları

1. **Proje klonla**
   ```bash
   git clone https://github.com/nevademircan/air-quality-backend.git
   cd air-quality-backend
   ```

2. **Kafka ve Zookeeper başlat**
   ```bash
   docker-compose up -d kafka zookeeper
   ```

3. **Veritabanı hazırla**
    - PostgreSQL veya MongoDB kur.
    - Database ve user oluştur:
      ```sql
      CREATE DATABASE air_quality;
      ```

4. **Config ayarlarını yap**
   `src/main/resources/application.yml` içinde:
    - Kafka broker URL
    - Veritabanı bağlantısı

5. **Bağımlılıkları yükle**
   ```bash
   ./mvnw clean install
   ```

6. **Uygulamayı çalıştır**
   ```bash
   ./mvnw spring-boot:run
   ```

## 📖 Kullanım Rehberi

- Kafka’dan veri almak için:
  POST /api/air-quality/fetch

- Manuel veri girişi yapmak için:
  POST /api/air-quality/manual-input

- Tüm verileri listelemek için:
  GET /api/air-quality/getAll

- Tarih aralığında anomalileri getirmek için:
  GET /api/air-quality/anomalies-by-date?start=2025-04-28T00:00:00&end=2025-04-29T23:59:59

- Lokasyona göre veri almak için:
  GET /api/air-quality/location?latitude=41.0&longitude=29.0

- En kirli bölgeleri listelemek için:
  GET /api/air-quality/pollution-density

- Son anomaliyi görmek için:
  GET /api/air-quality/last-anomaly

Swagger dokümantasyonu:
http://localhost:8080/swagger-ui/index.html

## 📑 API Dokümantasyonu

| Yöntem | Endpoint                            | Açıklama                                      |
|--------|-------------------------------------|---------------------------------------------|
| POST   | `/api/air-quality/fetch`           | Sensörden veri çek ve Kafka’ya gönder       |
| GET    | `/api/air-quality/getAll`          | Tüm hava kalitesi verilerini getir         |
| GET    | `/api/air-quality/anomalies-by-date` | Tarih aralığındaki anomalileri getir      |
| GET    | `/api/air-quality/location`        | Lokasyona göre hava kalitesi verisi getir  |
| GET    | `/api/air-quality/pollution-density` | En kirli bölgeleri listele                 |
| POST   | `/api/air-quality/manual-input`    | Manuel veri kaydı                          |
| GET    | `/api/air-quality/last-anomaly`    | Son anomaliyi getir                        |

## 💻 Script’lerin Kullanımı ve Parametreleri

**Örnek Kafka Producer script**
```bash
./scripts/send-test-data.sh --topic air-quality --broker localhost:9092 --file data.json
```

**Parametreler:**
- `--topic`: Kafka topic adı
- `--broker`: Kafka broker adresi
- `--file`: JSON veri dosyası

## 🛠️ Sorun Giderme Rehberi

| Sorun                              | Çözüm                                                    |
|-------------------------------------|---------------------------------------------------------|
| Kafka bağlantı hatası               | Kafka ve Zookeeper’ın çalıştığını kontrol edin.         |
| DB bağlantı hatası                  | DB URL, username, password ve port ayarlarını doğrulayın. |
| API 404 hatası                     | Endpoint ve port doğru mu bakın, örneğin `8080`.       |
| Mesajlar tüketilmiyor               | Topic ve consumer group ayarlarını gözden geçirin.      |
| Uygulama başlamıyor (`mvn` hatası) | Java 17+ ve Maven versiyonunu kontrol edin.            |

## 📃 Lisans

MIT License

## ✨ Katkı Sağlayanlar

- [Senin İsmin](https://github.com/kullaniciAdi)
- [Takım arkadaşların]
