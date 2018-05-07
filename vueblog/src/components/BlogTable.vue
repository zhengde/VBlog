<style type="text/css" xmlns:background-color="http://www.w3.org/1999/xhtml">
  .blog_table_footer {
    display: flex;
    box-sizing: content-box;
    padding-top: 10px;
    padding-bottom: 0px;
    margin-bottom: 0px;
    justify-content: space-between;
  }
</style>
<template>
  <div>
    <div style="display: flex;justify-content: flex-start">
      <el-input
        placeholder="通过标题搜索该分类下的博客..."
        prefix-icon="el-icon-search"
        v-model="keywords" style="width: 400px" size="mini">
      </el-input>
      <el-button type="primary" icon="el-icon-search" size="mini" style="margin-left: 3px" @click="searchClick">搜索
      </el-button>
      <el-button type="primary" size="mini" style="margin-left: 3px" @click="questClick">提问
      </el-button>
    </div>
    <el-table
      ref="multipleTable"
      :data="articles"
      tooltip-effect="dark"
      style="width: 100%;overflow-x: hidden; overflow-y: hidden;"
      max-height="390"
      @selection-change="handleSelectionChange" v-loading="loading">
      <el-table-column
        width="35" align="left" v-if="showEdit || showDelete">
        <!--type="selection"-->
      </el-table-column>
      <el-table-column
        label="标题"
        width="400" align="left">
        <template slot-scope="scope"><span style="color: #409eff;cursor: pointer" @click="itemClick(scope.row)">{{ scope.row.title}}</span>
        </template>
      </el-table-column>
      <el-table-column
        prop="cateName"
        label="所属话题"
        width="120" align="left">
      </el-table-column>
      <el-table-column
        prop="nickname"
        label="题主"
        width="120" align="left">
      </el-table-column>
      <el-table-column
        label="创建时间" width="140" align="left">
        <template slot-scope="scope">{{ scope.row.editTime | formatDateTime}}</template>
      </el-table-column>
      <el-table-column label="操作" align="left">
        <template slot-scope="scope">
          <el-button
            size="mini"
            @click="handleEdit(scope.$index, scope.row)">
            <div>
              <span v-show="!scope.row.forcus">关注问题</span>
              <span v-show="scope.row.forcus" style="color:#00BFFF;">已关注</span>
            </div>
          </el-button>
          <el-button
            size="mini"
            type="danger"
            @click="handleDelete(scope.$index, scope.row)" v-if="showDelete">删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="blog_table_footer">
      <!--<el-button type="danger" size="mini" style="margin: 0px;" v-show="this.articles.length>0 && showDelete"-->
      <!--:disabled="this.selItems.length==0" @click="deleteMany">批量删除-->
      <!--</el-button>-->
      <!--<span></span>-->
      <el-pagination
        background
        :page-size="pageSize"
        layout="prev, pager, next"
        :total="totalCount" @current-change="currentChange" v-show="this.articles.length>0">
      </el-pagination>
    </div>
  </div>
</template>

