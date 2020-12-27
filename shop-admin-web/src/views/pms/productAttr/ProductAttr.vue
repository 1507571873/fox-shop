<template>
  <div class="app-container">
    <el-card class="operate-container" shadow="never">
      <i class="el-icon-tickets" style="margin-top: 5px"></i>
      <span style="margin-top: 5px">数据列表</span>
      <el-button
          class="btn-add"
          @click="addProductAttrCate()"
          size="mini">
        添加
      </el-button>
    </el-card>
    <div class="table-container">
      <el-table ref="productAttrCateTable"
                style="width: 100%"
                :data="tableData"
                border>
        <el-table-column label="编号" width="100" align="center">
          <template slot-scope="scope">{{ scope.row.id }}</template>
        </el-table-column>
        <el-table-column label="类型名称" align="center">
          <template slot-scope="scope">{{ scope.row.name }}</template>
        </el-table-column>
        <el-table-column label="属性数量" width="200" align="center">
          <template slot-scope="scope">{{ scope.row.attributeCount == null ? 0 : scope.row.attributeCount }}</template>
        </el-table-column>
        <el-table-column label="参数数量" width="200" align="center">
          <template slot-scope="scope">{{ scope.row.paramCount == null ? 0 : scope.row.paramCount }}</template>
        </el-table-column>
        <el-table-column label="设置" width="200" align="center">
          <template slot-scope="scope">
            <el-button
                size="mini"
                @click="getAttrList(scope.$index,scope.row)">属性列表
            </el-button>
            <el-button
                size="mini"
                @click="getParamList(scope.$index,scope.row)">参数列表
            </el-button>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" align="center">
          <template slot-scope="scope">
            <el-button
                size="mini"
                @click="handleUpdate(scope.row)">编辑
            </el-button>
            <el-button
                size="mini"
                type="danger"
                @click="handleDelete(scope.row)">删除
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
          :page-sizes=pageSizes
          :page-size=listQuery.size
          layout="total, sizes, prev, pager, next, jumper"
          :total=total>
      </el-pagination>
    </div>
    <el-dialog
        :title="dialogTitle"
        :visible.sync="dialogVisible"
        width="30%">
      <el-form ref="productAttrCatForm" :model="productAttrCate"  label-width="120px">
        <el-form-item label="类型名称" prop="name">
          <el-input v-model="productAttrCate.name" auto-complete="off"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleConfirm('productAttrCatForm')">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
  import {queryProductAttr,saveOrUpdateData,queryDataById} from '@/api/category'
  const productAttrForm={
    name:"",
  }
export default {
  name: "ProductAttr",
  data() {
    return {
      //表格数据
      tableData:[],
      //表单标题
      dialogTitle:"",
      //表单
      productAttrCate:Object.assign({},productAttrForm),
      //控制弹框的开启关闭
      dialogVisible:false,
      //分页数据
      listQuery: {
        current: 1,
        size: 10,
      },
      pageSizes: [5, 10, 15, 20],
      total: 0,
    }
  },

  methods: {
    //查询 分页查询
    queryProductAttr(){
      queryProductAttr(this.listQuery).then(result=>{
        if (result.code===200){
          this.tableData=result.data.records
          this.total=result.data.total
        }
      })
    },
    //新增or修改弹框的方法
    addProductAttrCate(){
      this.dialogVisible=true;
      //刷新表单
      this.productAttrCate=Object.assign({},productAttrForm);
    },
    //去属性列表
    getAttrList(index,row){
      this.$router.push({path:'/productAttrList',query:{cid:row.id,cname:row.name,type:0}})
    },
    //去参数列表
    getParamList(index,row){
      this.$router.push({path:'/productAttrList2',query:{cid:row.id,cname:row.name,type:1}})
    },
    //修改回显
    handleUpdate(row){
      this.dialogVisible=true;
      queryDataById(row.id).then(result=>{
        if (result.code===200){
          this.productAttrCate=result.data
        }
      })
    },
    //新增or修改保存方法
    handleConfirm(){
      saveOrUpdateData(this.productAttrCate).then(result=>{
        if (result.code===200){
          this.$message(result.msg)
          this.queryProductAttr();//刷新表格
          this.dialogVisible=false;//关闭弹框
          //刷新表单
          this.productAttrCate=Object.assign({},productAttrForm);
        }
      })
    },
    //删除
    handleDelete(){
      alert("删除")
    },
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
    //查询 分页查询
    this.queryProductAttr();

  },
  watch: {
    '$route': function() {
      this.queryProductAttr();　　// 这边调用获取数据的方法
    }
  },
}
</script>

<style scoped>

</style>