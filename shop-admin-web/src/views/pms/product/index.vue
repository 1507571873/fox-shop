<template>
  <div class="app-container">
    <el-card class="filter-container" shadow="never">
      <div>
        <i class="el-icon-search"></i>
        <span>筛选搜索</span>
        <el-button
            style="float: right"
            @click="handleSearchList()"
            type="primary"
            size="small">
          查询结果
        </el-button>
        <el-button
            style="float: right;margin-right: 15px"
            @click="handleResetSearch()"
            size="small">
          重置
        </el-button>
      </div>
      <div style="margin-top: 15px">
        <el-form :inline="true" :model="searchForm" size="small" label-width="140px">
          <el-form-item label="输入搜索：">
            <el-input style="width: 203px" v-model="searchForm.keyword" placeholder="商品名称"></el-input>
          </el-form-item>
          <el-form-item label="商品货号：">
            <el-input style="width: 203px" v-model="searchForm.productSn" placeholder="商品货号"></el-input>
          </el-form-item>
          <el-form-item label="商品分类：">
            <el-cascader
                clearable
                v-model="selectProductCateValue"
                :options="productCateOptions">
            </el-cascader>
          </el-form-item>
          <el-form-item label="商品品牌：">
            <el-select v-model="searchForm.brandId" placeholder="请选择品牌" clearable>
              <el-option
                  v-for="item in brandOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="上架状态：">
            <el-select v-model="searchForm.publishStatus" placeholder="全部" clearable>
              <el-option
                  v-for="item in publishStatusOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="审核状态：">
            <el-select v-model="searchForm.verifyStatus" placeholder="全部" clearable>
              <el-option
                  v-for="item in verifyStatusOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
        </el-form>
      </div>
    </el-card>
    <el-card class="operate-container" shadow="never">
      <i class="el-icon-tickets"></i>
      <span>数据列表</span>
      <el-button
          class="btn-add"
          @click="handleAddProduct()"
          size="mini">
        添加
      </el-button>
    </el-card>
    <div class="table-container">
      <el-table ref="productTable"
                :data="list"
                style="width: 100%"
                @selection-change="handleSelectionChange"
                border>
        <el-table-column type="selection" width="60" align="center"></el-table-column>
        <el-table-column label="编号" width="100" align="center">
          <template slot-scope="scope">{{scope.row.id}}</template>
        </el-table-column>
        <el-table-column label="商品图片" width="120" align="center">
          <template slot-scope="scope"><img style="height: 80px" :src="scope.row.pic"></template>
        </el-table-column>
        <el-table-column label="商品名称" align="center">
          <template slot-scope="scope">
            <p>{{scope.row.name}}</p>
            <p>品牌：{{scope.row.brandName}}</p>
          </template>
        </el-table-column>
        <el-table-column label="价格/货号" width="120" align="center">
          <template slot-scope="scope">
            <p>价格：￥{{scope.row.price}}</p>
            <p>货号：{{scope.row.productSn}}</p>
          </template>
        </el-table-column>
        <el-table-column label="标签" width="140" align="center">
          <template slot-scope="scope">
            <p>上架：
              <el-switch
                  @change="handlePublishStatusChange(scope.$index, scope.row)"
                  :active-value="1"
                  :inactive-value="0"
                  v-model="scope.row.publishStatus">
              </el-switch>
            </p>
            <p>新品：
              <el-switch
                  @change="handleNewStatusChange(scope.$index, scope.row)"
                  :active-value="1"
                  :inactive-value="0"
                  v-model="scope.row.newStatus">
              </el-switch>
            </p>
            <p>推荐：
              <el-switch
                  @change="handleRecommendStatusChange(scope.$index, scope.row)"
                  :active-value="1"
                  :inactive-value="0"
                  v-model="scope.row.recommandStatus">
              </el-switch>
            </p>
          </template>
        </el-table-column>
        <el-table-column label="排序" width="100" align="center">
          <template slot-scope="scope">{{scope.row.sort}}</template>
        </el-table-column>
        <el-table-column label="SKU库存" width="100" align="center">
          <template slot-scope="scope">
            <el-button type="primary" icon="el-icon-edit" @click="handleShowSkuEditDialog(scope.$index, scope.row)" circle></el-button>
          </template>
        </el-table-column>
        <el-table-column label="销量" width="100" align="center">
          <template slot-scope="scope">{{scope.row.sale}}</template>
        </el-table-column>
        <el-table-column label="审核状态" width="100" align="center">
          <template slot-scope="scope">
            <p>{{scope.row.verifyStatus | verifyStatusFilter}}</p>
            <p>
              <el-button
                  type="text"
                  @click="handleShowVerifyDetail(scope.$index, scope.row)">审核详情
              </el-button>
            </p>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="160" align="center">
          <template slot-scope="scope">
            <p>
              <el-button
                  size="mini"
                  @click="handleShowProduct(scope.$index, scope.row)">查看
              </el-button>
              <el-button
                  size="mini"
                  @click="handleUpdateProduct(scope.$index, scope.row)">编辑
              </el-button>
            </p>
            <p>
              <el-button
                  size="mini"
                  @click="handleShowLog(scope.$index, scope.row)">日志
              </el-button>
              <el-button
                  size="mini"
                  type="danger"
                  @click="handleDelete(scope.$index, scope.row)">删除
              </el-button>
            </p>
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
<!--    <el-dialog
        title="编辑货品信息"
        :visible.sync="editSkuInfo.dialogVisible"
        width="40%">
      <span>商品货号：</span>
      <span>{{editSkuInfo.productSn}}</span>
      <el-input placeholder="按sku编号搜索" v-model="editSkuInfo.keyword" size="small" style="width: 50%;margin-left: 20px">
        <el-button slot="append" icon="el-icon-search" @click="handleSearchEditSku"></el-button>
      </el-input>
      <el-table style="width: 100%;margin-top: 20px"
                :data="editSkuInfo.stockList"
                border>
        <el-table-column
            label="SKU编号"
            align="center">
          <template slot-scope="scope">
            <el-input v-model="scope.row.skuCode"></el-input>
          </template>
        </el-table-column>
        <el-table-column
            v-for="(item,index) in editSkuInfo.productAttr"
            :label="item.name"
            :key="item.id"
            align="center">
          <template slot-scope="scope">
            {{getProductSkuSp(scope.row,index)}}
          </template>
        </el-table-column>
        <el-table-column
            label="销售价格"
            width="80"
            align="center">
          <template slot-scope="scope">
            <el-input v-model="scope.row.price"></el-input>
          </template>
        </el-table-column>
        <el-table-column
            label="商品库存"
            width="80"
            align="center">
          <template slot-scope="scope">
            <el-input v-model="scope.row.stock"></el-input>
          </template>
        </el-table-column>
        <el-table-column
            label="库存预警值"
            width="100"
            align="center">
          <template slot-scope="scope">
            <el-input v-model="scope.row.lowStock"></el-input>
          </template>
        </el-table-column>
      </el-table>
      <span slot="footer" class="dialog-footer">
        <el-button @click="editSkuInfo.dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleEditSkuConfirm">确 定</el-button>
      </span>
    </el-dialog>-->
  </div>
