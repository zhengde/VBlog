<template>
  <el-form :rules="rules" class="login-container" label-position="left"
           label-width="0px" v-loading="loading">
    <h3 class="login_title">系统注册</h3>
    <el-form-item prop="account">
      <el-input type="text" v-model="loginForm.username" auto-complete="off" placeholder="账号"></el-input>
    </el-form-item>
    <el-form-item prop="checkPass">
      <el-input type="password" v-model="loginForm.password" auto-complete="off" placeholder="密码"></el-input>
    </el-form-item>
    <el-form-item style="width: 100%">
      <el-button type="primary" @click.native.prevent="submitClick" style="width: 100%">注册</el-button>
    </el-form-item>
  </el-form>
</template>
<script>
  import {postRequest} from '../utils/api'
  import {putRequest} from '../utils/api'

  export default {
    data() {
      return {
        rules: {
          // account: [{required: true, message: '请输入用户名', trigger: 'blur'}],
          // checkPass: [{required: true, message: '请输入密码', trigger: 'blur'}]
        },
        checked: true,
        loginForm: {
          username: '',
          password: ''
        },
        loading: false
      }
    },
    methods: {
      submitClick: function () {
        var _this = this;
        if (_this.loginForm.username.length < 1 || _this.loginForm.username === null || _this.loginForm.password.length < 2 || _this.loginForm.password === null) {
          _this.$alert('用户名或密码格式不对，请重新输入');
        } else {
          this.loading = true;
          postRequest('/registry', {
            username: this.loginForm.username,
            password: this.loginForm.password
          }).then(resp => {
            _this.loading = false;
            if (resp.status == 200) {
              //成功
              var json = resp.data;
              if (json.status == 'success') {
                _this.$router.replace({path: '/home'});
              } else {
                _this.$alert('注册失败!', '失败!');
              }
            } else {
              //失败
              _this.$alert('注册失败!', '失败!');
            }
          }, resp => {
            _this.loading = false;
            _this.$alert('找不到服务器⊙﹏⊙∥!', '失败!');
          });
        }
      }
    }
  }
</script>
<style>
  .login-container {
    border-radius: 15px;
    background-clip: padding-box;
    margin: 180px auto;
    width: 350px;
    padding: 35px 35px 15px 35px;
    background: #fff;
    border: 1px solid #eaeaea;
    box-shadow: 0 0 25px #cac6c6;
  }

  .login_title {
    margin: 0px auto 40px auto;
    text-align: center;
    color: #505458;
  }

  .login_remember {
    margin: 0px 0px 35px 0px;
    text-align: left;
  }
</style>
