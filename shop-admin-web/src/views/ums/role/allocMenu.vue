<template>
  <el-card class="form-container" shadow="never">
    <el-tree
        :data="menuTreeList"
        show-checkbox
        default-expand-all
        node-key="id"
        ref="tree"
        highlight-current
        :props="defaultProps">
    </el-tree>
    <div style="margin-top: 20px" align="center">
      <el-button type="primary" @click="handleSave()">保存</el-button>
      <el-button @click="handleClear()">清空</el-button>
    </div>

  </el-card>
</template>

<script>
  import {queryMenuTree} from '../../../api/ums/menu/menu'
  import {queryRoleOrMenuData,saveData} from '../../../api/ums/roleOrMenu/roleOrMenu'
export default {
  name: "allocMenu",
  data() {
    return {
      menuTreeList:[],
      defaultProps: {
        children: 'children',
        label: 'label'
      },
      menuForm:{
        roleId:null,
        roleOrMenuList:[]
      }
    }
  },
  methods: {
    //查询
    queryTerrData(){
      queryMenuTree().then(result=>{
        this.menuTreeList=result.data
      })
    },
    queryRoleOrMenu(roleId){
      queryRoleOrMenuData(roleId).then(result=>{
        let menuList = result.data;
        let checkedMenuIds=[];
        if(menuList!=null&&menuList.length>0){
          for(let i=0;i<menuList.length;i++){
              checkedMenuIds.push(menuList[i].menuId);
          }
        }
        this.$refs.tree.setCheckedKeys(checkedMenuIds);
      });
    },
    //保存
    handleSave(){
      //获取选中的节点
      let checkedNodes =this.$refs.tree.getCheckedNodes();
      //alert(JSON.stringify(checkedNodes))
     //创建一个set集合 set集合可以去重
      let checkedMenuIds = new Set();
      //判断checkedNodes不为空
      if (checkedNodes!==null&&checkedNodes.length>0){
        //循环checkedNodes
        for (let i = 0; i < checkedNodes.length; i++) {
          //一条一条的赋值
          let checkedNode =checkedNodes[i];
          //把选中的节点的id放入set中
          checkedMenuIds.add(checkedNode.id);
        }
      }
      checkedMenuIds.forEach(s=>{
        this.menuForm.roleOrMenuList.push({
          roleId:this.$route.query.roleId,
          menuId:s
        })
      })
      saveData(this.menuForm).then(result=>{
        if (result.code===200){
          this.$message.success(result.msg)
          this.queryRoleOrMenu( this.menuForm.roleId)
          this.menuForm.roleOrMenuList=[];
        }
      })
    },
    //清空
    handleClear(){
      this.$refs.tree.setCheckedKeys([]);
    },
  },
  mounted() {
    this.menuForm.roleId = this.$route.query.roleId;
    this.queryTerrData();
    this.queryRoleOrMenu( this.menuForm.roleId);
  }
}
</script>

<style scoped>

</style>