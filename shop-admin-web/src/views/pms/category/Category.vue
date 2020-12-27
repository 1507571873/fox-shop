<template>
  <div>
  <div>
    <div class="crumbs">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item>
          <i class="el-icon-lx-cascades"></i> 商品分类
        </el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <div class="handle-box">
      <el-button
          type="primary"
          icon="el-icon-circle-plus"
          class="handle-del mr10"
          @click="saveOrUpdateCategroy"
      >新增分类</el-button>
      <el-button
          type="primary"
          icon="el-icon-delete"
          class="handle-del mr10"
          @click="delAllSelection"
      >批量删除</el-button>
      <el-input v-model="listQuery.name" placeholder="分类名称" class="handle-input mr10"></el-input>
      <el-button type="primary" icon="el-icon-search" @click="handleSearch">查询</el-button>
    </div>
  <el-table
      :data="tableData"
      border
      class="table"
      ref="multipleTable"
      header-cell-class-name="table-header"
      @selection-change="handleSelectionChange">
    <el-table-column type="selection" width="55" align="center"></el-table-column>
    <el-table-column prop="id" label="编号" width="55" align="center"></el-table-column>
    <el-table-column prop="name" label="分类名称"></el-table-column>
    <el-table-column prop="level" label="级别">
      <template slot-scope="scope">
        <span v-if="scope.row.level == 0">一级</span>
        <span v-else>二级</span>
      </template>
    </el-table-column>

    <el-table-column label="分类图标" align="center">
      <template slot-scope="scope">
        <el-image v-if="scope.row.icon != null"
                  class="table-td-thumb"
                  :src="scope.row.icon"
                  :preview-src-list="[scope.row.icon]"
        ></el-image>
      </template>
    </el-table-column>
    <el-table-column prop="productCount" label="商品数量"></el-table-column>
    <el-table-column prop="productUnit" label="数量单位"></el-table-column>
    <el-table-column label="导航栏" width="100" align="center">
      <template slot-scope="scope">
        <el-switch
            @change="handleShowStatusChange(scope.$index, scope.row)"
            :active-value="1"
            :inactive-value="0"
            v-model="scope.row.navStatus">
        </el-switch>
      </template>
    </el-table-column>
    <el-table-column label="是否显示" width="100" align="center">
      <template slot-scope="scope">
        <el-switch
            @change="handleShowStatusChange(scope.$index, scope.row)"
            :active-value="1"
            :inactive-value="0"
            v-model="scope.row.showStatus">
        </el-switch>
      </template>
    </el-table-column>
    <el-table-column prop="sort" label="排序"></el-table-column>

    <el-table-column label="设置" width="180" align="center">
      <template slot-scope="scope">
        <el-button
            v-if="scope.row.level == 0"
            size="mini"
            icon="el-icon-caret-bottom"
            @click="handleChildren( scope.row)"
        >查看下级</el-button>

        <el-button
            size="mini"
            v-else
            icon="el-icon-caret-top"
            @click="handleParent( scope.row)"
        >返回上级</el-button>
      </template>
    </el-table-column>
    <el-table-column label="操作" width="180" align="center">
      <template slot-scope="scope">
        <el-button
            type="text"
            icon="el-icon-edit"
            @click="handleEdit(scope.row)"
        >编辑</el-button>
        <el-button
            type="text"
            icon="el-icon-delete"
            class="red"
            @click="handleDelete(scope.$index, scope.row)"
        >删除</el-button>
      </template>
    </el-table-column>
  </el-table>
  <div class="pagination-container">
    <!--分页-->
    <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page=listQuery.current
        :page-sizes=pageSizes
        :page-size=listQuery.size
        layout="total, sizes, prev, pager, next, jumper"
        :total=total>
    </el-pagination>
  </div>
  </div>
    <!-- 编辑弹出框 -->
    <el-dialog title="新增商品分类" :visible.sync="editVisible" width="40%">
      <el-form ref="categoryForm" :model="categoryForm" label-width="120px">
        <el-form-item label="分类名称" prop="name">
          <el-input v-model="categoryForm.name"></el-input>
        </el-form-item>
        <el-form-item label="上级分类" prop="parentId">
          <el-select v-model="categoryForm.parentId" placeholder="请选择上级分类">
            <el-option label="无上级分类" value="-1"></el-option>
            <el-option v-for="category in parentCategoryList"
                       :label="category.name"
                       :key="category.id"
                       :value="category.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="数量单位" prop="productUnit">
          <el-input v-model="categoryForm.productUnit"></el-input>
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input v-model="categoryForm.sort"></el-input>
        </el-form-item>
        <el-form-item label="是否显示" prop="showStatus">
          <el-radio-group v-model="categoryForm.showStatus">
            <el-radio :label="1" >是</el-radio>
            <el-radio :label="0" >否</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="显示在导航栏"  prop="navStatus">
          <el-radio-group v-model="categoryForm.navStatus">
            <el-radio :label="1" >是</el-radio>
            <el-radio :label="0" >否</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="分类图标" prop="icon">
          <single-upload v-model="categoryForm.icon"></single-upload>
        </el-form-item>
        <el-form-item label="关键词" prop="keywords">
          <el-input v-model="categoryForm.keywords"></el-input>
        </el-form-item>
        <el-form-item label="分类描述" prop="description">
          <el-input
              type="textarea"
              :rows="2"
              placeholder="请输入分类描述"
              v-model="categoryForm.description">
          </el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
                <el-button @click="editVisible = false">取 消</el-button>
                <el-button type="primary" @click="saveEdit">确 定</el-button>
            </span>
    </el-dialog>
  </div>
