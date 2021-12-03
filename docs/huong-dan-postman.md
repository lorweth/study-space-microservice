# Hướng dẫn sử dụng postman test api

## Đăng nhập vào keycloak

1. Enable Direct Access Grant cho client_id đang sử dụng để đăng nhập

2. Thêm phương thức post tới địa chỉ `http://localhost:9080/auth/realms/jhipster/protocol/openid-connect/token` với request body như sau:

```json
username:admin
password:admin
grant_type:password
client_id:web_app
client_secret:web_app
```

chạy thử xem có lấy được access token không.

Lưu lại vào biến token để các request khác có thể sử dụng

```js
pm.test("Status code is 200", function () {
    pm.response.to.have.status(200);
    pm.globals.set("access_token", pm.response.json().access_token); // lưu access token vào biến global
});
```

> Mỗi lần muốn call api được phân quyền cần đăng nhập vào tài khoản có quyền đó và thêm authorization kiểu `Bearer token` với giá trị token là `{{access_token}}` trước khi call api
> Có thể dùng viết pre-request script call api lấy token trước khi call api nhất định

## Lên danh sách api và gọi