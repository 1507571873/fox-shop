<template>
  <div class="app-container">
    <el-card class="operate-container" shadow="never">
      <i class="el-icon-tickets" style="margin-top: 5px"></i>
      <span style="margin-top: 5px">数据列表</span>
      <el-button
        class="btn-add"
        @click="handleAddMenu()"
        size="mini">
        添加
      </el-button>
    </el-card>
    <div class="table-container">
      <el-table ref="menuTable"
                style="width: 100%"
                :data="list"
                v-loading="listLoading" border>
        <el-table-column label="编号" width="100" align="center">
          <template slot-scope="scope">{{scope.row.id}}</template>
        </el-table-column>
        <el-table-column label="菜单名称" align="center">
          <template slot-scope="scope">{{scope.row.title}}</template>
        </el-table-column>
        <el-table-column label="菜单级数" width="100" align="center">
          <template slot-scope="scope">{{scope.row.level | levelFilter}}</template>
        </el-table-column>
        <el-table-column label="前端名称" align="center">
          <template slot-scope="scope">{{scope.row.name}}</template>
        </el-table-column>
        <el-table-column label="前端图标" width="100" align="center">
          <template slot-scope="scope"><svg-icon :icon-class="scope.row.icon"></svg-icon></template>
        </el-table-column>
        <el-table-column label="是否显示" width="100" align="center">
          <template slot-scope="scope">
            <el-switch
              @change="handleHiddenChange(scope.$index, scope.row)"
              :active-value="0"
              :inactive-value="1"
              v-model="scope.row.hidden">
            </el-switch>
          </template>
        </el-table-column>
        <el-table-column label="排序" width="100" align="center">
          <template slot-scope="scope">{{scope.row.sort }}</template>
        </el-table-column>
        <el-table-column label="设置" width="120" align="center">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              :disabled="scope.row.level | disableNextLevel"
              @click="handleShowNextLevel(scope.$index, scope.row)">查看下级
            </el-button>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" align="center">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              @click="handleUpdate(scope.$index, scope.row)">编辑
            </el-button>
            <el-button
              size="mini"
              type="text"
              @click="handleDelete(scope.$index, scope.row)">删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="pagination-container">
      <!--分页-->
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page=listQuery.current
          :page-sizes=listQuery.pageSizes
          :page-size=listQuery.size
          layout="total, sizes, prev, pager, next, jumper"
          :total=total>
      </el-pagination>
    </div>
    <el-dialog
        :title="isEdit?'编辑角色':'添加角色'"
        :visible.sync="dialogVisible"
        width="40%">
      <el-form :model="menu"
               ref="menuFrom"
               label-width="150px">
        <el-form-item label="菜单名称：" prop="title">
          <el-input v-model="menu.title"></el-input>
        </el-form-item>
        <el-form-item label="上级菜单：">
          <el-select v-model="menu.parentId"
                     placeholder="请选择菜单">
            <el-option label="无上级分类" :value="0"></el-option>
            <el-option
                v-for="item in selectMenuList"
                :key="item.id"
                :label="item.title"
                :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="前端名称：" prop="name">
          <el-input v-model="menu.name"></el-input>
        </el-form-item>
        <el-form-item label="前端图标：" prop="icon">
          <el-input v-model="menu.icon" style="width: 80%"></el-input>
          <svg-icon style="margin-left: 8px" :icon-class="menu.icon"></svg-icon>
        </el-form-item>
        <el-form-item label="是否显示：">
          <el-radio-group v-model="menu.hidden">
            <el-radio :label="0">是</el-radio>
            <el-radio :label="1">否</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="排序：">
          <el-input v-model="menu.sort"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false" size="small">取 消</el-button>
        <el-button type="primary" @click="handleDialogConfirm()" size="small">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
  import {queryMenuPageData,saveOrUpdateData,queryDataById,queryMenuLevelData} from '../../../api/ums/menu/menu'
  const defaultMenu = {
    title: '',
    parentId: 0,
    name: '',
    icon: '',
    hidden: 0,
    sort: 0
  };
  export default {
    name: "menuList",
    data() {
      return {
        selectMenuList:[],
        isEdit:false,
        menu: Object.assign({}, defaultMenu),
        list: null,
        total: null,
        listLoading: true,
        listQuery: {
          current: 1,
          size: 10,
          level:0,
          pid:null
        },
        dialogVisible:false,
      }
    },
    methods: {
      //查询 分页查询
      queryMenuData(){
        queryMenuPageData(this.listQuery).then(result=>{
          if (result.code===200){
            this.listLoading=false;
            this.list=result.data.records;
            this.total=result.data.total;
          }
        })
      },
      queryMenuLevelData(){
        queryMenuLevelData(0).then(result=>{
          this.selectMenuList=result.data;
        })
      },
      //新增
      handleAddMenu(){
        this.menu=Object.assign({}, defaultMenu);
        this.queryMenuLevelData();
        this.dialogVisible=true;
      },
      //表单提交
      handleDialogConfirm(){
        saveOrUpdateData(this.menu).then(result=>{
          if (result.code===200){
              this.$message(result.msg);
            this.dialogVisible=false;
            this.queryMenuData();
          }
        })
      },
      //是否显示
      handleHiddenChange(){},
      //查看下级
      handleShowNextLevel(index,row){
        this.listQuery.level=1;
        this.listQuery.pid=row.id;
        this.queryMenuData();
      },
      //编辑
      handleUpdate(index,row){
        this.isEdit=true;
        queryDataById(row.id).then(result=>{
          if (result.code===200){
            this.handleAddMenu();
            this.menu=result.data;
          }
        })
      },
      //删除
      handleDelete(){},
      //每页展示条数
      handleSizeChange(val) {
        this.$set(this.listQuery,"size",val)
        this.$set(this.listQuery,"current",1)
        this.queryMenuData();
      },
      //上一页下一页
      handleCurrentChange(val) {
        this.$set(this.listQuery,"current",val)
        this.queryMenuData();
      },
    },
    filters: {
      levelFilter(value) {
        if (value === 0) {
          return '一级';
        } else if (value === 1) {
          return '二级';
        }
      },
      disableNextLevel(value) {
        if (value === 0) {
          return false;
        } else {
          return true;
        }
      }
    },
    mounted() {
      this.queryMenuData();
    }
  }
</script>

<style scoped>

</style>
