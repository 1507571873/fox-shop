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
            <el-input v-model="listQuery.keyword" class="input-width" placeholder="帐号/姓名" clearable></el-input>
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
      <el-table ref="adminTable"
                :data="list"
                style="width: 100%;"
                v-loading="listLoading" border>
        <el-table-column label="编号" width="100" align="center">
          <template slot-scope="scope">{{ scope.row.id }}</template>
        </el-table-column>
        <el-table-column label="帐号" align="center">
          <template slot-scope="scope">{{ scope.row.username }}</template>
        </el-table-column>
        <el-table-column label="姓名" align="center">
          <template slot-scope="scope">{{ scope.row.nickName }}</template>
        </el-table-column>
        <el-table-column label="邮箱" align="center">
          <template slot-scope="scope">{{ scope.row.email }}</template>
        </el-table-column>
        <el-table-column label="添加时间" width="160" align="center">
          <template slot-scope="scope">{{ scope.row.createTime | formatDateTime }}</template>
        </el-table-column>
        <el-table-column label="最后登录" width="160" align="center">
          <template slot-scope="scope">{{ scope.row.loginTime | formatDateTime }}</template>
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
        <el-table-column label="操作" width="180" align="center">
          <template slot-scope="scope">
            <el-button size="mini"
                       type="text"
                       @click="handleSelectRole(scope.$index, scope.row)">分配角色
            </el-button>
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
        :title="isEdit?'编辑用户':'添加用户'"
        :visible.sync="dialogVisible"
        width="40%">
      <el-form :model="admin"
               ref="adminForm"
               label-width="150px" size="small">
        <el-form-item label="帐号：">
          <el-input v-model="admin.username" style="width: 250px"></el-input>
        </el-form-item>
        <el-form-item label="姓名：">
          <el-input v-model="admin.nickName" style="width: 250px"></el-input>
        </el-form-item>
        <el-form-item label="邮箱：">
          <el-input v-model="admin.email" style="width: 250px"></el-input>
        </el-form-item>
        <el-form-item label="备注：">
          <el-input v-model="admin.note"
                    type="textarea"
                    :rows="5"
                    style="width: 250px"></el-input>
        </el-form-item>
        <el-form-item label="是否启用：">
          <el-radio-group v-model="admin.status">
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
    <el-dialog
        title="分配角色"
        :visible.sync="allocDialogVisible"
        width="30%">
      <el-select v-model="allocRoleIds" multiple placeholder="请选择" size="small" style="width: 80%">
        <el-option
            v-for="item in allRoleList"
            :key="item.id"
            :label="item.name"
            :value="item.id">
        </el-option>
      </el-select>
      <span slot="footer" class="dialog-footer">
        <el-button @click="allocDialogVisible = false" size="small">取 消</el-button>
        <el-button type="primary" @click="handleAllocDialogConfirm()" size="small">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>
<script>
import {queryAdminPageData, saveOrUpdateData, queryDataById} from '@/api/ums/admin/admin'
import {queryRolePageAll, saveUserOrRole,queryUserOrRoleById} from '@/api/ums/role/role'
import {formatDate} from '../../../router/date';

const defaultListQuery = {
  current: 1,
  size: 10,
  keyword: ""
};

const defaultAdmin = {
  id: null,
  username: null,
  nickName: null,
  email: null,
  note: null,
  status: 1,

};
export default {
  name: 'adminList',
  data() {
    return {
      listQuery: Object.assign({}, defaultListQuery),
      list: null,
      total: null,
      listLoading: false,
      dialogVisible: false,
      admin: Object.assign({}, defaultAdmin),
      isEdit: false,
      allocDialogVisible: false,
      allocRoleIds: [],
      allRoleList: [],
      allocAdminId: null,

      userOrRoleList: {
        adminId: null,
        umsAdminRoleList: []
      }
    }
  },
  filters: {
    formatDateTime(time) {
      if (time == null || time === '') {
        return null;
      }
      let date = new Date(time);
      return formatDate(date, 'yyyy-MM-dd hh:mm:ss')
    }
  },
  methods: {
    //分页查询
    queryAdminData() {
      queryAdminPageData(this.listQuery).then(result => {
        if (result.code === 200) {
          this.list = result.data.records
          this.total = result.data.total;
        }
      })
    },
    //条件查询
    handleSearchList() {
      this.queryAdminData();
    },

    //条件查询重置按钮
    handleResetSearch() {
      this.listQuery = Object.assign({}, defaultListQuery);
    },
    //新增按钮
    handleAdd() {
      this.admin = Object.assign({}, defaultAdmin);
      this.dialogVisible = true;
    },
    //是否启用
    handleStatusChange() {
      alert("是否启用");
    },
    //分配角色
    handleSelectRole(index, row) {
      this.getRoleData();
      this.queryUserOrRoleById(row.id);
      this.allocAdminId = row.id
      this.allocDialogVisible = true;
    },
    //获取到所有角色
    getRoleData() {
      queryRolePageAll().then(result => {
        if (result.code === 200) {
          this.allRoleList = result.data
        }
      })
    },
    //编辑按钮
    handleUpdate(index, row) {
      queryDataById(row.id).then(result => {
        if (result.code === 200) {
          this.handleAdd();
          this.admin = result.data;
        }
      })
    },
    //删除按钮
    handleDelete() {
      alert("删除按钮");
    },
    //新增修改表单的确认按钮
    handleDialogConfirm() {
      saveOrUpdateData(this.admin).then(result => {
        if (result.code === 200) {
          this.$message(result.msg);
          this.dialogVisible = false;
          this.admin = Object.assign({}, defaultAdmin);
          this.queryAdminData();
        }
      })
    },
    //分配角色表单的确认按钮
    handleAllocDialogConfirm() {
      this.$confirm('是否分配角色？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        let arr = Array.from(this.allocRoleIds);
        if (arr !== null && arr.length > 0) {
          for (let i = 0; i < arr.length; i++) {
            this.userOrRoleList.umsAdminRoleList.push({
              adminId: this.allocAdminId,
              roleId: arr[i]
            })
          }
          this.userOrRoleList.adminId = this.allocAdminId;
          saveUserOrRole(this.userOrRoleList).then(result => {
            if (result.code === 200) {
              this.$message.success(result.msg)
              this.allocDialogVisible = false;
              this.userOrRoleList.umsAdminRoleList=[];
              this.queryAdminData();
            }
          })
        } else {
          this.$message.warning("至少选择一个")
        }
      })
    },
    queryUserOrRoleById(id){
      queryUserOrRoleById(id).then(result=>{
        let allocRoleList = result.data;
        this.allocRoleIds=[];
        if(allocRoleList!=null&&allocRoleList.length>0){
          for(let i=0;i<allocRoleList.length;i++){
            this.allocRoleIds.push(allocRoleList[i].roleId);
          }
        }
      })
    },
    //每页展示条数
    handleSizeChange(val) {
      this.$set(this.listQuery, "size", val)
      this.$set(this.listQuery, "current", 1)
      this.queryAdminData();
    },
    //上一页下一页
    handleCurrentChange(val) {
      this.$set(this.listQuery, "current", val)
      this.queryAdminData();
    },
  },
  mounted() {
    this.queryAdminData();
  }

}
</script>
<style></style>


