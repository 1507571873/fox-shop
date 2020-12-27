<template>
  <div style="margin-top: 50px">
    <el-form :model="value" ref="productAttrForm" label-width="120px" style="width: 900px;margin: 0 auto" size="small">
      <el-form-item label="属性类型：">
        <el-select v-model="value.productAttributeCategoryId"
                   placeholder="请选择属性类型"
                   @change="handleProductAttrChange">
          <el-option
              v-for="item in productAttributeCategoryOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="商品规格：">
        <el-card shadow="never" class="cardBg">
          <div v-for="(productAttr,idx) in selectProductAttr">
            {{ productAttr.name }}：
            <el-checkbox-group v-if="productAttr.handAddStatus===0" v-model="selectProductAttr[idx].values">
              <el-checkbox v-for="item in getInputListArr(productAttr.inputList)" :label="item" :key="item"
                           class="littleMarginLeft"></el-checkbox>
            </el-checkbox-group>
            <div v-else>
              <el-checkbox-group v-model="selectProductAttr[idx].values">
                <div v-for="(item,index) in selectProductAttr[idx].options" style="display: inline-block"
                     class="littleMarginLeft">
                  <el-checkbox :label="item" :key="item"></el-checkbox>
                  <el-button type="text" class="littleMarginLeft" @click="handleRemoveProductAttrValue(idx,index)">删除
                  </el-button>
                </div>
              </el-checkbox-group>
              <el-input v-model.trim="addProductAttrValue" style="width: 160px;margin-left: 10px" clearable></el-input>
              <el-button class="littleMarginLeft" @click="handleAddProductAttrValue(idx)">增加</el-button>
            </div>
          </div>
        </el-card>
        <el-table style="width: 100%;margin-top: 20px"
                  :data="value.skuStockList"
                  border>
          <el-table-column
              v-for="(item,index) in selectProductAttr"
              :label="item.name"
              :key="item.id"
              align="center">
            <template slot-scope="scope">
              {{ getProductSkuSp(scope.row, index) }}
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
              width="80"
              align="center">
            <template slot-scope="scope">
              <el-input v-model="scope.row.lowStock"></el-input>
            </template>
          </el-table-column>
          <el-table-column
              label="SKU编号"
              align="center">
            <template slot-scope="scope">
              <el-input v-model="scope.row.skuCode"></el-input>
            </template>
          </el-table-column>
          <el-table-column
              label="图片"
              align="center">
            <template slot-scope="scope">
              <single-upload v-model="scope.row.pic"></single-upload>
            </template>
          </el-table-column>
          <el-table-column
              label="操作"
              width="80"
              align="center">
            <template slot-scope="scope">
              <el-button
                  type="text"
                  @click="handleRemoveProductSku(scope.$index, scope.row)">删除
              </el-button>
              <el-button
                  type="text"
                  @click="SingleUploadImg(scope.$index, scope.row)">上传图片
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-button
            type="primary"
            style="margin-top: 20px"
            @click="handleRefreshProductSkuList">刷新列表
        </el-button>
        <el-button
            type="primary"
            style="margin-top: 20px"
            @click="handleSyncProductSkuPrice">同步价格
        </el-button>
        <el-button
            type="primary"
            style="margin-top: 20px"
            @click="handleSyncProductSkuStock">同步库存
        </el-button>
      </el-form-item>
      <el-form-item label="商品参数：">
        <el-card shadow="never" class="cardBg">
          <div v-for="(item,index) in selectProductParam" :class="{littleMarginTop:index!==0}">
            <div class="paramInputLabel">{{ item.name }}:</div>
            <el-select v-if="item.inputType===1" class="paramInput" v-model="selectProductParam[index].value">
              <el-option
                  v-for="item in getParamInputList(item.inputList)"
                  :key="item"
                  :label="item"
                  :value="item">
              </el-option>
            </el-select>
            <el-input v-else class="paramInput" v-model="selectProductParam[index].value"></el-input>
          </div>
        </el-card>
      </el-form-item>
      <el-form-item label="商品相册：">
        <multi-upload v-model="selectProductPics"></multi-upload>
      </el-form-item>
      <el-form-item label="规格参数：">
        <el-tabs v-model="activeHtmlName" type="card">
          <el-tab-pane label="电脑端详情" name="pc">
            <quill-editor ref="text" v-model="content" class="myQuillEditor" :options="editorOption" />
            <el-button type="primary" @click="submit">提交</el-button>
          </el-tab-pane>
          <el-tab-pane label="移动端详情" name="mobile">
            <quill-editor ref="text" v-model="content" class="myQuillEditor" :options="editorOption" />
            <el-button type="primary" @click="submit">提交</el-button>
          </el-tab-pane>
        </el-tabs>
      </el-form-item>
      <el-form-item style="text-align: center">
        <el-button size="medium" @click="handlePrev">上一步，填写商品促销</el-button>
        <el-button type="primary" size="medium" @click="handleNext">下一步，选择商品关联</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import {queryProductAttr} from '@/api/category'
