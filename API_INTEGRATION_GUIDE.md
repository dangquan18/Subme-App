# Hướng dẫn tích hợp API với Localhost

## Cấu hình API

### 1. Địa chỉ API
File: `RetrofitClient.java`

- **Với Android Emulator**: `http://10.0.2.2:3000/`
- **Với thiết bị thật**: Thay đổi thành IP máy tính của bạn (VD: `http://192.168.1.100:3000/`)

### 2. Cách lấy IP máy tính
- Mở Command Prompt
- Gõ: `ipconfig`
- Tìm "IPv4 Address" trong phần "Wireless LAN adapter Wi-Fi" hoặc "Ethernet adapter"

### 3. Endpoints đã tích hợp

#### Login
```
POST http://localhost:3000/auth/login
Body: {
    "email": "a@example.com",
    "password": "hashed_password_123"
}
Response: {
    "access_token": "eyJhbGci..."
}
```

## Cấu trúc Code

### 1. Models
- **LoginRequest.java**: Request body cho đăng nhập
- **LoginResponse.java**: Response chứa access_token
- **TokenPayload.java**: Payload được decode từ JWT token

### 2. API Service
- **ApiService.java**: Interface định nghĩa các endpoints
- **RetrofitClient.java**: Singleton quản lý Retrofit instance

### 3. Token Management
- **TokenManager.java**: Quản lý token (lưu, lấy, xóa, decode)
  - `saveToken(String token)`: Lưu token vào SharedPreferences
  - `getToken()`: Lấy token
  - `clearToken()`: Xóa token (logout)
  - `isLoggedIn()`: Kiểm tra trạng thái đăng nhập
  - `decodeToken(String token)`: Decode JWT để lấy payload
  - `getUserInfo()`: Lấy thông tin user từ token
  - `getBearerToken()`: Lấy token với prefix "Bearer "

### 4. Activities
- **LoginActivity.java**: Màn hình đăng nhập
- **WelcomeActivity.java**: Màn hình chào mừng (đã cập nhật)
- **ProfileFragment.java**: Hiển thị thông tin user và nút logout

## Luồng hoạt động

1. **Khởi động app** → WelcomeActivity
2. **Nhấn "Đăng nhập"** → LoginActivity
3. **Nhập email/password** → Gọi API login
4. **Nhận access_token** → Lưu vào SharedPreferences
5. **Token được decode** → Lấy thông tin user (email, role, exp)
6. **Chuyển sang MainActivity** → Hiển thị app
7. **ProfileFragment** → Hiển thị email, role từ token
8. **Nhấn "Đăng xuất"** → Xóa token, quay về LoginActivity

## Token Payload

JWT token chứa thông tin:
```json
{
  "email": "dangq2359@gmail.com",
  "sub": 1,
  "role": "user",
  "iat": 1762134866,
  "exp": 1762138466
}
```

- **email**: Email người dùng
- **sub**: User ID
- **role**: Vai trò (user, admin, premium...)
- **iat**: Issued at (thời gian tạo token)
- **exp**: Expiration (thời gian hết hạn token)

## Cách sử dụng token trong API calls

```java
// Trong Activity/Fragment
TokenManager tokenManager = new TokenManager(context);
String bearerToken = tokenManager.getBearerToken();

// Gọi API với token
RetrofitClient.getApiService()
    .getUserProfile(bearerToken)
    .enqueue(new Callback<Object>() {
        @Override
        public void onResponse(Call<Object> call, Response<Object> response) {
            // Xử lý response
        }
        
        @Override
        public void onFailure(Call<Object> call, Throwable t) {
            // Xử lý lỗi
        }
    });
```

## Thêm Endpoints mới

Để thêm endpoint mới, cập nhật `ApiService.java`:

```java
// Ví dụ: Lấy danh sách gói
@GET("packages")
Call<List<Package>> getPackages(@Header("Authorization") String token);

// Ví dụ: Upgrade premium
@POST("user/upgrade")
Call<UpgradeResponse> upgradeToPremium(
    @Header("Authorization") String token,
    @Body UpgradeRequest request
);
```

## Kiểm tra kết nối

1. Đảm bảo backend đang chạy tại localhost:3000
2. Kiểm tra AndroidManifest.xml đã có:
   - `<uses-permission android:name="android.permission.INTERNET" />`
   - `android:usesCleartextTraffic="true"`
3. Nếu dùng thiết bị thật, đảm bảo cùng mạng WiFi với máy tính

## Xử lý lỗi thường gặp

### 1. Token hết hạn
```java
TokenPayload payload = tokenManager.getUserInfo();
if (payload != null && payload.isExpired()) {
    // Token hết hạn, yêu cầu đăng nhập lại
    tokenManager.clearToken();
    // Navigate to LoginActivity
}
```

### 2. API không kết nối được
- Kiểm tra URL trong RetrofitClient
- Kiểm tra backend đang chạy
- Kiểm tra firewall/antivirus

### 3. Response 401 Unauthorized
- Token không hợp lệ hoặc hết hạn
- Xóa token và yêu cầu đăng nhập lại

## Dependencies đã thêm

```gradle
// Retrofit for API calls
implementation("com.squareup.retrofit2:retrofit:2.9.0")
implementation("com.squareup.retrofit2:converter-gson:2.9.0")
implementation("com.squareup.okhttp3:okhttp:4.12.0")
implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

// Gson for JSON parsing
implementation("com.google.code.gson:gson:2.10.1")
```

## Testing

### Test với Postman/Insomnia
1. POST http://localhost:3000/auth/login
2. Body: `{"email": "a@example.com", "password": "hashed_password_123"}`
3. Nhận access_token
4. Dùng token này để test các endpoint khác

### Test trên app
1. Mở app → Nhấn "Đăng nhập"
2. Nhập email/password đã có trong database
3. Kiểm tra log để xem request/response
4. Kiểm tra ProfileFragment hiển thị đúng email/role
5. Test nút Logout

