<template>
  <el-tree :data="formTreeData" @node-click="handleNodeClick"></el-tree>

</template>
<script>
  import {getRequest} from '../utils/api'

  export default {
    data() {
      return {
        formTreeData: [],

        data: [],

      };
    },
    mounted: function () {
      var _this = this;
      getRequest("/topic").then(resp => {
        if (resp.status == 200) {
           let a = _this.getData(resp.data, 'parentId', 'id');
           console.log(a)
          _this.formTreeData = a;
        }
      }).catch(err => {
        console.log(arguments);
      })
    },

    methods: {
      handleNodeClick(data) {
        // todo 跳转
        console.log(data)
      },

      // formTreeData,
      getData :function (data, parentId, childId) {
        var tree = [];
        var tmp = {};
        //分离出父节点
        data.forEach(function (i, ind) {
          if (i[parentId]) {
            if (tmp[i[parentId]]) {
              tmp[i[parentId]].push(i);
            } else {
              tmp[i[parentId]] = [i];
            }
          } else {
            tree.push({
              info: i,
              label: i.name,
              //nodes : []
            });
          }
        });
        /**
         * 递归将公司信息形成树
         * 先 找出父节点为 parentId 的集合
         * 然后遍历集合中的节点，获取到每一个的 id
         * 将新获取到的 id 作为 父节点，重新进入递归
         * */
        var resFn = function (parentId, tmp) {
          var nodes = [],
            cnodes;
          if (parentId in tmp) {
            var tmp_ = tmp[parentId];
            delete tmp[parentId];
            for (var i = 0; i < tmp_.length; i++) {
              var node = {
                label: tmp_[i].name,
                info: tmp_[i]
              };
              cnodes = resFn(tmp_[i][childId], tmp);
              if (cnodes.length) {
                node.children = cnodes;
              }
              nodes.push(node);
            }
            return nodes;
          } else {
            return [];
          }
        };
        var cnode;
        for (var i = 0; i < tree.length; i++) {
          cnode = resFn(tree[i].info[childId], tmp);
          if (cnode.length) {
            tree[i].children = cnode;
          }
        }
        return tree;
      }

    }
  }



</script>
