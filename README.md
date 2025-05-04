# Kubernetes Mikroservis Uygulaması

Bu proje, Kubernetes üzerinde çalışan örnek bir mikroservis uygulamasıdır. Uygulama, user-service ve task-service olmak üzere iki ayrı Spring Boot servisi içerir ve PostgreSQL veritabanını kullanır.

## Proje Yapısı

```
├── kubernetes/
│   ├── postgres/         # PostgreSQL Kubernetes yapılandırma dosyaları
│   ├── user-service/     # user-service Kubernetes yapılandırma dosyaları
│   ├── task-service/     # task-service Kubernetes yapılandırma dosyaları
│   └── deploy.sh         # Kubernetes'e deploy etmek için script
├── user-service/         # Kullanıcı yönetimi servisi
├── task-service/         # Görev yönetimi servisi
└── pom.xml               # Ana Maven yapılandırma dosyası
```

## Gereksinimler

- Java 17+
- Maven
- Docker
- Kubernetes (Minikube veya Docker Desktop Kubernetes)

## Kurulum ve Çalıştırma

1. Projeyi klonlayın
2. Uygulamayı build edin ve Kubernetes'e deploy edin:

```bash
cd kubernetes
chmod +x deploy.sh
./deploy.sh
```

Bu komut:

- Maven ile projeleri build eder
- Docker imajlarını oluşturur
- Kubernetes kaynaklarını uygular

## Servisler ve Endpointler

### User Service (8080)

- `GET /api/users` - Tüm kullanıcıları listeler
- `GET /api/users/{id}` - ID'ye göre kullanıcı getirir
- `POST /api/users` - Yeni kullanıcı oluşturur
- `PUT /api/users/{id}` - Kullanıcıyı günceller
- `DELETE /api/users/{id}` - Kullanıcıyı siler

### Task Service (8081)

- `GET /api/tasks` - Tüm görevleri listeler
- `GET /api/tasks/{id}` - ID'ye göre görev getirir
- `GET /api/tasks/user/{userId}` - Kullanıcıya ait görevleri listeler
- `POST /api/tasks` - Yeni görev oluşturur
- `PUT /api/tasks/{id}` - Görevi günceller
- `DELETE /api/tasks/{id}` - Görevi siler

## Kubernetes Kaynakları

- **PostgreSQL**: Tek bir PostgreSQL Pod'u iki servis tarafından da kullanılır
- **user-service**: Kullanıcı yönetimi yapan servis
- **task-service**: Görev yönetimi yapan ve user-service ile iletişim kuran servis

## Servisler Arası İletişim

task-service, kullanıcı doğrulaması için user-service ile HTTP üzerinden iletişim kurar. Bu haberleşme, Kubernetes Service DNS isimlendirmesi üzerinden yapılır (`http://user-service:8080`).

## Temel Kubernetes Komutları

```bash
# Pod'ları listele
kubectl get pods

# Servisleri listele
kubectl get services

# Pod loglarını görüntüle
kubectl logs -f <pod-name>

# Pod detaylarını görüntüle
kubectl describe pod <pod-name>
```
