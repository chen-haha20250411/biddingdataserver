# Vue-Element-Admin 接口兼容指南

## 响应格式

所有接口返回统一格式：

```json
{
  "code": 20000,
  "success": true,
  "msg": "成功",
  "data": {...},
  "errcode": "0000" // 可选，原系统错误码
}
```

### 状态码说明

| 状态码 | 说明 | 备注 |
|--------|------|------|
| 20000 | 成功 | vue-element-admin 标准成功码 |
| 50000 | 系统错误 | 默认错误码 |
| 50008 | 非法token | token无效或未提供 |
| 50014 | token过期 | token已过期 |
| 40000 | 参数验证失败 | 请求参数错误 |
| 50012 | 用户不存在/无权限 | 用户相关错误 |

## 核心接口 (Vue-Element-Admin 兼容)

### 1. 登录接口

**POST** `/api/auth/login`

**请求格式**:
```json
{
  "username": "admin",
  "password": "123456",
  "code": "123456", // 验证码（可选）
  "captchaKey": "uuid-string" // 验证码key（从获取验证码接口获得）
}
```

**响应**:
```json
{
  "code": 20000,
  "success": true,
  "msg": "登录成功",
  "data": {
    "token": "jwt-token-string",
    "accessToken": "jwt-token-string"
  }
}
```

**注意**: 密码为明文，无需Base64编码。验证码可选，开发环境可不传。

### 2. 获取用户信息

**GET** `/api/user/info`

**Headers**: `token: xxx` 或 `Authorization: Bearer xxx`

**响应**:
```json
{
  "code": 20000,
  "success": true,
  "msg": "成功",
  "data": {
    "user": {
      "userId": 1,
      "username": "admin",
      "realName": "管理员",
      "roleinfoId": 1,
      "avatar": ""
    },
    "roles": ["role_1"],
    "permissions": ["/admin/user/list", ...] // 扁平化权限URL列表
  }
}
```

### 3. 退出登录

**POST** `/api/user/logout`

**Headers**: `token: xxx`

**响应**:
```json
{
  "code": 20000,
  "success": true,
  "msg": "退出成功"
}
```

### 4. 获取验证码（简单数字验证码）

**GET** `/api/auth/captcha`

**响应**:
```json
{
  "code": 20000,
  "success": true,
  "msg": "成功",
  "data": {
    "captchaKey": "uuid-string",
    "captcha": "123456" // 6位数字验证码
  }
}
```

### 5. 原系统接口（保持兼容）

以下原系统接口仍然可用：

| 接口 | 方法 | 说明 |
|------|------|------|
| `/admin/login` | POST | 原登录接口（需要Base64编码和验证码） |
| `/admin/getUserInfo` | GET | 原用户信息接口 |
| `/admin/logout` | POST | 原退出接口 |
| `/admin/captcha` | POST | 原验证码接口（返回Base64图片） |

### 6. 分页接口格式

所有分页接口返回统一格式：

```json
{
  "code": 20000,
  "success": true,
  "msg": "成功",
  "data": {
    "total": 100,
    "items": [...],
    "current": 1,
    "pageSize": 15
  }
}
```

 ### 已更新的控制器

以下控制器已更新为返回 vue-element-admin 兼容的分页格式：

| 控制器 | 路径前缀 | 说明 |
|--------|----------|------|
| `OperatorController` | `/admin/oper` | 用户管理 |
| `BiddingInfoController` | `/admin/biddingInfo` | 招标信息管理 |
| `AdminRoleinfoController` | `/admin/role` | 角色管理 |
| `Sys_dictController` | `/admin/sys_dict` | 数据字典管理 |
| `ZhongbiaoController` | `/admin/zhongbiao` | 中标信息管理 |
| `OperatorLogController` | `/admin/operLog` | 操作日志管理 |

所有分页查询接口（如 `/list`, `/toList`）现在返回统一的分页格式。

### 错误响应更新

所有错误响应现在使用标准的 vue-element-admin 状态码：
- `50000`: 系统错误（默认）
- `50008`: 非法token（TokenInterceptor 处理）
- `50014`: token过期（TokenInterceptor 处理）
- `40000`: 参数验证失败（GlobalExceptionHandler 处理）

控制器中的错误响应已逐步迁移到 `sendFailureMessage()` 方法，确保一致的错误格式。

## 跨域配置

已配置跨域支持，允许以下请求：
- 方法: GET, POST, PUT, DELETE, PATCH, OPTIONS, HEAD
- 头部: Authorization, Content-Type, X-Requested-With, token 等
- 凭证: 允许携带cookie

## 错误处理

### 全局异常处理
- 参数验证失败: 返回 40000 状态码，data 中包含字段级错误信息
- 运行时异常: 返回 50000 状态码，msg 为"系统异常，操作失败!"
- 空指针异常: 返回 50000 状态码，msg 为"系统异常，空指针错误!"

### Token 验证
- 缺少 token: 返回 50008 (非法token)
- token 失效: 返回 50014 (token过期)
- 用户不存在: 返回 50012 (用户错误)

