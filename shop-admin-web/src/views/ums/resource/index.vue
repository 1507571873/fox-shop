<template> 
  <div class="app-container">
    <el-card class="filter-container" shadow="never">
      <div>
        <i class="el-icon-search"></i>
        <span>筛选搜索</span>
        <el-button
          style="float:right"
          type="primary"
          @click="handleSearchList()"
          size="small">
          查询搜索
        </el-button>
        <el-button
          style="float:right;margin-right: 15px"
          @click="handleResetSearch()"
          size="small">
          重置
        </el-button>
      </div>
      <div style="margin-top: 15px">
        <el-form :inline="true" :model="listQuery" size="small" label-width="140px">
          <el-form-item label="资源名称：">
            <el-input v-model="listQuery.nameKeyword" class="input-width" placeholder="资源名称" clearable></el-input>
          </el-form-item>
          <el-form-item label="资源路径：">
            <el-input v-model="listQuery.urlKeyword" class="input-width" placeholder="资源路径" clearable></el-input>
          </el-form-item>
          <el-form-item label="资源分类：">
            <el-select v-model="listQuery.categoryId" placeholder="全部" clearable class="input-width">
              <el-option v-for="item in categoryOptions"
                         :key="item.id"
                         :label="item.name"
                         :value="item.id">
              </el-option>
            </el-select>
          </el-form-item>
        </el-form>
      </div>
    </el-card>
    <el-card class="operate-container" shadow="never">
      <i class="el-icon-tickets"></i>
      <span>数据列表</span>
      <el-button size="mini" class="btn-add" @click="handleAdd()" style="margin-left: 20px">添加</el-button>
      <el-button size="mini" class="btn-add" @click="handleShowCategory()">资源分类</el-button>
    </el-card>
    <div class="table-container">
      <el-table ref="resourceTable"
                :data="list"
                style="width: 100%;"
                v-loading="listLoading" border>
        <el-table-column label="编号" width="100" align="center">
          <template slot-scope="scope">{{scope.row.id}}</template>
        </el-table-column>
        <el-table-column label="资源名称" align="center">
          <template slot-scope="scope">{{scope.row.name}}</template>
        </el-table-column>
        <el-table-column label="资源路径" align="center">
          <template slot-scope="scope">{{scope.row.url}}</template>
        </el-table-column>
        <el-table-column label="描述" align="center">
          <template slot-scope="scope">{{scope.row.description}}</template>
        </el-table-column>
        <el-table-column label="添加时间" width="160" align="center">
          <template slot-scope="scope">{{scope.row.createTime | formatDateTime}}</template>
        </el-table-column>
        <el-table-column label="操作" width="140" align="center">
          <template slot-scope="scope">
            <el-button size="mini"
                       type="text"
                       @click="handleUpdate(scope.$index, scope.row)">
              编辑
            </el-button>
            <el-button size="mini"
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
      :title="isEdit?'编辑资源':'添加资源'"
      :visible.sync="dialogVisible"
      width="40%">
      <el-form :model="resource"
               ref="resourceForm"
               label-width="150px" size="small">
        <el-form-item label="资源名称：">
          <el-input v-model="resource.name" style="width: 250px"></el-input>
        </el-form-item>
        <el-form-item label="资源路径：">
          <el-input v-model="resource.url" style="width: 250px"></el-input>
        </el-form-item>
        <el-form-item label="资源分类：">
          <el-select v-model="resource.categoryId" placeholder="全部" clearable style="width: 250px">
            <el-option v-for="item in categoryOptions"
                       :key="item.id"
                       :label="item.name"
                       :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="描述：">
          <el-input v-model="resource.description"
                    type="textarea"
                    :rows="5"
                    style="width: 250px"></el-input>
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
import {formatDate} from '../../../router/date';
  import {queryResourcePageData,saveOrUpdateData,queryDataById,deleteById} from '../../../api/ums/resource/resource'
  import {queryResourceCategoryAll} from '../../../api/ums/resource/resourceCategory'
  const defaultListQuery = {
    current: 1,
    size: 10,
    nameKeyword: '',
    urlKeyword: '',
    categoryId:null
  };
  const defaultResource = {
    id: null,
    name: '',
    url: '',
    categoryId: null,
    description:''
  };
  export default {
    name: 'resourceList',
    data() {
      return {
        listQuery: Object.assign({}, defaultListQuery),
        list: null,
        total: null,
        listLoading: false,
        dialogVisible: false,
        resource: Object.assign({}, defaultResource),
        isEdit: false,
        categoryOptions:[],
        defaultCategoryId:null
      }
    },

    filters: {
      formatDateTime(time) {
        if (time == null || time === '') {
          return 'N/A';
        }
        let date = new Date(time);
        return formatDate(date, 'yyyy-MM-dd hh:mm:ss')
      }
    },
    methods: {
      //查询分页查询 条件查询
      queryResourceData(){
        queryResourcePageData(this.listQuery).then(result=>{
          if (result.code===200){
            this.list=result.data.records;
            this.total=result.data.total;
          }
        })
      },
      //查询资源分类
      queryResourceCategoryData(){
        queryResourceCategoryAll().then(result=>{
          if (result.code===200){
            this.categoryOptions=result.data;
          }
        })
      },
      //条件查询
      handleSearchList(){
        this.queryResourceData();
      },
      //条件查询重置按钮
      handleResetSearch(){
        this.listQuery=Object.assign({}, defaultListQuery);
      },
      //新增按钮
      handleAdd(){
        this.dialogVisible=true;
        this.resource=Object.assign({}, defaultResource);
      },
      //资源分类按钮
      handleShowCategory(){
        this.$router.push({path:"/categoryList"})
      },
      //编辑按钮
      handleUpdate(index,row){
        queryDataById(row.id).then(result=>{
            if (result.code===200){
              this.handleAdd();
              this.resource=result.data;
            }
        })
      },
      //删除按钮
      handleDelete(index, row){
        this.$confirm('是否要删除该资源?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          deleteById(row.id).then(result => {
            if (result.code===200){
              this.$message.success(result.msg)
            }
            this.queryResourceData();
          });
        });
      },
      //资源分类表单确定按钮
      handleDialogConfirm(){
        saveOrUpdateData(this.resource).then(result=>{
          if (result.code===200){
            this.$message(result.msg)
            this.dialogVisible=false;
            this.queryResourceData();
          }
        })
      },
      //每页展示条数
      handleSizeChange(val) {
        this.$set(this.listQuery,"size",val)
        this.$set(this.listQuery,"current",1)
        this.queryResourceData();
      },
      //上一页下一页
      handleCurrentChange(val) {
        this.$set(this.listQuery,"current",val)
        this.queryResourceData();
      },
    },
    mounted() {
      this.queryResourceData();
      this.queryResourceCategoryData();
    }
  }
</script>
<style></style>


