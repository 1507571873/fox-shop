<template>
  <div class="app-container">
    <el-card class="operate-container" shadow="never">

      <i class="el-icon-tickets" style="margin-top: 5px"></i>
      <span style="margin-top: 5px">数据列表</span>
      <el-button
          class="btn-add"
          @click="addProductAttr()"
          size="mini">
        添加
      </el-button>
    </el-card>
    <div class="table-container">
      <el-table ref="productAttrTable"
                :data="tableData"
                style="width: 100%"
                v-loading="listLoading"
                @selection-change="handleSelectionChange"
                border>
        <el-table-column type="selection" width="60" align="center"></el-table-column>
        <el-table-column label="编号" width="100" align="center">
          <template slot-scope="scope">{{ scope.row.id }}</template>
        </el-table-column>
        <el-table-column label="属性名称" width="140" align="center">
          <template slot-scope="scope">{{ scope.row.name }}</template>
        </el-table-column>
        <el-table-column label="商品类型" width="140" align="center">
          <template slot-scope="scope">{{ $route.query.cname }}</template>
        </el-table-column>
        <el-table-column label="属性是否可选" width="120" align="center">
          <template slot-scope="scope">{{ scope.row.selectType|selectTypeFilter }}</template>
        </el-table-column>
        <el-table-column label="属性值的录入方式" width="150" align="center">
          <template slot-scope="scope">{{ scope.row.inputType|inputTypeFilter }}</template>
        </el-table-column>
        <el-table-column label="可选值列表" align="center">
          <template slot-scope="scope">{{ scope.row.inputList }}</template>
        </el-table-column>
        <el-table-column label="排序" width="100" align="center">
          <template slot-scope="scope">{{ scope.row.sort }}</template>
        </el-table-column>
        <el-table-column label="操作" width="200" align="center">
          <template slot-scope="scope">
            <el-button
                size="mini"
                @click="handleUpdate(scope.$index, scope.row)">编辑
            </el-button>
            <el-button
                size="mini"
                type="danger"
                @click="handleDelete(scope.$index, scope.row)">删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="batch-operate-container">
      <el-select
          size="small"
          v-model="operateType" placeholder="批量操作">
        <el-option
            v-for="item in operates"
            :key="item.value"
            :label="item.label"
            :value="item.value">
        </el-option>
      </el-select>
      <el-button
          style="margin-left: 20px"
          class="search-button"
          @click="handleBatchOperate()"
          type="primary"
          size="small">
        确定
      </el-button>
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
        width="40%">
      <el-form :model="productAttr"  ref="productAttrFrom" label-width="150px">
        <el-form-item label="属性名称：" prop="name">
          <el-input v-model="productAttr.name"></el-input>
        </el-form-item>
        <el-form-item label="商品类型：">
          <el-input v-model="productAttr.typeName"></el-input>
        </el-form-item>
        <el-form-item label="分类筛选样式:">
          <el-radio-group v-model="productAttr.filterType">
            <el-radio :label="0">普通</el-radio>
            <el-radio :label="1">颜色</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="能否进行检索:">
          <el-radio-group v-model="productAttr.searchType">
            <el-radio :label="0">不需要检索</el-radio>
            <el-radio :label="1">关键字检索</el-radio>
            <el-radio :label="2">范围检索</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="商品属性关联:">
          <el-radio-group v-model="productAttr.relatedStatus">
            <el-radio :label="1">是</el-radio>
            <el-radio :label="0">否</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="属性是否可选:">
          <el-radio-group v-model="productAttr.selectType">
            <el-radio :label="0">唯一</el-radio>
            <el-radio :label="1">单选</el-radio>
            <el-radio :label="2">复选</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="属性值的录入方式:">
          <el-radio-group v-model="productAttr.inputType">
            <el-radio :label="0">手工录入</el-radio>
            <el-radio :label="1">从下面列表中选择</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="属性值可选值列表:">
          <el-input :autosize="true" type="textarea" v-model="inputListFormat"></el-input>
        </el-form-item>
        <el-form-item label="是否支持手动新增:">
          <el-radio-group v-model="productAttr.handAddStatus">
            <el-radio :label="1">是</el-radio>
            <el-radio :label="0">否</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="排序属性：">
          <el-input v-model="productAttr.sort"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button @click="dialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="onSubmit('productAttrFrom')">提交</el-button>
