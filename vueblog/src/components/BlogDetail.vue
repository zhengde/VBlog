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
          <span style="color: #20a0ff;margin-right:20px;font-size: 12px;">浏览 {{article.pageView==null?0:article.pageView}}</span>
          <span style="color: #20a0ff;margin-right:20px;font-size: 12px;"> {{article.editTime | formatDateTime}}</span>
          <span style="margin:0px 50px 0px 0px"></span>
        </div>
      </div>
    </el-col>
    <el-col>
      <div style="text-align: left" v-html="article.htmlContent">
      </div>
    </el-col>
    <el-col align="left">
      <!--<el-button @click="attention">关注问题</el-button>-->
      <el-button @click="answer(article)">回答问题</el-button>
      <el-button @click="removeArticleDialogShow" v-show="activeName!=='江南一点雨'?true:false">删除问题</el-button>
    </el-col>
    <el-col>
      <div><h3 style="text-align: left">答案区</h3></div>
      <div v-for="(item,index) in answers" :key="index" align="left">
        <p>{{users[index].nickname}}</p>
        {{item.content}}<br/>
        <el-button id="praise" size="small" @click="praise(item)">赞 {{item.praise_num}}</el-button>
        <el-button id="comment" type="text" @click="comment">评论</el-button>
        <!--<el-button id="collect" type="text" @click="collect">收藏</el-button>>-->
        <el-button id="complaint" type="text" @click="complaint">举报</el-button>
        <el-button id="remove" type="text" v-show="activeName!=='江南一点雨'?true:false"
                   @click="removeAnswerDialogShow(index)">删除
        </el-button>
      </div>
    </el-col>
    <el-dialog
      title="提示"
      :visible.sync="complaintVisible"
      width="30%"
    >
      <span>确定要进行举报吗</span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="complaintVisible = false">取 消</el-button>
        <el-button type="primary" @click="complaintVisible = false">确 定</el-button>
       </span>
    </el-dialog>
    <el-dialog
      title="提示"
      :visible.sync="removeArticleVisible"
      width="30%"
    >
      <span>确定要删除吗</span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="removeArticleVisible = false">取 消</el-button>
        <el-button type="primary" @click="removeArticle">确 定</el-button>
       </span>
    </el-dialog>
    <el-dialog
      title="提示"
      :visible.sync="removeAnswerVisible"
      width="30%"
    >
      <span>确定要删除吗</span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="removeAnswerVisible = false">取 消</el-button>
        <el-button type="primary" @click="removeAnswer">确 定</el-button>
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
  import index from "../router";

  export default {
    components: {ElButton},
    methods: {
      goBack() {
        this.$router.go(-1);
      },
      answer(articles) {
        this.$router.push({path: '/answer', query: {aid: this.$route.query.aid, title: articles.title}});
      },
      removeArticleDialogShow() {
        this.removeArticleVisible = true;
      },
      removeAnswerDialogShow(index) {
        this.index = index
        this.removeAnswerVisible = true;
      },
      // 删除问题
      removeArticle() {
        if (this.removeArticleVisible) {
          putRequest('/article/remove/' + this.$route.query.aid).then(resp => {
            if (resp.status === 200) {
              this.$router.push({path: '/articleList'})
              _this.removeArticleVisible = false;
            }
          });
        }
      },
      //  删除回答
      removeAnswer() {
        var _this = this
        if (this.removeAnswerVisible) {
          putRequest('/answer/remove/' + this.answers[this.index].id
          ).then(resp => {
            if (resp.status === 200) {
              _this.answers.splice(_this.index, 1)
              _this.removeAnswerVisible = false;
            }
          });
        }
      },
      praise(item) {
        if (item.num % 2 === 0) {
          item.praise_num = item.praise_num + 1;
          postRequest('/answer/praise', {id: item.id})
          // 偶数次点击按钮--》取消点赞
        } else {
          item.praise_num = item.praise_num - 1;
          postRequest('/answer/cancelPraise', {id: item.id})
        }
        item.num++;
      },
      comment() {
      },
      collect() {
      },
      complaint() {
        this.complaintVisible = true;
      },
    },
    mounted: function () {
      var aid = this.$route.query.aid;
      this.activeName = this.$route.query.an
      var _this = this;
      this.loading = true;
      getRequest("/article/" + aid).then(resp => {
        if (resp.status === 200) {
          _this.article = resp.data.articles;
          _this.answers = resp.data.answerList;
          _this.users = resp.data.userList;
          _this.answers.forEach(function (obj) {
            obj.num = 2
          })
        }
        _this.loading = false;
      }, resp => {
        _this.loading = false;
        _this.$message({type: 'error', message: '页面加载失败!'});
      });
      // todo 获取用户所有关注问题 id，判断本问题是否为用户关注的问题
      // getRequest("/article/").then(resp => {
      //   if (resp.status == 200) {
      //     _this.article = resp.data.articles;
      //   }
      //   _this.loading = false;
      // }, resp => {
      //   _this.loading = false;
      //   _this.$message({type: 'error', message: '页面加载失败!'});
      // });


    }
    ,
    data() {
      return {
        num: 2,
        article: {},
        loading: false,
        activeName: '',
        answers: [],
        users: [],
        complaintVisible: false,
        removeArticleVisible: false,
        removeAnswerVisible: false,
        index: 0
      }
    }
  }
</script>
