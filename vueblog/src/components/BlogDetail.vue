<template>
  <el-row v-loading="loading">
    <el-col :span="24">
      <div style="text-align: left;">
        <el-button type="text" icon="el-icon-back" @click="goBack" style="padding-bottom: 0px;">返回</el-button>
      </div>
    </el-col>
    <el-col :span="24">
      <div>
        <div><h3 style="margin-top: 0px;margin-bottom: 0px">{{article.title}}</h3></div>
        <div style="width: 100%;margin-top: 5px;display: flex;justify-content: flex-end;align-items: center">
          <div style="display: inline; color: #20a0ff;margin-left: 50px;margin-right:20px;font-size: 12px;">
            {{article.nickname}}
          </div>
          <span style="color: #20a0ff;margin-right:20px;font-size: 12px;">浏览 {{article.pageView==null?0:article.pageView}}</span>
          <span style="color: #20a0ff;margin-right:20px;font-size: 12px;"> {{article.editTime | formatDateTime}}</span>
          <!--<el-tag type="success" v-for="(item,index) in article.tags" :key="index" size="small"
                  style="margin-left: 8px">{{item.tagName}}
          </el-tag>-->
          <span style="margin:0px 50px 0px 0px"></span>
        </div>
      </div>
    </el-col>
    <el-col>
      <div style="text-align: left" v-html="article.htmlContent">
      </div>
    </el-col>
    <el-col align="left">
      <el-button @click="attention">关注问题</el-button>
      <el-button @click="answer">回答问题</el-button>
      <!--todo 删除功能测试完成后改成 this.activeName=='江南一点雨 -->
      <el-button @click="removeAnswer" v-if="this.activeName!='江南一点雨'?true:false">删除问题</el-button>
    </el-col>
    <el-col>
      <div><h3 style="text-align: left">答案区</h3></div>
      <div v-for="(item,index) in answers" :key="index" align="left">
        <p>{{users[index].nickname}}</p>
        {{item.content}}<br/>
        <el-button id="praise" size="small" @click="praise(item.praise_num)">赞 {{item.praise_num}}</el-button>
        <el-button id="comment" type="text" @click="comment">评论</el-button>
        <!--<el-button id="collect" type="text" @click="collect">收藏</el-button>>-->
        <el-button id="complaint" type="text" @click="complaint">举报</el-button>
      </div>
    </el-col>
    <el-dialog
      title="提示"
      :visible.sync="dialogVisible"
      width="30%"
      :before-close="handleClose">
      <span>确定要进行举报吗</span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="dialogVisible = false">确 定</el-button>
       </span>
    </el-dialog>
  </el-row>
</template>
<script>
  import {getRequest} from '../utils/api';
  import ElButton from "element-ui/packages/button/src/button";
  import {postRequest} from '../utils/api'
  import {putRequest} from '../utils/api'
  import {deleteRequest} from '../utils/api'

  export default {
    components: {ElButton},
    methods: {
      handleClose(done) {
        this.$confirm('确认关闭？')
          .then(_ => {
            done();
          })
          .catch(_ => {
          });
      },
      goBack() {
        this.$router.go(-1);
      },
      attention() {
        // todo button显示「已关注」，再点一次恢复原样式。修改 user 表的关注字段

        let button = document.getElementById('praise');
        if (this.num % 2 == 0) {
          console.log(num + '====已关注===');
          button.value = '已关注';
        } else {
          console.log(num + '=======');
          button.value = '关注问题';
        }
        this.num++;
      },
      answer(id) {
        this.$router.push({path: '/answer'});
      },
      removeAnswer() {
        // todo 设置 stauts = 0
        this.$router.push({path: '/remove/' + this.$route.query.aid});
      },
      praise(praise_num) {
        // todo 赞数+1 接口，前端数字+1
        console.log(praise_num);
        praise_num = praise_num + 1;
      },
      comment() {
        // todo
      },
      collect() {
        // UI 变化，
        this.getElementById('collect').value('已收藏');
      },
      complaint() {
        this.dialogVisible = true;
      },
    },
    mounted: function () {
      var aid = this.$route.query.aid;
      this.activeName = this.$route.query.an
      var _this = this;
      this.loading = true;
      getRequest("/article/" + aid).then(resp => {
        if (resp.status == 200) {
          _this.article = resp.data.articles;
          _this.answers = resp.data.answerList;
          _this.users = resp.data.userList;
        }
        _this.loading = false;
      }, resp => {
        _this.loading = false;
        _this.$message({type: 'error', message: '页面加载失败!'});
      });
      // todo 获取用户所有关注问题 id，判断本问题是否为用户关注的问题
      getRequest("/article/").then(resp => {
        if (resp.status == 200) {
          _this.article = resp.data.articles;
        }
        _this.loading = false;
      }, resp => {
        _this.loading = false;
        _this.$message({type: 'error', message: '页面加载失败!'});
      });


    },
    data() {
      return {
        num: 0,
        article: {},
        loading: false,
        activeName: '',
        answers: [],
        users: [],
        dialogVisible: false,
      }
    }
  }
</script>