import {queryProductAttrAll} from '@/api/productAttrList'
import multiUpload from "@/components/SingleUpload/multiUpload";
import SingleUpload from "@/components/SingleUpload/SingleUpload";
import { quillEditor } from 'vue-quill-editor'
import 'quill/dist/quill.core.css'
import 'quill/dist/quill.snow.css'
import 'quill/dist/quill.bubble.css'

export default {
  name: "ProductAttrDetail",
  components: {
    SingleUpload,
    multiUpload,
    quillEditor
  },
  props: {
    value: Object,
    isEdit: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      content:'',
      editorOption:'',
      //编辑模式时是否初始化成功
      hasEditCreated: false,
      //商品属性分类下拉选项
      productAttributeCategoryOptions: [],
      //选中的商品属性
      selectProductAttr: [],
      //选中的商品参数
      selectProductParam: [],
      //可手动添加的商品属性
      addProductAttrValue: '',
      //商品富文本详情激活类型
      activeHtmlName: 'pc',
      //图片
      selectProductPics: [],
      //图片上传form
      dialogFormVisible: false,

    }
  },
  computed: {
    //商品的编号
    productId() {
      return this.value.id;
    }
  },
  watch: {
    productId: function (newValue) {
      if (!this.isEdit) return;
      if (this.hasEditCreated) return;
      if (newValue === undefined || newValue == null || newValue === 0) return;
      this.handleProductAttrChange(this.value.productAttributeCategoryId)
      this.handleProductPics(this.value.albumPics);
      this.hasEditCreated = true;
    },
  },
  methods: {
    //图片
    handleProductPics(photo) {
      this.selectProductPics = photo.split(",")
    },
    //同步价格
    handleSyncProductSkuPrice() {
      this.$confirm('将同步第一个sku的价格到所有sku,是否继续', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        //判断 商品sku信息skuStockList 是否为空
        if (this.value.skuStockList != null && this.value.skuStockList.length > 0) {
          //创建一个空数组
          let tempSkuList = [];
          //合并数据
          tempSkuList = tempSkuList.concat(tempSkuList, this.value.skuStockList);
          //获取价格
          let price = this.value.skuStockList[0].price;
          for (let i = 0; i < tempSkuList.length; i++) {
            //给空数组里的价格赋值
            tempSkuList[i].price = price;
          }
          //清空之前的数据
          this.value.skuStockList = [];
          //合并数据 也是赋值
          this.value.skuStockList = this.value.skuStockList.concat(this.value.skuStockList, tempSkuList);
        }
      })
    },
    //同步库存和 预警库存
    handleSyncProductSkuStock() {
      this.$confirm('将同步第一个sku的库存和预警库存到所有sku,是否继续', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        //判断 商品sku信息skuStockList 是否为空
        if (this.value.skuStockList != null && this.value.skuStockList.length > 0) {
          //创建一个空数组
          let tempSkuList = [];
          //合并数据
          tempSkuList = tempSkuList.concat(tempSkuList, this.value.skuStockList);
          //获取库存和预警库存
          let stock = this.value.skuStockList[0].stock;
          let lowStock = this.value.skuStockList[0].lowStock;
          for (let i = 0; i < tempSkuList.length; i++) {
            //给空数组里的价格赋值
            tempSkuList[i].stock = stock;
            tempSkuList[i].lowStock = lowStock;
          }
          //清空之前的数据
          this.value.skuStockList = [];
          //合并数据 也是赋值
          this.value.skuStockList = this.value.skuStockList.concat(this.value.skuStockList, tempSkuList);
        }
      })
    },
    //刷新sku列表
    handleRefreshProductSkuList() {
      //首先给用户进行提示
      this.$confirm('刷新列表将导致sku信息重新生成，是否要刷新', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        //this.refreshProductAttrPics();
        this.refreshProductSkuList();
      });
    },
    getProductSkuSp(row, index) {
      //先把字符串传换成json数据
      let spData = JSON.parse(row.spData);
      //判断spData是否有数据
      if (spData != null && spData.length > index) {
        return spData[index].value;
      } else {
        return null;
      }
    },
    //刷新sku表格
    refreshProductSkuList() {
      //首先清空skuStockList
      this.value.skuStockList = [];
      //获取skuStockList
      let skuList = this.value.skuStockList;
      //获取属性的个数 最多为三个
      let attrLength = this.selectProductAttr.length;
      //判断有几个属性
      if (attrLength === 1) {
        //只有一个属性的时候 获取它
        let data = this.selectProductAttr[0];
        //循环勾选的属性
        for (let i = 0; i < data.values.length; i++) {
          skuList.push({
            spData: JSON.stringify([{key: data.name, value: data.values[i]}])
          })
        }
      } else if (attrLength === 2) {
        let data = this.selectProductAttr[0];
        let data1 = this.selectProductAttr[1];
        if (data.values.length === 0 || data1.values.length === 0) {
          this.$message({
            message: '属性选项不完整',
            type: 'warning',
            duration: 1000
          });
          return;
        }
        for (let i = 0; i < data.values.length; i++) {
          for (let j = 0; j < data1.values.length; j++) {
            let spData = [];
            spData.push({key: data.name, value: data.values[i]})
            spData.push({key: data1.name, value: data1.values[j]})
            skuList.push({
              spData: JSON.stringify(spData)
            })
          }
        }
      } else if (attrLength === 3) {
        let data = this.selectProductAttr[0];
        let data1 = this.selectProductAttr[1];
        let data2 = this.selectProductAttr[2];
        if (data.values.length === 0 || data1.values.length === 0 || data2.values.length === 0) {
          this.$message({
            message: '属性选项不完整',
            type: 'warning',
            duration: 1000
          });
          return;
        }
        for (let i = 0; i < data.values.length; i++) {
          for (let j = 0; j < data1.values.length; j++) {
            for (let k = 0; k < data2.values.length; k++) {
              let spData = [];
              spData.push({key: data.name, value: data.values[i]})
              spData.push({key: data1.name, value: data1.values[j]})
              spData.push({key: data2.name, value: data2.values[k]})
              skuList.push({
                spData: JSON.stringify(spData)
              })
            }
          }
        }
      }
    },
    //商品属性分类下拉选项
    getProductAttrCateList() {
      let param = {current: 1, size: 100};
      queryProductAttr(param).then(response => {
        this.productAttributeCategoryOptions = [];
        let list = response.data.records;
        for (let i = 0; i < list.length; i++) {
          this.productAttributeCategoryOptions.push({label: list[i].name, value: list[i].id});
        }
      });
    },
    //商品属性分类下拉选项的内容改变事件
    handleProductAttrChange(val) {
      this.getAttrList(val);
      this.getParamsList(val);
    },
    getInputListArr(inputList) {
      return inputList.split(",")
    },
    getParamInputList(inputList) {
      return inputList.split(',');
    },
    //参数设置
    getParamsList(val) {
      let listQuery = {cid: val, type: 1, current: 1, size: 20,};
      queryProductAttrAll(listQuery).then(result => {
        this.selectProductParam = [];
        let list = result.data.records;
        for (let i = 0; i < list.length; i++) {
          let value = "";
          if (this.isEdit){
            value=this.getProductParams(list[i].id);
          }
          this.selectProductParam.push({
            inputType: list[i].inputType,
            id: list[i].id,
            name: list[i].name,
            value:value,
            inputList: list[i].inputList
          })
        }
      })
    },
    getProductParams(paramId){
      let attrList=this.value.productAttributeValueList;

      let value = "";
      for (let i = 0; i < attrList.length; i++) {
          if (paramId===attrList[i].productAttributeId){
            value= attrList[i].value;
          }
      }
      return value;
    },
    //属性值回显
    getAttrValues(name) {

      let valueSet = new Set();
      let skuList = this.value.skuStockList;
      for (let i = 0; i < skuList.length; i++) {
        let spData=skuList[i].spData;
        let dataList=JSON.parse(spData);
        for (let j = 0; j < dataList.length; j++) {
          let key=dataList[j].key
          if (key === name){
            valueSet.add(dataList[j].value);
            break;
          }
        }
      }

      return Array.from(valueSet);
    },
    //判断是手动输入 回显
    getProductAttrValues(attrId){
      let attrList=this.value.productAttributeValueList;
      let options=[];
      for (let i = 0; i < attrList.length; i++) {
        if (attrId===attrList[i].productAttributeId){
          let attrValues=attrList[i].value;
          let optionsList=attrValues.split(",");
          for (let j = 0; j < optionsList.length; j++) {
            options.push(optionsList[j])
          }
        }
      }
      return options;
    },
    //属性设置
    getAttrList(val) {
      let listQuery = {cid: val, type: 0, current: 1, size: 20,};
      queryProductAttrAll(listQuery).then(result => {
        this.selectProductAttr = [];
        let list = result.data.records;
        for (let i = 0; i < list.length; i++) {
          let values = [];
          let options = [];
          if (this.isEdit) {
            //判断是手动输入
            if (list[i].handAddStatus===1){
              options=this.getProductAttrValues(list[i].id);
            }
            //属性值回显
            values=this.getAttrValues(list[i].name);
          }
          this.selectProductAttr.push({
            values: values,
            options: options,
            handAddStatus: list[i].handAddStatus,
            id: list[i].id,
            name: list[i].name,
            inputList: list[i].inputList
          })
        }
      })
    },
    handleAddProductAttrValue(idx) {
      let options = this.selectProductAttr[idx].options;
      //判断手动添加的属性值不为空
      if (this.addProductAttrValue === null || this.addProductAttrValue === '') {
        this.$message({
          message: '属性值不能为空',
          type: 'warning',
          duration: 1000
        });
        return;
      }
      //判断手动添加的属性值不能重复
      if (options.indexOf(this.addProductAttrValue) !== -1) {
        this.$message({
          message: '属性值不能重复',
          type: 'warning',
          duration: 1000
        });
        return;
      }
      //赋值
      this.selectProductAttr[idx].options.push(this.addProductAttrValue);
      //清空
      this.addProductAttrValue = null;
    },
    //删除手动添加的属性
    handleRemoveProductAttrValue(idx, index) {
      this.selectProductAttr[idx].options.splice(index, 1);
    },
    //图片上传
    SingleUploadImg() {
      this.dialogFormVisible = true;
    },
    //删除
    handleRemoveProductSku() {
      alert("删除")
    },
    //下一步
    handleNext() {
      //商品相册的处理
      this.selectProductPhotos();
      //参数 属性的处理
      this.mergeProductAttrValue();
      this.$emit('nextStep');
    },
    //上一步
    handlePrev() {
      this.$emit('prevStep');
    },
    //参数 属性的处理
    mergeProductAttrValue() {
      //重置商品属性参数相关列表
      this.value.productAttributeValueList = [];
      //合并商品的属性
      for (let i = 0; i < this.selectProductAttr.length; i++) {
        let attr = this.selectProductAttr[i];
        //判断是 手动添加的属性 并且有值
        if (attr.handAddStatus === 1&& attr.options!==null && attr.options.length > 0) {
          this.value.productAttributeValueList.push({
            productAttributeId: attr.id,
            value: this.getOptionsAttr(attr.options),
          })
        }
      }
      //合并商品的参数
      for (let i = 0; i < this.selectProductParam.length; i++) {
        let attr = this.selectProductParam[i]
        this.value.productAttributeValueList.push({
          productAttributeId: attr.id,
          value: attr.value
        })
      }
    },
    getOptionsAttr(options) {
      let value = "";
      for (let i = 0; i < options.length; i++) {
        if (i === options.length - 1) {
          value += options[i]
        } else {
          value += options[i] + ","
        }
      }
      return value;
    },
    //商品相册的处理
    selectProductPhotos() {
      //获取商品相册里的值 赋给list
      let list = this.selectProductPics;
      //判断list是否有图片
      if (list !== null && list.length > 0) {
        //定义空串
        let photos = "";
        //并循环list 给定义的空串photos赋值
        for (let i = 0; i < list.length; i++) {
          //拼接赋值
          photos += list[i] + ",";
        }
        //判断photos是否有值 并且最后一位是逗号
        if (photos !== "" && photos.endsWith(",")) {
          //截取逗号
          photos = photos.substr(0, photos.length - 1);
        }
        this.value.pic = this.value.skuStockList[0].pic;
        //然后给父组件的字段赋值
        this.value.albumPics = photos;
      }
    },
  },
  mounted() {
    //商品属性分类下拉选项
    this.getProductAttrCateList();
  }
}
</script>

<style scoped>

</style>