</template>

<script>
import {deleteData,saveOrUpdateData,queryDataById,queryData} from '@/api'
import SingleUpload from "../../../components/SingleUpload/SingleUpload"
export default {
  name: "Category",
  components:{SingleUpload},
  data() {
    return {
      //表格
      tableData: [],
      listQuery: {
        current: 1,
        size: 10,
        level:0,
        pid:null,
      },
      //弹框的控制
      editVisible:false,
      pageSizes: [5, 10, 15, 20],
      total: 0,
      //表单
      categoryForm:{
        name:"",
        parentId:"-1",
        icon:"",
        showStatus:1,
        navStatus:1,
        productUnit:"",
        keywords:"",
        description:""
      },
      parentCategoryList:[],
    }
  },
  methods: {
    //查询 分页查询
    queryCategory(){
      queryData(this.listQuery,"/fox-shop-admin/pms/category").then(result=>{
        this.tableData=result.data.records
        this.total = result.data.total;
      })
    },
    //查询下拉框
    queryParentCategoryList(){
      queryData(this.listQuery,"/fox-shop-admin/pms/category/parent").then(result=>{
        this.parentCategoryList=result.data;
      })
    },
    //新增和修改的弹框
    saveOrUpdateCategroy(){
      //刷新表单
      this.restForm();
      //下拉框
      this.queryParentCategoryList();
      //开启弹框
      this.editVisible=true;
      //刷新表单
      this.restForm();
    },
    //新增和修改的提交
    saveEdit(){
      saveOrUpdateData(this.categoryForm,"/fox-shop-admin/pms/category").then(result=>{
        //判断状态码是否为200 200为新增成功
        if (result.code===200){
          this.$message(result.msg)
          //关闭弹框
          this.editVisible=false;
          //刷新表格
          this.queryCategory();
          //清空表单
          this.restForm();
        }
      })
    },
    //清空表单
    restForm(){
      if (this.$refs.categoryForm !== undefined){
        this.$refs.categoryForm.resetFields();
      }
    },
    //批量删除
    delAllSelection(){
      alert("批量删除");
    },
    //修改回显
    handleEdit(row){
      queryDataById(row.id,"/fox-shop-admin/pms/category/ById").then(result=>{
        if (result.code===200){
          //新增和修改的弹框
          this.saveOrUpdateCategroy();
          this.categoryForm=result.data
        }
      })

    },
    //删除
    handleDelete(){
      alert("删除")
    },
    //下一级
    handleChildren(row){
      this.$set(this.listQuery, 'pid', row.id);
      this.$set(this.listQuery, 'level', null);
      this.$set(this.listQuery, 'current', 1);
      this.queryCategory();
    }
    //上一级
    ,handleParent(row){
      this.$set(this.listQuery, 'pid', null);
      this.$set(this.listQuery, 'level', 0);
      this.$set(this.listQuery, 'current', 1);
      this.queryCategory();
    }
    //上一页下一页
    ,handleCurrentChange(val){
      this.$set(this.listQuery, 'current', val);
      this.queryCategory();
    },
    //每页显示几条
    handleSizeChange(val){
      this.$set(this.listQuery, 'current', 1);
      this.$set(this.listQuery, 'size', val);
      this.queryCategory();
    },
  },
  mounted() {
    this.queryCategory();
  }
}
</script>

<style scoped>
.handle-box {
  margin-bottom: 20px;
}

.handle-select {
  width: 120px;
}

.handle-input {
  width: 300px;
  display: inline-block;
}
.table {
  width: 100%;
  font-size: 14px;
}
.red {
  color: #ff0000;
}
.mr10 {
  margin-right: 10px;
}

{
  align:center;
}
.table-td-thumb {
  display: block;
  margin: auto;
  width: 40px;
  height: 40px;
}
</style>