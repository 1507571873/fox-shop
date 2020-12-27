<template>
  <el-card class="form-container" shadow="never">
    <el-steps :active="active" finish-status="success" align-center>
      <el-step title="填写商品信息"></el-step>
      <el-step title="填写商品促销"></el-step>
      <el-step title="填写商品属性"></el-step>
      <el-step title="选择商品关联"></el-step>
    </el-steps>
    <!--  填写商品信息  -->
    <product-info-detail
        v-show="showStatus[0]"
        :is-edit="isEdit"
        v-model="productParam"
        @nextStep="nextStep">
    </product-info-detail>
    <!--  填写商品促销  -->
    <product-sale-detail
        v-show="showStatus[1]"
        :is-edit="isEdit"
        v-model="productParam"
        @nextStep="nextStep"
        @prevStep="prevStep">
    </product-sale-detail>
    <!-- 填写商品属性   -->
    <product-attr-detail
        v-show="showStatus[2]"
        :is-edit="isEdit"
        v-model="productParam"
        @nextStep="nextStep"
        @prevStep="prevStep">
    </product-attr-detail>
    <!--  选择商品关联  -->
    <product-relation-detail
        v-show="showStatus[3]"
        :is-edit="isEdit"
        v-model="productParam"
        @prevStep="prevStep"
        @finishCommit="finishCommit">
    </product-relation-detail>

  </el-card>
</template>

<script>
import ProductInfoDetail from './ProductInfoDetail';
import ProductSaleDetail from './ProductSaleDetail';
import ProductAttrDetail from './ProductAttrDetail';
import ProductRelationDetail from './ProductRelationDetail';
import {saveOrUpdateProductData,getProductIdById} from '@/api/product'
const defaultProductParam = {
  albumPics: '',
  brandId: null,
  brandName: '',
  deleteStatus: 0,
  description: '',
  detailDesc: '',
  detailHtml: '',
  detailMobileHtml: '',
  detailTitle: '',
  feightTemplateId: 0,
  flashPromotionCount: 0,
  flashPromotionId: 0,
  flashPromotionPrice: 0,
  flashPromotionSort: 0,
  giftPoint: 0,
  giftGrowth: 0,
  keywords: '',
  lowStock: 0,
  name: '',
  newStatus: 0,
  note: '',
  originalPrice: 0,
  pic: '',
 //会员价格{memberLevelId: 0,memberPrice: 0,memberLevelName: null}
  memberPriceList: [],
  //商品满减
  productFullReductionList: [{fullPrice: 0, reducePrice: 0}],
  //商品阶梯价格
  productLadderList: [{count: 0,discount: 0,price: 0}],
  previewStatus: 0,
  price: 0,
  productAttributeCategoryId: null,
  //商品属性参数相关列表{productAttributeId: 0, value: ''}
  productAttributeValueList: [],
  //商品sku库存信息{lowStock: 0, pic: '', price: 0, sale: 0, skuCode: '', spData: '', stock: 0}
  skuStockList: [],
  //商品相关专题{subjectId: 0}
  subjectProductRelationList: [],
  //商品相关优选{prefrenceAreaId: 0}
  prefrenceAreaProductRelationList: [],
  cateParentId:null,
  productCategoryId: null,
  productCategoryName: '',
  productSn: '',
  promotionEndTime: '',
  promotionPerLimit: 0,
  promotionPrice: null,
  promotionStartTime: '',
  promotionType: 0,
  publishStatus: 0,
  recommandStatus: 0,
  sale: 0,
  serviceIds: '',
  sort: 0,
  stock: 0,
  subTitle: '',
  unit: '',
  usePointLimit: 0,
  verifyStatus: 0,
  weight: 0
};
export default {
  name: "ProductDetail",
  components: {ProductInfoDetail, ProductSaleDetail, ProductAttrDetail, ProductRelationDetail},
  props: {
    isEdit: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      showStatus: [true, false, false, false],
      productParam: Object.assign({}, defaultProductParam),
      active:0,
    }
  },
  methods: {
    //隐藏全部
    hideAll(){
      for (let i=0; i<this.showStatus.length; i++){
        this.showStatus[i]=false;
      }
    },
    //下一步
    nextStep() {
      if(this.active < this.showStatus.length-1){
        this.active++;
        this.hideAll();
        this.showStatus[this.active]=true;
      }
    },
    //上一步
    prevStep() {
        this.active--;
        this.hideAll();
        this.showStatus[this.active]=true;
    },
    //提交
    finishCommit(){
      saveOrUpdateProductData(this.productParam).then(result=>{
        if (result.code===200){
          this.$message(result.msg);
          this.$router.push("/productTable")
        }else {
          this.$message.error("保存失败")
        }
      })
    },

  },
  mounted() {
    if (this.isEdit){
      getProductIdById(this.$route.query.productId).then(result=>{
        debugger
        this.productParam.promotionStartTime=new Date(result.data.promotionStartTime)
        this.productParam.promotionEndTime=new Date(result.data.promotionEndTime)
        this.productParam=result.data;
      })
    }
  }
}
</script>

<style scoped>

</style>