</template>

<script>
    import {deleteData,saveOrUpdateData,queryDataById,queryProductData} from '@/api/product'
//重置条件查询form表单
const defaultListQuery = {
  keyword: null,
  pageNum: 1,
  pageSize: 5,
  publishStatus: null,
  verifyStatus: null,
  productSn: null,
  productCategoryId: null,
  brandId: null
};
export default {
  name: "index",
  data() {
    return {
      editSkuInfo:{
        dialogVisible:false,
        productId:null,
        productSn:'',
        productAttributeCategoryId:null,
        stockList:[],
        productAttr:[],
        keyword:null
      },
      //分页数据
      listQuery: {
        current: 1,
        size: 10,
      },
      pageSizes: [5, 10, 15, 20],
      total: 0,
      //条件查询form表单
      searchForm:Object.assign({},defaultListQuery),
      //商品下拉框
      selectProductCateValue: null,
      productCateOptions: [],
      brandOptions: [],
      //表格数据
      list: [],
      //批量操作
      operateType:null,

      publishStatusOptions: [{
        value: 1,
        label: '上架'
      }, {
        value: 0,
        label: '下架'
      }],
      verifyStatusOptions: [{
        value: 1,
        label: '审核通过'
      }, {
        value: 0,
        label: '未审核'
      }],
      //批量删除 下拉框的内容
      operates: [
        {
          label: "商品上架",
          value: "publishOn"
        },
        {
          label: "商品下架",
          value: "publishOff"
        },
        {
          label: "设为推荐",
          value: "recommendOn"
        },
        {
          label: "取消推荐",
          value: "recommendOff"
        },
        {
          label: "设为新品",
          value: "newOn"
        },
        {
          label: "取消新品",
          value: "newOff"
        },
        {
          label: "转移到分类",
          value: "transferCategory"
        },
        {
          label: "移入回收站",
          value: "recycle"
        }
      ],
    }
  },
  filters: {
    verifyStatusFilter(value) {
      if (value === 1) {
        return '审核通过';
      } else {
        return '未审核';
      }
    }
  },
  methods: {
    //表格数据查询
    queryProductList(){
      queryProductData(this.listQuery).then(result=>{
          if (result.code===200){
            this.list=result.data.records;
            this.total=result.data.total;
          }
      })
    },
    //条件查询提交
    handleSearchList(){
      alert("条件查询提交")
    },
    //条件查询重置
    handleResetSearch(){
      alert("条件查询重置")
    },
    //列表新增
    handleAddProduct(){
      this.$router.push("/productAdd");
    },
    //列表复选框
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    //上下架
    handlePublishStatusChange(index, row) {
      alert("上下架")
    },
    //是否新品
    handleNewStatusChange(index, row) {
      alert("是否新品")
    },
    //是否推荐
    handleRecommendStatusChange(index, row) {
      alert("是否推荐")
    },
    //sku
    handleShowSkuEditDialog(){
      alert("sku")
    },
    //审核详情
    handleShowVerifyDetail(){
      alert("审核详情")
    },
    //查看
    handleShowProduct(){
      alert("查看");
    },
    //编辑
    handleUpdateProduct(index,row){
      this.$router.push({
        path:"/productUpdate",
        query:{productId:row.id}
      });
    },
    //日志
    handleShowLog(){
      alert("日志");
    },
    //删除
    handleDelete(){
      alert("删除");
    },
    //批量操作确认按钮
    handleBatchOperate(){
      alert("批量操作确认按钮");
    },
    //每页展示条数
    handleSizeChange(val) {
      this.$set(this.listQuery,"size",val)
      this.$set(this.listQuery,"current",1)
      this.queryProductList();
    },
    //上一页下一页
    handleCurrentChange(val) {
      this.$set(this.listQuery,"current",val)
      this.queryProductList();
    },
  },
  mounted() {
    this.queryProductList();
  }
}
</script>

<style scoped>

</style>