<!--          <el-button  v-if="!isEdit" @click="resetForm('productAttrFrom')">重置</el-button>-->
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
    import {queryProductAttr,saveOrUpdateData,queryDataById} from '@/api/productAttrList'
const defaultProductAttr = {
  filterType: 0,
  handAddStatus: 0,
  inputList: "",
  inputType: 0,
  name: '',
  productAttributeCategoryId: 0,
  relatedStatus: 0,
  searchType: 0,
  selectType: 0,
  sort: 0,
  type: 0,
  typeName:"",
};
export default {
  name: "productAttrList",
  data() {
    return {
      //表单标题
      dialogTitle:"",
      //弹框
      dialogVisible:false,
      //表单
      productAttr:Object.assign({}, defaultProductAttr),
      //表格数据
      tableData:[],
      //属性值可选列表
      inputListFormat:"",
      productAttrCateList:[],
      listLoading:"",
      //分页数据
      listQuery: {
        name:"",
        cid:this.$route.query.cid,
        type:this.$route.query.type,
        current: 1,
        size: 10,
      },
      pageSizes: [5, 10, 15, 20],
      total: 0,
      operateType:null,
      //批量操作
      operates: [
        {
          label: "删除",
          value: "deleteProductAttr"
        }
      ]
    }
  },
  //监听事件
  watch: {
    //监听该文本框的改变事件
    inputListFormat: function (newValue, oldValue) {
      newValue = newValue.replace(/\n/, ",");
      this.productAttr.inputList = newValue;
    },
    $route() {
      //将mounted中的数据在这里重新加载一下即可
      this.listQuery.type= this.$route.query.type
      this.listQuery.cid =  this.$route.query.cid
      //重新查询方法
      this.queryProductAttrData();
    }
  },
  methods: {
    //查询 分页查询
    queryProductAttrData() {
      queryProductAttr(this.listQuery).then(result=>{
        if (result.code===200){
          this.tableData=result.data.records
          this.total=result.data.total
        }
      })
    },
    //新增的弹框
    addProductAttr(){
      this.dialogVisible=true;
      this.reset();
    },
    //批量删除
    handleSelectionChange(){

    },
    //表单提交
    onSubmit(){
      saveOrUpdateData(this.productAttr).then(result=>{
        if (result.code===200){
          this.dialogVisible=false;
          this.queryProductAttrData();
          this.productAttr=Object.assign({}, defaultProductAttr);
        }
      })
    },
    //修改回显
    handleUpdate(index,row){
      this.dialogVisible=true;
      queryDataById(row.id).then(result=>{
        if (result.code===200){
          this.productAttr=result.data
          this.productAttr.typeName=this.$route.query.cname;
          this.inputListFormat=result.data.inputList
          this.inputListFormat = this.inputListFormat.replaceAll(",", '\n');
        }
      })
    },
    //删除
    handleDelete(){
      alert("删除");
    },

    //执行批量操作
    handleBatchOperate(){
      alert("执行批量操作");
    },
    //每页展示条数
    handleSizeChange(val) {
      this.$set(this.listQuery,"size",val)
      this.$set(this.listQuery,"current",1)
      this.queryProductAttrData();
    },
    //上一页下一页
    handleCurrentChange(val) {
      this.$set(this.listQuery,"current",val)
      this.queryProductAttrData();
    },
    reset(){
      this.productAttr = Object.assign({}, defaultProductAttr);
      this.productAttr.productAttributeCategoryId=this.$route.query.cid;
      this.productAttr.typeName=this.$route.query.cname;
      this.productAttr.type=this.$route.query.type;
      this.inputListFormat = this.inputListFormat.replaceAll(",", '\n');
    }
  },
  filters: {
    inputTypeFilter(value) {
      if (value === 1) {
        return '从列表中选取';
      } else {
        return '手工录入'
      }
    },
    selectTypeFilter(value) {
      if (value === 1) {
        return '单选';
      } else if (value === 2) {
        return '多选';
      } else {
        return '唯一'
      }
    },
  },
  mounted() {
    this.queryProductAttrData();
    this.reset()
  },

}
</script>

<style scoped>

</style>