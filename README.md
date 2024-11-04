Migros Case

Kuryelerin mağaza girişlerini ve mesafelerini loglamak için yazılmıştır.
Uygulama, stores.json dosyasındaki mağaza verilerini yüklemektedir.

Java 17
Spring Boot
SLF4J loglama işlemleri için.
JUnit & Mockito: Birim testler için.
Lombok: Getter, Setter, Constructor gibi tekrar eden kodları azaltmak için.

Tasarım Kalıpları
Singleton:
    Logger nesnesi, SLF4J kütüphanesi üzerinden her sınıfta tek bir instance olarak kullanılır. Bu sayede her sınıf için aynı logger nesnesi kullanılarak gereksiz logger oluşmaz.
Dependency Injection
    StoreService sınıfı, CourierService içinde bağımlılık olarak yer alır ve bu bağımlılık Spring tarafından yönetilir.

Kuryelerin konum güncellemeleri ve mağaza giriş kontrolleri bağımsız işlemler olduğundan @Async tanımlanmıştır.

Yüklenen json dosyasının kontrolleri ve gelen requestteki alanları kontrol etmek için validasyonlar eklenmiştir.

Mesafe hesaplaması için CalculationUtil içersindeki Haversine formülü kullanılmıştır.  

Metre ve süre ile ilgili değişiklikler Constants sınıfından güncellenerek yapılabilir. //iyileştirme ->Config server bağlantısı kullanılarak uygulama restart edilmeden güncellenebilir

Test
JUnit ile yazılmıştır