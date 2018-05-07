<template>
  <div style="margin: 20px;margin-top: -40px">
    <div align="left">
      <el-button type="text" style="margin-bottom: 35px;font-size: large" @click="back">
        首页
      </el-button>
    </div>

    <div align="left">
      <div>
        <img width="60" height="60"
             src="https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1514093920321&di=913e88c23f382933ef430024afd9128a&imgtype=0&src=http%3A%2F%2Fp.3761.com%2Fpic%2F9771429316733.jpg"/>
      </div>

      <table style="width: 100%;">
        <tr>
          <td style="width: 200px;"></td>
          <td style="width: 200px;"></td>
          <td></td>
        </tr>
        <tr>
          <td>江南一点雨</td>
          <td>获得 8 次赞</td>
          <td></td>
        </tr>
        <tr>
          <td>惠州学院大四学生</td>
          <td>获得 11 次收藏</td>
          <td style="padding-right: 60px">
            <el-button style="float:right;" @click="attentionUser" v-if="user.id!==this.$route.query.id">关注用户
            </el-button>
          </td>
        </tr>
      </table>

      <el-tabs style="margin-top: 10px;width: 100%; display:inline-block" v-model="activeName" @tab-click="handleClick">
        <el-tab-pane label="关注话题" name="first">
          <table>
            <tr v-for="(item,ind) in attentionTopic">
              <td>
                <el-button @click="first(item.id)" type="text">{{item.cateName}}</el-button>
              </td>
            </tr>
          </table>
        </el-tab-pane>
        <el-tab-pane label="关注问题" name="second">
          <table>
            <tr v-for="(item,ind) in attentionArticles">
              <td>
                <el-button @click="second(item.id)" type="text">{{item.title}}</el-button>
              </td>
            </tr>
          </table>
        </el-tab-pane>
        <el-tab-pane label="回答" name="third">
          <table>
            <tr v-for="(item,ind) in answerArticles">
              <td>
                <el-button @click="second(item.aid)" type="text">{{item.title}}</el-button>
              </td>
            </tr>
          </table>
        </el-tab-pane>
        <el-tab-pane label="提问" name="forth">
          <table>
            <tr v-for="(item,ind) in questionArticles">
              <td>
                <el-button @click="second(item.id)" type="text">{{item.title}}</el-button>
              </td>
            </tr>
          </table>
        </el-tab-pane>
      </el-tabs>

    </div>
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
            _this.answerArticles = resp.data;
          })
          // 请求「关注话题」数据
          getRequest('/category/getCategoriesByIds', {ids: user.attention_cids}).then(resp => {
            _this.attentionTopic = resp.data;
          })
        });
      })

    },

    methods: {
      first(cid) {
        this.$router.replace({path: '/articleList', query: {cid: cid}});
      },
      second(id) {
        this.$router.push({path: '/blogDetail', query: {aid: id}})
      },
      back() {
        this.$router.replace('/articleList');
      },
      attentionUser() {
        var _this = this;
        postRequest('/attentionUser', {id: _this.user.id, uid: _this.$route.query.id})
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
        attentionTopic: []
      }
    }
  }
</script>