<script>
  import {getRequest} from '../utils/api';
  import ElButton from "element-ui/packages/button/src/button";
  import {postRequest} from '../utils/api'
  import {putRequest} from '../utils/api'
  import {deleteRequest} from '../utils/api'

  export default {
    data() {
      return {
        attentionId: [],
        flag: false,
        articles: [],
        selItems: [],
        loading: false,
        currentPage: 1,
        totalCount: -1,
        pageSize: 6,
        keywords: '',
        dustbinData: []
      }
    },
    mounted: function () {
      var _this = this;
      this.loading = true;
      this.loadBlogs(1, this.pageSize);
      this.attention();
      var _this = this;
      window.bus.$on('blogTableReload', function () {
        _this.loading = true;
        _this.loadBlogs(_this.currentPage, _this.pageSize);
      });
    },
    methods: {
      attention() {
        var _this = this;
        getRequest('/currentUserName').then(resp => {
          if (resp.status === 200) {
            return resp.data;
          }
          // 获取该用户是否已关注过该问题，是则取消关注，否则关注
        }).then(username => {
          getRequest('/loadAttention', {
            username,
          }).then(resp => {
            if (resp.status === 200) {
              var data = resp.data.attention_qids;
              _this.attentionId = data.split(",");
              for (var i = 0; i < _this.articles.length; i++) {
                if (_this.attentionId.indexOf(_this.articles[i].id + "") + 1) {
                  _this.articles[i].forcus = true;
                } else {
                  _this.articles[i].forcus = false;
                }
              }
            }
          })
        })
      },
      questClick() {
        this.$router.push({path: '/editBlog'});
      },
      searchClick() {
        this.loadBlogs(1, this.pageSize);
      },
      itemClick(row) {
        this.$router.push({path: '/blogDetail', query: {aid: row.id}})
      },
      deleteMany() {
        var selItems = this.selItems;
        for (var i = 0; i < selItems.length; i++) {
          this.dustbinData.push(selItems[i].id)
        }
        this.deleteToDustBin(selItems[0].state)
      },
      //翻页
      currentChange(currentPage) {
        this.currentPage = currentPage;
        this.loading = true;
        this.loadBlogs(currentPage, this.pageSize);
      },
      loadBlogs(page, count) {
        var _this = this;
        var url = '';
        if (_this.state == -2) {
          url = "/admin/article/all" + "?page=" + page + "&count=" + count + "&keywords=" + this.keywords;
        } else {
          if (this.cid) {
            url = "/article/category?page=" + 1 + "&count=" + 6 + "&cid=" + this.cid;
          } else {
            url = "/article/all?state=" + this.state + "&page=" + page + "&count=" + count + "&keywords=" + this.keywords;
          }
        }
        getRequest(url).then(resp => {
          _this.loading = false;
          if (resp.status == 200) {
            resp.data.articles.forEach(function (obj) {
              obj.forcus = false;
            });
            _this.articles = resp.data.articles;
            _this.totalCount = resp.data.totalCount;
          } else {
            _this.$message({type: 'error', message: '数据加载失败!'});
          }
        }, resp => {
          _this.loading = false;
          if (resp.response.status == 403) {
            _this.$message({type: 'error', message: resp.response.data});
          } else {
            _this.$message({type: 'error', message: '数据加载失败!'});
          }
        }).catch(resp => {
          //压根没见到服务器
          _this.loading = false;
          _this.$message({type: 'error', message: '数据加载失败!'});
        })
      },
      handleSelectionChange(val) {
        this.selItems = val;
      },
      handleEdit(index, row) {
        var _this = this;
        var url = '/currentUserName';
        getRequest(url).then(resp => {
          if (resp.status === 200) {
            return resp.data;
          }
          // 获取该用户是否已关注过该问题，是则取消关注，否则关注
        }).then(username => {
          getRequest('/isAttention', {
            username,
            aid: row.id
          }).then(resp => {
            if (resp.status === 200) {
              var data = resp.data;
              this.attentionId = data.split(",");
              for (var i = 0; i < _this.articles.length; i++) {
                if (_this.attentionId.indexOf(_this.articles[i].id + "") + 1) {
                  _this.articles[i].forcus = true;
                } else {
                  _this.articles[i].forcus = false;
                }
              }
            }
          })
        })
      },
      handleDelete(index, row) {
        this.dustbinData.push(row.id);
        this.deleteToDustBin(row.state);
      },
      deleteToDustBin(state) {
        var _this = this;
        this.$confirm(state != 2 ? '将该文件放入回收站，是否继续?' : '永久删除该文件, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          _this.loading = true;
          var url = '';
          if (_this.state == -2) {
            url = "/admin/article/dustbin";
          } else {
            url = "/article/dustbin";
          }
          putRequest(url, {aids: _this.dustbinData, state: state}).then(resp => {
            if (resp.status == 200) {
              var data = resp.data;
              _this.$message({type: data.status, message: data.msg});
              if (data.status == 'success') {
                window.bus.$emit('blogTableReload')//通过选项卡都重新加载数据
              }
            } else {
              _this.$message({type: 'error', message: '删除失败!'});
            }
            _this.loading = false;
            _this.dustbinData = []
          }, resp => {
            _this.loading = false;
            _this.$message({type: 'error', message: '删除失败!'});
            _this.dustbinData = []
          });
        }).catch(() => {
          _this.$message({
            type: 'info',
            message: '已取消删除'
          });
          _this.dustbinData = []
        });
      }
    },
    computed: {
      cid: function () {
        return this.$route.query.cid;
      }
    },
    props: ['state', 'showEdit', 'showDelete', 'activeName']
  }
</script>