## 前端适配建议

### Vue-Element-Admin 配置

#### 1. 修改 .env 文件
```bash
# 开发环境
VUE_APP_BASE_API = 'http://localhost:8090/api'
```

#### 2. 修改 src/utils/request.js
```javascript
import axios from 'axios'
import { Message, MessageBox } from 'element-ui'
import store from '@/store'
import { getToken } from '@/utils/auth'

// 创建axios实例
const service = axios.create({
  baseURL: process.env.VUE_APP_BASE_API, // api的base_url
  timeout: 5000 // 请求超时时间
})

// 请求拦截器
service.interceptors.request.use(
  config => {
    // 在发送请求之前做些什么
    if (store.getters.token) {
      // 让每个请求携带token
      config.headers['token'] = getToken()
    }
    return config
  },
  error => {
    // 对请求错误做些什么
    console.log(error) // for debug
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  response => {
    const res = response.data
    
    // 如果状态码不是20000，则判断为错误
    if (res.code !== 20000) {
      Message({
        message: res.msg || 'Error',
        type: 'error',
        duration: 5 * 1000
      })
      
      // 50008: 非法token, 50014: token过期
      if (res.code === 50008 || res.code === 50014) {
        // 重新登录
        MessageBox.confirm('登录状态已过期，请重新登录', '确认登出', {
          confirmButtonText: '重新登录',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          store.dispatch('user/resetToken').then(() => {
            location.reload()
          })
        })
      }
      return Promise.reject(new Error(res.msg || 'Error'))
    } else {
      return res
    }
  },
  error => {
    console.log('err' + error) // for debug
    Message({
      message: error.message,
      type: 'error',
      duration: 5 * 1000
    })
    return Promise.reject(error)
  }
)

export default service
```

#### 3. 修改登录逻辑 (src/views/login/index.vue)
```javascript
// 登录方法
handleLogin() {
  this.$refs.loginForm.validate(valid => {
    if (valid) {
      this.loading = true
      this.$store.dispatch('user/login', this.loginForm)
        .then(() => {
          this.loading = false
          this.$router.push({ path: this.redirect || '/' })
        })
        .catch(() => {
          this.loading = false
        })
    } else {
      console.log('error submit!!')
      return false
    }
  })
}
```

#### 4. 修改用户store (src/store/modules/user.js)
```javascript
import { login, logout, getInfo } from '@/api/user'

// 登录
login({ commit }, userInfo) {
  const { username, password } = userInfo
  return new Promise((resolve, reject) => {
    login({ username: username.trim(), password: password }).then(response => {
      const { data } = response
      commit('SET_TOKEN', data.token)
      setToken(data.token)
      resolve()
    }).catch(error => {
      reject(error)
    })
  })
}

// 获取用户信息
getInfo({ commit, state }) {
  return new Promise((resolve, reject) => {
    getInfo(state.token).then(response => {
      const { data } = response
      
      if (!data) {
        reject('验证失败，请重新登录。')
      }
      
      const { roles, user, permissions } = data
      
      // 验证返回的roles是否非空
      if (!roles || roles.length <= 0) {
        reject('getInfo: roles must be a non-null array!')
      }
      
      commit('SET_ROLES', roles)
      commit('SET_NAME', user.realName || user.username)
      commit('SET_AVATAR', user.avatar)
      commit('SET_PERMISSIONS', permissions)
      resolve(data)
    }).catch(error => {
      reject(error)
    })
  })
}

// 退出登录
logout({ commit, state }) {
  return new Promise((resolve, reject) => {
    logout(state.token).then(() => {
      commit('SET_TOKEN', '')
      commit('SET_ROLES', [])
      removeToken()
      resetRouter()
      resolve()
    }).catch(error => {
      reject(error)
    })
  })
}
```

## 数据库兼容性

原系统表结构不变，以下关键表用于权限管理：
- `operator`: 用户表
- `admin_roleinfo`: 角色表
- `admin_menu`: 菜单表
- `admin_rolemenu`: 角色菜单关联表

 ## 测试示例

### 1. 登录测试
```bash
curl -X POST "http://localhost:8090/api/auth/login" \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"123456"}'
```

### 2. 获取用户信息
```bash
curl -X GET "http://localhost:8090/api/user/info" \
  -H "token: your-token-here"
```

### 3. 分页查询测试 (用户列表)
```bash
curl -X GET "http://localhost:8090/admin/oper/list?limit=10&currPageNo=1" \
  -H "token: your-token-here"
```

### 4. 分页查询测试 (招标信息)
```bash
curl -X GET "http://localhost:8090/admin/biddingInfo/toList?limit=10&currPageNo=1" \
  -H "token: your-token-here"
```

响应将包含 vue-element-admin 标准分页格式。

## 部署说明

1. 编译项目：`mvn clean install`
2. 启动服务：`java -jar biddingdataserver.jar`
3. 默认端口：8090
4. 前端配置：将接口地址指向 `http://localhost:8090`