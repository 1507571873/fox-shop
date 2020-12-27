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
          <el-form-item label="输入搜索：">
            <el-input v-model="listQuery.keyword" class="input-width" placeholder="角色名称" clearable></el-input>
          </el-form-item>
        </el-form>
      </div>
    </el-card>
    <el-card class="operate-container" shadow="never">
      <i class="el-icon-tickets"></i>
      <span>数据列表</span>
      <el-button size="mini" class="btn-add" @click="handleAdd()" style="margin-left: 20px">添加</el-button>
    </el-card>
    <div class="table-container">
      <el-table ref="roleTable"
                :data="list"
                style="width: 100%;"
                v-loading="listLoading" border>
        <el-table-column label="编号" width="100" align="center">
          <template slot-scope="scope">{{scope.row.id}}</template>
        </el-table-column>
        <el-table-column label="角色名称" align="center">
          <template slot-scope="scope">{{scope.row.name}}</template>
        </el-table-column>
        <el-table-column label="描述" align="center">
          <template slot-scope="scope">{{scope.row.description}}</template>
        </el-table-column>
        <el-table-column label="用户数"  width="100" align="center">
          <template slot-scope="scope">{{scope.row.adminCount}}</template>
        </el-table-column>
        <el-table-column label="添加时间" width="160" align="center">
          <template slot-scope="scope">{{scope.row.createTime | formatDateTime}}</template>
        </el-table-column>
        <el-table-column label="是否启用" width="140" align="center">
          <template slot-scope="scope">
            <el-switch
              @change="handleStatusChange(scope.$index, scope.row)"
              :active-value="1"
              :inactive-value="0"
              v-model="scope.row.status">
            </el-switch>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="160" align="center">
          <template slot-scope="scope">
            <el-row>
              <el-button size="mini"
                         type="text"
                         @click="handleSelectMenu(scope.$index, scope.row)">分配菜单
              </el-button>
              <el-button size="mini"
                         type="text"
                         @click="handleSelectResource(scope.$index, scope.row)">分配资源
              </el-button>
            </el-row>
            <el-row>
            <el-button size="mini"
                       type="text"
                       @click="handleUpdate(scope.$index, scope.row)">
              编辑
            </el-button>
            <el-button size="mini"
                       type="text"
                       @click="handleDelete(scope.$index, scope.row)">删除
            </el-button>
            </el-row>
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
      <el-form :model="role"
               ref="roleForm"
               label-width="150px" size="small">
        <el-form-item label="角色名称：">
          <el-input v-model="role.name" style="width: 250px"></el-input>
        </el-form-item>
        <el-form-item label="描述：">
          <el-input v-model="role.description"
                    type="textarea"
                    :rows="5"
                    style="width: 250px"></el-input>
        </el-form-item>
        <el-form-item label="是否启用：">
          <el-radio-group v-model="role.status">
            <el-radio :label="1">是</el-radio>
            <el-radio :label="0">否</el-radio>
          </el-radio-group>
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
  import {queryRolePageData,saveOrUpdateData,queryDataById,queryRoleDelete} from '../../../api/ums/role/role'
  const defaultListQuery = {
    current: 1,
    size: 10,
    keyword: ""
  };
  const defaultRole = {
    id: null,
    name: null,
    description: null,
    adminCount: 0,
    status: 1
  };
  export default {
    name: 'roleList',
    data() {
      return {
        listQuery: Object.assign({}, defaultListQuery),
        list: null,
        total: null,
        listLoading: false,
        dialogVisible: false,
        role: Object.assign({}, defaultRole),
        isEdit: false
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
      //查询
      queryRoleData(){
        queryRolePageData(this.listQuery).then(result=>{
          if (result.code===200){
            this.listLoading = false;
            this.list=result.data.records
            this.total = result.data.total;
          }
        })
      },
      //重置条件查询
      handleResetSearch() {
        this.listQuery = Object.assign({}, defaultListQuery);
      },
      //条件查询
      handleSearchList() {
        this.queryRoleData();
      },
      //新增按钮
      handleAdd(){
        this.dialogVisible=true;
        this.role= Object.assign({}, defaultRole);
      },
      //编辑
      handleUpdate(index,row){
        queryDataById(row.id).then(result=>{
          if (result.code===200){
            this.handleAdd();
            this.role=result.data;
          }
        })
      },
      //删除
      handleDelete(index,row){
        this.$confirm('是否要删除该资源?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          queryRoleDelete(row.id).then(result => {
            if (result.code===200){
              this.$message.success(result.msg)
            }
            this.queryRoleData();
          });
        });
      },
      //新增修改提交
      handleDialogConfirm(){
        saveOrUpdateData(this.role).then(result=>{
            if (result.code===200){
              this.$message(result.msg)
              this.dialogVisible=false;
              this.queryRoleData();
            }
        })
      },
      //是否启用
      handleStatusChange(){
        alert("是否启用")
      },
      handleSelectMenu(index,row){
        this.$router.push({path:'/allocMenu',query:{roleId:row.id}})
      },
      handleSelectResource(index,row){
        this.$router.push({path:'/allocResource',query:{roleId:row.id}})
      },

      //每页展示条数
      handleSizeChange(val) {
        this.$set(this.listQuery,"size",val)
        this.$set(this.listQuery,"current",1)
        this.queryRoleData();
      },
      //上一页下一页
      handleCurrentChange(val) {
        this.$set(this.listQuery,"current",val)
        this.queryRoleData();
      },
    },
    mounted() {
      this.queryRoleData();
    }
  }
</script>
<style></style>


