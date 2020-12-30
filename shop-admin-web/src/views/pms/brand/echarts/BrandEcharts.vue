<template>
  <div id="brandCharts" :style="{width: '1000px', height: '500px'}"></div>
</template>

<script>
// 引入基本模板
let echarts = require('echarts/lib/echarts')
// 引入柱状图组件
require('echarts/lib/chart/bar')
// 引入提示框和title组件
require('echarts/lib/component/tooltip')
require('echarts/lib/component/title')
import {queryBrandName} from '@/api'
export default {
  name: "BrandEcharts",
  data() {
    return {
      brandName:[],
      productNumber:[],
    }
  },
  methods: {
    getQueryBrandName(){
      queryBrandName().then(result=>{
          if (result.code===200){
            for (let i = 0; i < result.data.length; i++) {
                this.brandName.push(result.data[i].brandName)
                this.productNumber.push(result.data[i].productNumber)
            }
            this.getData();
            alert(JSON.stringify(this.brandName))
          }
      })
    },
    getData(){
      // 基于准备好的dom，初始化echarts实例
      let myChart = echarts.init(document.getElementById('brandCharts'))
      // 绘制图表
      myChart.setOption({
        title: { text: '品牌商品数量柱状图' },
        tooltip: {},
        xAxis: {
          data: this.brandName
        },
        yAxis: {},
        series: [{
          name: '销量',
          type: 'bar',
          data: this.productNumber
        }]
      });
      return myChart;
    }

  },
  mounted() {
    this.getQueryBrandName();
  }

}
</script>

<style scoped>

</style>
