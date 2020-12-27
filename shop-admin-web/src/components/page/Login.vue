<template>
    <div class="login-wrap">
        <div class="ms-login">
            <div class="ms-title">后台管理系统</div>
          <el-form :model="loginForm" status-icon :rules="loginRules" ref="loginForm" class="demo-ruleForm">
            <div class="hand"></div>
            <div class="hand rgt"></div>
            <el-form-item label="用户名" prop="userName">
              <el-input type="text" v-model.trim="loginForm.userName" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="密码" prop="password">
              <el-input type="password" v-model.trim="loginForm.password" autocomplete="off"></el-input>
            </el-form-item>
            <div>
              <center>
              <el-form-item>
                <el-button type="primary" @click="submitForm('loginForm')">登录</el-button>
              </el-form-item>
              </center>
            </div>
          </el-form>
        </div>
    </div>
</template>

<script>
export default {
  name: "login",
  data() {
    //用户名 正则  vue想和js做区分 所以var换成了 let
    let checkUserName = (rule, value, callback) => {
      //正则表达式
      let userReg = /^[\u4E00-\u9FA5A-Za-z0-9_]{3,16}$/;
      //判断用户输入的用户名是否符合规定的正则表达式
      if (!userReg.test(value)) {
        //不符合给用户做出提示
        return callback(new Error("用户名只能为3-16位英文字母数字和下划线"));
      } else {
        callback();
      }
    }
    //密码 正则  vue想和js做区分 所以var换成了 let
    let checkPassWord = (rule, value, callback) => {
      //正则表达式
      let passReg = /^[A-Za-z0-9]{3,16}$/;
      //判断用户输入的密码是否符合规定的正则表达式
      if (!passReg.test(value)) {
        //不符合给用户做出提示
        return callback(new Error("密码只能为3-16位英文字母和数字"));
      } else {
        callback();
      }
    }
    return {
      loginForm: {
        userName: "",
        password: ""
      },
      loginRules: {
        userName: [{validator: checkUserName, trigger: 'blur', required: true}],
        password: [{validator: checkPassWord, trigger: 'blur', required: true}]
      }


    }
  },
  methods: {
    //登录提交的方法
    submitForm(formName) {
      //登录提交的方法
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.$http({
            url: "/fox-shop-admin/LoginController",
            method: "post",
            params:{userName:this.loginForm.userName,
              password:this.loginForm.password
            }
          }).then(result => {
            if (result.data.code !== 200) {

              this.$message(result.data.msg);
            } else {
              //页面级缓存
              localStorage.setItem("token",result.data.data)
              this.$router.replace("/dashboard");
            }
          })
        } else {
          alert("error");
          return false;
        }

      })
    }
  },
  mounted() {
  }
}
</script>
<style scoped>
.login-wrap {
    position: relative;
    width: 100%;
    height: 100%;
    background-image: url(../../assets/img/dddd.jpg);
    background-size: 100%;
}
.ms-title {
    width: 100%;
    line-height: 50px;
    text-align: center;
    font-size: 20px;
    color: #044386;
    border-bottom: 1px solid #ddd;
}
.ms-login {
    position: absolute;
    left: 50%;
    top: 50%;
    width: 350px;
    margin: -190px 0 0 -175px;
    border-radius: 5px;
    background: rgb(252, 252, 252);
    overflow: hidden;
}
.ms-content {
    padding: 30px 30px;
}
.login-btn {
    text-align: center;
}
.login-btn button {
    width: 100%;
    height: 36px;
    margin-bottom: 10px;
}
.login-tips {
    font-size: 12px;
    line-height: 30px;
    color: #fff;
}
</style>