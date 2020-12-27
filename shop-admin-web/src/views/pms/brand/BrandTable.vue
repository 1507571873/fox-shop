<template>
  <div class="app-container">
    <el-card class="filter-container" shadow="never">
      <div>
        <i class="el-icon-search"></i>
        <span>筛选搜索</span>
        <el-button
            style="float: right"
            @click="searchBrandList()"
            type="primary"
            size="small">
          查询结果
        </el-button>
      </div>
      <div style="margin-top: 15px">
        <el-form :inline="true" :model="listQuery" size="small" label-width="140px">
          <el-form-item label="输入搜索：">
            <el-input style="width: 203px" v-model="listQuery.keyword" placeholder="品牌名称/关键字"></el-input>
          </el-form-item>
        </el-form>
      </div>
    </el-card>
    <el-card class="operate-container" shadow="never">
      <i class="el-icon-tickets"></i>
      <span>数据列表</span>
      <el-button
          style="float: right"
          size="small"
          @click="saveUpdateBrandData()">新增
      </el-button>
    </el-card>
    <div class="table-container">
      <el-table ref="brandTable"
                :data="brandTable"
                style="width: 100%"
                @selection-change="handleSelectionChange"
                border>
        <el-table-column type="selection" width="60" align="center"></el-table-column>
        <el-table-column label="编号" width="100" align="center">
          <template slot-scope="scope">{{ scope.row.id }}</template>
        </el-table-column>
        <el-table-column label="品牌名称" align="center">
          <template slot-scope="scope">{{ scope.row.name }}</template>
        </el-table-column>
        <el-table-column label="品牌首字母" width="100" align="center">
          <template slot-scope="scope">{{ scope.row.firstLetter }}</template>
        </el-table-column>
        <el-table-column label="排序" width="100" align="center">
          <template slot-scope="scope">{{ scope.row.sort }}</template>
        </el-table-column>
        <el-table-column label="品牌制造商" width="100" align="center">
          <template slot-scope="scope">
            <el-switch
                @change="handleFactoryStatusChange(scope.$index, scope.row)"
                :active-value="1"
                :inactive-value="0"
                v-model="scope.row.factoryStatus">
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
        <el-table-column label="相关" width="220" align="center">
          <template slot-scope="scope">
            <span>商品：</span>
            <el-button
                size="mini"
                type="text"
                @click="getProductList(scope.$index, scope.row)">100
            </el-button>
            <span>评价：</span>
            <el-button
                size="mini"
                type="text"
                @click="getProductCommentList(scope.$index, scope.row)">1000
            </el-button>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" align="center">
          <template slot-scope="scope">
            <el-button
                size="mini"
                @click="updateBrandData(scope.row.id)">编辑
            </el-button>
          <!--<el-button
                size="mini"
                type="danger"
                @click="handleDelete(scope.row)">删除
            </el-button>-->
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
    <el-dialog title="商品新增" :visible.sync="dialogFormVisible" width="40%">
      <el-form ref="BrandForm" :model="BrandForm" >
        <el-input v-model="BrandForm.id" prop="编号" type="hidden"></el-input>
        <el-form-item label="商品名称" prop="name">
          <el-input v-model="BrandForm.name"  ></el-input>
        </el-form-item>
        <el-form-item label="首字母" prop="firstLetter">
          <el-input v-model="BrandForm.firstLetter" autocomplete="off" ></el-input>
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input v-model="BrandForm.sort" autocomplete="off" ></el-input>
        </el-form-item>
        <el-form-item label="是否为品牌制造商" prop="factoryStatus">
          <el-radio-group v-model="BrandForm.factoryStatus">
            <el-radio :label="1" border style="width:70px;right: -50px"  >是</el-radio>
            <el-radio :label="0" border style="width:70px;right: -50px">否</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="是否显示" prop="showStatus">
          <el-radio-group v-model="BrandForm.showStatus">
            <el-radio :label="1" border  style="width:70px;right: -50px">是</el-radio>
            <el-radio :label="0" border style="width:70px;right: -50px">否</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="产品数量" prop="productCount">
          <el-input v-model="BrandForm.productCount" autocomplete="off" ></el-input>
        </el-form-item>
        <el-form-item label="产品评论量" prop="productCommentCount">
          <el-input v-model="BrandForm.productCommentCount" autocomplete="off" ></el-input>
        </el-form-item>
        <el-form-item label="品牌LOGO：" prop="logo">
          <single-upload v-model="BrandForm.logo"></single-upload>
        </el-form-item>
        <el-form-item label="品牌专区大图：">
          <single-upload v-model="BrandForm.bigPic"></single-upload>
        </el-form-item>
        <el-form-item label="品牌故事：">
          <el-input
              placeholder="请输入内容"
              type="textarea"
              v-model="BrandForm.brandStory"
              :autosize="true"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="addBrandData()">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {deleteData,saveOrUpdateData,queryDataById,queryData} from '@/api'
  import SingleUpload from "../../../components/SingleUpload/SingleUpload"
  export default {
  name: "BrandTable",
    components: {
      SingleUpload
    },
  data() {
    return {
      //表格
      brandTable: [],
      listQuery: {
        current: 1,
        size: 10
      },
      pageSizes: [5, 10, 15, 20],
      total:0,
      //form表单
      BrandForm:{
        id:'',
        name:'',
        firstLetter:'',
        sort:'',
        factoryStatus:'',
        showStatus:'',
        productCount:'',
        productCommentCount:'',
        logo:null,
        bigPic:null,
        brandStory:"",
      },
      //控制新增弹框的开关
      dialogFormVisible : false,
    }
  },
  methods: {
    //批量删除
    handleSelectionChange() {
    },
    //弹框
    saveUpdateBrandData(){
      this.restForm();
      this.dialogFormVisible=true
    },
    //新增
    addBrandData(){
      saveOrUpdateData(this.BrandForm,"/fox-shop-admin/pms/brand").then(result => {
        if (result.code===200){
          this.dialogFormVisible=false
          this.$message(result.msg);
          this.queryUserDate();
        }
      })
    },
    //修改回显
    updateBrandData(id){
      this.dialogFormVisible=true
      queryDataById(id,"/fox-shop-admin/pms/brand/update").then(result => {
        if (result.code===200){
          this.BrandForm = result.data;
        }
      })
    },
    //清空表单
    restForm(){
      if (this.$refs.BrandForm !== undefined){
        this.$refs.BrandForm.resetFields();
      }
    },
    //分页查询
    queryUserDate() {
      queryData(this.listQuery,"/fox-shop-admin/pms/brand").then(result=>{
        this.brandTable = result.data.records;
        this.total = result.data.data.total;
      })
    },
    //条件查询
    searchBrandList() {
    },
    //删除
    /*handleDelete(row){
      this.$confirm('是否要删除该品牌', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteData(row.id,"/fox-shop-admin/pms/brand").then(result => {
          this.$message({
            message: result.msg,
            type: 'success',
          });
          this.queryUserDate();
        });
      });
    },*/
    //每页展示条数
    handleSizeChange(val) {
      this.$set(this.listQuery,"size",val)
      this.$set(this.listQuery,"current",1)
      this.queryUserDate();
    },
    //上一页下一页
    handleCurrentChange(val) {
      this.$set(this.listQuery,"current",val)
      this.queryUserDate();
    },
  },
  mounted() {
    this.queryUserDate()
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

.table-td-thumb {
  display: block;
  margin: auto;
  width: 40px;
  height: 40px;
}
</style>
