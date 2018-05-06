<template>
  <div style="margin: 30px">
    <div>
      首页
    </div>

    <div align="left">
      <img width="60" height="60"
           src="https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1514093920321&di=913e88c23f382933ef430024afd9128a&imgtype=0&src=http%3A%2F%2Fp.3761.com%2Fpic%2F9771429316733.jpg"/>
      <el-button style="margin-left: 220px" onclick="attentionUser">
        关注用户
      </el-button>
      <div>
        <span>江南一点雨</span>
        <span style="margin-left: 200px">获得 8 次赞</span><br/>
        <span>惠州学院大四学生</span><span style="margin-left: 150px">获得 15 次收藏</span>
      </div>

    </div>

    <el-tabs style="margin-top: 10px" v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="关注话题" name="first">关注话题</el-tab-pane>
      <el-tab-pane label="关注问题" name="second">关注问题</el-tab-pane>
      <el-tab-pane label="回答" name="third">回答</el-tab-pane>
      <el-tab-pane label="提问" name="forth">提问</el-tab-pane>
    </el-tabs>

  </div>

</template>
<script>
  import {postRequest} from '../utils/api'
  import {putRequest} from '../utils/api'
  import {deleteRequest} from '../utils/api'
  import {getRequest} from '../utils/api'
  import {uploadFileRequest} from '../utils/api'
  import ElButton from "element-ui/packages/button/src/button";

  export default {
    components: {ElButton},
    mounted: function () {
      var _this = this;
      // 请求用户数据
      getRequest('/user/' + this.$route.query.id).then(resp => {
        _this.user = resp.data;
        return resp.data;
        // 请求「提问」、「关注问题」数据
      }).then(user => {
        getRequest('/article/getArticlesByIds', {
          questionIds: user.question_ids,
          attentionQids: user.attention_qids
        }).then(resp => {
          _this.questionArticles = resp.data.questionArticles;
          _this.attentionArticles = resp.data.attentionArticles;
          return user;
          // 请求「回答」数据
        }).then(user => {
          getRequest('/answer/getAnswerByUid', {uid: user.id}).then(resp => {
            _this.answerArticles = resp.data.answerArticles;
          });
          // 请求「关注话题」数据
          getRequest('/category/getCategoriesByIds', {ids: user.attention_cids}).then(resp => {
            _this.attentionTopic = resp.data.attentionTopic;
          })
        });
      })


    },

    methods: {
      attentionUser() {
        var _this = this;
        postRequest('/attentionUser/', {uid: this.user.id}).then(resp => {
          if (resp.status === 200) {
            var json = resp.data;
            _this.$message({type: json.status, message: json.msg});
          }
        }, resp => {
          if (resp.response.status === 403) {
            _this.$message({
              type: 'error',
              message: resp.response.data
            });
          }
        });
      },
      handleClick(tab, event) {
        console.log(tab, event);
      }
    },
    data() {
      return {
        activeName: 'first',
        user: {
          id: '',
          nickname: '',
          username: '',
          introduction: '',
          praise_sum: 0,
          collect_sum: 0,
          attention_cids: '',
          attention_qids: '',
          attention_ids: '',
          question_ids: '',
          be_attention_ids: '',
        },
        questionArticles: [],
        attentionArticles: [],
        answerArticles: [],
        attentionTopic: [],
      }
    }
  }
</script>
<style>
  .post-article > .main > #editor {
    width: 100%;
    height: 450px;
    text-align: left;
  }
</style>
